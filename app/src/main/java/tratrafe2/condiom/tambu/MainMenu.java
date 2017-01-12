package tratrafe2.condiom.tambu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainMenu extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }
    public void StartGame(View v){
        Intent intent = new Intent(this, Game.class);
        startActivity(intent);
    }

    public void SettingsMenu(View v){
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }
}
