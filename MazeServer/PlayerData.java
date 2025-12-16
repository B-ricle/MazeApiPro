
import java.io.File;          
import java.io.FileWriter;    
import java.io.BufferedWriter; 
import java.io.FileReader;    
import java.io.BufferedReader; 
import java.io.IOException;   
import java.util.ArrayList;  
import java.util.List;        

public class PlayerData{
    private String playerName;
    private int playerID;
    private int score;
    private long time; // The time elasped

    public PlayerData(String playerName, int playerID, int score, long time){
        this.playerName = playerName;
        this.playerID = playerID;
        this.score = score;
        this.time = time;
    }
    //Getters to retrieve the player's information
    public String getName(){
        return playerName;
    }
    
    public int getId(){
        return playerID;
    }

    public int getScore(){
        return score;
    }
    
    public long getTime(){
        return time;
    }
    //Setters
    public void setScore(int score){
        this.score = score;
    }
    public void setTime(long time){
        this.time = time;
    }

    @Override
    public String toString(){
        return playerName + "," + playerID + "," + score + "," + time;
    }

    

}