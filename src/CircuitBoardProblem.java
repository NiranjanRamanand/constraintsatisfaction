package csp;

import java.util.ArrayList;
import java.util.HashMap;

public class CircuitBoardProblem extends ConstraintSatisfactionProblem {

	public CircuitBoardProblem(ArrayList<Constraint> constraints, HashMap<Integer, ArrayList<Integer>> domains) {
		super(constraints, domains);
	}

}
