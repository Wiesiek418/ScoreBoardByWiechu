package pl.com.example.scoreboardbywiechu.gamesSettings;


import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import pl.com.example.scoreboardbywiechu.elements.Pair;
import pl.com.example.scoreboardbywiechu.elements.Player;
import pl.com.example.scoreboardbywiechu.elements.points.Point;
import pl.com.example.scoreboardbywiechu.elements.points.PointsCalculator;

public class GameSettings {


    protected int numberOfPlayers;    //number of players in game
    protected int numberOfParts;      //number of parts in game
    protected int currentPart=1;      //current part
    protected String nameOfGame = "Own Game";   //name of the game
    private Player[] playerVector;  //player vector in which we have players info
    private int currentPlayers=0;   //how many players we have in current game if the number of current players is less that number of players dont start the game

    protected PointsCalculator pointsCalculator;
    protected long endTimeInMillis;
    protected long extraTimeinMillis;       //nie lepiej dac tam gdzie potrzebne? moze zostac nie przeszkadza na razie
    protected boolean isSetExtraTime=false;

    //public function sector
    public GameSettings(int numOfPlayers, int parts)
    {
        if(numOfPlayers<=0||parts<=0)
        {
            throw new IllegalArgumentException("Wrong arguments");
        }

        this.endTimeInMillis=30*1000;
        this.numberOfPlayers = numOfPlayers;
        this.numberOfParts = parts;
        this.playerVector = new Player[numOfPlayers];
        this.pointsCalculator = null;
    }

    public GameSettings(int numOfPlayers, int parts, long timeInMillis)
    {
        if(numOfPlayers<=0||parts<=0)
        {
            throw new IllegalArgumentException("Wrong arguments");
        }

        this.endTimeInMillis=timeInMillis;
        this.numberOfPlayers = numOfPlayers;
        this.numberOfParts = parts;
        this.playerVector = new Player[numOfPlayers];
        this.pointsCalculator = null;
    }
    //SETTER
    public void setEndTime(long timeInMillis)
    {
        this.endTimeInMillis=timeInMillis;
    }
    public void setPointsCalculator(PointsCalculator pc) {this.pointsCalculator=pc;}
    //GETTER
    public int getNumberOfPlayers()
    {
        return this.numberOfPlayers;
    }

    public int getNumberOfParts()
    {
        return this.numberOfParts;
    }

    public String getNameOfGame()
    {
        return this.nameOfGame;
    }

    public long getEndTime() { return this.endTimeInMillis+this.extraTimeinMillis; }    //end time with extra time
    public long getRegularEndTime() { return this.endTimeInMillis; }                    //end time without extra time
    public long getExtraTime() {return this.extraTimeinMillis; }                        //extra time in miliseconds
    public long getEndMinute() { return (this.endTimeInMillis/1000)/60; }               //regular end minute
    public long getEndSecond() { return (this.endTimeInMillis/1000)%60; }               //regular end seconds

    public int getCurrentPlayers() { return this.currentPlayers; }
    public int getCurrentPart() { return this.currentPart; }
    public boolean getExtraTimeIsSet() { return this.isSetExtraTime; }

    public Player getPlayer(int i)
    {
        if(i<currentPlayers)
            return this.playerVector[i];

        return null;
    }

    public List<Player> getPlayers()
    {
        return Arrays.asList(playerVector);
    }

    //add Player to the game
    //numberOfPlayers == max number of players in game
    //currentPlayers == current number of players in game
    //stupid idea but i dont have any other one
    //when we have full stack of players then we may start the game
    public void addPlayer(Player player)
    {
        if(currentPlayers>=numberOfPlayers)
        {
            throw new IllegalStateException("Too many players. Game is full!");
        }

        for(int i=0;i<currentPlayers;i++)
        {
            if(playerVector[i].equals(player))
            {
                throw new IllegalArgumentException("Player already exist.");
            }
        }

        playerVector[currentPlayers]=player;
        currentPlayers++;

    }

    public void nextPart()
    {
        if(currentPart<numberOfParts)
        {
            isSetExtraTime=false;
            extraTimeinMillis=0;
            currentPart++;
        }
    }

    protected void addPart(int howMany)
    {
        this.numberOfParts+=howMany;
    }

    public boolean isEnd()
    {
        if(currentPart>=numberOfParts)
        {
            return true;
        }

        nextPart();
        return false;
    }

}
