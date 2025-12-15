//This class is responsible for rendering/drawing the maze

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;



public class MazeRenderer extends Canvas{
    private GraphicsContext gc;

    public MazeRenderer(int width, int height){
        super(width, height);
        gc = this.getGraphicsContext2D();

    }
    public void render(int[][] maze){
        gc.setFill(Color.BLACK);
        gc.fillRect(0,0, getWidth(), getHeight());

        int rows = maze.length;
        int cols = maze[0].length;

        double cellWidth = getWidth() / cols;
        double cellHeight = getHeight() / rows;

        for(int row = 0; row < rows; row++){
            for(int col = 0; col < cols; col++){
                if(maze[row][col] == 1){
                    gc.setFill(Color.BLACK);
                } else {
                    gc.setFill(Color.WHITE);
                }
                gc.fillRect(col * cellWidth, row * cellHeight, cellWidth, cellHeight);
            }
        }
    }
}
