import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

import java.util.ArrayList;
import java.util.List;

class Solver {
    private final int[][] grid;
    private final int gridSize;
    private final int squareSize;
    private final Model model;
    private final List<IntVar> variables = new ArrayList<>();
    
    Solver(int[][] grid) {
        this.grid = grid;
        gridSize = grid.length;
        squareSize = (int) Math.sqrt(gridSize);

        model = new Model("Sudoku solver");
        final IntVar[][] rows = new IntVar[gridSize][gridSize];
        final IntVar[][] columns = new IntVar[gridSize][gridSize];
        final IntVar[][] squares = new IntVar[gridSize][gridSize];

        getVariables(model, rows, columns, squares);

        applyConstraints(model, rows, columns, squares);
    }

    private void getVariables(Model model, IntVar[][] rows, IntVar[][] columns, IntVar[][] squares) {
        int squareIndex;
        int iSquareIndex;
        int jSquareIndex;

        for (int j = 0; j < gridSize; ++j) {
            for (int i = 0; i < gridSize; ++i) {
                IntVar variable;

                if (grid[i][j] == 0) {
                    variable = model.intVar(i + "," + j, 1, gridSize);
                } else {
                    variable = model.intVar(i + "," + j, grid[i][j]);
                }

                squareIndex = (i / squareSize) + (j / squareSize) * squareSize;
                iSquareIndex = i - (int) Math.floor(i / squareSize) * squareSize;
                jSquareIndex = (j - (int) Math.floor(j / squareSize) * squareSize) * squareSize;

                squares[squareIndex][iSquareIndex + jSquareIndex] = variable;
                rows[i][j] = variable;
                columns[j][i] = variable;

                variables.add(variable);
            }
        }
    }

    private void applyConstraints(Model model, IntVar[][] rows, IntVar[][] columns, IntVar[][] squares) {
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

    int[][] solve() {
        final int[][] solution = new int[gridSize][gridSize];

        model.getSolver().solve();

        for (IntVar variable : variables) {
            int x = Character.getNumericValue(variable.getName().charAt(0));
            int y = Character.getNumericValue(variable.getName().charAt(2));
            solution[x][y] = variable.getValue();
        }

        return solution;
    }
}
