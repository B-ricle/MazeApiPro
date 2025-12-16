import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class MazeRenderer extends Canvas {
    private GraphicsContext gc;
    private final int visibilityRadius = 2; // player can see 2 cells in every direction

    public MazeRenderer(int width, int height, int rows, int cols){
        super(width, height);
        gc = this.getGraphicsContext2D();
    }

    // Render the maze with fog-of-war and player
    // Accepts explored array from MazeApp
    public void render(int[][] maze, int playerRow, int playerCol, boolean[][] explored){
        int rows = maze.length;
        int cols = maze[0].length;
        double cellWidth = getWidth() / cols;
        double cellHeight = getHeight() / rows;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                // Player sees tiles within visibility radius
                if (Math.abs(row - playerRow) <= visibilityRadius && Math.abs(col - playerCol) <= visibilityRadius) {
                    gc.setFill(maze[row][col] == 1 ? Color.BLACK : Color.WHITE);
                    explored[row][col] = true; // mark as explored
                } else {
                    // Show previously explored tiles or dark gray for unexplored
                    gc.setFill(explored[row][col] ? (maze[row][col] == 1 ? Color.BLACK : Color.WHITE) : Color.DARKGRAY);
                }
                gc.fillRect(col * cellWidth, row * cellHeight, cellWidth, cellHeight);
            }
        }

        // Draw the player on top
        gc.setFill(Color.GREEN);
        double padding = cellWidth * 0.15;
        double size = cellWidth - padding * 2;
        gc.fillOval(playerCol * cellWidth + padding, playerRow * cellHeight + padding, size, size);
    }
}
