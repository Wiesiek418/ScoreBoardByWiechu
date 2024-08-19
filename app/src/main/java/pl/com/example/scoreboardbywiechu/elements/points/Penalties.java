package pl.com.example.scoreboardbywiechu.elements.points;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

import pl.com.example.scoreboardbywiechu.elements.Player;

public class Penalties {
    private int numberOfCurrentTurn;
    private boolean isFinalTurn;    //final turn if first players score and second not finish game
    //in final turn end game for the players which do not score a point
    private int turnLimiter; //to x turn without a final turn
    private List<Player> playerList;    //players who always play
    private List<Player> eliminatedPlayerList;    //players who is eliminated
    private int numberOfPlayers;
    private int currentPlayer;

    private List<int []> turnHistory;
    private int[] pointsPlayers;


    public Penalties()
    {
        numberOfCurrentTurn = 1;
        isFinalTurn = false;
        turnLimiter = 5;
        playerList = new ArrayList<>();
        currentPlayer=0;
        turnHistory = new ArrayList<>();
        eliminatedPlayerList = new ArrayList<>();
    }

    public int[] getPoints()
    {
        return pointsPlayers;
    }

    public int getPlayerPoints(Player player)
    {
        if(isInPlayerList(player))
        {
            return pointsPlayers[playerList.indexOf(player)];
        }

        return 0;
    }

    public void setPlayerList(List<Player> playerList)
    {
        this.playerList = playerList;
        numberOfPlayers=playerList.size();
        initializeNewTurnHistoryList();
        initializePointsPlayers();
    }

    public void addPoint(Player player) //player score point
    {
        boolean findPlayer = isInPlayerList(player) && !eliminatedPlayerList.contains(player)  && !isEndGame(); //is in game and is not eliminated

        if(findPlayer)
        {
            int playerIndex = playerList.indexOf(player);
            turnHistory.get(turnHistory.size()-1)[playerIndex]=1;   //score information ("goal scored")
            pointsPlayers[playerIndex]++;
            endGameOfPlayersNotChance();
            startNewTurn();
        }
        /*else
        {
            throw new IllegalArgumentException("Player not found or is eliminated");
        }*/
    }

    public void missingPoint(Player player)
    {
        boolean findPlayer = isInPlayerList(player) && !eliminatedPlayerList.contains(player)  && !isEndGame(); //is in game and is not eliminated

        if(findPlayer)
        {
            int playerIndex = playerList.indexOf(player);
            turnHistory.get(turnHistory.size()-1)[playerIndex]=0;   //score information ("goal scored")
            endGameOfPlayersNotChance();
            startNewTurn();

        }
        //TODO: do dodania opcje wykrywania czy gracz usuniety
        /*else
        {
            throw new IllegalArgumentException("Player not found");
        }*/
    }


    //return true if player is in eliminated list
    public boolean isEliminated(Player player)
    {
        return eliminatedPlayerList.contains(player);
    }


    public boolean isEndGame()
    {
        int eliminatedPlayers = eliminatedPlayerList.size();
        return numberOfPlayers - eliminatedPlayers <= 1;
    }



    private boolean isInPlayerList(Player player)
    {
        return playerList.stream().anyMatch(x -> x.equals(player));
    }

    private void endGameOfPlayersNotChance()
    {
        int[] currentTurn = turnHistory.get(turnHistory.size()-1);
        int max=0;
        for(int number: pointsPlayers)
        {
            max = Math.max(number, max);
        }


        for(int i=0;i<numberOfPlayers;i++)
        {
            int prob;
            if(currentTurn[i]>=0)
            {
                prob = turnLimiter - numberOfCurrentTurn + pointsPlayers[i];
            }
            else
            {
                prob = turnLimiter - numberOfCurrentTurn + pointsPlayers[i]+1;
            }

            if(prob<max)
            {
                if(!eliminatedPlayerList.contains(playerList.get(i)))
                    eliminatedPlayerList.add(playerList.get(i));
            }
        }
    }

    //starczylby zwykly stream ale chce sie pobawic z pula watkow
    //stream is enough but I am student so I well fun with the thread pool
    private int[] pointsOfPlayers() throws ExecutionException, InterruptedException
    {
        int[] points = new int[numberOfPlayers];

        ExecutorService executor = Executors.newFixedThreadPool(4); //number of threads

        List<Future<Integer>> futuresResult = new ArrayList<>();

        for(int i=0;i<numberOfPlayers;i++)
        {
            final int playerIndex = i;

            Callable<Integer> task = () ->{
                int playerScore=0;
                for(int j=0;j<numberOfCurrentTurn;j++)
                {
                    int score = turnHistory.get(j)[playerIndex];
                    if(score==0 || score==1)
                        playerScore+=turnHistory.get(j)[playerIndex];
                    else break;
                }

                return playerScore;
            };
            futuresResult.add(executor.submit(task));
        }

        for(int i=0;i<numberOfPlayers;i++)
        {
            points[i]=futuresResult.get(i).get();
        }

        executor.shutdown();
        return points;
    }

    private int[] pointsOfPlayersOptimalization()   //version of pointsOfPlayer with stream method
    {
        int[] points = IntStream.range(0,numberOfPlayers).map(playerIndex -> {
           int playerScore = 0;
           for(int i=0;i<numberOfCurrentTurn;i++)
           {
               int score = turnHistory.get(i)[playerIndex];
               if(score == 0 || score == 1)
               {
                   playerScore+=score;
               }
               else
                   break;
           }

           return playerScore;
        }).toArray();

        return points;
    }


    //-1 - not shot yet, 0 - not goal, 1 - goal
    //create a new turn array
    private void initializeNewTurnHistoryList()
    {
        int[] initializeTurn =new int[numberOfPlayers];
        for(int i=0;i<numberOfPlayers;i++)
        {
            initializeTurn[i]=-1;
        }
        turnHistory.add(initializeTurn);
    }

    private void initializePointsPlayers()
    {
        pointsPlayers = new int[numberOfPlayers];
        for(int i=0;i<numberOfPlayers;i++)
        {
            pointsPlayers[i]=0;
        }
    }

    private boolean isTurnFinished()
    {
        int[] currentTurn = turnHistory.get(turnHistory.size()-1);

        //Find index of the last player who is not eliminated
        for(int i=numberOfPlayers-1;i>=0;i--)
        {
            Player player = playerList.get(i);
            if(!eliminatedPlayerList.contains(player))
            {
                return currentTurn[i] != -1;    //if player not shot return false if shot (0 or 1) return true
            }
        }
        return false;
    }

    //if everybody finish and is not end game create a new turn
    private void startNewTurn()
    {
        if(isTurnFinished()&&!isEndGame())
        {
            numberOfCurrentTurn++;
            initializeNewTurnHistoryList();
            if(numberOfCurrentTurn>=turnLimiter)
            {
                turnLimiter=numberOfCurrentTurn;
            }
        }
    }
}
