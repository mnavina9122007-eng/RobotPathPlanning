# RobotPathPlanning

## Java Mini Project – Robot Path Planning Using A* Algorithm

### Description
This project implements robot path planning using the A* (A-Star) search algorithm. It helps a robot find an optimal path from a starting position to a goal position while avoiding obstacles in a grid.

### Technologies Used
- Java
- Java Swing (GUI)
- A* Search Algorithm

### Features
- Grid-based robot navigation
- Start and goal position selection
- Obstacle avoidance
- Pathfinding using the A* algorithm
- Graphical User Interface (GUI)

### Project Files
- `Main.java` – Main entry point of the application.
- `AStarPathfinder.java` – Implements the A* pathfinding algorithm.
- `RobotGrid.java` – Manages the robot's grid environment.
- `RobotGUI.java` – Provides the graphical user interface.

### How to Run
1. Install the Java Development Kit (JDK).
2. Open the project in Visual Studio Code.
3. Ensure the Java files are in the same project folder.
4. Run `Main.java`.

### Algorithm
The A* algorithm finds a path by combining:
- **g(n):** Cost from the starting position to the current node.
- **h(n):** Estimated cost from the current node to the goal.
- **f(n) = g(n) + h(n):** Total estimated path cost.

### Author
Project developed as a Java Mini Project.
