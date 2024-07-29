package pl.com.example.scoreboardbywiechu.elements;

import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pl.com.example.scoreboardbywiechu.R;
import pl.com.example.scoreboardbywiechu.layouts.gameActivities.MainActivity;

public class PointsCalculator {


    //Set (list of the gems in one set)
    private class Sets
    {
        private Player player;   //winner of the set
        private List<Gems> gemsList;

        //przemysl jeszcze
        Sets(Point point)
        {
            this.player = point.getPlayer();
            this.gemsList = new ArrayList<>();
            this.gemsList.add(new Gems(point));
        }

        Sets()
        {
            this.player = null;
            this.gemsList = new ArrayList<>();
        }

        public void setWinner(Player player)
        {
            this.player=player;
        }
        public List<Gems> getList() {return this.gemsList;}

        public void addGem(Gems gems)
        {
            gemsList.add(gems);
        }

        public void removeGemFromPlayer(Player player)
        {
            for (int i = this.gemsList.size() - 1; i >= 0; i--) {
                if (this.gemsList.get(i).getPlayer().equals(player))
                {
                    this.gemsList.remove(i);
                    player.deleteGems();
                    return;
                }
            }
        }

    }

    //Gems (list of the points in one gem)
    private class Gems
    {
        private Player player;   //winner of the gem
        private List<Point> pointList;

        Gems()
        {
            this.player = null;
            this.pointList = new ArrayList<>();
        }

        //przemysl jeszcze
        Gems(Point point)
        {
            this.player = point.getPlayer();
            this.pointList = new ArrayList<>();
            this.pointList.add(point);
        }

        public void setWinner(Player player)
        {
            this.player=player;
        }

        public Player getPlayer()
        {
            return this.player;
        }
        public List<Point> getList() {return this.pointList;}
        public void addPoint(Point point)
        {
            pointList.add(point);
        }

        public void removePointFromPlayer(Player player)
        {
            for (int i = this.pointList.size() - 1; i >= 0; i--) {
                if (this.pointList.get(i).getPlayer().equals(player))
                {
                    this.pointList.remove(i);
                    player.deletePoints();
                    return;
                }
            }
        }

    }


    // SETS>>GEM>>POINTS
    private final int LAST_SCORE = 7;
    private int advantagesLimit = 2;

    private boolean gemsMode;
    private boolean pointsMode;
    private boolean setsMode;

    private boolean playWithAdvantages;
    private int maxPointsLimit=10;    //how many sets you need to win a game
    private int maxGemsLimit=6;    //how many gems you need to win a set
    private int maxSetsLimit=3;

    private short pointsCountMode;      //0 - normal mode 1,2,3,...maxPoints; 1 - tennis mode 0,15,30,40,45; 2 - idk (maybe basketball mode)

    private boolean tiebreakMode;       //true - is 6-6 and you have to play tiebreak, false - is not time for tiebreak

    private List<Sets> gameHistory;
    private Sets setHistory;
    private Gems gemHistory;

    //maybe add a only two player (two player limiter)
    private List<Player> playerList;

    private int currentGem=0;
    private int currentSet=0;

    private MainActivity mainActivity;

    /*
    TODO:
        historia teraz tutaj a nie w innych miejscach
        musze wszystko ladnie posprzatac
        bo syf
    */


    //TODO: dodac usuwanie punktow
    public PointsCalculator(int maxSets,int maxGems,int maxPoints, boolean setsMode, boolean gemsMode, boolean pointsMode, List<Player> playerList,MainActivity mainActivity)
    {
        this.maxSetsLimit = maxSets;
        this.maxGemsLimit = maxGems;
        this.setsMode = setsMode;
        this.gemsMode = gemsMode;
        this.pointsMode = pointsMode;

        this.playerList = playerList;

        gemHistory = new Gems();
        gameHistory = new ArrayList<>();
        setHistory = new Sets();
        playWithAdvantages=true;   //temporary

        this.mainActivity = mainActivity;
    }

    //nie lepiej jakby znikaly dodatkowe wyswietlacze od dolu to znaczy ze jak mamy tylko punkty to beda one dzialac jako sety?
    public void setDisplayPoints()
    {
        TextView setView = mainActivity.findViewById(R.id.setView);
        TextView gemView = mainActivity.findViewById(R.id.gemView);
        TextView pointsView = mainActivity.findViewById(R.id.pointsView);

        if(setsMode)
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
    public void addPointToPlayer(Player player, long time)
    {
        if(pointsCountMode==1)
            tennisPointsAdder(player,time);
        else
            normalPointsAdder(player,time);
    }

    public void displayScore()
    {
        TextView setsView;
        TextView gemView;
        TextView pointsView;

        if(setsMode)
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

    public void displayLastScoreHistory(short modeDisplay)    //modeDisplay: 0-points; 1-gems; 2-sets
    {
        List<?> history = null;

        switch (modeDisplay)
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

                if(!gemsMode&&!pointsMode)
                    addSet(findPlayer);    //add a point to the player
                else if(!pointsMode)
                    addGem(findPlayer);
                else
                {
                    addPoint(findPlayer,time);
                    return;
                }


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
                Point point = new Point(player,time,currentSet,currentGem,findPlayer.getPoints());
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

    private void addPoint(Player player,long time)
    {
        player.addPoints();
        Point point = new Point(player,time,currentSet,currentGem,player.getPoints());  //to change (do more elastic for the every adder)
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
                addGem(player);
            }
        }
    }

    private void addGem(Player player)
    {
        player.addGems();
        gemHistory.setWinner(player);
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
                addSet(player);
            }
        }
    }

    private void addSet(Player player)
    {
        player.addSets();
        setHistory.setWinner(player);
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


}
