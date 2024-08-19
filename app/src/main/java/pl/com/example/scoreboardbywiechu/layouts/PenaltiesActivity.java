package pl.com.example.scoreboardbywiechu.layouts;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

import pl.com.example.scoreboardbywiechu.R;
import pl.com.example.scoreboardbywiechu.elements.Player;
import pl.com.example.scoreboardbywiechu.elements.points.Penalties;

public class PenaltiesActivity extends AppCompatActivity {

    private Penalties penalties;
    private List<Player> playerList;
    private int currentPlayerIndex = 0;

    private Button addPointButton;
    private Button missPointButton;
    private TextView scoreView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penalties);

        penalties = new Penalties();

        playerList = new ArrayList<>();
        playerList.add(new Player("p1"));
        playerList.add(new Player("p2"));
        playerList.add(new Player("p3"));

        penalties.setPlayerList(playerList);

        addPointButton = findViewById(R.id.addButton);  //add point button when player goal
        missPointButton = findViewById(R.id.missButton);    //miss point button when player missed
        //TODO:: zapomnialem o cofaniu do dodania kiedys

        //TODO 1:: wyswietlanie ze gracz odpadl
        //TODO 2:: dodanie endgame w activity
        //TODO 3:: lepsze przechodzenie pomiedzy graczami
        scoreView = findViewById(R.id.playerScoreTextView);

        addPointButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                penalties.addPoint(playerList.get(currentPlayerIndex));
                updatePlayerPointsTextView();
                nextPlayer();
            }
        });

        missPointButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                penalties.missingPoint(playerList.get(currentPlayerIndex));
                updatePlayerPointsTextView();
                nextPlayer();
            }
        });

    }

    private void nextPlayer()
    {
        currentPlayerIndex++;
        if(currentPlayerIndex>=playerList.size())
        {
            currentPlayerIndex=0;
        }
    }

    private void checkIfIsEnd()
    {
        if(penalties.isEndGame())
        {
            addPointButton.setVisibility(View.GONE);
            missPointButton.setVisibility(View.GONE);
        }
    }

    private void updatePlayerPointsTextView() {
        StringBuilder pointsText = new StringBuilder("Player Points:\n");
        for (Player player : playerList) {
            pointsText.append(player.getNameOfPlayer()).append(": ").append(penalties.getPlayerPoints(player)).append("\n");
        }
        scoreView.setText(pointsText.toString());
    }
}