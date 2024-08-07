package pl.com.example.scoreboardbywiechu.layouts;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.view.LayoutInflater;
import android.widget.Toast;

import pl.com.example.scoreboardbywiechu.R;
import pl.com.example.scoreboardbywiechu.layouts.gameActivities.DefaultActivity;
import pl.com.example.scoreboardbywiechu.layouts.gameActivities.FootballActivity;
import pl.com.example.scoreboardbywiechu.layouts.gameActivities.PingPongActivity;

public class SelectionActivity extends AppCompatActivity {

    private LinearLayout settingsContainer;
    private LayoutInflater inflate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_selection);

        settingsContainer = findViewById(R.id.settingContainer);
        inflate = LayoutInflater.from(this);

        ImageView gameIcon = findViewById(R.id.gameIcon);
        gameIcon.setImageResource(R.drawable.ic_launcher_foreground);
        showFootballSettings();

        gameIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            showFootballSettings();
            }
        });

        gameIcon.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showPingPongSettings();
                return true;
            }
        });

    }

    private void showFootballSettings()
    {
        settingsContainer.removeAllViews();
        View footballView = inflate.inflate(R.layout.football_settings_layout,settingsContainer,false);
        settingsContainer.addView(footballView);


        Button footballStartButton = footballView.findViewById(R.id.footballStartButton);

        EditText player1Name = footballView.findViewById(R.id.playerOneName);
        EditText player2Name = footballView.findViewById(R.id.playerTwoName);
        EditText halfTime = footballView.findViewById(R.id.halfTimeLenght);
        halfTime.setText("45");              //default values for halt time

        EditText overtimeTime = footballView.findViewById(R.id.overtimeLenght);
        overtimeTime.setText("15");         //default values for halt time

        CheckBox randomExtraTimeCheck = footballView.findViewById(R.id.extraTimeCheck);
        CheckBox overtimeCheck = footballView.findViewById(R.id.overtimeCheck);
        CheckBox penaltiesCheck = footballView.findViewById(R.id.penaltiesCheck);
        CheckBox goldgoalCheck = footballView.findViewById(R.id.goldgoalCheck);

        setFootballCheckElements(overtimeCheck,goldgoalCheck,penaltiesCheck,overtimeTime);


        footballStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO: wykrywanie takich samych nazw i dlugosc nazwy
                if (isCorrect(player1Name) && isCorrect(player2Name)) {
                    try {
                        Intent footballIntent = new Intent(SelectionActivity.this, FootballActivity.class);
                        footballIntent.putExtra("p1", player1Name.getText().toString());
                        footballIntent.putExtra("p2", player2Name.getText().toString());
                        footballIntent.putExtra("ht", Integer.parseInt(halfTime.getText().toString()) * 60 * 1000);  //Convert text to miliseconds time
                        footballIntent.putExtra("otT", Integer.parseInt(overtimeTime.getText().toString()) * 60 * 1000);  //Convert text to miliseconds time

                        footballIntent.putExtra("retF",randomExtraTimeCheck.isChecked());

                        footballIntent.putExtra("otF",overtimeCheck.isChecked());
                        footballIntent.putExtra("pF",penaltiesCheck.isChecked());
                        footballIntent.putExtra("ggF",goldgoalCheck.isChecked());

                        startActivity(footballIntent);
                    } catch (NumberFormatException e) {
                        Toast.makeText(SelectionActivity.this, R.string.invalid_number_error, Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(SelectionActivity.this, R.string.invalid_name_error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setFootballCheckElements(CheckBox overtimeCheck,CheckBox goldgoalCheck,CheckBox penaltiesCheck, EditText overtimeTime)
    {
        //if goldgoal is checked unenabled a overtime and penalites
        goldgoalCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked)
                {
                    overtimeCheck.setChecked(false);
                    overtimeCheck.setEnabled(false);
                    penaltiesCheck.setEnabled(false);
                    penaltiesCheck.setChecked(false);

                }
                else
                {
                    overtimeCheck.setEnabled(true);
                    penaltiesCheck.setEnabled(true);
                }
            }
        });

        //if overtime is checked unenabled a goldgoal
        overtimeCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked)
                {
                    goldgoalCheck.setEnabled(false);
                    goldgoalCheck.setChecked(false);
                    overtimeTime.setVisibility(View.VISIBLE);
                }
                else
                {
                    goldgoalCheck.setEnabled(true);
                    overtimeTime.setVisibility(View.GONE);
                }
            }
        });

        //if penalities is checked unenabled a goldgoal
        penaltiesCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked)
                {
                    goldgoalCheck.setEnabled(false);
                    goldgoalCheck.setChecked(false);
                }
                else
                {
                    goldgoalCheck.setEnabled(true);
                }
            }
        });
    }
    private void showPingPongSettings()
    {
        settingsContainer.removeAllViews();
        View pingpongView = inflate.inflate(R.layout.pingpong_settings_layout,settingsContainer,false);
        settingsContainer.addView(pingpongView);


        Button pingpongStartButton = pingpongView.findViewById(R.id.pingpongStartButton);
        pingpongStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: try catch do wykrywania poprawnie wpisanego kodu
                EditText player1Name = pingpongView.findViewById(R.id.playerOneName);
                EditText player2Name = pingpongView.findViewById(R.id.playerTwoName);
                EditText pointsToWinSet = pingpongView.findViewById(R.id.pointsToWinSets);
                EditText decidingSetPoints = pingpongView.findViewById(R.id.decidingSetPoints);
                EditText setsToWin = pingpongView.findViewById(R.id.setsToWin);

                //TODO: do zmiany pozniej po testach
                Intent pingpongIntent = new Intent(SelectionActivity.this, DefaultActivity.class);
                pingpongIntent.putExtra("p1",player1Name.getText().toString());
                pingpongIntent.putExtra("p2",player2Name.getText().toString());
                pingpongIntent.putExtra("ptws",pointsToWinSet.getText().toString());
                pingpongIntent.putExtra("dst",decidingSetPoints.getText().toString());
                pingpongIntent.putExtra("stw",setsToWin.getText().toString());


                startActivity(pingpongIntent);
            }
        });
    }

    private boolean isCorrect(EditText et)
    {
        //TODO: Add word correctness verification
        try{
            String text = et.getText().toString();
            return !text.trim().isEmpty();
        }
        catch (Error e)
        {
            return false;
        }

    }
}