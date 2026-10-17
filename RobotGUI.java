import javax.swing.*;
import java.awt.*;
import java.util.*;

public class RobotGUI extends JFrame {

    private static final int SIZE = 5;

    private final JButton[][] cells = new JButton[SIZE][SIZE];

    private final int[][] originalGrid = {
        {0, 0, 0, 0, 0},
        {0, 1, 1, 0, 0},
        {0, 0, 0, 0, 0},
        {0, 1, 0, 1, 0},
        {0, 0, 0, 0, 0}
    };

    private final int[][] grid = new int[SIZE][SIZE];

    private JLabel statusLabel;

    public RobotGUI() {

        setTitle("Robot Path Planning Visualizer");
        setSize(750, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));

        JLabel title = new JLabel(
            "ROBOT PATH PLANNING VISUALIZER",
            SwingConstants.CENTER
        );

        title.setFont(new Font("Arial", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        JPanel gridPanel = new JPanel(
            new GridLayout(SIZE, SIZE, 5, 5)
        );

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {

                final int r = row;
                final int c = col;

                JButton button = new JButton();
                button.setFont(new Font("Arial", Font.BOLD, 20));
                button.setOpaque(true);

                button.addActionListener(e -> toggleObstacle(r, c));

                cells[row][col] = button;
                gridPanel.add(button);
            }
        }

        add(gridPanel, BorderLayout.CENTER);

        JButton aStarButton = new JButton("Find A* Path");
        JButton dijkstraButton = new JButton("Find Dijkstra Path");
        JButton compareButton = new JButton("Compare Algorithms");
        JButton resetButton = new JButton("Reset Grid");

        aStarButton.addActionListener(e -> runAlgorithm(true));
        dijkstraButton.addActionListener(e -> runAlgorithm(false));
        compareButton.addActionListener(e -> compareAlgorithms());
        resetButton.addActionListener(e -> resetGrid());

        JPanel buttonPanel = new JPanel(new FlowLayout());

        buttonPanel.add(aStarButton);
        buttonPanel.add(dijkstraButton);
        buttonPanel.add(compareButton);
        buttonPanel.add(resetButton);

        statusLabel = new JLabel(
            "Choose an algorithm to find a path.",
            SwingConstants.CENTER
        );

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(statusLabel, BorderLayout.NORTH);
        bottomPanel.add(buttonPanel, BorderLayout.CENTER);

        add(bottomPanel, BorderLayout.SOUTH);

        resetGrid();
        setVisible(true);
    }

    private void toggleObstacle(int row, int col) {

        if ((row == 0 && col == 0)
                || (row == SIZE - 1 && col == SIZE - 1)) {
            statusLabel.setText(
                "Start and Destination cannot be obstacles."
            );
            return;
        }

        clearPath();

        grid[row][col] = (grid[row][col] == 1) ? 0 : 1;

        updateGrid();
        statusLabel.setText("Obstacle updated.");
    }

