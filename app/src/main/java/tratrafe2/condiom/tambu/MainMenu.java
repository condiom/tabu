package tratrafe2.condiom.tambu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenu extends Activity {
    static Button btnCont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        SharedPreferences sharedPref = getSharedPreferences("userSettings", Context.MODE_PRIVATE);
        boolean cont = sharedPref.getBoolean("cont", false);
        btnCont=(Button) findViewById(R.id.btnCont);
        if(cont){
            enableCont();
        }else {
            disableCont();
        }
    }
    public void ContinueGame(View v){
        Intent intent = new Intent(this, Game.class);
        startActivity(intent);
    }

    public void SettingsMenu(View v){
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }
    public void Exit(View v){
       Intent intent = new Intent(this, Test.class);
       startActivity(intent);
       finish();
    }
    MainMenu that=this;
    /**
     * Start a new game
     * reset scores
     */
    public void StartGame(View v) {
        if (btnCont.isEnabled()) {
            new AlertDialog.Builder(this)
                    .setTitle("New Game")
                    .setMessage("Do you really want to create a new game?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            Intent intent = new Intent(that, Game.class);
                            startActivity(intent);
                            Game.setNewGame();
                            enableCont();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null).show();
        }else{
            Intent intent = new Intent(this, Game.class);
            startActivity(intent);
            Game.setNewGame();
            enableCont();
        }
    }

    public static void enableCont(){
        btnCont.setEnabled(true);
    }
    public static void disableCont(){
        btnCont.setEnabled(false);
    }
    public static boolean getCont(){
        return btnCont.isEnabled();
    }
}
