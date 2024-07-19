package pl.com.example.scoreboardbywiechu.elements;

import android.os.Handler;
import android.os.Looper;

import pl.com.example.scoreboardbywiechu.layouts.MainActivity;

public class GameTime extends Thread
{
    private MainActivity gameActivity;

    private long endTime;
    private boolean isDone;
    private Handler handler;
    private long elapsedtime;

    public GameTime(MainActivity gameActivity,long endTimeInMillis)
    {
        this.gameActivity=gameActivity;

        this.endTime=endTimeInMillis;
        this.isDone=false;
        this.handler=new Handler(Looper.getMainLooper());

    }

    //when we need change a end time
    public synchronized void changeEndTime(long extraTimeInMillis)
    {
        if(!this.isDone)
        {
            this.endTime+=(extraTimeInMillis);
        }
    }

    public synchronized void stopTime()
    {
        this.isDone=true;
    }

    public synchronized void startTimeBeforeStop()
    {
        this.isDone=false;
    }

    public long getMinute()
    {
        return (elapsedtime/1000) / 60;
    }

    public long getSeconds()
    {
        return (elapsedtime/1000) % 60;
    }


    //we get a endtime in constructor
    @Override
    public void run() {
        long startTime = System.currentTimeMillis();

        while(((System.currentTimeMillis()-startTime)<=endTime))
        {
            synchronized (this)
            {
                elapsedtime = System.currentTimeMillis()-startTime;
            }
            long minutes = (elapsedtime/1000) / 60;
            long seconds = (elapsedtime/1000) % 60;


            handler.post(()-> gameActivity.updateTimer(minutes,seconds));


            try
            {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        synchronized(this)
        {
            this.isDone=true;
        }

        handler.post(()->gameActivity.finishTime());
    }
}
