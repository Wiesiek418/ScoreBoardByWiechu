package pl.com.example.scoreboardbywiechu.elements.points;

import java.util.ArrayList;
import java.util.List;

import pl.com.example.scoreboardbywiechu.elements.Player;

//Gems (list of the points in one gem)
public class Gems
{
    private Player player;   //winner of the gem
    private List<Point> pointList;
    private int numOfGem;
    private Point winnerPoint;      //test

    Gems()
    {
        this.player = null;
        this.pointList = new ArrayList<>();
        this.numOfGem=1;
        this.winnerPoint=null;  //test
    }

    //przemysl jeszcze
    Gems(Point point)
    {
        this.player = point.getPlayer();
        this.pointList = new ArrayList<>();
        this.pointList.add(point);
        this.numOfGem = point.getPlayer().getGems();
        this.winnerPoint=point; //test
    }

    public void setWinner(Player player, long time)
    {
        this.player=player;
        this.numOfGem=player.getGems();
        this.winnerPoint = new Point(player,time,this.numOfGem);
    }

    public Player getPlayer()
    {
        return this.player;
    }

    public long getTime()
    {
        if(winnerPoint!=null)
        {
            return winnerPoint.getTime();
        }
        else
            return -1;
    }
    public Point getWinnerPoint() {return this.winnerPoint;}
    public List<Point> getList() {return this.pointList;}
    public Player getPlayerFromList(int index){return this.pointList.get(index).getPlayer();}
    public int getNumOfGem(){return this.numOfGem;}
    public void addPoint(Point point)
    {
        pointList.add(point);
    }

    //test
    public void winnerPoint(Point point)
    {
        this.player=point.getPlayer();
        this.numOfGem=player.getGems();
        this.winnerPoint=point; //test
    }

    public int pointsOfPlayer(Player player)
    {
        for(int i=pointList.size()-1;i>=0;i--)
        {
            if(pointList.get(i).getPlayer().equals(player))
            {
                return pointList.get(i).getNumPoint();
            }
        }
        return 0;
    }

}