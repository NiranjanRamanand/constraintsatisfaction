package csp;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Constraint {

	ArrayList<Integer> vars = new ArrayList<Integer>();

	// Depends a lot on the type of constraint
	public abstract boolean isSatisfied(int[] assigned);

	public abstract HashMap<Integer, ArrayList<Integer>> trimDomain(int[] assigned);

	public ArrayList<Integer> getVars() {
		return vars;
	}
}
