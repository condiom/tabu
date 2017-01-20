package tratrafe2.condiom.tambu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenu extends Activity {
    static Button btnCont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        btnCont=(Button) findViewById(R.id.btnCont);
        disableCont();
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
       finish();
    }
    MainMenu that=this;
    /**
     * Start a new game
     * reset scores
     */
    public void StartGame(View v) {
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
    }

    public static void enableCont(){
        btnCont.setEnabled(true);
    }
    public static void disableCont(){
        btnCont.setEnabled(false);
    }
}
