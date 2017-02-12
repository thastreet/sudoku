public class Main {

    private static final int[][] EASY_GRID_9 = new int[][] {
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

    private static final int[][] EASY_GRID_4 = new int[][] {
            {0, 3, 2, 4},
            {0, 0, 0, 0},
            {4, 0, 0, 2},
            {3, 0, 0, 0}
    };

    public static void main(String[] args) {
        final Solver solver = new Solver(EASY_GRID_4);
        final int[][] solution = solver.solve();

        displaySolution(EASY_GRID_4.length, solution);
    }

    private static void displaySolution(int gridSize, int[][] solution) {
        for (int j = 0; j < gridSize; ++j) {
            for (int i = 0; i < gridSize; ++i) {
                System.out.print(solution[j][i]);
            }

            System.out.println();
        }
    }
}