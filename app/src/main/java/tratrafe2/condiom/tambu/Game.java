package tratrafe2.condiom.tambu;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Game extends Activity {
    TextView timer;
    TextView txt1;
    TextView txt2;
    TextView txt3;
    TextView txt4;
    TextView txtMain;
    TextView txtScore1;
    TextView txtScore2;
    TextView lblScore1;
    TextView lblScore2;
    Button btnStart;
    Button btnWrong;
    Button btnSkip;
    Button btnCorrect;
    int t1Score;
    int t2Score;
    int teamPlaying;
    Card[] cardArray;
    int currentCard;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        initializeViews();
        initializeVariables();

        cardArray = Card.initArray(this);

        //TODO: na valoume on button click, na ginete random ena i j na kali tin printCard
        printCard(cardArray, currentCard);
    }

    /**
     * initialise views
     */
    ColorStateList oldColors;
    public  void initializeViews(){

        txt1 = (TextView) findViewById(R.id.txt1);
        txt2 = (TextView) findViewById(R.id.txt2);
        txt3 = (TextView) findViewById(R.id.txt3);
        txt4 = (TextView) findViewById(R.id.txt4);
        timer = (TextView) findViewById(R.id.timer);
        txtMain = (TextView) findViewById(R.id.txtMain);
        txtScore1 = (TextView) findViewById(R.id.txtScore1);
        txtScore2 = (TextView) findViewById(R.id.txtScore2);
        lblScore1 = (TextView) findViewById(R.id.txtTeam1);
        lblScore2 = (TextView) findViewById(R.id.txtTeam2);
        btnStart = (Button) findViewById(R.id.btnStart);
        btnCorrect = (Button) findViewById(R.id.btnCorrect);
        btnSkip = (Button) findViewById(R.id.btnSkip);
        btnWrong = (Button) findViewById(R.id.btnWrong);
        btnCorrect.setEnabled(false);
        btnWrong.setEnabled(false);
        btnSkip.setEnabled(false);

        float sizeOfLetters = 30;
        txtMain.setTextSize(sizeOfLetters + 15);
        txt1.setTextSize(sizeOfLetters);
        txt2.setTextSize(sizeOfLetters);
        txt3.setTextSize(sizeOfLetters);
        txt4.setTextSize(sizeOfLetters);
        timer.setTextSize(sizeOfLetters);
        txtScore1.setTextSize(sizeOfLetters);
        txtScore2.setTextSize(sizeOfLetters);
        lblScore1.setTextSize(sizeOfLetters);
        lblScore2.setTextSize(sizeOfLetters);
        txt3.setGravity(Gravity.CENTER_HORIZONTAL);
        oldColors =  txt1.getTextColors(); //save original colors
        updateScores();
    }

    /**
     * initialise Variables
     */
    public void initializeVariables(){
        t1Score=0;
        t2Score=0;
        // alasi tin to StartClick() etsi proti pezi i omada 1
        teamPlaying=2;
        currentCard=50;
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
        timer.setTextColor(oldColors);
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
                btnStart.setEnabled(true);
                btnCorrect.setEnabled(false);
                btnWrong.setEnabled(false);
                btnSkip.setEnabled(false);
            }
        }.start();
    }

    /**
     * Ananeoni ta score tis omadas

     */
    public void updateScores(){
        txtScore1.setText(t1Score+"");
        txtScore2.setText(t2Score+"");
    }
    /**
     * Epilogi tis epomenis kartas
     * prepi na aferume tis kartes pu epextikan ke na elexume an tis eperasame ules
     * ke na kamume reset
     */
    public void selectNewCard(){

        currentCard=(int)(Math.random()*cardArray.length);

        printCard(cardArray, currentCard);
    }
    /**
     * Wrong Click
     * plin 1 to score tis omadas pu pezi
     * analoga me ta settings alasumeta meta
     */
    public void WrongClick(View v) {
        if(teamPlaying==1)
            t1Score--;
        else
            t2Score--;
        if(t1Score<0)
            t1Score=0;
        if(t2Score<0)
            t2Score=0;
        updateScores();
        selectNewCard();
    }
    /**
     * Skip Click
     * plin 1 to score tis omadas pu pezi
     * analoga me ta settings alasumeta meta
     */
    public void SkipClick(View v) {
        if(teamPlaying==1)
            t1Score--;
        else
            t2Score--;
        if(t1Score<0)
            t1Score=0;
        if(t2Score<0)
            t2Score=0;
        updateScores();
        selectNewCard();

    }
    /**
     * Correct Click
     * sin 1 to score tis omadas pu pezi
     * analoga me ta settings alasumeta meta
     */
    public void CorrectClick(View v) {
        if (teamPlaying == 1)
            t1Score++;
        else
            t2Score++;
        updateScores();
        selectNewCard();
    }

    /**
     * Start Click
     * start next team's timer
     *
     */
    public void StartClick(View v) {
        if (teamPlaying == 1)
            teamPlaying=2;
        else
            teamPlaying=1;
        selectNewCard();
        startCounter(60);
        btnStart.setEnabled(false);
        btnCorrect.setEnabled(true);
        btnWrong.setEnabled(true);
        btnSkip.setEnabled(true);

    }

    /**
     * Return to main menu
     *
     */
    public void exitClick(View v){
    finish();
    }
}
