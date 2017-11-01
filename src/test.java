import java.util.ArrayList;

import de.ovgu.dke.teaching.ml.tictactoe.api.IBoard;

public class test {
	private ArrayList<Integer> getBoardFeatures(IBoard board) {
		int size = board.getSize();
		ArrayList my_lines = new ArrayList();
		ArrayList opp_lines = new ArrayList();

		int my;
		for (my = 0; my <= size; ++my) {
			my_lines.add(Integer.valueOf(0));
		}

		for (my = 0; my <= size; ++my) {
			opp_lines.add(Integer.valueOf(0));
		}

		int j;
		int j1;
		int enemy;
		for (j = 0; j < size; ++j) {
			for (j1 = 0; j1 < size; ++j1) {
				my = 0;
				enemy = 0;

				int j2;
				for (j2 = 0; j2 < size; ++j2) {
					if (this == board.getFieldValue(new int[]{j2, j, j1})) {
						++my;
					} else if (board.getFieldValue(new int[]{j2, j, j1}) != null) {
						++enemy;
					}
				}

				if (enemy == 0) {
					my_lines.set(my, Integer.valueOf(((Integer) my_lines.get(my)).intValue() + 1));
				}

				if (my == 0) {
					opp_lines.set(enemy, Integer.valueOf(((Integer) opp_lines.get(enemy)).intValue() + 1));
				}

				my = 0;
				enemy = 0;

				for (j2 = 0; j2 < size; ++j2) {
					if (this == board.getFieldValue(new int[]{j, j2, j1})) {
						++my;
					} else if (board.getFieldValue(new int[]{j, j2, j1}) != null) {
						++enemy;
					}
				}

				if (enemy == 0) {
					my_lines.set(my, Integer.valueOf(((Integer) my_lines.get(my)).intValue() + 1));
				}

				if (my == 0) {
					opp_lines.set(enemy, Integer.valueOf(((Integer) opp_lines.get(enemy)).intValue() + 1));
				}

				my = 0;
				enemy = 0;

				for (j2 = 0; j2 < size; ++j2) {
					if (this == board.getFieldValue(new int[]{j, j1, j2})) {
						++my;
					} else if (board.getFieldValue(new int[]{j, j1, j2}) != null) {
						++enemy;
					}
				}

				if (enemy == 0) {
					my_lines.set(my, Integer.valueOf(((Integer) my_lines.get(my)).intValue() + 1));
				}

				if (my == 0) {
					opp_lines.set(enemy, Integer.valueOf(((Integer) opp_lines.get(enemy)).intValue() + 1));
				}
			}
		}

		for (j = 0; j < size; ++j) {
			my = 0;
			enemy = 0;

			for (j1 = 0; j1 < size; ++j1) {
				if (this == board.getFieldValue(new int[]{j1, j1, j})) {
					++my;
				} else if (board.getFieldValue(new int[]{j1, j1, j}) != null) {
					++enemy;
				}
			}

			if (enemy == 0) {
				my_lines.set(my, Integer.valueOf(((Integer) my_lines.get(my)).intValue() + 1));
			}

			if (my == 0) {
				opp_lines.set(enemy, Integer.valueOf(((Integer) opp_lines.get(enemy)).intValue() + 1));
			}
		}

		for (j = 0; j < size; ++j) {
			my = 0;
			enemy = 0;

			for (j1 = 0; j1 < size; ++j1) {
				if (this == board.getFieldValue(new int[]{j1, size - 1 - j1, j})) {
					++my;
				} else if (board.getFieldValue(new int[]{j1, size - 1 - j1, j}) != null) {
					++enemy;
				}
			}

			if (enemy == 0) {
				my_lines.set(my, Integer.valueOf(((Integer) my_lines.get(my)).intValue() + 1));
			}

			if (my == 0) {
				opp_lines.set(enemy, Integer.valueOf(((Integer) opp_lines.get(enemy)).intValue() + 1));
			}
		}

		for (j = 0; j < size; ++j) {
			my = 0;
			enemy = 0;

			for (j1 = 0; j1 < size; ++j1) {
				if (this == board.getFieldValue(new int[]{j1, j, j1})) {
					++my;
				} else if (board.getFieldValue(new int[]{j1, j, j1}) != null) {
					++enemy;
				}
			}

			if (enemy == 0) {
				my_lines.set(my, Integer.valueOf(((Integer) my_lines.get(my)).intValue() + 1));
			}

			if (my == 0) {
				opp_lines.set(enemy, Integer.valueOf(((Integer) opp_lines.get(enemy)).intValue() + 1));
			}
		}

		for (j = 0; j < size; ++j) {
			my = 0;
			enemy = 0;

			for (j1 = 0; j1 < size; ++j1) {
				if (this == board.getFieldValue(new int[]{j1, j, size - 1 - j1})) {
					++my;
				} else if (board.getFieldValue(new int[]{j1, j, size - 1 - j1}) != null) {
					++enemy;
				}
			}

			if (enemy == 0) {
				my_lines.set(my, Integer.valueOf(((Integer) my_lines.get(my)).intValue() + 1));
			}

			if (my == 0) {
				opp_lines.set(enemy, Integer.valueOf(((Integer) opp_lines.get(enemy)).intValue() + 1));
			}
		}

		for (j = 0; j < size; ++j) {
			my = 0;
			enemy = 0;

			for (j1 = 0; j1 < size; ++j1) {
				if (this == board.getFieldValue(new int[]{j, j1, j1})) {
					++my;
				} else if (board.getFieldValue(new int[]{j, j1, j1}) != null) {
					++enemy;
				}
			}

			if (enemy == 0) {
				my_lines.set(my, Integer.valueOf(((Integer) my_lines.get(my)).intValue() + 1));
			}

			if (my == 0) {
				opp_lines.set(enemy, Integer.valueOf(((Integer) opp_lines.get(enemy)).intValue() + 1));
			}
		}

		for (j = 0; j < size; ++j) {
			my = 0;
			enemy = 0;

			for (j1 = 0; j1 < size; ++j1) {
				if (this == board.getFieldValue(new int[]{j, j1, size - 1 - j1})) {
					++my;
				} else if (board.getFieldValue(new int[]{j, j1, size - 1 - j1}) != null) {
					++enemy;
				}
			}

			if (enemy == 0) {
				my_lines.set(my, Integer.valueOf(((Integer) my_lines.get(my)).intValue() + 1));
			}

			if (my == 0) {
				opp_lines.set(enemy, Integer.valueOf(((Integer) opp_lines.get(enemy)).intValue() + 1));
			}
		}

		my = 0;
		enemy = 0;

		for (j = 0; j < size; ++j) {
			if (this == board.getFieldValue(new int[]{j, j, j})) {
				++my;
			} else if (board.getFieldValue(new int[]{j, j, j}) != null) {
				++enemy;
			}
		}

		if (enemy == 0) {
			my_lines.set(my, Integer.valueOf(((Integer) my_lines.get(my)).intValue() + 1));
		}

		if (my == 0) {
			opp_lines.set(enemy, Integer.valueOf(((Integer) opp_lines.get(enemy)).intValue() + 1));
		}

		my = 0;
		enemy = 0;

		for (j = 0; j < size; ++j) {
			if (this == board.getFieldValue(new int[]{size - 1 - j, j, j})) {
				++my;
			} else if (board.getFieldValue(new int[]{size - 1 - j, j, j}) != null) {
				++enemy;
			}
		}

		if (enemy == 0) {
			my_lines.set(my, Integer.valueOf(((Integer) my_lines.get(my)).intValue() + 1));
		}

		if (my == 0) {
			opp_lines.set(enemy, Integer.valueOf(((Integer) opp_lines.get(enemy)).intValue() + 1));
		}

		my = 0;
		enemy = 0;

		for (j = 0; j < size; ++j) {
			if (this == board.getFieldValue(new int[]{j, size - 1 - j, j})) {
				++my;
			} else if (board.getFieldValue(new int[]{j, size - 1 - j, j}) != null) {
				++enemy;
			}
		}

		if (enemy == 0) {
			my_lines.set(my, Integer.valueOf(((Integer) my_lines.get(my)).intValue() + 1));
		}

		if (my == 0) {
			opp_lines.set(enemy, Integer.valueOf(((Integer) opp_lines.get(enemy)).intValue() + 1));
		}

		my = 0;
		enemy = 0;

		// has a diagonal full
		for (j = 0; j < size; ++j) {
			if (this == board.getFieldValue(new int[]{j, j, size - 1 - j})) {
				++my;
			} else if (board.getFieldValue(new int[]{j, j, size - 1 - j}) != null) {
				++enemy;
			}
		}

		if (enemy == 0) {
			my_lines.set(my, Integer.valueOf(((Integer) my_lines.get(my)).intValue() + 1));
		}

		if (my == 0) {
			opp_lines.set(enemy, Integer.valueOf(((Integer) opp_lines.get(enemy)).intValue() + 1));
		}

		my_lines.addAll(opp_lines);
		return my_lines;
	}

}
