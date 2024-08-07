package pl.com.example.scoreboardbywiechu.elements.points;

import java.util.ArrayList;
import java.util.List;

import pl.com.example.scoreboardbywiechu.elements.Player;

//Set (list of the gems in one set)
public class Sets
{
    private Player player;   //winner of the set
    private List<Gems> gemsList;
    private int numOfSet;
    private Point winnerPoint;

    //przemysl jeszcze
    Sets(Point point)
    {
        this.player = point.getPlayer();
        this.gemsList = new ArrayList<>();
        this.gemsList.add(new Gems(point));
        this.winnerPoint = point;
    }

    Sets()
    {
        this.player = null;
        this.gemsList = new ArrayList<>();
        this.numOfSet=0;
        this.winnerPoint=null;
    }

    public void setWinner(Player player,long time)
    {
        this.player=player;
        this.numOfSet=player.getSets();
        this.winnerPoint = new Point(player,time,numOfSet);
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
    public List<Gems> getList() {return this.gemsList;}
    public Player getPlayerFromList(int index){return this.gemsList.get(index).getPlayer();}
    public int getNumOfSet(){return this.numOfSet;}

    public void addGem(Gems gems)
    {
        gemsList.add(gems);
    }

        /*public void removeGemFromPlayer(Player player)
        {
            for (int i = this.gemsList.size() - 1; i >= 0; i--) {
                if (this.gemsList.get(i).getPlayer().equals(player))
                {
                    this.gemsList.remove(i);
                    player.deleteGems();
                    return;
                }
            }
        }*/

    public int gemsOfPlayer(Player player)
    {
        for(int i=gemsList.size()-1;i>=0;i--)
        {
            if(gemsList.get(i).getPlayer().equals(player))
            {
                return gemsList.get(i).getNumOfGem();
            }
        }
        return 0;
    }

}
