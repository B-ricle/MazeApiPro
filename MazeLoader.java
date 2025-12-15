//This will create the maze and loads mazes
import java.util.Random;


public class MazeLoader{
    private int cols;
    private int rows;
    private int[][] maze;
    

    public MazeLoader(int cols, int rows){
        this.rows = rows;
        this.cols = cols;
        this.maze = new int[rows][cols];
    }

    public int[][] getMaze(){
        return maze;
    }

    public void generateRandomMaze(){
        Random rand = new Random();
        for(int row = 0; row < rows; row++){
            for(int col = 0; col < cols; col++){
                //This will make the outer wall
                if(row == 0 || row == rows -1 || col == 0 || col == cols-1){
                    maze[row][col] = 1;
                } else {
                    //This will randomly assign a wall or path
                    maze[row][col] = rand.nextInt(2);
                }
            }
        }
        maze[1][1] = 0;
        maze[rows-2][cols-2] = 0;
    }
}