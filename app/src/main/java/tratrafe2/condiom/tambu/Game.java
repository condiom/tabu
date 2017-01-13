package tratrafe2.condiom.tambu;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Game extends Activity {
    float sizeOfLetters;
    LinearLayout llTeamNames;
    LinearLayout llTeamScores;
    TextView timer,txt1, txt2,txt3, txt4,txtMain;
    Button btnStart, btnWrong,btnSkip,btnCorrect;
    int Scores[];
    TextView teamNames[],teamScores[];
    int teamPlaying,currentCard,realTime,NumOfTeams,wrongPoints,skipPoints,goal,rounds;
    boolean radio;
    Card[] cardArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        getValues();
        initializeViews();
        initializeVariables();



        cardArray = Card.initArray(this);

        //TODO: na valoume on button click, na ginete random ena i j na kali tin printCard
        printCard(cardArray, currentCard);
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
        timer = (TextView) findViewById(R.id.txtTimer);
        txtMain = (TextView) findViewById(R.id.txtMain);
        btnStart = (Button) findViewById(R.id.btnStart);
        btnCorrect = (Button) findViewById(R.id.btnCorrect);
        btnSkip = (Button) findViewById(R.id.btnSkip);
        btnWrong = (Button) findViewById(R.id.btnWrong);
        llTeamNames=(LinearLayout)findViewById(R.id.llteamNames);
        llTeamScores=(LinearLayout)findViewById(R.id.llteamScores);
        btnCorrect.setEnabled(false);
        btnWrong.setEnabled(false);
        btnSkip.setEnabled(false);

        sizeOfLetters = 30;
        txtMain.setTextSize(sizeOfLetters + 15);
        txt1.setTextSize(sizeOfLetters);
        txt2.setTextSize(sizeOfLetters);
        txt3.setTextSize(sizeOfLetters);
        txt4.setTextSize(sizeOfLetters);
        timer.setTextSize(sizeOfLetters);
        txt3.setGravity(Gravity.CENTER_HORIZONTAL);
        oldColors =  txt1.getTextColors(); //save original colors

        timer.setText(String.format("%d:%03d", realTime, 0));

    }

    /**
     * initialise Variables
     */
    public void initializeVariables(){
        Scores=new int[NumOfTeams];
        teamNames = new TextView[NumOfTeams];
        teamScores = new TextView[NumOfTeams];
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.weight = 1.0f;
        lp.gravity=Gravity.CENTER_VERTICAL;
        for (int i=0;i<NumOfTeams;i++){
            Scores[i]=0;
            teamNames[i]= new TextView(this);
            teamNames[i].setText("Team "+(i+1));


            teamScores[i]=new TextView(this);
            teamScores[i].setText(0+"");

            teamNames[i].setLayoutParams(lp);
            teamScores[i].setLayoutParams(lp);

            llTeamNames.addView(teamNames[i]);
            llTeamScores.addView(teamScores[i]);

        }

        // alasi tin to StartClick() etsi proti pezi i omada 1
        teamPlaying=-1;
        currentCard=50;//TODO na gini random
        updateScores();
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
                timer.setText("TIME'S UP\nTEAM "+(((teamPlaying+1)%NumOfTeams)+1)+" PLAY'S");
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
        for (int i=0;i<NumOfTeams;i++){
            teamScores[i].setText(Scores[i]+"");
        }
    }
    /**
     * Epilogi tis epomenis kartas
     * //TODO prepi na aferume tis kartes pu epextikan ke na elexume an tis eperasame ules
     * ke na kamume reset
     */
    public void selectNewCard(){
        currentCard=(int)(Math.random()*cardArray.length);
        printCard(cardArray, currentCard);
    }
    /**
     * Wrong Click
     */
    /*
    esvisa ta gt en 8a edulefkan me poles omades
    ke ekama aplos na aferun 1
    */
    // TODO prepi na ginun implement me tis poles omades vasi ton settings

    public void WrongClick(View v) {
        Scores[teamPlaying]--;
        if(Scores[teamPlaying]<0){
            Scores[teamPlaying]=0;
        }
        updateScores();
        selectNewCard();
    }
    /**
     * Skip Click
     */
    public void SkipClick(View v) {
        Scores[teamPlaying]--;
        if(Scores[teamPlaying]<0){
            Scores[teamPlaying]=0;
        }

        updateScores();
        selectNewCard();
    }
    /**
     * Correct Click
     * sin 1 to score tis omadas pu pezi
     * analoga me ta settings alasumeta meta
     */
    public void CorrectClick(View v) {
        Scores[teamPlaying]++;
        updateScores();
        selectNewCard();
    }

    /**
     * Start Click
     * start next team's timer
     *
     */
    public void StartClick(View v) {
        teamPlaying++;
        teamPlaying=teamPlaying%NumOfTeams;

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
