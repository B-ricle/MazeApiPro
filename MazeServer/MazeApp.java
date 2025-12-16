
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;
import javafx.animation.AnimationTimer;
import javafx.scene.paint.Color;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextInputDialog;
import java.util.Optional;

public class MazeApp extends Application {

    // Size of each maze cell in pixels
    private final int cellSize = 40;

    // Core game objects
    private MazeLoader loader;
    private int[][] maze;
    private Player player;
    private MazeRenderer renderer;

    // Game state variables
    private int level = 1;
    private int score = 0;
    private long startTime;

    // Tracks which tiles have been explored (fog-of-war support)
    private boolean[][] explored;

    // Used to briefly display "Level Complete" text
    private long levelCompleteTime = 0;

    // Pause state
    private boolean paused = false;

    // Manages saving/loading player data
    private PlayerDataManager dataManager = new PlayerDataManager();

    // Unique player ID (used for saving/loading)
    private int playerID = 1;

    public static void main(String[] args) {
        // Launch the JavaFX application
        launch(args);
    }

    @Override
    public void start(Stage stage) {

        // --- TITLE SCREEN / PLAYER NAME INPUT ---
        TextInputDialog dialog = new TextInputDialog("Player1");
        dialog.setTitle("Maze Game");
        dialog.setHeaderText("Enter Player Name");
        dialog.setContentText("Name:");

        Optional<String> result = dialog.showAndWait();
        String playerName = result.orElse("Player1");

        // Assign player ID for simplicity
        playerID = playerName.hashCode();

        // Load previous player data from file (if it exists)
        dataManager.loadFromFile("PlayerDataStorage.txt");

        // Restore saved score and elapsed time if player exists
        PlayerData existingPlayer = dataManager.getPlayer(playerID);
        if (existingPlayer != null) {
            score = existingPlayer.getScore();
            startTime = System.currentTimeMillis() - (existingPlayer.getTime() * 1000);
        } else {
            startTime = System.currentTimeMillis();
        }

        // Initialize the first level
        startNewLevel(playerName);

        // Root layout that holds the maze renderer
        StackPane root = new StackPane(renderer);
        Scene scene = new Scene(root);

        // Handle keyboard input for player movement and pause
        scene.setOnKeyPressed(e -> {
            KeyCode code = e.getCode();

            // --- PAUSE MENU ---
            if (code == KeyCode.ESCAPE) {
                paused = !paused;
            }

            if (!paused) {
                int dRow = 0, dCol = 0;
                if (code == KeyCode.UP || code == KeyCode.W) {
                    dRow = -1;
                }
                if (code == KeyCode.DOWN || code == KeyCode.S) {
                    dRow = 1;
                }
                if (code == KeyCode.LEFT || code == KeyCode.A) {
                    dCol = -1;
                }
                if (code == KeyCode.RIGHT || code == KeyCode.D) {
                    dCol = 1;
                }

                if (dRow != 0 || dCol != 0) {
                    movePlayer(dRow, dCol);
                }
            }
        });

        // Game loop that continuously redraws the maze and HUD
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                renderMaze();
            }
        };
        timer.start();

        // Set up the main window
        stage.setTitle("Maze Game");
        stage.setScene(scene);
        stage.show();

        // Ensure the renderer can receive key input
        renderer.requestFocus();
    }

    // Creates a new maze or advances the game to the next level
    private void startNewLevel(String playerName) {

        // Maze size increases as the level increases
        int newSize = Math.max(9, 7 + level * 2); // slightly larger to reduce stuck player

        // Generate a new random maze
        loader = new MazeLoader(newSize, newSize);
        loader.generateRandomMaze();
        maze = loader.getMaze();

        // --- ENSURE A SMALL CLEAR AREA AROUND START ---
        // This prevents the player from getting stuck immediately
        int startRow = 1;
        int startCol = 1;
        for (int r = startRow - 1; r <= startRow + 1; r++) {
            for (int c = startCol - 1; c <= startCol + 1; c++) {
                if (r > 0 && r < maze.length - 1 && c > 0 && c < maze[0].length - 1) {
                    maze[r][c] = 0; // make walkable
                }
            }
        }

        // Reset fog-of-war tracking for the new maze
        explored = new boolean[maze.length][maze[0].length];

        // Create the player if this is the first level
        if (player == null) {
            player = new Player(playerName, playerID, startRow, startCol);
        } else {
            player.setPosition(startRow, startCol);
            player.setName(playerName);
        }

        // Mark starting position as explored
        explored[startRow][startCol] = true;

        // Create a new renderer sized to the maze
        renderer = new MazeRenderer(
                maze[0].length * cellSize,
                maze.length * cellSize,
                maze.length,
                maze[0].length
        );
    }

    // Handles player movement and collision checking
    private void movePlayer(int dRow, int dCol) {
        int[] pos = player.getPosition();
        int newRow = pos[0] + dRow;
        int newCol = pos[1] + dCol;

        // Check bounds and ensure the destination is a walkable path
        if (newRow >= 0 && newRow < maze.length
                && newCol >= 0 && newCol < maze[0].length
                && maze[newRow][newCol] == 0) {

            // Move the player
            player.setPosition(newRow, newCol);

            // --- UPDATE EXPLORED TILES ---
            explored[newRow][newCol] = true;

            // Check if the exit has been reached
            checkExit();
        }
    }

    // Checks whether the player has reached the exit tile
    private void checkExit() {
        int[] pos = player.getPosition();
        int exitRow = maze.length - 2;
        int exitCol = maze[0].length - 2;

        if (pos[0] == exitRow && pos[1] == exitCol) {
            level++;
            score++;

            // Record the time the level was completed
            levelCompleteTime = System.currentTimeMillis();

            // Save the player's updated progress
            PlayerData playerData = new PlayerData(
                    player.getName(),
                    playerID,
                    score,
                    (System.currentTimeMillis() - startTime) / 1000
            );

            dataManager.addPlayer(playerData);
            dataManager.saveToFile("PlayerDataStorage.txt");

            // Start the next level
            startNewLevel(player.getName());
        }
    }

    // Renders the maze, HUD, and messages
    private void renderMaze() {
        int[] pos = player.getPosition();

        // Render the maze with fog-of-war
        renderer.render(maze, pos[0], pos[1], explored);

        GraphicsContext gc = renderer.getGraphicsContext2D();

        // Draw HUD background
        gc.setFill(Color.rgb(255, 255, 255, 0.7));
        gc.fillRect(5, 5, 150, 80);

        // Draw HUD text
        gc.setFill(Color.BLACK);
        gc.fillText("Score: " + score, 10, 20);
        gc.fillText("Level: " + level, 10, 40);

        long elapsed = (System.currentTimeMillis() - startTime) / 1000;
        gc.fillText("Time: " + elapsed, 10, 60);

        // Pause message
        if (paused) {
            gc.setFill(Color.BLUE);
            gc.fillText("PAUSED", 200, 200);
        }

        // Level complete message (2 seconds)
        if (levelCompleteTime > 0
                && System.currentTimeMillis() - levelCompleteTime < 2000) {

            gc.setFill(Color.RED);
            gc.fillText("Level Complete!", 200, 200);
        }
    }

    @Override
    public void stop() {
        // Save player data when the application closes
        PlayerData playerData = new PlayerData(
                player.getName(),
                playerID,
                score,
                (System.currentTimeMillis() - startTime) / 1000
        );

        dataManager.addPlayer(playerData);
        dataManager.saveToFile("PlayerDataStorage.txt");
    }
}
