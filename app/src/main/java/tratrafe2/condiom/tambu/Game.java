package tratrafe2.condiom.tambu;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewOutlineProvider;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Random;


public class Game extends Activity {
    final int MAXTEAMS = 6;
    int MAXFILES = 13;
    int countFiles = 0;
    int teamPlaying = 0;
    int debug = 0;
    String words="words_gr_";
    long currentTime = 0;
    static boolean newGame;
    CountDownTimer cdt;
    ColorStateList oldColors;

    MediaPlayer mpTimer,mpCorrect,mpSkip,mpWrong,mpRound;

    ProgressBar pbar;
    LinearLayout llTeamNames, llindicators, touchLayout;
    TextView console, timer, txt1, txt2, txt3, txt4, txt5, txtMain;
    TextView indicators[];
    TextView teamBalls[];
    Button btnWrong, btnSkip, btnCorrect, btnNewGame;
    ImageButton btnPause;

    int Scores[],colors[];
    int currentCard, realTime, NumOfTeams, wrongPoints, skipPoints, goal, rounds, index, mode, maxRounds, goalRounds;
    Card cardArray[];
    String teamNamesStr[], winingTeam;


    //CIRCLES
    View bt1, bt2, bt3, bt4;
    FrameLayout fr;
    CountDownTimer cdt2;
    View tx;
    View b;
    private float x1, x2, y1, y2;
    static final int MIN_DISTANCE = 150;
    static final int MIN_NEG_DISTANCE = -150;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        fr = (FrameLayout) findViewById(R.id.frLayout);
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
        teamBalls = new TextView[NumOfTeams];
        colors = new int[NumOfTeams];
        indicators = new TextView[NumOfTeams];
        teamPlaying = sharedPref.getInt("teamPlaying", 0);
        MAXFILES = sharedPref.getInt("MAXFILES", 13);
        countFiles = sharedPref.getInt("countFiles", 0);
        for (int i = 0; i < NumOfTeams; i++) {
            Scores[i] = sharedPref.getInt("teamScores" + i, 0);
        }
        index = sharedPref.getInt("index", -5);
        if (index == -5) {
            int nextFile = getResources().getIdentifier(words + (++countFiles), "raw", getPackageName());
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
     *
     */
    public void initializeVariables() {
        mpTimer = MediaPlayer.create(this, R.raw.timer);
        mpCorrect = MediaPlayer.create(this, R.raw.correct);
        mpWrong = MediaPlayer.create(this, R.raw.wrong);
        mpSkip = MediaPlayer.create(this, R.raw.skip);
        mpRound = MediaPlayer.create(this, R.raw.round);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        lp.weight = 1.0f;
        lp.gravity = Gravity.CENTER;

        //Insert team names
        for (int i = 0; i < NumOfTeams; i++) {
            final int j=i;
            teamBalls[i] = new TextView(this);
            teamBalls[i].setBackgroundResource(R.drawable.circle_ball);
            GradientDrawable sd = (GradientDrawable)  teamBalls[i].getBackground().mutate();
            Random rnd = new Random();
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            colors[i]=color;
            sd.setColor(color); //TODO ADD COLOR BASED ON TEAM

            teamBalls[i].setText(Scores[i] + "");
            teamBalls[i].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            indicators[i] = new TextView(this);
            indicators[i].setBackgroundResource(R.drawable.rectangle_shape);
            indicators[i].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            indicators[i].setVisibility(View.INVISIBLE);

            teamBalls[i].setLayoutParams(lp);
            teamBalls[i].setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            indicators[i].setLayoutParams(lp);

            fr.addView(teamBalls[i]);
            llindicators.addView(indicators[i]);
        }
        fr.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                fr.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                float pos = 1.0f*(fr.getWidth()-teamBalls[0].getWidth()*NumOfTeams) / (2*NumOfTeams);
                float space = teamBalls[0].getWidth() / 2.0f;
                for (int i = 0; i < NumOfTeams; i++) {
                    float myPos=1.0f*(pos+(2*space+2*pos)*i);
                    teamBalls[i].setX(myPos);
                }
            }
        });
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
                timer.setText(milisUntilFinished / 1000 + "");
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
                ballThrow(teamBalls[teamPlaying]);
                indicators[teamPlaying].setVisibility(View.VISIBLE);
                touchLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startClick();
                        touchLayout.setOnClickListener(null);
                    }
                });
            }
        }.start();
    }

    /**
     * When a team wins
     */
    private void gameEnd() {
        cdt.cancel();
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
            editor.putString("teamBalls" + i, teamNamesStr[i]);
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
            if(teamBalls[i].getText().toString().compareTo(String.valueOf(Scores[i]))!=0) {
                teamBalls[i].setText(Scores[i] + "");
                RunAnimation(teamBalls[i]);
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
            int nextFile = getResources().getIdentifier(words + (++countFiles), "raw", getPackageName());
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void revealCircle(View v,int i,final float x,final float y) {
        // previously invisible view
        View myView = findViewById(i);
        // get the center for the clipping circle
        int cx = (int) (v.getX() + v.getWidth() / 2);
        int cy = (int) (v.getY() + v.getHeight() / 2);


        // get the final radius for the clipping circle
        float finalRadius = (float) Math.hypot(cx, cy);

        // create the animator for this view (the start radius is zero)
        Animator anim =
                ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius);



        // make the view visible and start the animation
        myView.setVisibility(View.VISIBLE);
        anim.setDuration(1000);
        anim.addListener(new Animator.AnimatorListener(){

            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                ColorDrawable cd=new ColorDrawable();
                cd.setColor(colors[teamPlaying]);
                ((ImageView)findViewById(R.id.img2)).setImageDrawable(cd);
                ((ImageView)findViewById(R.id.img1)).setVisibility(View.INVISIBLE);
                b.setX(x);
                b.setY(y);
                ((GradientDrawable ) ((TextView)b).getBackground()).setStroke(3,Color.BLACK,0,0);
                int last=teamPlaying-1;
                if(last==-1)
                    last=NumOfTeams-1;
                ((GradientDrawable ) ((TextView)teamBalls[last]).getBackground()).setStroke(0,Color.BLACK,0,0);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        anim.start();

    }

    public void ballThrow(View v) {
        b = v;
        final float oldX=b.getX();
        final float oldY=b.getY();
        if (cdt2 != null)
            cdt2.cancel();
        tx = (View) findViewById(R.id.timerFL);
        final float finalX=fr.getWidth()/2.0f ;
        cdt2 = new CountDownTimer(100000, 1) {
            float speedX;
            float speed = speedX=10f;

            @Override
            public void onTick(long l) {
                speed += 0.1f;
                if (b.getX() + b.getWidth() / 2 != finalX) {
                    if (b.getX() + b.getWidth() / 2 > finalX) {
                        b.setX(b.getX() - speedX);
                    } else {
                        b.setX(b.getX() + speedX);
                    }
                    b.setY(b.getY() + speed);
                    if (Math.abs((b.getX() + b.getWidth() / 2) - (finalX)) < speed) {
                        b.setX((finalX) - b.getWidth() / 2);
                    }

                } else {

                    if (b.getY() + b.getHeight() / 2 < tx.getY() + tx.getHeight() / 2)
                        b.setY(b.getY() + speed);
                    else {
                        b.setY(-b.getHeight() / 2 + tx.getY() + tx.getHeight() / 2);
                        cdt2.cancel();
                        ColorDrawable cd = new ColorDrawable();
                        cd.setColor(colors[teamPlaying]);
                        ((ImageView) findViewById(R.id.img1)).setImageDrawable(cd);
                        revealCircle(b, R.id.img1, oldX, oldY);
                    }
                }
            }
            @Override
            public void onFinish() {
            }
        };

        cdt2.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                y1 = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                y2 = event.getY();
                float deltaX = x2 - x1;
                float deltaY = y2 - y1;
                if ((deltaX > MIN_DISTANCE) && (Math.abs(deltaY) < MIN_DISTANCE)) {
                    Toast.makeText(this, "left2right swipe", Toast.LENGTH_SHORT).show();
                } else if ((deltaX < MIN_NEG_DISTANCE) && (Math.abs(deltaY) < MIN_DISTANCE)) {
                    Toast.makeText(this, "right2left swipe", Toast.LENGTH_SHORT).show();
                } else if ((Math.abs(deltaX) < MIN_DISTANCE) && (deltaY > MIN_DISTANCE)) {
                    Toast.makeText(this, "up2down swipe", Toast.LENGTH_SHORT).show();
                } else if ((Math.abs(deltaX) < MIN_DISTANCE) && (deltaY < MIN_NEG_DISTANCE)) {
                    Toast.makeText(this, "down2up swipe", Toast.LENGTH_SHORT).show();
                }

                break;
        }
        return super.onTouchEvent(event);
    }
}
