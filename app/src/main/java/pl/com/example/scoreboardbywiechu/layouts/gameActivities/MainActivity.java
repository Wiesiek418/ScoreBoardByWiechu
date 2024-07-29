package pl.com.example.scoreboardbywiechu.layouts.gameActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import pl.com.example.scoreboardbywiechu.R;
import pl.com.example.scoreboardbywiechu.elements.PointsCalculator;
import pl.com.example.scoreboardbywiechu.gamesSettings.GameSettings;
import pl.com.example.scoreboardbywiechu.elements.GameTime;
import pl.com.example.scoreboardbywiechu.elements.Player;
import pl.com.example.scoreboardbywiechu.elements.Point;
import pl.com.example.scoreboardbywiechu.layouts.SelectionActivity;

public class MainActivity extends AppCompatActivity{
    final int LAST_SCORE=10;    //const number of displays last scored
    private Button buttonPlusFirst; //button to add points for first player
    private Button buttonPlusSecond;    //button to add points for second player
    private Button buttonStart;         //button to start a timer and game
    private Button buttonMinusFirst;    //button to remove points from first player
    private Button buttonMinusSecond;   //button to remove points from second player\

    private Button buttonStartTime;
    private Button buttonStop;

    private Button buttonRestart;

    protected GameSettings gameSettings;    //object in which we have a game settings and his logic
    protected GameTime gameTime;            //object of the timer logic

    private LinearLayout leftHistory;       //view to see last points of first player
    private LinearLayout rightHistory;      //view to see last points of second player


    private Player leftPlayer;              //info about first player
    private Player rightPlayer;             //info about second player

    private TextView leftNameView;          //view to display a name of first player
    private TextView rightNameView;         //view to display a name of second player

    private PointsCalculator pointsCalculator;

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

        Player p1 = new Player("p1");
        Player p2 = new Player("p2");
        gameSettings = new GameSettings(2,2,10*1000);
        gameSettings.addPlayer(p1);
        gameSettings.addPlayer(p2);



