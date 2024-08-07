package pl.com.example.scoreboardbywiechu.layouts.gameActivities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import pl.com.example.scoreboardbywiechu.R;
import pl.com.example.scoreboardbywiechu.elements.Player;
import pl.com.example.scoreboardbywiechu.elements.points.PointsCalculator;
import pl.com.example.scoreboardbywiechu.gamesSettings.FootballSettings;
import pl.com.example.scoreboardbywiechu.gamesSettings.GameSettings;


//LAYOUT for football with specific methods
public class FootballActivity extends MainActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Pomijamy super.onCreate(savedInstanceState) z MainActivity i odwołujemy się bezpośrednio do AppCompatActivity
        super.onCreate(savedInstanceState); // To wywołuje AppCompatActivity.onCreate(), pomijając MainActivity.onCreate()

        initializeActivity();
    }

    private void initializeActivity() {
        // Inicjalizacja specyficzna dla FootballActivity
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        String p1Name = getIntent().getStringExtra("p1");
        String p2Name = getIntent().getStringExtra("p2");
        int halfTime = getIntent().getIntExtra("ht", 45 * 60 * 1000);
        int overtimeTime = getIntent().getIntExtra("otT", 15 * 60 * 1000);

        boolean randomExtraTimeFlag = getIntent().getBooleanExtra("retF", false);
        boolean overtimeFlag = getIntent().getBooleanExtra("otF", false);
        boolean penaltiesFlag = getIntent().getBooleanExtra("pF", false);
        boolean goldGoalFlag = getIntent().getBooleanExtra("ggF", false);



        if (goldGoalFlag) {
            gameSettings = new FootballSettings(randomExtraTimeFlag, true);
        } else {
            gameSettings = new FootballSettings(randomExtraTimeFlag, overtimeFlag, penaltiesFlag, overtimeTime);
        }

        gameSettings.addPlayer(new Player(p1Name));
        gameSettings.addPlayer(new Player(p2Name));
        gameSettings.setEndTime(halfTime);

        //TODO: temporary
        pointsCalculator = new PointsCalculator(Integer.MAX_VALUE, 0, 0, (byte) 0b100, gameSettings.getPlayers(), this);
        gameSettings.setPointsCalculator(pointsCalculator);

        initializeViewElement();
        setLayout();

    }

    @Override
    public void updateTimer(long minutes,long seconds)
    {
        TextView textView = findViewById(R.id.timeView);

        if(minutes>=(gameSettings.getEndMinute()) && !gameSettings.getExtraTimeIsSet() && seconds >=(gameSettings.getEndSecond()-1) )
        {
            ((FootballSettings) gameSettings).randomExtraTime();
            gameTime.changeEndTime(gameSettings.getExtraTime());
        }

        String timeView;
        if(gameSettings.getExtraTimeIsSet()&&gameSettings.getExtraTime()!=0)
        {
            timeView = String.format("%02d:%02d +%d :%d", minutes, seconds, gameSettings.getExtraTime() / 1000 / 60,gameSettings.getCurrentPart());
        }
        else
        {
            timeView = String.format("%02d:%02d :%d (%d)", minutes, seconds,gameSettings.getCurrentPart(),gameSettings.getEndTime()/1000/60);
        }
        textView.setText(timeView);

    }

    //TODO: if is normal part do nothing
    //If is a gold goal and it is the first goal in this part end game
    //if is a penalty do penalty part
    @Override
    protected void addPoint(Player player) {
        super.addPoint(player);
        if(((FootballSettings) gameSettings).getIsGoldGoal())    //co ja tutaj zrobilem ma wykrywac zlota pilke i jesli wpadl gol zakoncz gre
            super.finishGame();

    }

    private void penalties()
    {
        pointsCalculator.setGameMode((byte) 0b110);
        pointsCalculator.setDisplayPoints();
        //w karnych wystepuja tury od nich wszystko sie ustala
        //moze dodam taki system w pointcalculator i tam bedzie to ladnie dzialac
        //TODO: system turowy
    }
}
