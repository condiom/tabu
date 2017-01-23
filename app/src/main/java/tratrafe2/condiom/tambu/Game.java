package tratrafe2.condiom.tambu;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;

import org.w3c.dom.Text;


public class Game extends Activity {
    final int MAXTEAMS = 6;
    int MAXFILES = 2;
    int countFiles = 0;
    int teamPlaying = 0;
    int debug = 0;
    long currentTime = 0;
    static boolean newGame;
    CountDownTimer cdt;
    ColorStateList oldColors;

    MediaPlayer mpTimer,mpCorrect,mpSkip,mpWrong,mpRound;

    ProgressBar pbar;
    LinearLayout llTeamNames, llTeamScores, llindicators, touchLayout;
    TextView console, timer, txt1, txt2, txt3, txt4, txt5, txtMain;
    TextView teamNames[], teamScores[], indicators[];
    Button btnWrong, btnSkip, btnCorrect, btnNewGame;
    ImageButton btnPause;

    int Scores[];
    int currentCard, realTime, NumOfTeams, wrongPoints, skipPoints, goal, rounds, index, mode, maxRounds, goalRounds;
    Card cardArray[];
    String teamNamesStr[], winingTeam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        getSettings();
        initializeViews();
        initializeVariables();
        if (newGame) {
            newGame = false;
            resetGame();
        }
        RunAnimation(console);
    }

    private void RunAnimation(TextView x) {
        class FlipListener implements ValueAnimator.AnimatorUpdateListener {
            private final View mFrontView;
            private final View mBackView;
            private boolean mFlipped;

            public FlipListener(final View front, final View back) {
                this.mFrontView = front;
                this.mBackView = back;
                this.mBackView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationUpdate(final ValueAnimator animation) {
                final float value = animation.getAnimatedFraction();
                final float scaleValue = 0.625f + (1.5f * (value - 0.5f) * (value - 0.5f));

                if(value <= 0.5f){
                    this.mFrontView.setRotationY(180 * value);
                    this.mFrontView.setScaleX(scaleValue);
                    this.mFrontView.setScaleY(scaleValue);
                    if(mFlipped){
                        setStateFlipped(false);
                    }
                } else {
                    this.mBackView.setRotationY(-180 * (1f- value));
                    this.mBackView.setScaleX(scaleValue);
                    this.mBackView.setScaleY(scaleValue);
                    if(!mFlipped){
                        setStateFlipped(true);
                    }
                }
            }

            private void setStateFlipped(boolean flipped) {
                mFlipped = flipped;
                this.mFrontView.setVisibility(flipped ? View.INVISIBLE : View.VISIBLE);
                this.mBackView.setVisibility(flipped ? View.VISIBLE : View.INVISIBLE);
            }
        }
        ValueAnimator mFlipAnimator = ValueAnimator.ofFloat(0f, 1f);
        mFlipAnimator.addUpdateListener(new FlipListener(x, x));
        mFlipAnimator.start();
    }

    public static void setNewGame() {
        newGame = true;
    }

    public void resetGame() {
        currentTime = realTime * 1000;
        teamPlaying = 0;
        for (int i = 0; i < NumOfTeams; i++) {
            Scores[i] = 0;
        }
        rounds = 0;
        txt1.setText("");
        txt2.setText("");
        txt3.setText("");
        txt4.setText("");
        txt5.setText("");
        txtMain.setText("Tap to START!");
        timer.setTextColor(oldColors);
        pbar.setVisibility(View.VISIBLE);
        pbar.setProgress(60);
        timer.setText("" + realTime);
        console.setText(teamNamesStr[teamPlaying] + "'s turn!");
        for (int i = 0; i < indicators.length; i++) {
            indicators[i].setVisibility(View.INVISIBLE);
        }
        indicators[teamPlaying].setVisibility(View.VISIBLE);
        updateScores();
    }

    public void getSettings() {
        SharedPreferences sharedPref = getSharedPreferences("userSettings", Context.MODE_PRIVATE);
        boolean settingsChanged = sharedPref.getBoolean("settingsChanged", false);
        realTime = sharedPref.getInt("realTime", 30);
        NumOfTeams = sharedPref.getInt("teams", 2);
        wrongPoints = sharedPref.getInt("wrong", 0);
        skipPoints = sharedPref.getInt("skip", 0);
        goal = sharedPref.getInt("goal", 50);
        rounds = sharedPref.getInt("rounds", 0);
        mode = sharedPref.getInt("mode", 0);
        maxRounds = sharedPref.getInt("maxRounds", 50);
        goalRounds = sharedPref.getInt("goalRounds", 100);
        currentTime = sharedPref.getLong("currentTime", realTime * 1000);
        teamNamesStr = new String[MAXTEAMS];
        for (int i = 0; i < MAXTEAMS; i++) {
            teamNamesStr[i] = sharedPref.getString("team" + i, "team " + i);
            if (teamNamesStr[i].compareTo("") == 0) {
                teamNamesStr[i] = "team " + i;
            }
        }
        Scores = new int[NumOfTeams];
        teamNames = new TextView[NumOfTeams];
        indicators = new TextView[NumOfTeams];
        teamScores = new TextView[NumOfTeams];
        teamPlaying = sharedPref.getInt("teamPlaying", 0);
        MAXFILES = sharedPref.getInt("MAXFILES", 2);
        countFiles = sharedPref.getInt("countFiles", 0);
        for (int i = 0; i < NumOfTeams; i++) {
            Scores[i] = sharedPref.getInt("teamScores" + i, 0);
        }
        index = sharedPref.getInt("index", -5);
        if (index == -5) {
            int nextFile = getResources().getIdentifier("words" + (++countFiles), "raw", getPackageName());
            cardArray = Card.initArray(this, nextFile);
            index = cardArray.length - 1;
            return;
        } else {
            cardArray = new Card[index + 1];
            Gson gson = new Gson();
            for (int i = 0; i <= index; i++) {
                String json = sharedPref.getString("cardArray" + i, "");
                Card obj = (Card) gson.fromJson(json, Card.class);
                cardArray[i] = obj;
            }
        }
        if (settingsChanged) {
            settingsChanged = false;
            currentTime = realTime * 1000;
            teamPlaying = 0;
            for (int i = 0; i < NumOfTeams; i++) {
                Scores[i] = 0;
            }
            rounds = 0;
        }
    }

    /**
     * initialise views
     */
       public void initializeViews() {

        txt1 = (TextView) findViewById(R.id.txt1);
        txt2 = (TextView) findViewById(R.id.txt2);
        txt3 = (TextView) findViewById(R.id.txt3);
        txt4 = (TextView) findViewById(R.id.txt4);
        txt5 = (TextView) findViewById(R.id.txt5);
        console = (TextView) findViewById(R.id.txtConsole);
        timer = (TextView) findViewById(R.id.txtTimer);
        txtMain = (TextView) findViewById(R.id.txtMain);
        btnCorrect = (Button) findViewById(R.id.btnCorrect);
        btnSkip = (Button) findViewById(R.id.btnSkip);
        btnWrong = (Button) findViewById(R.id.btnWrong);
        llTeamNames = (LinearLayout) findViewById(R.id.llteamNames);
        llTeamScores = (LinearLayout) findViewById(R.id.llteamScores);
        llindicators = (LinearLayout) findViewById(R.id.llindicators);
        touchLayout = (LinearLayout) findViewById(R.id.TouchLayout);
        btnCorrect.setEnabled(false);
        btnWrong.setEnabled(false);
        btnSkip.setEnabled(false);
        oldColors = txt1.getTextColors(); //save original colors
        pbar = (ProgressBar) findViewById(R.id.progressBar);

        console.setText("Round " + rounds + ": " + teamNamesStr[teamPlaying] + "'s turn!");
        txt1.setText("");
        txt2.setText("");
        txt3.setText("");
        txt4.setText("");
        txt5.setText("");
        txtMain.setText("Tap to START!");
        if (currentTime / 1000 < 10) {
            pbar.setVisibility(View.INVISIBLE);
            timer.setTextColor(Color.BLACK);
            timer.setText(String.format("%d:%03d", currentTime / 1000, currentTime % 1000));
        } else {
            pbar.setVisibility(View.VISIBLE);
            int temp = (int) ((1 - ((currentTime % 1000) / 1000.0)) * 60);
            pbar.setProgress(temp);
            timer.setText(currentTime / 1000 + "");
        }
        touchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startClick();
                touchLayout.setOnClickListener(null);
            }
        });
    }

    /**
     * initialise Variables
     * //TODO me kapio tropo na kamoume ta indicators (rectangles) na exoun to size tou onomatos tis omadas.
     */
    public void initializeVariables() {
        mpTimer = MediaPlayer.create(this, R.raw.timer);
        mpCorrect = MediaPlayer.create(this, R.raw.correct);
        mpWrong = MediaPlayer.create(this, R.raw.wrong);
        mpSkip = MediaPlayer.create(this, R.raw.skip);
        mpRound = MediaPlayer.create(this, R.raw.round);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);

        lp.weight = 1.0f;
        lp.gravity = Gravity.CENTER;

        //Insert team names
        for (int i = 0; i < NumOfTeams; i++) {

            teamNames[i] = new TextView(this);
            teamNames[i].setText(teamNamesStr[i]);
            teamNames[i].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            teamScores[i] = new TextView(this);
            teamScores[i].setText(Scores[i] + "");
            teamScores[i].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            indicators[i] = new TextView(this);
            indicators[i].setBackgroundResource(R.drawable.rectangle_shape);
            indicators[i].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            indicators[i].setVisibility(View.INVISIBLE);

            teamNames[i].setLayoutParams(lp);
            teamScores[i].setLayoutParams(lp);
            indicators[i].setLayoutParams(lp);

            llTeamNames.addView(teamNames[i]);
            llTeamScores.addView(teamScores[i]);
            llindicators.addView(indicators[i]);
        }
        indicators[teamPlaying].setVisibility(View.VISIBLE);
    }

    /**
     * Tiponi ta periexomena tis kartas [i] pou ton pinaka cardArray.
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
     * Metritis pros ta katw arxizontas p to seconds.
     *
     * @param milis
     */
    public void startCounter(long milis) {
        timer.setTextColor(oldColors);
        cdt = new CountDownTimer((int) (milis), 10) {
            @Override
            public void onTick(long milisUntilFinished) {
                if (pbar.getProgress() >= 60) {
                    pbar.setProgress((pbar.getProgress() + 1) % 60);
                } else {
                    pbar.setProgress(pbar.getProgress() + 1);
                }
                if (milisUntilFinished / 1000 < 10) {
                    pbar.setVisibility(View.INVISIBLE);
                    timer.setTextColor(Color.BLACK);
                    timer.setText(String.format("%d:%03d", milisUntilFinished / 1000, milisUntilFinished % 1000));
                    if((milisUntilFinished/1000<3)&&(milisUntilFinished%500<=100)) {
                        mpTimer.start();
                    }else if((milisUntilFinished%1000<=100)&&(milisUntilFinished/1000>0))
                        mpTimer.start();
                } else {
                    pbar.setVisibility(View.VISIBLE);
                    timer.setText(milisUntilFinished / 1000 + "");
                }
                currentTime = milisUntilFinished;
                if(milisUntilFinished%5000<=100){
                    //TODO add a counter and change console message every click
                    RunAnimation(console);
                }
            }
            @Override
            public void onFinish() {
                mpRound.start();
                if (teamPlaying == NumOfTeams - 1) {
                    rounds++;
                    if (mode == 0) {
                        if (rounds >= maxRounds) {
                            gameEnd();
                            return;
                        }
                    }
                }
                teamPlaying = (teamPlaying + 1) % NumOfTeams;
                btnCorrect.setEnabled(false);
                btnWrong.setEnabled(false);
                btnSkip.setEnabled(false);
                currentTime = realTime * 1000;

                timer.setText("TIME'S UP");
                Animation anim = new AlphaAnimation(0.0f, 1.0f);
                anim.setDuration(200); //You can manage the time of the blink with this parameter
                anim.setStartOffset(0);
                anim.setRepeatMode(Animation.REVERSE);
                anim.setRepeatCount(10);
                timer.startAnimation(anim);
                anim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation arg0) {
                        //TODO INSERT "TOUCH HERE TO START"
                        timer.setTextColor(oldColors);
                        pbar.setVisibility(View.VISIBLE);
                        pbar.setProgress(60);
                        timer.setText("" + realTime);
                        txt1.setText("");
                        txt2.setText("");
                        txt3.setText("");
                        txt4.setText("");
                        txt5.setText("");
                        txtMain.setText("Tap to START!");
                        console.setText("Round " + rounds + ": " + teamNamesStr[teamPlaying] + "'s turn!");
                        for (int i = 0; i < indicators.length; i++) {
                            indicators[i].setVisibility(View.INVISIBLE);
                        }
                        indicators[teamPlaying].setVisibility(View.VISIBLE);
                        touchLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startClick();
                                touchLayout.setOnClickListener(null);
                            }
                        });
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
            }
        }.start();
    }

    /**
     * When a team wins
     */
    private void gameEnd() {
                //Insertion Sort (fast enough for maximum 6 values)
        for (int i = 1; i < Scores.length; i++) {
            int temp = Scores[i];
            String temp2 = teamNamesStr[i];
            int j;
            for (j = i - 1; j >= 0 && temp < Scores[j]; j--) {
                Scores[j + 1] = Scores[j];
                teamNamesStr[j + 1] = teamNamesStr[j];
            }
            Scores[j + 1] = temp;
            teamNamesStr[j + 1] = temp2;
        }
        SharedPreferences sharedPref = getSharedPreferences("userSettings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        for (int i = 0; i < NumOfTeams; i++) {
            editor.putInt("finalScores" + i, Scores[i]);
            editor.putString("teamNames" + i, teamNamesStr[i]);
        }
        editor.commit();
        Intent intent = new Intent(this, End.class);
        startActivity(intent);
        End.setNumber(NumOfTeams);
        MainMenu.disableCont();
        saveValues();
        finish();

    }

    /**
     * Ananeoni ta score tis omadas
     */
    public void updateScores() {
        for (int i = 0; i < NumOfTeams; i++) {
            if(teamScores[i].getText().toString().compareTo(String.valueOf(Scores[i]))!=0) {
                teamScores[i].setText(Scores[i] + "");
                RunAnimation(teamScores[i]);
            }
            if (mode == 1 && Scores[i] >= goalRounds) {
                gameEnd();
                return;
            }
        }
    }

    /**
     * Epilogi tis epomenis kartas me patenta ston pinaka
     * kathe epilegmeni karta pai sti thesi index k to index mionete kata 1
     * otan to index gini -1,
     * //TODO diavazi to epomeno paketoui lexeon
     */
    public void selectNewCard() {
        currentCard = (int) (Math.random() * (index + 1));
        printCard(cardArray, currentCard);
        Card temp = cardArray[index];
        cardArray[index] = cardArray[currentCard];
        cardArray[currentCard] = temp;
        index--;
        if (index == -1) {
            if (countFiles >= MAXFILES) {
                countFiles = 0;
                //Toast.makeText(this, "Last Card pack is done! Check for updates.", LENGTH_LONG).show();
            } else {
                //Toast.makeText(this, "Card pack: " + (countFiles) + " is done!", LENGTH_SHORT).show();
            }
            int nextFile = getResources().getIdentifier("words" + (++countFiles), "raw", getPackageName());
            cardArray = Card.initArray(this, nextFile);
            index = cardArray.length - 1;
        }

    }

    /**
     * Wrong Click
     */
    public void WrongClick(View v) {
        mpWrong.start();
        if (wrongPoints == -1) {
            Scores[teamPlaying]--;
        } else if (wrongPoints == 1) {
            for (int i = 0; i < Scores.length; i++) {
                if (i != teamPlaying) {
                    Scores[i]++;
                }
            }
        }
        if (Scores[teamPlaying] < 0) {
            Scores[teamPlaying] = 0;
        }
        updateScores();
        selectNewCard();
    }

    /**
     * Skip Click
     */
    public void SkipClick(View v) {
        mpSkip.start();
        if (skipPoints == -1) {
            Scores[teamPlaying]--;
        } else if (skipPoints == 1) {
            for (int i = 0; i < Scores.length; i++) {
                if (i != teamPlaying) {
                    Scores[i]++;
                }
            }
        }
        if (Scores[teamPlaying] < 0) {
            Scores[teamPlaying] = 0;
        }
        updateScores();
        selectNewCard();
    }

    /**
     * Correct Click
     */
    public void CorrectClick(View v) {
        mpCorrect.start();
        Scores[teamPlaying]++;
        updateScores();
        selectNewCard();
    }

    /**
     * Start Click
     * start next team's timer
     */
    public void PauseClick(View v) {
        if (cdt != null)
            cdt.cancel();
        btnCorrect.setEnabled(false);
        btnWrong.setEnabled(false);
        btnSkip.setEnabled(false);
        saveValues();
        txt1.setText("");
        txt2.setText("");
        txt3.setText("");
        txt4.setText("");
        txt5.setText("");
        txtMain.setText("Tap to START!");
        touchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startClick();
                touchLayout.setOnClickListener(null);
            }
        });
        //TODO CALL DIAFIMISI
    }

    public void startClick() {
        selectNewCard();
        startCounter(currentTime);
        btnCorrect.setEnabled(true);
        btnWrong.setEnabled(true);
        btnSkip.setEnabled(true);
    }


    /**
     * Return to main menu
     */
    public void exitClick(View v) {
        if (cdt != null)
            cdt.cancel();
        saveValues();
        finish();
    }

    /**
     * Saves game to file.
     */
    public void saveValues() {
        SharedPreferences sharedPref = getSharedPreferences("userSettings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("cont",MainMenu.getCont());
        editor.putInt("MAXFILES", MAXFILES);
        editor.putLong("currentTime", currentTime);
        editor.putInt("countFiles", countFiles);
        editor.putInt("teamPlaying", teamPlaying);
        editor.putInt("index", index);
        editor.putBoolean("settingsChanged", false);
        editor.putInt("rounds", rounds);
        Gson gson = new Gson();
        for (int i = 0; i <= index; i++) {
            String json = gson.toJson(cardArray[i]);
            editor.putString("cardArray" + i, json);
        }
        for (int i = 0; i < NumOfTeams; i++) {
            editor.putInt("teamScores" + i, Scores[i]);
        }
        editor.commit();
    }
}