    private void clearPath() {

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (grid[row][col] == 2) {
                    grid[row][col] = 0;
                }
            }
        }
    }

    private void resetGrid() {

        for (int row = 0; row < SIZE; row++) {
            System.arraycopy(
                originalGrid[row], 0,
                grid[row], 0, SIZE
            );
        }

        updateGrid();
        statusLabel.setText("Grid reset.");
    }

    private void updateGrid() {

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {

                JButton button = cells[row][col];

                if (row == 0 && col == 0) {
                    button.setText("R");
                    button.setBackground(Color.GREEN);
                } else if (row == SIZE - 1 && col == SIZE - 1) {
                    button.setText("D");
                    button.setBackground(Color.CYAN);
                } else if (grid[row][col] == 1) {
                    button.setText("#");
                    button.setBackground(Color.RED);
                } else if (grid[row][col] == 2) {
                    button.setText("*");
                    button.setBackground(Color.YELLOW);
                } else {
                    button.setText("");
                    button.setBackground(Color.WHITE);
                }
            }
        }
    }

    private static class Node {

        int row, col, distance, priority;
        Node parent;

        Node(int row, int col, int distance,
             int priority, Node parent) {
            this.row = row;
            this.col = col;
            this.distance = distance;
            this.priority = priority;
            this.parent = parent;
        }
    }

    private int heuristic(int row, int col) {
        return Math.abs(SIZE - 1 - row)
             + Math.abs(SIZE - 1 - col);
    }

    private int[][] copyGrid() {

        int[][] copy = new int[SIZE][SIZE];

        for (int row = 0; row < SIZE; row++) {
            System.arraycopy(grid[row], 0, copy[row], 0, SIZE);
        }

        return copy;
    }

    private int findPath(int[][] map, boolean useAStar) {
        int[][] distance = new int[SIZE][SIZE];
        boolean[][] visited = new boolean[SIZE][SIZE];
        Node[][] parent = new Node[SIZE][SIZE];

        for (int[] row : distance) {
            Arrays.fill(row, Integer.MAX_VALUE);
        }

        PriorityQueue<Node> queue = new PriorityQueue<>(
            Comparator.comparingInt(node -> node.priority)
        );

        distance[0][0] = 0;

        queue.add(new Node(
            0, 0, 0,
            useAStar ? heuristic(0, 0) : 0,
            null
        ));

        int[] rowMove = {-1, 1, 0, 0};
        int[] colMove = {0, 0, -1, 1};

        while (!queue.isEmpty()) {

            Node current = queue.poll();

            int row = current.row;
            int col = current.col;

            if (visited[row][col]) {
                continue;
            }

            visited[row][col] = true;

            if (row == SIZE - 1 && col == SIZE - 1) {
                break;
            }

            for (int i = 0; i < 4; i++) {

                int nr = row + rowMove[i];
                int nc = col + colMove[i];

                if (nr < 0 || nr >= SIZE
                        || nc < 0 || nc >= SIZE) {
                    continue;
                }

                if (map[nr][nc] == 1 || visited[nr][nc]) {
                    continue;
                }

                int newDistance = distance[row][col] + 1;

                if (newDistance < distance[nr][nc]) {

                    distance[nr][nc] = newDistance;

                    parent[nr][nc] = current;

                    int priority = newDistance;

                    if (useAStar) {
                        priority += heuristic(nr, nc);
                    }

                    queue.add(new Node(
                        nr, nc, newDistance, priority, current
                    ));
                }
            }
        }

        if (distance[SIZE - 1][SIZE - 1] == Integer.MAX_VALUE) {
            return -1;
        }

        int row = SIZE - 1;
        int col = SIZE - 1;

        while (row != 0 || col != 0) {

            map[row][col] = 2;

            Node previous = parent[row][col];

            if (previous == null) {
                break;
            }

            row = previous.row;
            col = previous.col;
        }

        return distance[SIZE - 1][SIZE - 1];
    }

    private void runAlgorithm(boolean useAStar) {

        clearPath();

        int steps = findPath(grid, useAStar);

        updateGrid();

        String name = useAStar ? "A*" : "Dijkstra";

        if (steps == -1) {
            statusLabel.setText(name + ": No path found!");
        } else {
            statusLabel.setText(name + " path found! Steps: " + steps);
        }
    }

    private void compareAlgorithms() {

        int[][] aStarGrid = copyGrid();
        int[][] dijkstraGrid = copyGrid();

        int aStarSteps = findPath(aStarGrid, true);
        int dijkstraSteps = findPath(dijkstraGrid, false);

        String result;

        if (aStarSteps == -1 && dijkstraSteps == -1) {
            result = "Neither algorithm found a path.";
        } else if (aStarSteps == -1) {
            result = "A*: No path\nDijkstra: " + dijkstraSteps + " steps";
        } else if (dijkstraSteps == -1) {
            result = "A*: " + aStarSteps + " steps\nDijkstra: No path";
        } else {
            result = "A* Path Length: " + aStarSteps
                   + "\nDijkstra Path Length: " + dijkstraSteps
                   + "\n\nBoth algorithms find a shortest path "
                   + "for this grid.";
        }

        JOptionPane.showMessageDialog(
            this,
            result,
            "Algorithm Comparison",
            JOptionPane.INFORMATION_MESSAGE
        );

        clearPath();

        // Display A* route after comparison.
        findPath(grid, true);

        updateGrid();

        statusLabel.setText("Algorithm comparison completed.");
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(RobotGUI::new);
    }
}