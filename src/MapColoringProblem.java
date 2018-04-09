package csp;

import java.util.ArrayList;
import java.util.HashMap;

public class MapColoringProblem extends ConstraintSatisfactionProblem {

	public MapColoringProblem(ArrayList<Constraint> constraints, HashMap<Integer, ArrayList<Integer>> domains) {
		super(constraints, domains);
	}

}
