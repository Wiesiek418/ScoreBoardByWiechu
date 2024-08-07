package pl.com.example.scoreboardbywiechu.elements.points;

import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import pl.com.example.scoreboardbywiechu.R;
import pl.com.example.scoreboardbywiechu.elements.Player;
import pl.com.example.scoreboardbywiechu.layouts.gameActivities.MainActivity;

public class PointsCalculator {
    //TODO: w miare ogarniety syf teraz czas na wieksze porzadki i rozwoj dalszy
    // SETS>>GEM>>POINTS
    // USUNAC POZOSTALOSCI PO STARYCH PUNKTACH
    private final int LAST_SCORE = 5;
    private int advantagesLimit = 2;

    private byte gameMode;          //game mode - binary number (xyz) x-bit "set mode on/off"; y-bit "gem mode on/off"; z-bit "point mode on/off"

    private boolean setMode;
    private boolean gemsMode;
    private boolean pointsMode;


    private boolean playWithAdvantages;
    private int maxPointsLimit=10;    //how many sets you need to win a game
    private int maxGemsLimit=6;    //how many gems you need to win a set
    private int maxSetsLimit=3;

    private short pointsCountMode;      //0 - normal mode 1,2,3,...maxPoints; 1 - tennis mode 0,15,30,40,45; 2 - idk (maybe basketball mode)

    //TODO: do dodania
    private boolean tiebreakMode;       //true - is 6-6 and you have to play tiebreak, false - is not time for tiebreak

    private List<Sets> gameHistory;
    private Sets setHistory;
    private Gems gemHistory;

    //maybe add a only two player (two player limiter)
    private List<Player> playerList;

    private MainActivity mainActivity;

    public PointsCalculator()
    {
        this.gameMode = 0b111;
        setGameMode(this.gameMode);

        this.maxSetsLimit = Integer.MAX_VALUE;
        this.maxGemsLimit = Integer.MAX_VALUE;
        this.maxPointsLimit = Integer.MAX_VALUE;

        this.playerList = null;

        gemHistory = new Gems();
        gameHistory = new ArrayList<>();
        setHistory = new Sets();


        playWithAdvantages=false;

        this.mainActivity = null;
    }


    //TODO: poprawic konstruktor
    //game mode - binary number (xyz) x-bit "set mode on/off"; y-bit "gem mode on/off"; z-bit "point mode on/off"
    public PointsCalculator(int maxSets,int maxGems,int maxPoints, byte gameMode, List<Player> playerList, MainActivity mainActivity)
    {
        //ARGUMENT CHECKER
        if(gameMode < 0 || gameMode > 7)
        {
            throw new IllegalArgumentException("Illegal game mode number. Accept only 3 bit numbers");
        }

        //GAME MODE CONFIG
        setGameMode(gameMode);

        //LIMITER
        this.maxSetsLimit = maxSets;
        this.maxGemsLimit = maxGems;
        this.maxPointsLimit = maxPoints;


        this.playerList = playerList;

        gemHistory = new Gems();
        gameHistory = new ArrayList<>();
        setHistory = new Sets();


        playWithAdvantages=false;

        this.mainActivity = mainActivity;
    }

    public void setGameMode(byte gameMode)
    {
        this.gameMode = gameMode;
        this.setMode = (gameMode & 0b100) != 0;
        this.gemsMode = (gameMode & 0b010) != 0;
        this.pointsMode = (gameMode & 0b001) != 0;
    }

    public void setAdvantagesLimit(int limit)
    {
        this.advantagesLimit=limit;
    }
    public void setAdvantages(boolean mode)
    {
        this.playWithAdvantages=mode;
    }

