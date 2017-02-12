import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

import java.util.*;

public class Main {

    private static final int[][] EASY_GRID = new int[][] {
            {0, 7, 1, 0, 9, 0, 8, 0, 0},
            {0, 0, 0, 3, 0, 6, 0, 0, 0},
            {4, 9, 0, 0, 0, 0, 7, 0, 5},
            {0, 1, 0, 9, 0, 0, 0, 0, 0},
            {9, 0, 2, 0, 0, 0, 6, 0, 3},
            {0, 0, 0, 0, 0, 8, 0, 2, 0},
            {8, 0, 5, 0, 0, 0, 0, 7, 6},
            {0, 0, 0, 6, 0, 7, 0, 0, 0},
            {0, 0, 7, 0, 4, 0, 3, 5, 0}
    };

    private static final int GRID_SIZE = EASY_GRID.length;

    private static final int SQUARE_SIZE = (int) Math.sqrt(GRID_SIZE);

    public static void main(String[] args) {
        final Model model = new Model("Sudoku solver");
        final IntVar[][] rows = new IntVar[GRID_SIZE][GRID_SIZE];
        final IntVar[][] columns = new IntVar[GRID_SIZE][GRID_SIZE];
        final IntVar[][] squares = new IntVar[GRID_SIZE][GRID_SIZE];

        final List<IntVar> variables = getVariables(model, rows, columns, squares);

        applyConstraints(model, rows, columns, squares);

        model.getSolver().solve();

        displaySolution(variables);
    }

    private static List<IntVar> getVariables(Model model, IntVar[][] rows, IntVar[][] columns, IntVar[][] squares) {
        final ArrayList<IntVar> variables = new ArrayList<>();

        int squareIndex;
        int iSquareIndex;
        int jSquareIndex;

        for (int j = 0; j < GRID_SIZE; ++j) {
            for (int i = 0; i < GRID_SIZE; ++i) {
                IntVar variable;

                if (EASY_GRID[i][j] == 0) {
                    variable = model.intVar(i + "," + j, 1, GRID_SIZE);
                } else {
                    variable = model.intVar(i + "," + j, EASY_GRID[i][j]);
                }

                squareIndex = (i / SQUARE_SIZE) + (j / SQUARE_SIZE) * SQUARE_SIZE;
                iSquareIndex = i - (int) Math.floor(i / SQUARE_SIZE) * SQUARE_SIZE;
                jSquareIndex = (j - (int) Math.floor(j / SQUARE_SIZE) * SQUARE_SIZE) * SQUARE_SIZE;

                squares[squareIndex][iSquareIndex + jSquareIndex] = variable;
                rows[i][j] = variable;
                columns[j][i] = variable;

                variables.add(variable);
            }
        }

        return variables;
    }

    private static void applyConstraints(Model model, IntVar[][] rows, IntVar[][] columns, IntVar[][] squares) {
        for (IntVar[] row : rows) {
            model.allDifferent(row).post();
        }

        for (IntVar[] column : columns) {
            model.allDifferent(column).post();
        }

        for (IntVar[] square : squares) {
            model.allDifferent(square).post();
        }
    }

    private static void displaySolution(List<IntVar> variables) {
        final int[][] solution = new int[GRID_SIZE][GRID_SIZE];

        for (IntVar variable : variables) {
            int x = Character.getNumericValue(variable.getName().charAt(0));
            int y = Character.getNumericValue(variable.getName().charAt(2));
            solution[x][y] = variable.getValue();
        }

        for (int j = 0; j < GRID_SIZE; ++j) {
            for (int i = 0; i < GRID_SIZE; ++i) {
                System.out.print(solution[j][i]);
            }

            System.out.println();
        }
    }
}