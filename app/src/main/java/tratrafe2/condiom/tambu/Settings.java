package tratrafe2.condiom.tambu;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by tratrafe2 on 12/01/2017.
 */

public class Settings extends Activity {
    SeekBar teamsBar,timeBar,wrongBar,skipBar;
    RadioButton btnGoal,btnRounds;
    TextView txtTeams,txtTime,txtWrong,txtSkip;

    int realTime,timeProgr,teams,wrong,skip,goal,rounds;
    boolean radio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

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

        //Reading settings from file
        radio=true;
        getValues();

        //Setting settings according to file
        btnRounds.setChecked(radio);
        btnGoal.setChecked(!radio);
        teamsBar.setProgress(teams);
        txtTeams.setText("Number of Teams: " + (teamsBar.getProgress() + 2));
        timeBar.setProgress(timeProgr);
        if(timeBar.getProgress()==0){
            realTime=30;
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
                txtTeams.setText("Number of Teams: " + (teamsBar.getProgress() + 2));
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
        teams=sharedPref.getInt("teams",teams);
        wrong=sharedPref.getInt("wrong",wrong);
        skip =sharedPref.getInt("skip",skip);
        goal=sharedPref.getInt("goal",goal);
        rounds=sharedPref.getInt("rounds",rounds);
        radio=sharedPref.getBoolean("radio",radio);
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
        editor.putInt("teams",teams);//done
        editor.putInt("wrong",wrong);//done
        editor.putInt("skip",skip);//done
        editor.putInt("goal",goal);
        editor.putInt("rounds",rounds);
        editor.putBoolean("radio",radio);//done
        editor.apply();
    }

    /**
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
     * Exits settings menu and saves to file.
     * @param v
     */
    public void exitClickS(View v){
        saveValues();
        finish();
    }
}
