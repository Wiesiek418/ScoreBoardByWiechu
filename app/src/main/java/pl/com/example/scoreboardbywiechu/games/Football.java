package pl.com.example.scoreboardbywiechu.games;

import java.util.Random;
import java.util.Vector;

public class Football extends GameSettings{

    private boolean flagRandomExtraTime = false;
    private boolean flagOvertime = false;
    private boolean flagPenalties = false;
    private boolean flagGoldGoal = false;
    static int[] CHANCE = {30,20,15,15,10,4,3,2,1};
    //private static int[] CHANCE = {50,40,10};
    private long overtimeTimeMinute = 15;

    private Vector penaltyScore;
    private boolean isPenaltyTime = false;

    public Football()
    {
        super(2,2);
        super.endTimeInMillis=45*60*1000;
        super.nameOfGame="Football";
    }

    //If Gold Goal you dont have a overtime and penalty
    //If Overtime you have penalty but dont have gold goal

    //fRandExtraTime = extra time is random
    //fOT = Is it overtime?
    //fPen = Is it penalties if is draw?
    //fGG = Is it gold goal mode?
    public Football(boolean fRandExtraTime,boolean fOT, boolean fPen, int overtimeTimeMinute)
    {
        super(2,2);
        super.nameOfGame="Football";
        this.overtimeTimeMinute=overtimeTimeMinute;
        this.flagRandomExtraTime=fRandExtraTime;
        this.flagOvertime=fOT;
        this.flagPenalties=fPen;
        this.flagGoldGoal=false;


        this.endTimeInMillis=45*60*1000;
    }

    public Football(boolean fRandExtraTime,boolean fOT, boolean fPen)
    {

        super(2,2);
        super.nameOfGame="Football";

        this.flagRandomExtraTime=fRandExtraTime;
        this.flagOvertime=fOT;
        this.flagPenalties=fPen;
        this.flagGoldGoal=false;

        this.endTimeInMillis=45*60*1000;

    }

    public Football(boolean fRandExtraTime, boolean fGG)
    {
        super(2,2);
        super.nameOfGame="Football";

        this.flagRandomExtraTime=fRandExtraTime;
        this.flagOvertime=false;
        this.flagPenalties=false;
        this.flagGoldGoal=fGG;


        this.endTimeInMillis=45*60*1000;
    }

    public Football(boolean fRandExtraTime)
    {
        super(2,2,10);
        super.nameOfGame="Football";

        this.flagRandomExtraTime=fRandExtraTime;
        this.flagOvertime=false;
        this.flagPenalties=false;
        this.flagGoldGoal=false;


        this.endTimeInMillis=45*60*1000;
    }

    //SETTER

    //extraTime
    public void setFlagRandomExtraTime(boolean flag)
    {
        this.flagRandomExtraTime=flag;
    }

    public void setFlagRandomExtraTime()
    {
        this.flagRandomExtraTime=!this.flagRandomExtraTime;
    }



    //overtime
    public void setFlagOvertime(boolean flag)
    {
        this.flagOvertime=flag;
    }

    public void setFlagOvertime()
    {
        this.flagOvertime=!this.flagOvertime;
    }

    //penalties
    public void setFlagPenalties(boolean flag)
    {
        this.flagPenalties=flag;
    }

    public void setFlagPenalties()
    {
        this.flagPenalties=!this.flagPenalties;
    }

    //gold goal
    public void setFlagGoldGoal(boolean flag)
    {
        this.flagGoldGoal=flag;
    }

    public void setFlagGoldGoal()
    {
        this.flagGoldGoal=!this.flagGoldGoal;
    }

    public void setOvertimeTime(int timeInMinute)
    {
        this.overtimeTimeMinute = timeInMinute;
    }



    //GETTER


    public long getOvertimeTime() {return this.overtimeTimeMinute; }

    public boolean getFlagRandomExtraTime()
    {
        return this.flagRandomExtraTime;
    }


    public void randomExtraTime()
    {
        if(this.flagRandomExtraTime)
        {
            this.extraTimeinMillis=chanceForTime()*1000*60;     //chanceForTime give us a time in minutes, so we need to change to miliseconds
            this.isSetExtraTime=true;

        }
    }


    @Override
    public boolean isEnd()
    {


        if(currentPart>=numberOfParts)
        {
            if(this.flagOvertime)
            {
                if(this.currentPart==2)
                {
                    if(isNextPart(2))
                    {
                        //TODO:poprawic
                        endTimeInMillis=endTimeInMillis/2;
                        nextPart();
                        return false;
                    }
                    else
                        return true;

                }
                else if(this.currentPart==4)
                {
                    if(isNextPart(1) && this.flagPenalties)
                    {
                        nextPart();
                        isPenaltyTime=true;
                        return false;
                    }
                    else
                        return true;
                }
            }
            else if(this.flagPenalties&&this.currentPart<=2)
            {
                if(isNextPart(1))
                {
                    nextPart();
                    isPenaltyTime=true;
                    return false;
                }
                else
                    return true;
            }
            else if(this.flagGoldGoal&&this.currentPart<=2)
            {
                if(isNextPart(1))
                {
                    nextPart();
                    return false;
                }
                else
                    return true;
            }
            else
                return true;
        }

        nextPart();
        return false;
    }

    //PRIVATE SECTOR

    //if is draw add extra parts (for overtime add 2 parts, for penalties 1 part, for gg 1 part)
    private boolean isNextPart(int extraParts)
    {
        int[] score = summaryScore();
        if(isDraw(score))
        {
            this.numberOfParts+=extraParts;
            return true;
        }
        else
        {
            return false;
        }
    }

    private boolean isDraw(int[] scores)
    {
        int curScore=scores[0];
        for(int i=0;i<scores.length;i++)
        {
            if(curScore!=scores[i])
                return false;
        }
        return true;
    }

    private int chanceForTime()
    {
        Random rand = new Random();
        int extra = Math.abs(rand.nextInt()%100);
        int sum=0;
        for(int i=0;i<CHANCE.length;i++)
        {
            sum+=CHANCE[i];
            if(extra<sum)
                return i;
        }
        return 0;
    }
}