    //nie lepiej jakby znikaly dodatkowe wyswietlacze od dolu to znaczy ze jak mamy tylko punkty to beda one dzialac jako sety?
    public void setDisplayPoints()
    {
        if(mainActivity==null)
            throw new IllegalStateException("MainActivity variable is null");

        TextView setView = mainActivity.findViewById(R.id.setView);
        TextView gemView = mainActivity.findViewById(R.id.gemView);
        TextView pointsView = mainActivity.findViewById(R.id.pointsView);

        if(setMode)
            setView.setVisibility(View.VISIBLE);
        else
            setView.setVisibility(View.GONE);

        if(gemsMode)
            gemView.setVisibility(View.VISIBLE);
        else
            gemView.setVisibility(View.GONE);

        if(pointsMode)
            pointsView.setVisibility(View.VISIBLE);
        else
            pointsView.setVisibility(View.GONE);
    }


    public int[] getTheUpperScore()
    {
        int sizeList = playerList.size();
        int score[] = new int[sizeList];
        if(setMode)
        {
            for(int i=0;i<sizeList;i++)
            {
                score[i] = playerList.get(i).getSets();
            }
            return score;
        }
        else if(gemsMode)
        {
            for(int i=0;i<sizeList;i++)
            {
                score[i] = playerList.get(i).getGems();
            }
            return score;
        }
        else if(pointsMode)
        {
            for(int i=0;i<sizeList;i++)
            {
                score[i] = playerList.get(i).getPoints();
            }
            return score;
        }
        return null;
    }


    public void addPointToPlayer(Player player, long time)
    {
        if(playerList==null)
            throw new IllegalStateException("PlayerList variable is null");

        if(pointsCountMode==1)
            tennisPointsAdder(player,time); //not yet TODO soon
        else
            normalPointsAdder(player,time);
    }


    public void removePointFromPlayer(Player player)
    {
        if(!gemHistory.getList().isEmpty())
        {
            deletePoint(player);
        }
        else if(!setHistory.getList().isEmpty())
        {
            deleteGem(player);
        }
        else if(!gameHistory.isEmpty())
        {
            deleteSet(player);
        }

    }


    public void deletePoint(Player player)
    {
        int index = findLastIndex(gemHistory.getList(),player);

        if(index!=-1)
        {
            gemHistory.getList().remove(index);
            player.deletePoints();
        }

    }


    public void deleteGem(Player player)
    {
        int index = findLastIndex(setHistory.getList(),player);
        if(index!=-1)
        {
            //lastGemBack();
            setHistory.getList().remove(index);
            player.deleteGems();
        }

    }


    public void deleteSet(Player player)
    {

        int index = findLastIndex(gameHistory,player);
        if(index!=-1)
        {
            //lastSetBack();
            //lastGemBack();
            gameHistory.remove(index);
            player.deleteSet();
        }
    }


    public void displayScore()
    {
        if(mainActivity==null)
            throw new IllegalStateException("MainActivity variable is null");

        displayLastScoreHistory((short) 0);         //test
        TextView setsView;
        TextView gemView;
        TextView pointsView;

        if(setMode)
        {
            setsView = mainActivity.findViewById(R.id.setView);
            StringBuilder sb = new StringBuilder();

            for(int i=0;i<playerList.size();i++)
            {
                if(i!=0)
                {
                    sb.append(":");
                }
                sb.append(playerList.get(i).getSets());
            }
            setsView.setText(sb.toString());
        }
        if(gemsMode)
        {
            gemView = mainActivity.findViewById(R.id.gemView);
            StringBuilder sb = new StringBuilder();

            for(int i=0;i<playerList.size();i++)
            {
                if(i!=0)
                {
                    sb.append(":");
                }
                sb.append(playerList.get(i).getGems());
            }
            gemView.setText(sb.toString());
        }
        if(pointsMode)
        {
            pointsView = mainActivity.findViewById(R.id.pointsView);
            StringBuilder sb = new StringBuilder();

            for(int i=0;i<playerList.size();i++)
            {
                if(i!=0)
                {
                    sb.append(":");
                }
                sb.append(playerList.get(i).getPoints());
            }
            pointsView.setText(sb.toString());
        }
    }

