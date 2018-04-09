package csp;

import java.util.ArrayList;
import java.util.HashMap;

public class MapColoringDriver {
	HashMap<Integer, ArrayList<Integer>> neighbors = new HashMap<>();
	int numColors;

	public MapColoringDriver(HashMap<Integer, ArrayList<Integer>> neighbors, int numColors) {
		this.neighbors = neighbors;
		this.numColors = numColors;
	}

	public ArrayList<Constraint> getConstraints() {
		ArrayList<Constraint> constraints = new ArrayList<>();

		for (int i = 0; i < neighbors.size(); i++) {
			for (int j = 0; j < i; j++) {
				if (neighbors.get(i).contains(j))
					constraints.add(new AdjRegionConstraint(i, j));
			}
		}
		return constraints;
	}

	public HashMap<Integer, ArrayList<Integer>> getDomains(int numColors) {
		HashMap<Integer, ArrayList<Integer>> domains = new HashMap<>();
		int numRegions = neighbors.size();

		for (int i = 0; i < numRegions; i++) {
			domains.put(i, new ArrayList<Integer>());

			for (int j = 1; j <= numColors; j++)
				domains.get(i).add(j);

		}
		return domains;
	}

	public int[] solve() {
		return new MapColoringProblem(getConstraints(), getDomains(numColors)).backtrack();
	}

}

class Run {
	public static void main(String[] args) {
		HashMap<Integer, ArrayList<Integer>> neighbors = new HashMap<>();
		ArrayList<String> names = new ArrayList<>();

		names.add("WA");
		names.add("NT");
		names.add("SA");
		names.add("Q");
		names.add("NSW");
		names.add("V");
		names.add("T");

		ArrayList<String> colorList = new ArrayList<>();
		colorList.add("Blue");
		colorList.add("Red");
		colorList.add("Yellow");
		colorList.add("Purple");

		neighbors.put(0, new ArrayList<Integer>());
		neighbors.get(0).add(1);
		neighbors.get(0).add(2);

		neighbors.put(1, new ArrayList<Integer>());
		neighbors.get(1).add(0);
		neighbors.get(1).add(2);
		neighbors.get(1).add(3);

		neighbors.put(2, new ArrayList<Integer>());
		neighbors.get(2).add(0);
		neighbors.get(2).add(1);
		neighbors.get(2).add(3);
		neighbors.get(2).add(4);
		neighbors.get(2).add(5);

		neighbors.put(3, new ArrayList<Integer>());
		neighbors.get(3).add(1);
		neighbors.get(3).add(2);
		neighbors.get(3).add(4);

		neighbors.put(4, new ArrayList<Integer>());
		neighbors.get(4).add(2);
		neighbors.get(4).add(3);
		neighbors.get(4).add(5);

		neighbors.put(5, new ArrayList<Integer>());
		neighbors.get(5).add(2);
		neighbors.get(5).add(4);

		neighbors.put(6, new ArrayList<Integer>());

		MapColoringDriver mcd = new MapColoringDriver(neighbors, 4);

		int[] colors = mcd.solve();

		for (int i = 0; i < colors.length; i++) {
			System.out.println(names.get(i) + ": " + colorList.get(colors[i]));
		}

	}
}