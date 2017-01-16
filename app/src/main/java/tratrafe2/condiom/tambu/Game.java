package tratrafe2.condiom.tambu;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;


public class Game extends Activity {
   final int MAXTEAMS = 6;
   int MAXFILES = 2;
   int countFiles = 0;
   int teamPlaying = 0;
   int debug = 0;
   boolean paused = false;

   CountDownTimer cdt;

   ProgressBar pbar;
   LinearLayout llTeamNames, llTeamScores, llindicators;
   TextView timer, txt1, txt2, txt3, txt4, txt5, txtMain;
   TextView teamNames[], teamScores[], indicators[];
   Button btnStart, btnWrong, btnSkip, btnCorrect, btnNewGame;

   int Scores[];
   int currentCard, realTime, NumOfTeams, wrongPoints, skipPoints, goal, rounds, index, radioInfo;
   float sizeOfLetters;
   boolean radio;
   Card cardArray[];
   String teamNamesStr[],winingTeam;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_game);


      getSettings();
      initializeViews();
      initializeVariables();

   }

   public void resetGame() {
      currentTime = realTime;
      teamPlaying = 0;
      for (int i = 0; i < NumOfTeams; i++) {
         Scores[i] = 0;
      }
      rounds=0;
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
      radio = sharedPref.getBoolean("radio", true);
      radioInfo = sharedPref.getInt("radioInfo", 50);
      currentTime = sharedPref.getInt("currentTime", realTime);
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
         resetGame();
      }
   }

   /**
    * initialise views
    */
   ColorStateList oldColors;

   public void initializeViews() {

      txt1 = (TextView) findViewById(R.id.txt1);
      txt2 = (TextView) findViewById(R.id.txt2);
      txt3 = (TextView) findViewById(R.id.txt3);
      txt4 = (TextView) findViewById(R.id.txt4);
      txt5 = (TextView) findViewById(R.id.txt5);
      timer = (TextView) findViewById(R.id.txtTimer);
      txtMain = (TextView) findViewById(R.id.txtMain);
      btnStart = (Button) findViewById(R.id.btnStart);
      btnCorrect = (Button) findViewById(R.id.btnCorrect);
      btnSkip = (Button) findViewById(R.id.btnSkip);
      btnWrong = (Button) findViewById(R.id.btnWrong);
      llTeamNames = (LinearLayout) findViewById(R.id.llteamNames);
      llTeamScores = (LinearLayout) findViewById(R.id.llteamScores);
      llindicators = (LinearLayout) findViewById(R.id.llindicators);
      btnNewGame = (Button) findViewById(R.id.btnNewGame);
      btnCorrect.setEnabled(false);
      btnWrong.setEnabled(false);
      btnSkip.setEnabled(false);
      /*
      sizeOfLetters = 30;
      txtMain.setTextSize(sizeOfLetters + 15);
      txt1.setTextSize(sizeOfLetters);
      txt2.setTextSize(sizeOfLetters);
      txt3.setTextSize(sizeOfLetters);
      txt4.setTextSize(sizeOfLetters);
      txt5.setTextSize(sizeOfLetters);
      timer.setTextSize(sizeOfLetters);
      */
      oldColors = txt1.getTextColors(); //save original colors

      //timer.setText(String.format("%d:%03d", realTime, 0));
      timer.setText(teamNamesStr[teamPlaying] + "'s turn!");
      txt1.setText("");
      txt2.setText("");
      txt3.setText("");
      txt4.setText("");
      txt5.setText("");
      txtMain.setText("");
   }

   /**
    * initialise Variables
    * //TODO me kapio tropo na kamoume ta indicators (rectangles) na exoun to size tou onomatos tis omadas.
    */
   @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
   public void initializeVariables() {
      LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);

      lp.weight = 1.0f;
      lp.gravity = Gravity.CENTER_VERTICAL;
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
      updateScores();
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
    * @param minutes
    */
   int currentTime = 0;

   public void startCounter(int seconds) {
      pbar = (ProgressBar) findViewById(R.id.progressBar);
      timer.setTextColor(oldColors);
      pbar.setVisibility(View.VISIBLE);
      cdt = new CountDownTimer(seconds * 1000, 10) {
         @Override
         public void onTick(long milisUntilFinished) {
            pbar.setProgress((pbar.getProgress() + 1) % 60);
            if (milisUntilFinished / 1000 <= 10) {
               pbar.setVisibility(View.INVISIBLE);
               timer.setTextColor(Color.RED);
               timer.setText(String.format("%d:%03d", milisUntilFinished / 1000, milisUntilFinished % 1000));
            } else {
               timer.setText(milisUntilFinished / 1000 + "");
            }
            currentTime = (int) (milisUntilFinished / 1000);
         }

         @Override
         public void onFinish() {
            if(teamPlaying==NumOfTeams-1){
               rounds++;
               if(radio)
               {
                  if(rounds==radioInfo){
                     gameEnd();
                     return;
                  }
               }
            }
            teamPlaying = (teamPlaying + 1) % NumOfTeams;

            btnStart.setEnabled(true);
            btnCorrect.setEnabled(false);
            btnWrong.setEnabled(false);
            btnSkip.setEnabled(false);
            btnNewGame.setEnabled(true);
            btnStart.setText("START");
            paused = !paused;
            currentTime = realTime;

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
                  btnStart.setEnabled(false);

               }

               @Override
               public void onAnimationEnd(Animation arg0) {
                  btnStart.setEnabled(true);
                  timer.setTextColor(oldColors);
                  timer.setText(teamNamesStr[teamPlaying] + "'s turn!");
                  for (int i = 0; i < indicators.length; i++) {
                     indicators[i].setVisibility(View.INVISIBLE);
                  }
                  indicators[teamPlaying].setVisibility(View.VISIBLE);
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
      int max=0;
      int maxTeam=0;
      for(int i=0;i<NumOfTeams;i++){
         if(max<Scores[i]){
            max=Scores[i];
            maxTeam=i;
         }
      }
      winingTeam=teamNamesStr[maxTeam];
      resetGame();
      saveValues();
      Intent intent = new Intent(this, End.class);
      startActivity(intent);
   }

   /**
    * Ananeoni ta score tis omadas
    */
   public void updateScores() {
      for (int i = 0; i < NumOfTeams; i++) {
         if(!radio&&Scores[i]>=radioInfo){
            gameEnd();
         }
         teamScores[i].setText(Scores[i] + "");
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
      if (wrongPoints == 0) {
         Scores[teamPlaying]--;
      } else if (wrongPoints == 2) {
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
      if (skipPoints == 0) {
         Scores[teamPlaying]--;
      } else if (skipPoints == 2) {
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
      Scores[teamPlaying]++;
      updateScores();
      selectNewCard();
   }

   /**
    * Start Click
    * start next team's timer
    */
   public void StartClick(View v) {
      if (!paused) {
         paused = !paused;
         selectNewCard();
         startCounter(currentTime);
         btnStart.setText("PAUSE ||");
         btnCorrect.setEnabled(true);
         btnWrong.setEnabled(true);
         btnSkip.setEnabled(true);
         btnNewGame.setEnabled(false);
      } else {
         paused = !paused;
         btnStart.setText("START");
         btnCorrect.setEnabled(false);
         btnWrong.setEnabled(false);
         btnSkip.setEnabled(false);
         btnNewGame.setEnabled(true);
         pbar.setProgress(0);
         cdt.cancel();

      }
   }

   /**
    * Start a new game
    * reset scores
    */
   public void newGameClick(View v) {
      new AlertDialog.Builder(this)
              .setTitle("New Game")
              .setMessage("Do you really want to create a new game?")
              .setIcon(android.R.drawable.ic_dialog_alert)
              .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                 public void onClick(DialogInterface dialog, int whichButton) {
                    resetGame();
                    txt1.setText("");
                    txt2.setText("");
                    txt3.setText("");
                    txt4.setText("");
                    txt5.setText("");
                    txtMain.setText("");
                    timer.setTextColor(oldColors);
                    timer.setText(teamNamesStr[teamPlaying] + "'s turn!");
                    for (int i = 0; i < indicators.length; i++) {
                       indicators[i].setVisibility(View.INVISIBLE);
                    }
                    indicators[teamPlaying].setVisibility(View.VISIBLE);
                    updateScores();
                 }
              })
              .setNegativeButton(android.R.string.no, null).show();
   }

   /**
    * Return to main menu
    */
   public void exitClick(View v) {
      saveValues();
      finish();
   }

   /**
    * Saves game to file.
    */
   public void saveValues() {
      SharedPreferences sharedPref = getSharedPreferences("userSettings", Context.MODE_PRIVATE);
      SharedPreferences.Editor editor = sharedPref.edit();
      editor.putInt("MAXFILES", MAXFILES);
      editor.putInt("currentTime", currentTime);
      editor.putInt("countFiles", countFiles);
      editor.putInt("teamPlaying", teamPlaying);
      editor.putInt("index", index);
      editor.putBoolean("settingsChanged", false);
      editor.putString("winingTeam",winingTeam);
      editor.putInt("rounds",rounds);
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
