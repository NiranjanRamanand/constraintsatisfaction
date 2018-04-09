package csp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;

public class ConstraintSatisfactionProblem {
	ArrayList<Constraint> constraints;
	int[] assignment;
	HashSet<Integer> assigned = new HashSet<>();
	HashMap<Integer, ArrayList<Integer>> domains;
	boolean MRV = false, LCV = true, MAC3 = false;

	HashMap<HashSet<Integer>, HashSet<Constraint>> storedConstraints = new HashMap<>();

	public ConstraintSatisfactionProblem(ArrayList<Constraint> constraints,
			HashMap<Integer, ArrayList<Integer>> domains) {

		this.constraints = constraints;
		this.domains = domains;

		assignment = new int[domains.keySet().size()];

	}

	HashSet<Constraint> getConstraints(HashSet<Integer> relevantVars) {
		if (storedConstraints.containsKey(relevantVars)) {
			return storedConstraints.get(relevantVars);
		} else {
			HashSet<Constraint> validConstraints = new HashSet<>();

			for (Constraint c : constraints) {
				if (relevantVars.containsAll(c.getVars())) {
					validConstraints.add(c);
				}
			}
			storedConstraints.put(relevantVars, validConstraints);

			return validConstraints;
		}
	}

	boolean assigned() {
		return assigned.size() == assignment.length;
	}

	HashSet<Integer> getAssigned() {
		return assigned;

	}

	int[] backtrack() {
		HashMap<Integer, ArrayList<Integer>> removed = new HashMap<>();

		if (assigned()) {
			return assignment;
		} else {
			int nextVar = getUnassignedVar();

			if (nextVar == -1)
				return null;

			ArrayList<Integer> domain = LCV ? sortedLCVs(nextVar) : domains.get(nextVar);
			HashSet<Constraint> validConstraints;

			for (int element : domain) {
				assignment[nextVar] = element;
				assigned.add(nextVar);

				if (!MAC3 || MAC3(nextVar, removed)) {
					validConstraints = getConstraints(getAssigned());
					boolean allSatisfied = true;

					for (Constraint c : validConstraints) {
						if (MRV) {
							HashMap<Integer, ArrayList<Integer>> toRemove = c.trimDomain(assignment);
							updateDomains(toRemove, removed);
						}

						if (!c.isSatisfied(assignment))
							allSatisfied = false;

					}

					if (allSatisfied) {
						backtrack();

						if (assigned())
							return assignment;
					}
				}
				resetDomains(removed);
			}
			assignment[nextVar] = 0;
			assigned.remove(nextVar);
		}

		return null;
	}

	boolean MAC3(int var, HashMap<Integer, ArrayList<Integer>> removed) {
		ArrayList<Integer> queue = new ArrayList<>();
		queue.add(var);
		HashSet<Constraint> varConstraints;

		while (!queue.isEmpty()) {
			int curr = queue.remove(0);
			// get neighbors through constraints
			HashSet<Integer> currList = new HashSet<>();
			currList.add(curr);

			varConstraints = getConstraints(currList);

			// Each constraint including the current var
			for (Constraint c : varConstraints) {
				ArrayList<Integer> vars = c.getVars();
				int unassigned = -1;

				// get the unassigned var
				for (int i : vars) {
					if (i != curr && assignment[i] == 0)
						unassigned = i;
				}
				boolean change = false;

				if (unassigned != -1) {
					ArrayList<Integer> domain = domains.get(unassigned);

					for (int j : new ArrayList<Integer>(domain)) {
						assignment[unassigned] = j;
						assigned.add(unassigned);

						if (!c.isSatisfied(assignment)) {//Need to add back to queue
							change = true;
							removed.get(unassigned).add(j);
							domain.remove(j);
						}
					}

					assignment[unassigned] = 0;
					assigned.remove(unassigned);

					if (domain.isEmpty())//arc inconsistent
						return false;
				}

				if (change)
					queue.add(unassigned);
			}
		}

		return true;
	}

	ArrayList<Integer> sortedLCVs(int var) {
		ArrayList<Integer> domain = domains.get(var);
		//Comparator implemented such that values with
		//less constraints show up earlier when sorted
		Collections.sort(domain, new Comparator<Integer>() {

			@Override
			public int compare(Integer o1, Integer o2) {
				HashMap<Integer, ArrayList<Integer>> removed = new HashMap<>();
				HashMap<Integer, ArrayList<Integer>> toRemove = new HashMap<>();

				HashSet<Integer> extendedDomain = getAssigned();
				extendedDomain.add(var);

				assignment[var] = o1;
				assigned.add(var);

				for (Constraint c : getConstraints(extendedDomain)) {
					toRemove = c.trimDomain(assignment);
					updateDomains(toRemove, removed);
				}

				int count1 = toRemove.size();
				resetDomains(removed);

				assignment[var] = o2;
				assigned.add(var);
				for (Constraint c : getConstraints(extendedDomain)) {
					toRemove = c.trimDomain(assignment);
					updateDomains(toRemove, removed);
				}

				int count2 = toRemove.size();
				resetDomains(removed);

				assignment[var] = 0;
				assigned.remove(var);
				return count2 - count1;

			}

		});

		return domain;

	}

	int getUnassignedVar() {
		int minDomainSize = Integer.MAX_VALUE, size;
		HashMap<Integer, ArrayList<Integer>> freq = new HashMap<>();

		//Find vars with smallest domains
		for (int i = 0; i < assignment.length; i++) {
			ArrayList<Integer> domain = domains.get(i);

			if (assignment[i] == 0) {//assignment to zero => no value assigned
				if (!MRV)//just get the first unassigned value if no MRV
					return i;
				if (domain.size() < minDomainSize) {
					minDomainSize = domain.size();

					if (freq.containsKey(minDomainSize)) {
						freq.get(minDomainSize).add(i);
					} else {
						ArrayList<Integer> start = new ArrayList<>();
						start.add(i);

						freq.put(domain.size(), start);
					}
				}
			}
		}

		//Tiebreak based on number of constraints
		if (minDomainSize != Integer.MAX_VALUE) {
			ArrayList<Integer> minDomain = freq.get(minDomainSize);
			int minConstraints = Integer.MAX_VALUE, minIndex = 0;

			for (int i : minDomain) {
				HashSet<Integer> extendedDomain = getAssigned(); //currently assigned vars
				extendedDomain.add(i);

				if ((size = getConstraints(extendedDomain).size()) < minConstraints) {
					minConstraints = size;
					minIndex = i;
				}
			}

			return minIndex;
		} else {
			return -1;
		}
	}

	void updateDomains(HashMap<Integer, ArrayList<Integer>> toRemove, HashMap<Integer, ArrayList<Integer>> removed) {
		for (int i : toRemove.keySet()) {
			if (removed.containsKey(i)) {
				removed.get(i).addAll(toRemove.get(i));
			} else {
				removed.put(i, toRemove.get(i));
			}
			domains.get(i).removeAll(toRemove.get(i));
		}
	}

	void resetDomains(HashMap<Integer, ArrayList<Integer>> removed) {
		for (int i : removed.keySet()) {
			domains.get(i).addAll(removed.get(i));
		}
		removed.clear();
	}
}
