package csp;

import java.util.ArrayList;
import java.util.HashMap;

public class CircuitBoardDriver {
	int width, height;
	ArrayList<int[]> rectangles;

	public CircuitBoardDriver(ArrayList<int[]> rectangles, int width, int height) {
		this.rectangles = rectangles;
		this.height = height;
		this.width = width;
	}

	public ArrayList<Constraint> getConstraints() {
		ArrayList<Constraint> constraints = new ArrayList<>();

		for (int i = 0; i < rectangles.size(); i++)
			for (int j = 0; j < i; j++)
				constraints.add(new AdjRectConstraint(width, height, i, rectangles.get(i)[0], rectangles.get(i)[1], j,
						rectangles.get(j)[0], rectangles.get(j)[1]));
		return constraints;
	}

	public HashMap<Integer, ArrayList<Integer>> getDomains() {
		HashMap<Integer, ArrayList<Integer>> domains = new HashMap<>();

		for (int i = 0; i < rectangles.size(); i++) {
			domains.put(i, new ArrayList<Integer>());
			for (int j = 0; j <= height - rectangles.get(i)[1]; j++)
				for (int k = 0; k <= width - rectangles.get(i)[0]; k++)
					domains.get(i).add(j * width + k + 1);
		}
		return domains;
	}

	public int[] solve() {
		return new CircuitBoardProblem(getConstraints(), getDomains()).backtrack();
	}

}

class Run2 {
	public static void main(String[] args) {
		ArrayList<int[]> rectangles = new ArrayList<>();

		int[] rect1 = { 3, 4 };
		int[] rect2 = { 3, 5 };
		int[] rect3 = { 2, 2 };
		int[] rect4 = { 5, 1 };
		int[] rect5 = { 2, 2 };
		int[] rect6 = { 2, 2 };
		int[] rect7 = { 2, 3 };

		rectangles.add(rect1);
		rectangles.add(rect2);
		rectangles.add(rect3);
		rectangles.add(rect4);
		rectangles.add(rect5);
		rectangles.add(rect6);
		rectangles.add(rect7);

		int WIDTH = 10;
		int HEIGHT = 5;


		//For bulk time testing:
		int [] colors = null;
		long start = System.currentTimeMillis();
		for(int i = 0; i < 10000; i++){
			CircuitBoardDriver cbd = new CircuitBoardDriver(rectangles, WIDTH, HEIGHT);
			colors = cbd.solve();
		}
		long finish = System.currentTimeMillis();
		System.out.println((double)(finish - start)/10000);

		//CircuitBoardDriver cbd = new CircuitBoardDriver(rectangles, WIDTH, HEIGHT);
		//int [] colors = cbd.solve();


		HashMap<Integer, Integer> pic = new HashMap<>();

		if (colors != null) {
			for (int index = 0; index < rectangles.size(); index++) {
				for (int i = (colors[index] - 1) / WIDTH; i < (colors[index] - 1) / WIDTH
						+ rectangles.get(index)[1]; i++) {
					for (int j = (colors[index] - 1) % WIDTH; j < (colors[index] - 1) % WIDTH
							+ rectangles.get(index)[0]; j++) {
						pic.put(i * WIDTH + j, index);
					}
				}
			}
		} else {
			System.out.println("NO SOLUTION");
		}

		for (int i = 0; i < HEIGHT; i++) {
			for (int j = 0; j < WIDTH; j++) {
				if (pic.containsKey(i * WIDTH + j))
					System.out.print(pic.get(i * WIDTH + j));
				else
					System.out.print(".");
			}
			System.out.println();
		}

	}
}