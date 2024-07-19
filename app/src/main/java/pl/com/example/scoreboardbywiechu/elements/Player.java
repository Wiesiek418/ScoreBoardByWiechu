package pl.com.example.scoreboardbywiechu.elements;

import androidx.annotation.Nullable;

public class Player {
    private String nameOfPlayer;
    private int points=0;

    public Player(String name)
    {
        if(name == null)
            this.nameOfPlayer="Guest";
        else
            this.nameOfPlayer=name;
    }

    public String getNameOfPlayer() {
        return nameOfPlayer;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(this==obj)
            return true;

        if(obj==null||getClass() != obj.getClass())
            return false;

        Player objPlayer = (Player) obj;

       return this.nameOfPlayer.equals(objPlayer.nameOfPlayer);
    }

    public void addPoints()
    {
        this.points++;
    }

    public void addPoints(int points)
    {
        this.points+=points;
    }

    public void removePoints()
    {
        this.points--;
    }

    public void removePoints(int points)
    {
        this.points-=points;
    }

    public int getPoints() {
        return points;
    }

    public void deletePoints() {
        if(this.points>0)
            this.points--;
    }

    public void deletePoints(int i) {
        if(this.points-i>=0)
            this.points-=i;
    }
}
