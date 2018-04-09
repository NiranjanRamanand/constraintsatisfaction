package csp;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;

public class AdjRectConstraint extends Constraint {
	int var1, var2;
	int[] rect1 = new int[2];
	int[] rect2 = new int[2];
	int width, height;

	public AdjRectConstraint(int width, int height, int var1, int w1, int h1, int var2, int w2, int h2) {
		this.var1 = var1;
		this.var2 = var2;
		this.height = height;
		this.width = width;

		rect1[0] = w1;
		rect1[1] = h1;
		rect2[0] = w2;
		rect2[1] = h2;
		vars.add(var1);
		vars.add(var2);
	}

	@Override
	public boolean isSatisfied(int[] assigned) {
		Rectangle rectA = new Rectangle((assigned[var2] - 1) % width, (assigned[var2]) / width, rect2[0], rect2[1]);
		Rectangle rectB = new Rectangle((assigned[var1] - 1) % width, (assigned[var1]) / width, rect1[0], rect1[1]);

		return !rectB.intersects(rectA);
	}

	public HashMap<Integer, ArrayList<Integer>> trimDomain(int[] assigned) {
		HashMap<Integer, ArrayList<Integer>> trimmed = new HashMap<>();
		ArrayList<Integer> restrictions = new ArrayList<>();

		if (assigned[var1] != 0 && assigned[var2] == 0) {//remove domain values already used by var1 from var2
			for (int i = (assigned[var1] - 1) / width; i < (assigned[var1] - 1) / width + rect1[0]; i++)
				for (int j = (assigned[var1] - 1) % width; j < (assigned[var1] - 1) % width + rect1[1]; j++)
					restrictions.add(i * width + j);
			trimmed.put(var2, restrictions);
		}

		if (assigned[var2] != 0 && assigned[var1] == 0) {//vice-versa
			for (int i = (assigned[var2] - 1) / width; i < (assigned[var2] - 1) / width + rect2[0]; i++)
				for (int j = (assigned[var2] - 1) % width; j < (assigned[var2] - 1) % width + rect2[1]; j++)
					restrictions.add(i * width + j);
			trimmed.put(var1, restrictions);
		}
		return trimmed;
	}
}