        initializeViewElement();
        setLayout();

    }

    //initialization of the view element
    protected void initializeViewElement()
    {
        buttonPlusFirst = findViewById(R.id.plusFirst);
        buttonPlusSecond = findViewById(R.id.plusSecond);
        buttonStart = findViewById(R.id.startButton);
        buttonMinusFirst = findViewById(R.id.deleteFirst);
        buttonMinusSecond = findViewById(R.id.deleteSecond);

        buttonRestart = findViewById(R.id.restart);

        leftHistory = findViewById(R.id.leftPlayerScores);
        rightHistory = findViewById(R.id.rightPlayerScores);

        leftNameView = findViewById(R.id.leftName);
        rightNameView = findViewById(R.id.rightName);

        buttonStop = findViewById(R.id.Stop);
        buttonStartTime = findViewById(R.id.Start);

        //temporary
        pointsCalculator = new PointsCalculator(2,6,10,true,false,false,gameSettings.getPlayers(),this);
    }


    //TODO: do poprawy kiedys
    //set a layout view if 1 player or 2 players
    //i should create a others layout for it but i always think
    protected void setLayout()
    {
        int players = gameSettings.getNumberOfPlayers();

        pointsCalculator.setDisplayPoints();

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonStart.setVisibility(View.GONE);
                findViewById(R.id.scoreLayout).setVisibility(View.VISIBLE);
                startTime();

            }
        });

        buttonRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent selectIntent = new Intent(MainActivity.this, SelectionActivity.class);
                startActivity(selectIntent);
            }
        });

        if(players==2)
        {
            buttonPlusFirst.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addPoint(gameSettings.getPlayer(0));

                }
            });

            buttonPlusSecond.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    addPoint(gameSettings.getPlayer(1));

                }
            });

            buttonMinusFirst.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removePoint(gameSettings.getPlayer(0));
                }
            });

            buttonMinusSecond.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removePoint(gameSettings.getPlayer(1));
                }
            });

            //TODO: zrobic lepiej w layout bo narazie chaos
            //START STOP buttons
            buttonStop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    gameTime.stopTime();
                }
            });
            buttonStartTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    gameTime.startTimeBeforeStop();
                }
            });


            //TODO: zrobic bardziej elastyczne
            leftPlayer=gameSettings.getPlayer(0);
            rightPlayer=gameSettings.getPlayer(1);


            leftNameView.setText(leftPlayer.getNameOfPlayer());
            rightNameView.setText(rightPlayer.getNameOfPlayer());
        }
        else if(players==1)
        {
            buttonPlusFirst.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addPoint(gameSettings.getPlayer(0));
                }
            });

            buttonMinusFirst.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removePoint(gameSettings.getPlayer(0));
                }
            });

            leftNameView.setText(leftPlayer.getNameOfPlayer());

            buttonPlusSecond.setVisibility(View.GONE);
            buttonMinusSecond.setVisibility(View.GONE);
            rightHistory.setVisibility(View.GONE);
            rightNameView.setVisibility(View.GONE);
        }
        else
        {
            gameTime.stopTime();
            buttonStart.setVisibility(View.VISIBLE);
        }
    }


    //POINTS
    protected void addPoint(Player player)
    {
        //gameSettings.addPointTo(player,gameTime.getElapsedTime());
        pointsCalculator.addPointToPlayer(player,gameTime.getElapsedTime());
        //updateScore(gameSettings);
        pointsCalculator.displayScore();
    }

    protected void removePoint(Player player)
    {
        //TODO: DODAC USUWANIE PUNKTOW
        //gameSettings.removePointFrom(player);
        //updateScore(gameSettings);
        pointsCalculator.displayScore();
    }


    private <T extends GameSettings> void displayLastScore(T game)
    {
        int pVsize = game.getSizePointVector();
        int lastScore=0;
        leftHistory.removeAllViews();
        rightHistory.removeAllViews();

        if(pVsize<=LAST_SCORE)
            lastScore=pVsize;
        else
            lastScore=LAST_SCORE;

        for(int i=0;i<lastScore;i++)
        {
            Point point = gameSettings.getIndexPointVector(pVsize-i-1);
            TextView scoreView = new TextView(this);
            TextView scoreViewBlank = new TextView(this);

            scoreView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            scoreViewBlank.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            scoreView.setBackgroundColor(ContextCompat.getColor(this,R.color.scorePoint));
            int size = getResources().getDimensionPixelSize(R.dimen.pointSize);

            scoreView.getLayoutParams().height=size;
            scoreView.getLayoutParams().width=size;
            scoreViewBlank.getLayoutParams().height=size;
            scoreViewBlank.getLayoutParams().width=size;

            scoreView.setText(String.valueOf(point.getNumPoint()));
            scoreView.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);
            scoreView.setGravity(Gravity.CENTER);


            if(point.getPlayer().equals(leftPlayer))
            {
                leftHistory.addView(scoreView);
                rightHistory.addView(scoreViewBlank);
            }
            else
            {
                rightHistory.addView(scoreView);
                leftHistory.addView(scoreViewBlank);
            }
        }
    }


    //TIMER

    private void startTime()
    {
        if(gameTime!=null)
        {
            try {
                gameTime.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        gameTime = new GameTime(this,gameSettings.getEndTime());
        gameTime.start();
    }


    //to chyba do poprawy
    public void updateTimer(long minutes,long seconds)
    {
        TextView textView = findViewById(R.id.timeView);

        String timeView;
        if(gameSettings.getExtraTimeIsSet()&&gameSettings.getExtraTime()!=0)
        {
            timeView = String.format("%02d:%02d +%d :%d", minutes, seconds, gameSettings.getExtraTime() / 1000 / 60,gameSettings.getCurrentPart());
        }
        else
        {
            timeView = String.format("%02d:%02d :%d", minutes, seconds,gameSettings.getCurrentPart());
        }
        textView.setText(timeView);

    }

    public void finishGame()
    {
        if(gameTime.isAlive())
        {
            gameTime.stopTime();
        }

        RelativeLayout bPL = findViewById(R.id.buttonPointsLayout);
        bPL.setVisibility(View.GONE);

        RelativeLayout relativeLayout = findViewById(R.id.finishLayout);
        relativeLayout.setVisibility(View.VISIBLE);
    }

    public void finishTime()
    {
        if(gameSettings.isEnd()) {
            finishGame();
        }
        else
        {
            startTime();
        }
    }
}