    //TODO: do poprawy kiedys
    public void displayLastScoreHistory(short modeDisplay)    //modeDisplay: 0-points; 1-gems; 2-sets
    {
        if(mainActivity==null)
            throw new IllegalStateException("MainActivity variable is null");

        mainActivity.leftHistory.removeAllViews();
        mainActivity.rightHistory.removeAllViews();
        List<?> history = getListScore(modeDisplay);

        //TODO: wyswietlanie zrobic w main activity albo jakiejs nowej klasie
        //      chce aby w point calculator bylo wszystko co zwiazane z liczeniem wyniku a nie wyswietlaniem go
        int sizeOfList = history.size();
        int howManyLastPoints = Math.min(LAST_SCORE,sizeOfList);

        for(int i=sizeOfList-1;i>=(sizeOfList-howManyLastPoints);i--)
        {
            Point point = getPoint(history,i);
            if(point==null)
                break;

            TextView scoreView = new TextView(mainActivity);
            TextView scoreViewBlank = new TextView(mainActivity);

            scoreView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            scoreViewBlank.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            scoreView.setBackgroundColor(ContextCompat.getColor(mainActivity,R.color.scorePoint));
            int size = mainActivity.getResources().getDimensionPixelSize(R.dimen.pointSize);

            scoreView.getLayoutParams().height=size;
            scoreView.getLayoutParams().width=size;
            scoreViewBlank.getLayoutParams().height=size;
            scoreViewBlank.getLayoutParams().width=size;

            scoreView.setText(String.valueOf(point.getNumPoint()));
            scoreView.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);
            scoreView.setGravity(Gravity.CENTER);


            //TODO: poprawic jakos
            if(point.getPlayer().equals(mainActivity.leftPlayer))
            {
                mainActivity.leftHistory.addView(scoreView);
                mainActivity.rightHistory.addView(scoreViewBlank);
            }
            else
            {
                mainActivity.rightHistory.addView(scoreView);
                mainActivity.leftHistory.addView(scoreViewBlank);
            }
        }

    }

    private List<?> getListScore(short mode)
    {
        List<?> history;

        switch (mode)
        {
            case 0:
                history = gemHistory.getList();
                break;
            case 1:
                history = setHistory.getList();
                break;
            default:
                history = gameHistory;
                break;
        }
        return history;
    }

    private Point getPoint(List<?> history, int index)
    {
        if(history==gemHistory.getList())
        {
            return gemHistory.getList().get(index);
        }
        else if(history == setHistory.getList())
        {
            return setHistory.getList().get(index).getWinnerPoint();
        }
        else if(history == gameHistory)
        {
            return gameHistory.get(index).getWinnerPoint();
        }
        return null;
    }

    //TODO: dopracowac game mode czyli wybor czy tylko sety, czy sety i punkty, czy sety i gemy itp
    private void normalPointsAdder(Player player,long time)
    {
        int numberOfPlayers = playerList.size();
        for(int i=0;i<numberOfPlayers;i++)
        {
            if(playerList.get(i).equals(player))
            {
                Player findPlayer = playerList.get(i);

                if((gameMode&0b001) != 0)
                    addPoint(findPlayer,time);  //add a point to the player
                else if((gameMode&0b010) != 0)
                    addGem(findPlayer,time);    //add a gem to the player if point is disabled
                else
                    addSet(findPlayer,time);    //add a set to the player if gem is disabled

                return;
            }
        }
    }

    private void tennisPointsAdder(Player player,long time)
    {
        int numberOfPlayers = playerList.size();
        for(int i=0;i<numberOfPlayers;i++)
        {
            if(playerList.get(i).equals(player))
            {
                Player findPlayer = playerList.get(i);
                addTennisPointToPlayer(findPlayer);
                Point point = new Point(player,time,findPlayer.getPoints());
                gemHistory.addPoint(point);

                //check if is end
                return;
            }
        }
    }

    private void addTennisPointToPlayer(Player player)
    {
        int getPlayerPoints = player.getPoints();
        switch(getPlayerPoints)
        {
            case 0:
                player.setPoints(15);
            case 15:
                player.setPoints(30);
            case 30:
                player.setPoints(40);
            case 'A':
                player.setPoints(45);
            case 40:
            {
                boolean isSet=false;
                for(Player other: playerList) {
                    if (other.getPoints() == 'A' && !other.equals(player)) {
                        other.setPoints(40);
                        player.setPoints('A');
                        isSet = true;
                    }
                }
                if(!isSet)
                {
                    player.setPoints(45);
                }
            }
        }
    }


    //TODO:: dodac przypadek jakby nie bylo posredniego
    //Only add point to point counter
    private void addPoint(Player player,long time)
    {
        player.addPoints();
        Point point = new Point(player,time,player.getPoints());
        gemHistory.addPoint(point);

        if(player.getPoints()>=maxPointsLimit)
        {
            boolean hasAdvantages = true;       //potential winner of the gem
            if(playWithAdvantages)
            {
                for(Player other: playerList)
                {
                    if(!other.equals(player) && (player.getPoints() - other.getPoints())<advantagesLimit)   //check if it is another player and a difference between other player and that player is enough big
                    {
                        hasAdvantages=false;
                        break;
                    }
                }
            }

            if(hasAdvantages)   //if is winner add gems and end this part
            {
                addGem(player,time);
            }
        }
    }

    private void addGem(Player player,long time)
    {

        player.addGems();
        gemHistory.setWinner(player,time);
        setHistory.addGem(gemHistory);
        gemHistory=new Gems();
        clearPointsOfPlayers();

        if(player.getGems()>=maxGemsLimit)
        {
            boolean advantages = true;

            //more option in plans  (tiebreak etc)
            if(playWithAdvantages)
            {
                for(Player other: playerList)
                {
                    if(!other.equals(player) && (player.getGems() - other.getGems())<advantagesLimit)
                    {
                        advantages=false;
                        break;
                    }
                }
            }

            if(advantages)
            {
                addSet(player,time);
            }
        }
    }

    private void addSet(Player player,long time)
    {
        player.addSets();
        setHistory.setWinner(player,time);
        setHistory.addGem(gemHistory);
        gameHistory.add(setHistory);
        gemHistory=new Gems();
        setHistory=new Sets();
        clearGemsOfPlayers();

        if(player.getSets()>=maxSetsLimit)
        {
            //TODO: add finish logic
            return;
        }
    }


    private void clearPointsOfPlayers()
    {
        for(Player p: playerList)
        {
            p.setPoints(0);
        }
    }

    private void clearGemsOfPlayers()
    {
        for(Player p: playerList)
        {
            p.setGems(0);
        }
    }

    //TODO: last poprawic albo usunac
    //add to players points from the last gem
    private void lastGemBack()
    {
        Sets lastSet = setHistory;
        gemHistory = lastSet.getList().get(lastSet.getList().size()-1);
        for(Player p: playerList)
        {
            p.setPoints(gemHistory.pointsOfPlayer(p));
        }
    }

    //add to players gems from the last set
    private void lastSetBack()
    {
        Sets lastSet = gameHistory.get(gameHistory.size()-1);
        for(Player p: playerList)
        {
            p.setGems(lastSet.gemsOfPlayer(p));
        }
    }

    private int findLastIndex(List<?> list,Player player)
    {
        for(int i = list.size()-1;i>=0;i--)
        {
            if(getPlayerFromList(list,i).equals(player))
            {
                return i;
            }
        }
        return -1;
    }

    private Player getPlayerFromList(List<?> list, int index) {
        if (list == gemHistory.getList()) {
            return gemHistory.getPlayerFromList(index);
        } else if (list == setHistory.getList()) {
            return setHistory.getPlayerFromList(index);
        } else if (list == gameHistory) {
            return gameHistory.get(index).getPlayer();
        }
        return null;
    }



}
