public class RobotGrid {

    public static void main(String[] args) {

        int rows = 5;
        int columns = 5;

        // 0 = Empty cell, 1 = Obstacle
        int[][] grid = {
            {0, 0, 0, 0, 0},
            {0, 1, 1, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 1, 0, 1, 0},
            {0, 0, 0, 0, 0}
        };

        System.out.println("ROBOT PATH PLANNING GRID");
        System.out.println();

        for (int i = 0; i < rows; i++) {

            for (int j = 0; j < columns; j++) {

                if (i == 0 && j == 0) {
                    System.out.print(" R ");
                } else if (i == 4 && j == 4) {
                    System.out.print(" D ");
                } else if (grid[i][j] == 1) {
                    System.out.print(" # ");
                } else {
                    System.out.print(" . ");
                }
            }

            System.out.println();
        }

        System.out.println();
        System.out.println("R = Robot Start");
        System.out.println("D = Destination");
        System.out.println(". = Empty Cell");
        System.out.println("# = Obstacle");
    }
}


