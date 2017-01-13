package tratrafe2.condiom.tambu;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Game extends Activity {
    TextView timer,txt1, txt2,txt3, txt4,txt5,txtMain, txtScore1,txtScore2,lblScore1,lblScore2;
    Button btnStart, btnWrong,btnSkip,btnCorrect;
    int t1Score, t2Score, teamPlaying,currentCard,realTime,NumOfTeams,wrongPoints,skipPoints,goal,rounds;
    boolean radio;
    int indexLast;
    Card[] cardArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        getValues();
        initializeViews();
        initializeVariables();

        cardArray = Card.initArray(this);
        indexLast=cardArray.length-1;

    }


    public void getValues(){
        SharedPreferences sharedPref=getSharedPreferences("userSettings", Context.MODE_PRIVATE);
        realTime=sharedPref.getInt("realTime",realTime);
        NumOfTeams=sharedPref.getInt("teams",NumOfTeams);
        wrongPoints=sharedPref.getInt("wrong",wrongPoints);
        skipPoints =sharedPref.getInt("skip",skipPoints);
        goal=sharedPref.getInt("goal",goal);
        rounds=sharedPref.getInt("rounds",rounds);
        radio=sharedPref.getBoolean("radio",radio);
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
        txt5 = (TextView) findViewById(R.id.txt5);
        timer = (TextView) findViewById(R.id.txtTimer);
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
        txt5.setTextSize(sizeOfLetters);
        timer.setTextSize(sizeOfLetters);
        txtScore1.setTextSize(sizeOfLetters);
        txtScore2.setTextSize(sizeOfLetters);
        lblScore1.setTextSize(sizeOfLetters);
        lblScore2.setTextSize(sizeOfLetters);
        txt3.setGravity(Gravity.CENTER_HORIZONTAL);
        oldColors =  txt1.getTextColors(); //save original colors

        timer.setText(String.format("%d:%03d", realTime, 0));
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
        txt5.setText(cardArray[i].apagorevmenes[4]);
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
     * //TODO prepi na aferume tis kartes pu epextikan ke na elexume an tis eperasame ules
     * ke na kamume reset
     */
    public void selectNewCard(){
        currentCard=(int)(Math.random()*(indexLast+1));
        printCard(cardArray, currentCard);
        Card temp=cardArray[indexLast];
        cardArray[indexLast]=cardArray[currentCard];
        cardArray[currentCard]=temp;
        indexLast--;
        if(indexLast==-1)
            indexLast=cardArray.length-1;

    }
    /**
     * Wrong Click
     */
    public void WrongClick(View v) {
        if(teamPlaying==1)
            if(wrongPoints==0){
                t1Score--;
            }else if(wrongPoints==2){
                t2Score++;
            }
        if(teamPlaying==2)
            if(wrongPoints==0){
                t2Score--;
            }else if(wrongPoints==2){
                t1Score++;
            }
        if(t1Score<0)
            t1Score=0;
        if(t2Score<0)
            t2Score=0;
        updateScores();
        selectNewCard();
    }
    /**
     * Skip Click
     */
    public void SkipClick(View v) {
        if(teamPlaying==1)
            if(skipPoints==0){
                t1Score--;
            }else if(skipPoints==2){
                t2Score++;
            }
        if(teamPlaying==2)
            if(skipPoints==0){
                t2Score--;
            }else if(skipPoints==2){
                t1Score++;
            }
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
        startCounter(realTime);
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
