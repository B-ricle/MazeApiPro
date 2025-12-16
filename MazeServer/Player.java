// This creates the player while storing their data so that it can be called back on

public class Player {

    private int row;
    private int col;
    private String name;
    private int id;

    public Player(String name, int id, int row, int col) {
        this.name = name;
        this.id = id;
        this.row = row;
        this.col = col;
    }

    // We want to return the position of where the player is 
    public int[] getPosition() {
        return new int[]{row, col};
    }

    // This will get the player's ID and name
    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    // Allow updating the player's name
    public void setName(String newName) {
        this.name = newName;
    }

    // This will move the character to a specified column
    public void move(int dRow, int dCol) {
        row += dRow;
        col += dCol;
    }

    // Sets the position of the character 
    public void setPosition(int newRow, int newCol) {
        row = newRow;
        col = newCol;
    }
}
