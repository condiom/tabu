package tratrafe2.condiom.tambu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;

public class Game extends Activity {
   final int MAXTEAMS = 6;
   int MAXFILES = 2;
   int countFiles = 0;
   int teamPlaying = 0;
   int debug = 0;


   LinearLayout llTeamNames, llTeamScores;
   TextView timer, txt1, txt2, txt3, txt4, txt5, txtMain;
   TextView teamNames[], teamScores[];
   Button btnStart, btnWrong, btnSkip, btnCorrect, btnNewGame;

   int Scores[];
   int currentCard, realTime, NumOfTeams, wrongPoints, skipPoints, goal, rounds, index;
   float sizeOfLetters;
   boolean radio;
   Card cardArray[];
   String teamNamesStr[];

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
   }

   public void getSettings() {
      SharedPreferences sharedPref = getSharedPreferences("userSettings", Context.MODE_PRIVATE);
      boolean settingsChanged = sharedPref.getBoolean("settingsChanged", false);

      realTime = sharedPref.getInt("realTime", 30);
      NumOfTeams = sharedPref.getInt("teams", 2);
      wrongPoints = sharedPref.getInt("wrong", 0);
      skipPoints = sharedPref.getInt("skip", 0);
      goal = sharedPref.getInt("goal", 50);
      rounds = sharedPref.getInt("rounds", 20);
      radio = sharedPref.getBoolean("radio", true);
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
      timer.setText(String.format("%d:%03d", currentTime, 0) + " " + teamNamesStr[teamPlaying] + " PLAYS");
      txt1.setText("");
      txt2.setText("");
      txt3.setText("");
      txt4.setText("");
      txt5.setText("");
      txtMain.setText("");
   }

   /**
    * initialise Variables
    */
   public void initializeVariables() {
      LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
      lp.weight = 1.0f;
      lp.gravity = Gravity.CENTER_VERTICAL;
      for (int i = 0; i < NumOfTeams; i++) {
         teamNames[i] = new TextView(this);
         teamNames[i].setText(teamNamesStr[i]);
         teamScores[i] = new TextView(this);
         teamScores[i].setText(Scores[i] + "");

         teamNames[i].setLayoutParams(lp);
         teamScores[i].setLayoutParams(lp);

         llTeamNames.addView(teamNames[i]);
         llTeamScores.addView(teamScores[i]);

      }
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
    * Metritis pros ta katw arxizontas p to minutes.
    *
    * @param minutes
    */
   int currentTime = 0;

   public void startCounter(int minutes) {
      timer.setTextColor(oldColors);
      countDownTimer:
      new CountDownTimer(minutes * 1000, 10) {
         @Override
         public void onTick(long milisUntilFinished) {
            timer.setText(String.format("%d:%03d", milisUntilFinished / 1000, milisUntilFinished % 1000) + " " + teamNamesStr[teamPlaying] + " PLAYS");
            if (milisUntilFinished / 1000 <= 10) {
               timer.setTextColor(Color.RED);

            }
            currentTime = (int) (milisUntilFinished / 1000);
         }

         @Override
         public void onFinish() {
            teamPlaying = (teamPlaying + 1) % NumOfTeams;
            timer.setText("TIME'S UP " + teamNamesStr[teamPlaying] + " PLAYS");
            btnStart.setEnabled(true);
            btnCorrect.setEnabled(false);
            btnWrong.setEnabled(false);
            btnSkip.setEnabled(false);
            btnNewGame.setEnabled(true);
            currentTime = realTime;
         }
      }.start();
   }

   /**
    * Ananeoni ta score tis omadas
    */
   public void updateScores() {
      for (int i = 0; i < NumOfTeams; i++) {
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
            Toast.makeText(this, "Last Card pack is done! Check for updates.", LENGTH_LONG).show();
         } else {
            Toast.makeText(this, "Card pack: " + (countFiles) + " is done!", LENGTH_SHORT).show();
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
      selectNewCard();
      startCounter(currentTime);
      btnStart.setEnabled(false);
      btnCorrect.setEnabled(true);
      btnWrong.setEnabled(true);
      btnSkip.setEnabled(true);
      btnNewGame.setEnabled(false);
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
                    timer.setText(String.format("%d:%03d", currentTime, 0) + " " + teamNamesStr[teamPlaying] + " PLAYS");
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
