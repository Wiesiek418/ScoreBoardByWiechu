package pl.com.example.scoreboardbywiechu.elements;

import android.os.Handler;
import android.os.Looper;

import pl.com.example.scoreboardbywiechu.layouts.MainActivity;

public class GameTime extends Thread
{
    private MainActivity gameActivity;

    private long endTime;
    private boolean paused = false;
    private Handler handler;
    private long elapsedtime;
    private long pausedTime=0;
    private long startTime=0;

    public GameTime(MainActivity gameActivity,long endTimeInMillis)
    {
        this.gameActivity=gameActivity;

        this.endTime=endTimeInMillis;

        this.handler=new Handler(Looper.getMainLooper());

    }

    //when we need change a end time
    public synchronized void changeEndTime(long extraTimeInMillis)
    {
        this.endTime+=(extraTimeInMillis);
    }


    public synchronized void stopTime()
    {
        if(!paused)
        {
            this.paused=true;
            pausedTime=System.currentTimeMillis();
        }
    }
    public synchronized void startTimeBeforeStop()
    {
        if(paused)
        {
            this.paused=false;
            startTime+=(System.currentTimeMillis()-pausedTime);
        }

    }


    public long getMinute()
    {
        return (elapsedtime/1000) / 60;
    }

    public long getSeconds()
    {
        return (elapsedtime/1000) % 60;
    }

    public long getElapsedTime() {return elapsedtime;}

    //we get a endtime in constructor
    @Override
    public void run() {
        startTime = System.currentTimeMillis();
        elapsedtime = 0;
        while(elapsedtime<=endTime)
        {
            if(!paused)
            {
                synchronized (this)
                {
                    elapsedtime = System.currentTimeMillis()-startTime;
                }

                long minutes = (elapsedtime/1000) / 60;
                long seconds = (elapsedtime/1000) % 60;

                handler.post(()-> gameActivity.updateTimer(minutes,seconds));
            }

            try
            {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        //synchronized(this)
        //{
        //    this.isDone=true;
        //}

        handler.post(()->gameActivity.finishTime());
    }
}
