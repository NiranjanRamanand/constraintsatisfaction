package csp;

import java.util.ArrayList;
import java.util.HashMap;

public class AdjRegionConstraint extends Constraint {
	int var1, var2;

	public AdjRegionConstraint(int var1, int var2) {
		this.var1 = var1;
		this.var2 = var2;
		vars.add(var1);
		vars.add(var2);
	}

	@Override
	public boolean isSatisfied(int[] assigned) {
		return assigned[var2] != assigned[var1];
	}

	public HashMap<Integer, ArrayList<Integer>> trimDomain(int[] assigned) {
		HashMap<Integer, ArrayList<Integer>> trimmed = new HashMap<>();
		ArrayList<Integer> restrictions = new ArrayList<>();

		if (assigned[var1] != 0 && assigned[var2] == 0) {
			restrictions.add(assigned[var1]);
			trimmed.put(var2, restrictions);
		}

		if (assigned[var2] != 0 && assigned[var1] == 0) {
			restrictions.add(assigned[var2]);
			trimmed.put(var1, restrictions);
		}

		return trimmed;
	}
}
