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

public class DefaultActivity extends MainActivity {
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
        if(p1Name.isEmpty())
        {
            p1Name = "Guest1";
        }
        String p2Name = getIntent().getStringExtra("p2");
        if(p2Name.isEmpty())
        {
            p2Name = "Guest2";
        }

        gameSettings = new GameSettings(2, 1);
        gameSettings.addPlayer(new Player(p1Name));
        gameSettings.addPlayer(new Player(p2Name));
        gameSettings.setEndTime(Long.MAX_VALUE);

        //TODO: temporary
        pointsCalculator = new PointsCalculator(Integer.MAX_VALUE,0,0,(byte) 0b100,gameSettings.getPlayers(),this);
        gameSettings.setPointsCalculator(pointsCalculator);

        initializeViewElement();
        setLayout();
    }

}
