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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class End extends Activity {
    TextView txts[];
    LinearLayout llwin;
    ImageView trophy;
    static int number = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        llwin = (LinearLayout) findViewById(R.id.llwin);
        txts = new TextView[number];

        txts[number - 1] = new TextView(this);
        txts[number - 1].setText("WINNER");//just to align
        txts[number - 1].setTextSize(30);//just to align
        txts[number - 1].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);//just to align
        llwin.addView(txts[number - 1]);

        trophy = new ImageView(this);
        trophy.setAdjustViewBounds(true);
        trophy.setScaleType(ImageView.ScaleType.FIT_CENTER);
        trophy.setImageResource(R.drawable.trophy);
        llwin.addView(trophy);

        SharedPreferences sharedPref = getSharedPreferences("userSettings", Context.MODE_PRIVATE);
        int counter = 1;
        for (int i = number - 1; i >= 0; i--) {
            if (i != number - 1) {
                txts[i] = new TextView(this);
                txts[i].setTextSize(25);
                txts[i].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            }

            int score = sharedPref.getInt("finalScores" + i, -1);
            String name = sharedPref.getString("teamBalls" + i, "ERROR");
            txts[i].setText((counter++) + ".\t\t" + name + "\t\t" + score);
            if (i != number - 1) {
                llwin.addView(txts[i]);
            }
        }
        MainMenu.disableCont();
    }

    public static void setNumber(int x) {
        number = x;
    }
    public void exitEnd(View v) {
        finish();
    }
}
