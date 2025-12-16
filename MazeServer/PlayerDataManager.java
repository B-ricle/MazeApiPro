import java.util.HashMap;
import java.util.Map;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;          
import java.io.FileReader;
import java.io.BufferedReader;

public class PlayerDataManager{

    //This will set the player's data
    private final Map<Integer, PlayerData> playerMap = new HashMap<>();

    //This will add the player to the database
    public void addPlayer(PlayerData player){
        playerMap.put(player.getId(), player); //in this case the key = ID and value = playerData
    }
    //This will get the player's id
    public PlayerData getPlayer(int id){
        return playerMap.get(id);
    }
    //This will give the player a new Score/update the player's score
    public void updatePlayerScore(int id, int newScore){
        PlayerData player = playerMap.get(id);
        if(player != null){
            player.setScore(newScore);
        }
    }
    //This will go through all of the players and print each of them
    public void printAllPlayers(){
        for(PlayerData p : playerMap.values()){
            System.out.println(p);
        }
    }
    /*This method is meant to only write the data into the file when the user first comes in it will be stored
        it will be stored in a txt file called "PlayerStorageData.txt" this will
        keep track of things such as the player's name, id, score, and elapsed time

        *Update* It might be better just have the file be filename so that incase
        I change the Storage area it can still be used the same depending on the file
    */
    public void saveToFile(String filename){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(filename))){
            for(PlayerData p : playerMap.values()){
                //This will write all of the player's said data into file 
                writer.write(p.toString());
                //Creates a newline
                writer.newLine();
            }


        } catch(IOException e){
            System.err.println("Error writing into the file: " + e.getMessage());
        }
    }


    public void loadFromFile(String filename){
        File file = new File(filename);
        if(!file.exists()){
            return; //No fill then there isnt anything to load
        }
        try(BufferedReader reader = new BufferedReader(new FileReader(filename))){
            String line;
            while((line = reader.readLine()) != null){ //for each line in the file
                String[] parts = line.split(",");
                String playerName = parts[0];
                int playerID = Integer.parseInt(parts[1]);
                int score = Integer.parseInt(parts[2]);
                long time = Long.parseLong(parts[3]);

                PlayerData player = new PlayerData(playerName, playerID, score, time);
                playerMap.put(playerID, player);//This will actually add the player to the map
            }



        } catch(IOException e){
            System.err.println("Error reading this file: " + e.getMessage());
        }
    }





}