package pl.com.example.scoreboardbywiechu.elements.points;

import pl.com.example.scoreboardbywiechu.elements.Player;

public class Point {
    private Player player;  //data about a player who score the point
    private long time;    //time when he score the point
    private int numPoint;   //number of the point

    public Point(Player pl, long time,int numPoint)
    {
        this.player=pl;
        this.time=time;
        this.numPoint = numPoint;
    }


    public Player getPlayer()
    {
        return this.player;
    }

    public long getTime() { return this.time; }

    public int getNumPoint() {return this.numPoint;}

}


