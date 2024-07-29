package pl.com.example.scoreboardbywiechu.elements;

public class Point {
    private Player player;
    private double time;
    private int setNumber;
    private int gemNumber;
    private int numPoint;

    public Point(Player pl, double time, int setNumber,int gemNumber, int numPoint)
    {
        this.player=pl;
        this.time=time;
        this.setNumber = setNumber;
        this.gemNumber = gemNumber;
        this.numPoint = numPoint;
    }

    public Player getPlayer()
    {
        return this.player;
    }

    public double getTime() { return this.time; }

    public int getSet() { return this.setNumber; }
    public int getGem() { return this.gemNumber; }

    public int getNumPoint() {return this.numPoint;}
    //TODO:
    //Czy points to dobry wybor?
}
