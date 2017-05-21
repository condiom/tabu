package tratrafe2.condiom.tambu;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.Random;


/**
 * Created by tratrafe2 on 12/01/2017.
 */

public class Settings extends Activity {
    final int MAXMAXROUNDS = 100;
    final int MAXGOALROUNDS = 200;
    final int MAXTEAMS = 6;
    final int MAXTIME = 210;

    RadioButton btnGoal, btnRounds, btnInfinity;
    TextView txtTeams, txtTime, txtWrong, txtSkip, temp, txtMaxRounds, txtGoalPoints;
    TextView[] teamNames;
    EditText[] teamNamesInput;
    LinearLayout llTeamNames;
    int realTime, wrong, skip, maxRounds, goalRounds, mode;
    int teams = 0;
    String teamNamesAr[];
    LinearLayout[] tabs;
    LinearLayout[] colorsLL;
    int colors[];
    int selectedColors[];
    View colorViews[][];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        colors=new int[MAXTEAMS];
        selectedColors=new int[MAXTEAMS];
        colorViews=new View[MAXTEAMS][MAXTEAMS];

        maxRounds = 50;
        goalRounds = 100;

        btnGoal = (RadioButton) findViewById(R.id.btnGoal);
        btnRounds = (RadioButton) findViewById(R.id.btnRounds);
        btnInfinity = (RadioButton) findViewById(R.id.btnInfinite);

        txtMaxRounds = (TextView) findViewById(R.id.txtMaxRounds);
        txtGoalPoints = (TextView) findViewById(R.id.txtGoalPoints);
        txtTeams = (TextView) findViewById(R.id.txtTeamNum);
        txtTime = (TextView) findViewById(R.id.txtTime);
        txtWrong = (TextView) findViewById(R.id.txtWrong);
        txtSkip = (TextView) findViewById(R.id.txtSkip);

        // For teamnames textvies and editviews
        llTeamNames = (LinearLayout) findViewById(R.id.llteamNames);
        teamNames = new TextView[MAXTEAMS];
        teamNamesInput = new EditText[MAXTEAMS];
        teamNamesAr = new String[MAXTEAMS];
        tabs = new LinearLayout[MAXTEAMS];
        for (int i = 0; i < MAXTEAMS; i++) {
            teamNamesAr[i] = "";
            teamNames[i] = new TextView(this);
            teamNames[i].setText("Team " + (i + 1) + " :");
            teamNamesInput[i] = new EditText(this);
        }
        //Reading settings from file
        getValues();


       initializeViews();
        //Setting settings according to file
        switch (mode) {
            case 0:
                btnRounds.setChecked(true);
                btnGoal.setChecked(false);
                btnInfinity.setChecked(false);
                break;
            case 1:
                btnRounds.setChecked(false);
                btnGoal.setChecked(true);
                btnInfinity.setChecked(false);
                break;
            case 2:
                btnRounds.setChecked(false);
                btnGoal.setChecked(false);
                btnInfinity.setChecked(true);
                break;
            default:
                btnRounds.setChecked(true);
                btnGoal.setChecked(false);
                btnInfinity.setChecked(false);
                mode = 0;
                break;
        }
        txtMaxRounds.setText(String.valueOf(maxRounds));
        txtGoalPoints.setText(String.valueOf(goalRounds));
        txtTeams.setText(String.valueOf(teams));
        txtTime.setText(String.valueOf(realTime));

