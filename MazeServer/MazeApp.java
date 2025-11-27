import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class MazeApp extends Application{
    private final int cellSize = 40;
    //Maze representation: 0 = path, 1 = wall
    private int[][] maze = {
        {1,1,1,1,1},
        {1,0,0,0,1},
        {1,0,1,0,1},
        {1,0,1,0,1},
        {1,1,1,1,1}
    };

    private int playerRow = 1;
    private int playerCol = 1;

}

    @Override
    public void start(Stage stage){
        Canvas canvas = new Canvas(maze[0].length * cellSize, maze.length * celSize);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawMaze(gc);
        //This will create the scene and handle key presses for player movement
        Scene scene = new Scene(newStackPane(canvas));
        //When the key is pressed, the charater will move accordingly 
        scene.setOnKeyPressed(e -> [
            switch(event.getCode()){
                case UP -> movePlayer(-1,0);
                case W -> movePlayer(-1,0);

                case DOWN -> movePlayer(1,0);
                case S -> movePlayer(1,0);

                case LEFT -> movePlayer(0,-1);
                case A -> movePlayer(0,-1);

                case RIGHT -> movePlayer(0,1);
                case D -> movePlayer(0,1);

            }
            drawMaze(gc);
        ]);

        stage.setTitle("Maze Game");
        stage.setScene(scene);
        stage.show();
        
    }
    private void movePlayer(int dRow, int dCol){
        int newRow = playerRow + dRow;
        int newCol = playerCol + dCol;
        //We only move if the new position is a path(0)
        if(maze[newRow][newCol] == 0){
            playerRow = newRow;
            playerCol = newCol;
        }  
    }

    private void drawMaze(GraphicsContext gc){
        for(int i = 0; i < maze.length; i++){
            for(int j = 0; j < maze[i].length; j++){
                if(maze[i][j] == 1){
                    gc.setFill(Color.Black);
                } else {
                    gc.setFill(Color.White);
                    gc.fillRect(j * cellSize, i * cellSize, cellSize, cellSize)
                }
            }
        }
        //Draw the player
        gc.setFill(Color.Green);
        gc.fillOval(player * cellSize, playerCol * cellSize, cellSize, cellSize);
    }
    public static void main(String[] args){
        launch(args);
    }




