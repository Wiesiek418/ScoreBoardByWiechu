package pl.com.example.scoreboardbywiechu.layouts;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import pl.com.example.scoreboardbywiechu.R;
import pl.com.example.scoreboardbywiechu.elements.Player;
import pl.com.example.scoreboardbywiechu.games.Football;
import pl.com.example.scoreboardbywiechu.games.GameSettings;


//LAYOUT for football with specific methods
public class FootballActivity extends MainActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String p1Name = getIntent().getStringExtra("p1");
        String p2Name = getIntent().getStringExtra("p2");
        int halfTime = getIntent().getIntExtra("ht",45*60*1000);
        int overtimeTime = getIntent().getIntExtra("otT",15*60*1000);

        boolean randomExtraTimeFlag = getIntent().getBooleanExtra("retF",false);
        boolean overtimeFlag = getIntent().getBooleanExtra("otF",false);
        boolean penaltiesFlag = getIntent().getBooleanExtra("pF",false);
        boolean goldGoalFlag = getIntent().getBooleanExtra("ggF",false);


        if(goldGoalFlag)
        {
            gameSettings = new Football(randomExtraTimeFlag,true);
        }
        else
        {
            gameSettings = new Football(randomExtraTimeFlag,overtimeFlag,penaltiesFlag,overtimeTime);
        }

        gameSettings.addPlayer(new Player(p1Name));
        gameSettings.addPlayer(new Player(p2Name));
        gameSettings.setEndTime(halfTime);

        initializeViewElement();
        setLayout();
    }

    @Override
    public void updateTimer(long minutes,long seconds)
    {
        TextView textView = findViewById(R.id.timeView);

        if(minutes>=(gameSettings.getEndMinute()) && !gameSettings.getExtraTimeIsSet() && seconds >=(gameSettings.getEndSecond()-1) )
        {
            ((Football) gameSettings).randomExtraTime();
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

    @Override
    protected void addPoint(Player player) {
        super.addPoint(player);
        if(((Football) gameSettings).getIsEndFlag())
            super.finishGame();
    }
}
