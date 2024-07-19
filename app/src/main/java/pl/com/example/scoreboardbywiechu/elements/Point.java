package pl.com.example.scoreboardbywiechu.elements;

public class Point {
    private Player player;
    private double time;
    private int part;
    private int numPoint;

    public Point(Player pl, double time, int numPart, int numPoint)
    {
        this.player=pl;
        this.time=time;
        this.part=numPart;
        this.numPoint = numPoint;
    }

    public Player getPlayer()
    {
        return this.player;
    }

    public double getTime() { return this.time; }

    public int getPart() { return this.part; }

    public int getNumPoint() {return this.numPoint;}
    //TODO:
    //Czy points to dobry wybor?
}
