package tratrafe2.condiom.tambu;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class End extends Activity {
   TextView txtWinner;
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_end);
      SharedPreferences sharedPref = getSharedPreferences("userSettings", Context.MODE_PRIVATE);
      String winingTeam=sharedPref.getString("winingTeam","ERROR");
      txtWinner=(TextView) findViewById(R.id.txtWinner);
      txtWinner.setText(winingTeam);
      MainMenu.disableCont();
   }

}
