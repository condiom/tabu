package tratrafe2.condiom.tambu;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by tratrafe2 on 12/01/2017.
 */

public class Settings extends Activity {
    final int MAXTEAMS=6;
    Context that;
    SeekBar teamsBar,timeBar,wrongBar,skipBar;
    RadioButton btnGoal,btnRounds;
    TextView txtTeams,txtTime,txtWrong,txtSkip,temp;
    TextView[] teamNames;
    EditText[] teamNamesInput;
    LinearLayout llTeamNames;
    int realTime,timeProgr,wrong,skip,goal,rounds;
    int teams=0;
    boolean radio;
    String teamNamesAr[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        that = this;
        //Initializing koumpia
        teamsBar =(SeekBar) findViewById(R.id.btnTeams);
        timeBar =(SeekBar) findViewById(R.id.btnTime);
        wrongBar =(SeekBar) findViewById(R.id.btnWrongPoints);
        skipBar =(SeekBar) findViewById(R.id.btnSkipPoints);
        txtTeams =(TextView) findViewById(R.id.txtTeams);
        txtTime =(TextView) findViewById(R.id.txtTimes);
        txtWrong =(TextView) findViewById(R.id.txtWrong);
        txtSkip =(TextView) findViewById(R.id.txtSkip);
        btnGoal =(RadioButton) findViewById(R.id.btnGoal);
        btnRounds =(RadioButton) findViewById(R.id.btnRounds);

       // For team names textvies and editviews
       llTeamNames=(LinearLayout)findViewById(R.id.llteamNames);
       teamNames=new TextView[MAXTEAMS];
       teamNamesInput = new EditText[MAXTEAMS];
       teamNamesAr=new String[MAXTEAMS];
       for(int i=0;i<MAXTEAMS;i++){
          teamNamesAr[i]="";
          teamNames[i]=new TextView(this);
          teamNames[i].setText("Team "+(i+1)+" :");
          teamNamesInput[i]=new EditText(this);
       }

        //Reading settings from file
        radio=true;
        getValues();
       temp =new TextView(this);
       temp.setText("Team Names:");
       temp.setTextSize(20);
       llTeamNames.addView(temp);
       for(int j=0;j<teams+2;j++){
          teamNamesInput[j].setText(teamNamesAr[j]);
          llTeamNames.addView(teamNames[j]);
          llTeamNames.addView(teamNamesInput[j]);

       }
        //Setting settings according to file
        btnRounds.setChecked(radio);
        btnGoal.setChecked(!radio);
        teamsBar.setProgress(teams);
        txtTeams.setText("Number of Teams: " + (teamsBar.getProgress() + 2));
        timeBar.setProgress(timeProgr);
        if(timeBar.getProgress()==0){
            //TODO
           // realTime=30;
           // just fo testing
            realTime=12;
        }else{
            realTime=((timeBar.getProgress() * 30)+30);
        }
        txtTime.setText("Time per round: " + realTime);

        wrongBar.setProgress(wrong);
        if(wrong-1==0) {
            txtWrong.setText("Wrong:   0 ");
        }else if(wrong-1==1){
            txtWrong.setText("Wrong:  +1 Others");
        }else{
            txtWrong.setText("Wrong:  -1 Team");
        }
        skipBar.setProgress(skip);
        if(skip-1==0) {
            txtSkip.setText("Skip:     0 ");
        }else if(skip-1==1){
            txtSkip.setText("Skip:    +1   Others");
        }else{
            txtSkip.setText("Skip:    -1   Team");
        }

        //SeekBars listeners
        teamsBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

               int teams=(teamsBar.getProgress() + 2);
               txtTeams.setText("Number of Teams: " + teams);
               llTeamNames.removeAllViews();
               llTeamNames.addView(temp);
               for(int j=0;j<teams;j++){
                  teamNamesInput[j].setText(teamNamesAr[j]);
                  llTeamNames.addView(teamNames[j]);
                  llTeamNames.addView(teamNamesInput[j]);

               }

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        timeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(timeBar.getProgress()==0){
                    realTime=30;
                }else{
                    realTime=((timeBar.getProgress() * 30)+30);
                }
                txtTime.setText("Time per round: " + realTime);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        wrongBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(wrongBar.getProgress()-1==0) {
                    txtWrong.setText("Wrong:   0 ");
                }else if(wrongBar.getProgress()-1==1){
                    txtWrong.setText("Wrong:  +1 Others");
                }else{
                    txtWrong.setText("Wrong:  -1 Team");
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        skipBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(skipBar.getProgress()-1==0) {
                    txtSkip.setText("Skip:     0 ");
                }else if(skipBar.getProgress()-1==1){
                    txtSkip.setText("Skip:    +1 Others");
                }else{
                    txtSkip.setText("Skip:    -1 Team");
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    /**
     * Reads settings from file. (same file can be used in other activities).
     */
    public void getValues(){
        SharedPreferences sharedPref=getSharedPreferences("userSettings", Context.MODE_PRIVATE);
        timeProgr=sharedPref.getInt("timeProgr",timeProgr);
        teams=sharedPref.getInt("teams",2)-2;
        wrong=sharedPref.getInt("wrong",wrong);
        skip =sharedPref.getInt("skip",skip);
        goal=sharedPref.getInt("goal",goal);
        rounds=sharedPref.getInt("rounds",rounds);
        radio=sharedPref.getBoolean("radio",radio);
       for(int i=0;i<MAXTEAMS;i++){
          teamNamesAr[i]=sharedPref.getString("team"+i,teamNamesAr[i]);
       }
    }

    /**
     * Saves settings to file.
     */
    public void saveValues(){
        SharedPreferences sharedPref=getSharedPreferences("userSettings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPref.edit();
        teams=teamsBar.getProgress();
        timeProgr=timeBar.getProgress();
        wrong=wrongBar.getProgress();
        skip=skipBar.getProgress();

        editor.putInt("realTime",realTime);//done
        editor.putInt("timeProgr",timeProgr);//done
        editor.putInt("teams",teams+2);//done
        editor.putInt("wrong",wrong);//done
        editor.putInt("skip",skip);//done
        editor.putInt("goal",goal);
        editor.putInt("rounds",rounds);
        editor.putBoolean("settingsChanged",true);
        for(int i=0;i<MAXTEAMS;i++){
          String nameOfTeam=teamNamesInput[i].getText().toString();
        editor.putString("team"+i,nameOfTeam);
        }
        editor.putBoolean("radio",radio);//done
        editor.apply();
    }

    /**
    /*
     * Sets which radio button is clicked and what happens then. (it is called in the activity xml)
     * @param view
     */
    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.btnRounds:
                if (checked)
                    radio=true;
                //TODO
                    break;
            case R.id.btnGoal:
                if (checked)
                    radio=false;
                //TODO
                    break;
        }
    }

    /**
     * Exits settings menu
     * @param v
     */
    public void exitClickS(View v){
        finish();
    }
   /**
    * Exits settings menu and saves to file.
   * @param v
   */
    public void saveAndExitClick(View v){
       new AlertDialog.Builder(this)
               .setTitle("Save Settings")
               .setMessage("If you save settings any open games will be lost")
               .setIcon(android.R.drawable.ic_dialog_alert)
               .setPositiveButton("Save", new DialogInterface.OnClickListener() {

                  public void onClick(DialogInterface dialog, int whichButton) {
                     saveValues();
                     finish();
                  }
               })
               .setNegativeButton("Don't save", null).show();

    }

}
