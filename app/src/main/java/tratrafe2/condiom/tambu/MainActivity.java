package tratrafe2.condiom.tambu;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.test.RenamingDelegatingContext;
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

        float sizeOfLetters = 30;
        txtMain.setTextSize(sizeOfLetters + 15);
        txt1.setTextSize(sizeOfLetters);
        txt2.setTextSize(sizeOfLetters);
        txt3.setTextSize(sizeOfLetters);
        txt4.setTextSize(sizeOfLetters);
        timer.setTextSize(sizeOfLetters);

        txt3.setGravity(Gravity.CENTER_HORIZONTAL);
        int i = 50;
        startCounter(60);
        //TODO: na valoume on button click, na ginete random ena i j na kali tin printCard
        printCard(cardArray, i);
    }

    /**
     * Tiponi ta periexomena tis kartas [i] pou ton pinaka cardArray.
     * isos nan kalitera o pinakas nan lista.
     * {
     *  lista -> o(N) gia na e3agis stixio
     *         -> mporoume kathe karta p ethkiavastike na tin kamnoume remove p ti lista
     * pinakas -> O(1) gia na thkiavasis stixio
     *          ->kratoume 1 pointer sto telos tou array. kathe fora p thkiavazoume le3i,
     *            kamnoume swap me to stixio ston pointer, j katevazoume ton pointer kata 1.
     * }
     *
     * @param cardArray
     * @param i
     */
    public void printCard(Card[] cardArray, int i) {
        txtMain.setText(cardArray[i].word);
        txt1.setText(cardArray[i].apagorevmenes[0]);
        txt2.setText(cardArray[i].apagorevmenes[1]);
        txt3.setText(cardArray[i].apagorevmenes[2]);
        txt4.setText(cardArray[i].apagorevmenes[3]);
    }

    /**
     * Metritis pros ta katw arxizontas p to minutes.
     *
     * @param minutes
     */
    public void startCounter(int minutes) {
        countDownTimer:
        new CountDownTimer(minutes * 1000, 10) {
            @Override
            public void onTick(long milisUntilFinished) {
                timer.setText(String.format("%d:%03d", milisUntilFinished / 1000, milisUntilFinished % 1000));
                if (milisUntilFinished / 1000 <= 10) {
                    timer.setTextColor(Color.RED);
                }
            }

            @Override
            public void onFinish() {
                timer.setText("TIME'S UP");
            }
        }.start();
    }
}
