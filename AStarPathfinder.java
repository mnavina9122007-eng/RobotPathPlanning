import java.util.*;

public class AStarPathfinder {

    static int SIZE = 5;

    static int[][] grid = {
        {0, 0, 0, 0, 0},
        {0, 1, 1, 0, 0},
        {0, 0, 0, 0, 0},
        {0, 1, 0, 1, 0},
        {0, 0, 0, 0, 0}
    };

    static int[] rowMove = {-1, 1, 0, 0};
    static int[] colMove = {0, 0, -1, 1};

    static class Node {
        int row, col;
        int g, h;
        Node parent;

        Node(int row, int col, int g, int h, Node parent) {
            this.row = row;
            this.col = col;
            this.g = g;
            this.h = h;
            this.parent = parent;
        }

        int f() {
            return g + h;
        }
    }

    static int heuristic(int row, int col) {
        return Math.abs(4 - row) + Math.abs(4 - col);
    }

    public static void main(String[] args) {

        PriorityQueue<Node> openList =
            new PriorityQueue<>(Comparator.comparingInt(Node::f));

        boolean[][] visited = new boolean[SIZE][SIZE];

        openList.add(new Node(0, 0, 0, heuristic(0, 0), null));

        Node destination = null;

        while (!openList.isEmpty()) {

            Node current = openList.poll();

            if (visited[current.row][current.col]) {
                continue;
            }

            visited[current.row][current.col] = true;

            if (current.row == 4 && current.col == 4) {
                destination = current;
                break;
            }

            for (int i = 0; i < 4; i++) {

                int newRow = current.row + rowMove[i];
                int newCol = current.col + colMove[i];

                if (newRow >= 0 && newRow < SIZE &&
                    newCol >= 0 && newCol < SIZE &&
                    grid[newRow][newCol] == 0 &&
                    !visited[newRow][newCol]) {

                    openList.add(new Node(
                        newRow,
                        newCol,
                        current.g + 1,
                        heuristic(newRow, newCol),
                        current
                    ));
                }
            }
        }

        if (destination == null) {
            System.out.println("No path found!");
            return;
        }

        while (destination != null) {
            grid[destination.row][destination.col] = 2;
            destination = destination.parent;
        }

        System.out.println("A* PATHFINDING RESULT");
        System.out.println();

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {

                if (i == 0 && j == 0) {
                    System.out.print(" R ");
                } else if (i == 4 && j == 4) {
                    System.out.print(" D ");
                } else if (grid[i][j] == 1) {
                    System.out.print(" # ");
                } else if (grid[i][j] == 2) {
                    System.out.print(" * ");
                } else {
                    System.out.print(" . ");
                }
            }
            System.out.println();
        }

        System.out.println();
        System.out.println("R = Robot Start");
        System.out.println("D = Destination");
        System.out.println("* = Calculated Path");
        System.out.println("# = Obstacle");
        System.out.println(". = Empty Cell");
    }
}
