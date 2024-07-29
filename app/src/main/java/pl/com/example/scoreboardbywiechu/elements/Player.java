package pl.com.example.scoreboardbywiechu.elements;

import androidx.annotation.Nullable;

public class Player {
    private String nameOfPlayer;
    private int points=0;
    private int gems=0;
    private int sets=0;

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

    //SETTER
    public void setPoints(int points) {this.points=points;}
    public void setGems(int gems) {this.gems=gems;}
    public void setSets(int sets) {this.sets=sets;}

    //GETTER
    public int getPoints() {
        return points;
    }
    public int getGems() {
        return gems;
    }
    public int getSets() {
        return sets;
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

    //ADDER

    public void addPoints()
    {
        this.points++;
    }

    public void addPoints(int points) {this.points+=points;}

    public void addGems() {this.gems++;}
    public void addGemsEmptyPoints() {this.gems++; this.points=0;}

    public void addSets() {this.sets++;}
    public void addSetsEmptyGems() {this.sets++; this.gems=0; this.points=0;}


    //DELETER
    public void deletePoints() {
        if(this.points>0)
            this.points--;
    }

    public void deletePoints(int i) {
        if(this.points-i>=0)
            this.points-=i;
    }

    public void deleteGems() {
        if(this.gems>0)
            this.gems--;
    }
}
