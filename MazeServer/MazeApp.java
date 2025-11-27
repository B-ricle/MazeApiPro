import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;

public class MazeApp extends Application {
    private final int cellSize = 40;
    // Maze representation: 0 = path, 1 = wall
    private int[][] maze = {
        {1,1,1,1,1},
        {1,0,0,0,1},
        {1,0,1,0,1},
        {1,0,1,0,1},
        {1,1,1,1,1}
    };

    private int playerRow = 1;
    private int playerCol = 1;

    @Override
    public void start(Stage stage) {
        Canvas canvas = new Canvas(maze[0].length * cellSize, maze.length * cellSize);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawMaze(gc);

        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root);

        scene.setOnKeyPressed(e -> {
            KeyCode code = e.getCode();
            // Move on arrow keys or WASD
            if (code == KeyCode.UP || code == KeyCode.W) {
                movePlayer(-1, 0);
            } else if (code == KeyCode.DOWN || code == KeyCode.S) {
                movePlayer(1, 0);
            } else if (code == KeyCode.LEFT || code == KeyCode.A) {
                movePlayer(0, -1);
            } else if (code == KeyCode.RIGHT || code == KeyCode.D) {
                movePlayer(0, 1);
            }
            drawMaze(gc);
        });

        stage.setTitle("Maze Game");
        stage.setScene(scene);
        stage.show();

        // Give the canvas focus so key events are received
        canvas.requestFocus();
    }

    private void movePlayer(int dRow, int dCol) {
        int newRow = playerRow + dRow;
        int newCol = playerCol + dCol;
        // Check bounds and move only if the new position is a path (0)
        if (newRow >= 0 && newRow < maze.length &&
            newCol >= 0 && newCol < maze[0].length &&
            maze[newRow][newCol] == 0) {
            playerRow = newRow;
            playerCol = newCol;
        }
    }

    private void drawMaze(GraphicsContext gc) {
        // Clear canvas
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                if (maze[i][j] == 1) {
                    gc.setFill(Color.BLACK);
                } else {
                    gc.setFill(Color.WHITE);
                }
                gc.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
            }
        }

        // Draw the player (slightly inset so it looks nicer)
        gc.setFill(Color.GREEN);
        double padding = cellSize * 0.15;
        double size = cellSize - padding * 2;
        gc.fillOval(playerCol * cellSize + padding, playerRow * cellSize + padding, size, size);
    }

    public static void main(String[] args) {
        launch(args);
    }
}