        if (wrong == 0) {
            txtWrong.setText("Nothing");
        } else if (wrong == 1) {
            txtWrong.setText("+1 Others");
        } else {
            txtWrong.setText("-1 Team");
        }
        if (skip == 0) {
            txtSkip.setText("Nothing");
        } else if (skip == 1) {
            txtSkip.setText("+1 Others");
        } else {
            txtSkip.setText("-1 Team");
        }



    }

    /**
     * Reads settings from file. (same file can be used in other activities).
     */
    public void getValues() {

        SharedPreferences sharedPref = getSharedPreferences("userSettings", Context.MODE_PRIVATE);
        teams = sharedPref.getInt("teams", 2);
        realTime = sharedPref.getInt("realTime", 30);
        wrong = sharedPref.getInt("wrong", wrong);
        skip = sharedPref.getInt("skip", skip);
        mode = sharedPref.getInt("mode", -1);
        maxRounds = sharedPref.getInt("maxRounds", MAXMAXROUNDS / 2);
        goalRounds = sharedPref.getInt("goalRounds", MAXGOALROUNDS / 2);
       Random rnd = new Random();
        for (int i = 0; i < MAXTEAMS; i++) {
           teamNamesAr[i] = sharedPref.getString("team" + i, teamNamesAr[i]);
           colors[i]=sharedPref.getInt("colors" + i, Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)));
           selectedColors[i]=sharedPref.getInt("teamColor" + i,i);
        }
    }

    /**
     * Saves settings to file.
     */
    public void saveValues() {
        SharedPreferences sharedPref = getSharedPreferences("userSettings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("realTime", realTime);//done
        editor.putInt("teams", teams);//done
        editor.putInt("wrong", wrong);//done
        editor.putInt("skip", skip);//done
        editor.putBoolean("settingsChanged", true);
        editor.putInt("goalRounds", goalRounds);
        editor.putInt("maxRounds", maxRounds);
        for (int i = 0; i < MAXTEAMS; i++) {
            String nameOfTeam = teamNamesInput[i].getText().toString();
            editor.putString("team" + i, nameOfTeam);
           editor.putInt("colors" + i, colors[i]);
           editor.putInt("teamColor" + i,  selectedColors[i]);

        }
        editor.putInt("mode", mode);//done
        editor.apply();
    }

    /**
     * /*
     * Sets which radio button is clicked and what happens then. (it is called in the activity xml)
     *
     * @param view
     */
    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.btnRounds:
                if (checked)
                    mode = 0;
                break;
            case R.id.btnGoal:
                if (checked)
                    mode = 1;
                break;
            case R.id.btnInfinite:
                if (checked)
                    mode = 2;
                break;
            default:
                mode = -1;
        }
    }

    /**
     * Exits settings menu
     *
     * @param v
     */
    public void exitClickS(View v) {
        finish();
    }

    /**
     * Exits settings menu and saves to file.
     *
     * @param v
     */
    public void saveAndExitClick(View v) {
        new AlertDialog.Builder(this)
                .setTitle("Save Settings")
                .setMessage("If you save settings any open games will be lost")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        saveValues();
                        MainMenu.disableCont();
                        finish();
                    }
                })
                .setNegativeButton("Don't save", null).show();

    }

    public void reduceMaxOnClick(View v) {
        maxRounds -= 5;
        if (maxRounds < 5) {
            maxRounds = 5;
        }
        txtMaxRounds.setText(maxRounds + "");
    }

    public void increaseMaxOnClick(View v) {
        maxRounds += 5;
        if (maxRounds > MAXMAXROUNDS) {
            maxRounds = MAXMAXROUNDS;
        }
        txtMaxRounds.setText(maxRounds + "");
    }

    public void reduceGoalOnClick(View v) {
        goalRounds -= 5;
        if (goalRounds < 5) {
            goalRounds = 5;
        }
        txtGoalPoints.setText(goalRounds + "");
    }

    public void increaseGoalOnClick(View v) {
        goalRounds += 5;
        if (goalRounds > MAXGOALROUNDS) {
            goalRounds = MAXGOALROUNDS;
        }
        txtGoalPoints.setText(goalRounds + "");
    }

    public void reduceTeamOnClick(View v) {
        teams -= 1;
        if (teams < 2) {
            teams = 2;
        }
        txtTeams.setText(teams + "");
        drawPlayers();
    }

    public void increaseTeamOnClick(View v) {
        teams += 1;
        if (teams > MAXTEAMS) {
            teams = MAXTEAMS;
        }
        txtTeams.setText(teams + "");
        drawPlayers();

    }

    public void increaseTimeOnClick(View v) {
        if (realTime == 3) { //TODO remove this line
            realTime -= 3;
        }
        realTime += 30;
        if (realTime > MAXTIME) {
            realTime = MAXTIME;
        }
        txtTime.setText(realTime + "");
    }

    public void reduceTimeOnClick(View v) {
        realTime -= 30;
        if (realTime <= 3) { //TODO change to 30-30
            realTime = 3;
        }
        txtTime.setText(String.valueOf(realTime));
    }

    public void reduceSkipOnClick(View v) {
        skip--;
        if (skip < -1)
            skip = -1;
        if (skip == 0) {
            txtSkip.setText("Nothing");
        } else if (skip == 1) {
            txtSkip.setText("+1 Others");
        } else {
            txtSkip.setText("-1 Team");
        }
    }

    public void increaseSkipOnClick(View v) {
        skip++;
        if (skip > 1)
            skip = 1;
        if (skip == 0) {
            txtSkip.setText("Nothing");
        } else if (skip == 1) {
            txtSkip.setText("+1 Others");
        } else {
            txtSkip.setText("-1 Team");
        }
    }

    public void reduceWrongOnClick(View v) {
        wrong--;
        if (wrong < -1)
            wrong = -1;
        if (wrong == 0) {
            txtWrong.setText("Nothing");
        } else if (wrong == 1) {
            txtWrong.setText("+1 Others");
        } else {
            txtWrong.setText("-1 Team");
        }
    }

    public void increaseWrongOnClick(View v) {
        wrong++;
        if (wrong > 1)
            wrong = 1;
        if (wrong == 0) {
            txtWrong.setText("Nothing");
        } else if (wrong == 1) {
            txtWrong.setText("+1 Others");
        } else {
            txtWrong.setText("-1 Team");
        }
    }

    public void initializeViews(){

      colorsLL=new LinearLayout[MAXTEAMS];
      for (int j = 0; j < MAXTEAMS; j++) {
         TextView txttab=new TextView(this);
         txttab.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
         txttab.setText("\t\t\t\t\t\t");
         tabs[j]= new LinearLayout(this);
         tabs[j].setOrientation(LinearLayout.HORIZONTAL);
         tabs[j].addView(txttab);
         teamNamesInput[j].setText(teamNamesAr[j]);
         teamNames[j].setTextSize(18);
         tabs[j].addView(teamNames[j]);
         teamNamesInput[j].setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
         tabs[j].addView(teamNamesInput[j]);
         colorsLL[j]= new LinearLayout(this);
         colorsLL[j].setOrientation(LinearLayout.HORIZONTAL);
         for(int i=0;i<MAXTEAMS;i++) {
            ImageButton r = new ImageButton(this);
            r.setBackgroundResource(R.drawable.circle_ball);
            GradientDrawable sd = (GradientDrawable) r.getBackground().mutate();
            sd.setColor(colors[i]);
            final int index = i;
            final int team = j;
            if(selectedColors[j]==i){
               ((GradientDrawable) r.getBackground()).setStroke(5, Color.BLACK, 0, 0);
            }
            r.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                  int myTeam = team;
                  int myIndex = index;
                  for (int i = 0; i < MAXTEAMS; i++) {
                     if (selectedColors[i] == index) {
                        ((GradientDrawable) ((ImageButton) colorViews[i][index]).getBackground()).setStroke(0, Color.BLACK, 0, 0);
                        selectedColors[i] = selectedColors[myTeam];
                        ((GradientDrawable) ((ImageButton) colorViews[i][selectedColors[myTeam]]).getBackground()).setStroke(5, Color.BLACK, 0, 0);


                        ((GradientDrawable) ((ImageButton) colorViews[myTeam][selectedColors[myTeam]]).getBackground()).setStroke(0, Color.BLACK, 0, 0);
                        selectedColors[myTeam] = index;
                        ((GradientDrawable) ((ImageButton) view).getBackground()).setStroke(5, Color.BLACK, 0, 0);
                        return;
                     }
                  }
                  ((GradientDrawable) ((ImageButton) colorViews[myTeam][selectedColors[myTeam]]).getBackground()).setStroke(0, Color.BLACK, 0, 0);
                  selectedColors[myTeam] = index;
                  ((GradientDrawable) ((ImageButton) view).getBackground()).setStroke(5, Color.BLACK, 0, 0);
               }
            });
            colorViews[j][i] = r;
            colorsLL[j].addView(r);
         }
      }
      drawPlayers();
   }

    public void drawPlayers(){
       llTeamNames.removeAllViews();
       for (int j = 0; j < teams; j++) {
          llTeamNames.addView(tabs[j]);
          llTeamNames.addView(colorsLL[j]);

       }
    }

}
