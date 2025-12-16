
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MazeLoader {

    private int rows;
    private int cols;
    private int[][] maze;
    private boolean[][] visited;
    private Random rand = new Random();

    public MazeLoader(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        maze = new int[rows][cols];
        visited = new boolean[rows][cols];
    }

    public int[][] getMaze() {
        return maze;
    }

    // Generate a solvable maze using recursive backtracking
    public void generateRandomMaze() {
        // Fill with walls
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                maze[r][c] = 1;
                visited[r][c] = false;
            }
        }

        // Start carving from (1,1)
        carvePath(1, 1);

        // Ensure start and exit are open
        maze[1][1] = 0;
        maze[rows - 2][cols - 2] = 0;
    }

    // Recursive backtracking to carve paths
    private void carvePath(int r, int c) {
        maze[r][c] = 0;      // mark current cell as path
        visited[r][c] = true; // mark current cell as visited

        List<int[]> directions = new ArrayList<>();
        directions.add(new int[]{-2, 0}); // up
        directions.add(new int[]{2, 0});  // down
        directions.add(new int[]{0, -2}); // left
        directions.add(new int[]{0, 2});  // right
        Collections.shuffle(directions, rand); // randomize directions

        for (int[] dir : directions) {
            int newR = r + dir[0];
            int newC = c + dir[1];

            // Check bounds and if new cell is unvisited
            if (newR > 0 && newR < rows - 1 && newC > 0 && newC < cols - 1 && !visited[newR][newC]) {
                // Remove wall between current and new cell
                maze[r + dir[0] / 2][c + dir[1] / 2] = 0;
                carvePath(newR, newC);
            }
        }
    }
}
