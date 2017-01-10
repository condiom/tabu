package tratrafe2.condiom.tambu;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;

import java.util.Scanner;

public class MainActivity extends Activity {

    String[][] text;
    TextView txt1;
    TextView txt2;
    TextView txt3;
    TextView txt4;
    TextView txtMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = new String[200][5];
        txt1 = (TextView) findViewById(R.id.txt1);
        txt2 = (TextView) findViewById(R.id.txt2);
        txt3 = (TextView) findViewById(R.id.txt3);
        txt4 = (TextView) findViewById(R.id.txt4);
        txtMain = (TextView) findViewById(R.id.txtMain);
        Scanner sc = null;
        try {
            sc = new Scanner(getResources().openRawResource(R.raw.words));

        } catch (Exception e) {

        }
        for (int i = 0; i < 200; i++) {
            for (int j = 0; j < 5; j++) {
                text[i][j] = sc.next();
            }
        }
        float sizeOfLetters = 30;
        txtMain.setTextSize(sizeOfLetters + 15);
        txt1.setTextSize(sizeOfLetters);
        txt2.setTextSize(sizeOfLetters);
        txt3.setTextSize(sizeOfLetters);
        txt4.setTextSize(sizeOfLetters);

        txt3.setGravity(Gravity.CENTER_HORIZONTAL);

        txtMain.setText(text[0][0]);
        txt1.setText(text[0][1]);
        txt2.setText(text[0][2]);
        txt3.setText(text[0][3]);
        txt4.setText(text[0][4]);

    }
}
