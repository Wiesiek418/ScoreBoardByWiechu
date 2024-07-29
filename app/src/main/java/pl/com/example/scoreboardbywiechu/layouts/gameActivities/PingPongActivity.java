package pl.com.example.scoreboardbywiechu.layouts.gameActivities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import pl.com.example.scoreboardbywiechu.R;
import pl.com.example.scoreboardbywiechu.elements.Player;
import pl.com.example.scoreboardbywiechu.gamesSettings.GameSettings;

public class PingPongActivity extends MainActivity {

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
        int pointsToWin = getIntent().getIntExtra("ptws", 11);
        int decidingPointsSet = getIntent().getIntExtra("dst", 15);
        int setToWin = getIntent().getIntExtra("stw", 3);

        gameSettings = new GameSettings(2, 2);
        gameSettings.addPlayer(new Player(p1Name));
        gameSettings.addPlayer(new Player(p2Name));
        initializeViewElement();
        setLayout();
    }
}
