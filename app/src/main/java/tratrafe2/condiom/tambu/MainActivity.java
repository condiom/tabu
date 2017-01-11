package tratrafe2.condiom.tambu;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.widget.TextView;

import java.util.Scanner;

public class MainActivity extends Activity {
    TextView timer;
    TextView txt1;
    TextView txt2;
    TextView txt3;
    TextView txt4;
    TextView txtMain;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt1 = (TextView) findViewById(R.id.txt1);
        txt2 = (TextView) findViewById(R.id.txt2);
        txt3 = (TextView) findViewById(R.id.txt3);
        txt4 = (TextView) findViewById(R.id.txt4);
        timer = (TextView) findViewById(R.id.timer);
        txtMain = (TextView) findViewById(R.id.txtMain);

        Card temp = new Card();
        Card[] cardArray = temp.initArray(this);
        System.out.println("hello");
        float sizeOfLetters = 30;
        txtMain.setTextSize(sizeOfLetters + 15);
        txt1.setTextSize(sizeOfLetters);
        txt2.setTextSize(sizeOfLetters);
        txt3.setTextSize(sizeOfLetters);
        txt4.setTextSize(sizeOfLetters);
        timer.setTextSize(sizeOfLetters);

        txt3.setGravity(Gravity.CENTER_HORIZONTAL);
        int i = 50;
            startCounter();
            txtMain.setText(cardArray[i].word);
            txt1.setText(cardArray[i].apagorevmenes[0]);
            txt2.setText(cardArray[i].apagorevmenes[1]);
            txt3.setText(cardArray[i].apagorevmenes[2]);
            txt4.setText(cardArray[i].apagorevmenes[3]);
        }


    public void startCounter(){
        countDownTimer: new CountDownTimer(60*1000,1000){
            @Override
            public void onTick(long milisUntilFinished){
                timer.setText(""+milisUntilFinished/1000);
            }

            @Override
            public void onFinish() {
                timer.setText("DONE");
            }
        };
    }
}
