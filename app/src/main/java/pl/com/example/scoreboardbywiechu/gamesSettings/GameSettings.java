package pl.com.example.scoreboardbywiechu.gamesSettings;


import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import pl.com.example.scoreboardbywiechu.elements.Pair;
import pl.com.example.scoreboardbywiechu.elements.Player;
import pl.com.example.scoreboardbywiechu.elements.Point;

public class GameSettings {


    protected int numberOfPlayers;    //number of players in game
    protected int numberOfParts;      //number of parts in game
    protected int currentPart=1;      //current part
    protected String nameOfGame = "Own Game";   //name of the game
    private Player[] playerVector;  //player vector in which we have players info
    private int currentPlayers=0;   //how many players we have in current game if the number of current players is less that number of players dont start the game

    protected long endTimeInMillis;
    protected long extraTimeinMillis;       //nie lepiej dac tam gdzie potrzebne? moze zostac nie przeszkadza na razie
    protected boolean isSetExtraTime=false;

    private Vector<Point> pointVector;  //TODO: jak to przebudowac zeby dzialalo dla wszystkich gier


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
        pointVector = new Vector<>();

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
        pointVector = new Vector<>();

    }
    //SETTER
    public void setEndTime(long timeInMillis)
    {
        this.endTimeInMillis=timeInMillis;
    }

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

    public int getSizePointVector() { return this.pointVector.size(); }
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

    public Point getIndexPointVector(int i)
    {
        if(i<=pointVector.size())
        {
            return pointVector.get(i);
        }
        return null;
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




    //SCORE SECTION
    //GET SCORE as a Vector of pair in which we have a info about player and his score
    //every element of this vector is about one player so the size of vector == number of the players
    public Vector<Pair<Player, Integer>> getScore()
    {
        Vector<Pair<Player,Integer>> vectorScores = new Vector<>();
        for(Player player:playerVector)
        {
            int score = player.getPoints();
            Pair<Player,Integer> newPair = new Pair<>(player,score);
            vectorScores.add(newPair);
        }
        return vectorScores;
    }

    //get a score table in which for one index is like one person and the element of this index is score from player class
    public int[] summaryScore()
    {
        int[] score = new int[numberOfPlayers];

        for(int i=0;i<numberOfPlayers;i++)
        {
            score[i]=this.playerVector[i].getPoints();
        }
        return score;
    }

    //removing last point from player
    public void removePointFrom(Player player)
    {
        for(int i=0;i<numberOfPlayers;i++)
        {
            if(playerVector[i].equals(player))
            {
                playerVector[i].deletePoints();
                removeFromPointsVector(player);
            }
        }
    }

    //add point to point Vector and to the player from player vector
    //in point vector we get a Point class which has a info about who score, when and in which part
    public void addPointTo(Player player, double time)
    {
        for(int i=0;i<numberOfPlayers;i++)
        {
            if(playerVector[i].equals(player))
            {
                playerVector[i].addPoints();    //add a point to the player class where we have a number of total points which player has
                Point point = new Point(player,time,currentPart,currentPart,playerVector[i].getPoints());   //TODO: POPRAWIC
                pointVector.add(point);
            }
        }
    }

    //get a summary score from one part for one player
    private int scorePlayerForOnePart(Player player,int part)
    {
        int counter=0;
        for(Point point: pointVector)
        {
            if(player.equals(point.getPlayer())&&point.getSet()==part)
                counter++;
        }
        return counter;
    }

    //delete a last point from point vector in which is player id
    private void removeFromPointsVector(Player player)
    {
        // we looking for a last point for player
        for (int i = this.pointVector.size() - 1; i >= 0; i--) {
            if (player.equals(this.pointVector.get(i).getPlayer()))
            {
                this.pointVector.remove(i);
                return;
            }
        }
    }

}
