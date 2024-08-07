package pl.com.example.scoreboardbywiechu.layouts.gameActivities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import pl.com.example.scoreboardbywiechu.R;
import pl.com.example.scoreboardbywiechu.elements.Player;
import pl.com.example.scoreboardbywiechu.elements.points.PointsCalculator;
import pl.com.example.scoreboardbywiechu.gamesSettings.GameSettings;

public class PingPongActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();
    }

    private void initialize()
    {
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String p1Name = getIntent().getStringExtra("p1");
        String p2Name = getIntent().getStringExtra("p2");
        int pointsToWin = getIntent().getIntExtra("ptws", 11);
        int decidingPointsSet = getIntent().getIntExtra("dst", 15); //TODO: hmm nie lepiej dodac tablice i mozliwosc zmiany kazdego seta?
        int setToWin = getIntent().getIntExtra("stw", 3);



        gameSettings = new GameSettings(2, 1);      //1 part many sets
        gameSettings.addPlayer(new Player(p1Name));
        gameSettings.addPlayer(new Player(p2Name));
        gameSettings.setEndTime(Long.MAX_VALUE);    //INF TIME

        //TODO: temporary
        pointsCalculator = new PointsCalculator(setToWin,pointsToWin,0,(byte) 0b110,gameSettings.getPlayers(),this);
        pointsCalculator.setAdvantages(true);   //to add a client choose
        pointsCalculator.setAdvantagesLimit(2); //to add a client choose
        gameSettings.setPointsCalculator(pointsCalculator);

        initializeViewElement();
        setLayout();
    }
}
