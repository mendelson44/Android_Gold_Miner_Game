package com.example.gold_miner_game.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;

import com.example.gold_miner_game.R;
import com.example.gold_miner_game.logic.GameManager;
import com.example.gold_miner_game.logic.SensorManager;
import com.example.gold_miner_game.model.AllPlayers;
import com.example.gold_miner_game.model.MyBackgroundMusic;
import com.example.gold_miner_game.model.Player;
import com.example.gold_miner_game.model.GameObstacle;
import com.example.gold_miner_game.model.Level;
import com.example.gold_miner_game.logic.CallBack_CharacterMovement;
import com.example.gold_miner_game.model.SoundPlayer;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {


    private RelativeLayout[][] gameTracksArray;
    private RelativeLayout[] track1;
    private RelativeLayout[] track2;
    private RelativeLayout[] track3;
    private RelativeLayout[] track4;
    private RelativeLayout[] track5;

    private FloatingActionButton GoldMiner_BTL_left;
    private FloatingActionButton GoldMiner_BTL_right;
    private ShapeableImageView GoldMiner_IMG_character;
    private MaterialTextView GoldMiner_LBL_money;
    private MaterialTextView GoldMiner_LBL_target;
    private MaterialTextView GoldMiner_LBL_main_levelView;
    private MaterialTextView GoldMiner_LBL_level;
    private MaterialTextView GoldMiner_LBL_level_moneyBag;
    private MaterialTextView GoldMiner_LBL_level_gold;
    private MaterialTextView GoldMiner_LBL_level_rock;
    private MaterialTextView GoldMiner_LBL_level_TNT;
    private CardView GoldMiner_cardView_nextLevel;
    private MaterialButton GoldMiner_BTL_level_continue;
    private ShapeableImageView[] GoldMiner_IMG_hearts;

    //cardVIEW Variables:
    private MaterialButton MAIN_CARDVIEW_BTN_OK;
    private CardView MAIN_GAMEOVER_CARDVIEW;
    private MaterialTextView MAIN_LBLCARD_FINALSCORE;
    private AppCompatEditText MAIN_EDITTEXT_NAME;

    private boolean isGameRunning = true;
    private boolean sensors;
    private boolean slow;
    private int speedOfObjects;
    private double lat;
    private double lng;
    private final int VIBRATIONTIMING = 300;
    private final int FIELD_GAME_COLUMNS = 5;
    private final int FIELD_GAME_ROWS = 8;
    private final int LIVES = 3;


    private Handler handler = new Handler(Looper.getMainLooper());
    private SensorManager sensorsManager;
    private GameManager gameManager;
    AllPlayers allPlayers;
    private SoundPlayer soundPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        MyBackgroundMusic.getInstance().setResourceId(R.raw.gold_miner_game_music);
        soundPlayer = new SoundPlayer(this);


        findViews();
        getGameMode();
        adjustGameMode();
        showCardOfEndGame(false);
        AllPlayers allPlayers = AllPlayers.getInstance();
        gameTracksArray = getAllGameTracks(FIELD_GAME_ROWS,FIELD_GAME_COLUMNS);
        GoldMiner_BTL_left.setOnClickListener(view -> moveLeft());
        GoldMiner_BTL_right.setOnClickListener(view -> moveRight());
        MAIN_CARDVIEW_BTN_OK.setOnClickListener(view -> savePlayerScoreAndGoToPlayersActivity());
        gameManager = new GameManager(LIVES,FIELD_GAME_COLUMNS,FIELD_GAME_ROWS);
        gameManager.generateLevels();
        UI_nextLevel(gameManager.nextLevel(this));
        GoldMiner_BTL_level_continue.setOnClickListener(view -> startGame());
    }

    private void getGameMode() {
        Intent intent = getIntent();
        String jsonString = intent.getStringExtra("jsonString");
        try {
            JSONObject jsonObject = new JSONObject(jsonString);

            sensors = jsonObject.getBoolean("sensors");
            slow = jsonObject.getBoolean("slow");
            lat = jsonObject.getDouble("lat");
            lng = jsonObject.getDouble("lng");

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void adjustGameMode() {
        if (slow) {
            speedOfObjects = 850;
        } else {
            speedOfObjects = 650;
        }
        if (sensors) {
            initMovementSensors();
            GoldMiner_BTL_left.setVisibility(View.INVISIBLE);
            GoldMiner_BTL_right.setVisibility(View.INVISIBLE);
            sensorsManager.start();
        }
    }

    private void initMovementSensors() {
        sensorsManager = new SensorManager(this, new CallBack_CharacterMovement() {

            @Override
            public void CharacterMoveRight() {
                moveRight();
            }

            @Override
            public void CharacterMoveLeft() {
                moveLeft();
            }
        });
    }

    private RelativeLayout[][] getAllGameTracks(int rows, int columns) {

        RelativeLayout[][] relativeLayouts = new RelativeLayout[rows][columns];
        for (int i = 0; i < rows; i++) {
            relativeLayouts[i][0] = track1[i];
            relativeLayouts[i][1] = track2[i];
            relativeLayouts[i][2] = track3[i];
            relativeLayouts[i][3] = track4[i];
            relativeLayouts[i][4] = track5[i];
        }
        return relativeLayouts;
    }

    private void findViews() {

        GoldMiner_IMG_character = findViewById(R.id.GoldMiner_IMG_character);
        GoldMiner_BTL_left = findViewById(R.id.GoldMiner_BTL_left);
        GoldMiner_BTL_right = findViewById(R.id.GoldMiner_BTL_right);
        GoldMiner_LBL_money = findViewById(R.id.GoldMiner_LBL_money);
        GoldMiner_LBL_target = findViewById(R.id.GoldMiner_LBL_target);
        GoldMiner_LBL_main_levelView = findViewById(R.id.GoldMiner_LBL_main_levelView);
        GoldMiner_LBL_level_moneyBag = findViewById(R.id.GoldMiner_LBL_level_moneyBag);
        GoldMiner_LBL_level_gold = findViewById(R.id.GoldMiner_LBL_level_gold);
        GoldMiner_LBL_level_rock = findViewById(R.id.GoldMiner_LBL_level_rock);
        GoldMiner_LBL_level_TNT = findViewById(R.id.GoldMiner_LBL_level_TNT);
        GoldMiner_cardView_nextLevel = findViewById(R.id.GoldMiner_cardView_nextLevel);
        GoldMiner_LBL_level = findViewById(R.id.GoldMiner_LBL_level);
        GoldMiner_BTL_level_continue = findViewById(R.id.GoldMiner_BTL_level_continue);
        MAIN_GAMEOVER_CARDVIEW = findViewById(R.id.MAIN_GAMEOVER_CARDVIEW);
        MAIN_LBLCARD_FINALSCORE = findViewById(R.id.MAIN_LBLCARD_FINALSCORE);
        MAIN_CARDVIEW_BTN_OK = findViewById(R.id.MAIN_CARDVIEW_BTN_OK);
        MAIN_EDITTEXT_NAME = findViewById(R.id.MAIN_EDITTEXT_NAME);

        GoldMiner_IMG_hearts = new ShapeableImageView[]{
                findViewById(R.id.GoldMiner_IMG_heart1),
                findViewById(R.id.GoldMiner_IMG_heart2),
                findViewById(R.id.GoldMiner_IMG_heart3)
        };

        track1 = new RelativeLayout[] {
                findViewById(R.id.GoldMiner_LAY_gameTrack_1_index_1),
                findViewById(R.id.GoldMiner_LAY_gameTrack_1_index_2),
                findViewById(R.id.GoldMiner_LAY_gameTrack_1_index_3),
                findViewById(R.id.GoldMiner_LAY_gameTrack_1_index_4),
                findViewById(R.id.GoldMiner_LAY_gameTrack_1_index_5),
                findViewById(R.id.GoldMiner_LAY_gameTrack_1_index_6),
                findViewById(R.id.GoldMiner_LAY_gameTrack_1_index_7),
                findViewById(R.id.GoldMiner_LAY_gameTrack_1_index_8)
        };
        track2 = new RelativeLayout[] {
                findViewById(R.id.GoldMiner_LAY_gameTrack_2_index_1),
                findViewById(R.id.GoldMiner_LAY_gameTrack_2_index_2),
                findViewById(R.id.GoldMiner_LAY_gameTrack_2_index_3),
                findViewById(R.id.GoldMiner_LAY_gameTrack_2_index_4),
                findViewById(R.id.GoldMiner_LAY_gameTrack_2_index_5),
                findViewById(R.id.GoldMiner_LAY_gameTrack_2_index_6),
                findViewById(R.id.GoldMiner_LAY_gameTrack_2_index_7),
                findViewById(R.id.GoldMiner_LAY_gameTrack_2_index_8)
        };
        track3 = new RelativeLayout[] {
                findViewById(R.id.GoldMiner_LAY_gameTrack_3_index_1),
                findViewById(R.id.GoldMiner_LAY_gameTrack_3_index_2),
                findViewById(R.id.GoldMiner_LAY_gameTrack_3_index_3),
                findViewById(R.id.GoldMiner_LAY_gameTrack_3_index_4),
                findViewById(R.id.GoldMiner_LAY_gameTrack_3_index_5),
                findViewById(R.id.GoldMiner_LAY_gameTrack_3_index_6),
                findViewById(R.id.GoldMiner_LAY_gameTrack_3_index_7),
                findViewById(R.id.GoldMiner_LAY_gameTrack_3_index_8)
        };
        track4 = new RelativeLayout[] {
                findViewById(R.id.GoldMiner_LAY_gameTrack_4_index_1),
                findViewById(R.id.GoldMiner_LAY_gameTrack_4_index_2),
                findViewById(R.id.GoldMiner_LAY_gameTrack_4_index_3),
                findViewById(R.id.GoldMiner_LAY_gameTrack_4_index_4),
                findViewById(R.id.GoldMiner_LAY_gameTrack_4_index_5),
                findViewById(R.id.GoldMiner_LAY_gameTrack_4_index_6),
                findViewById(R.id.GoldMiner_LAY_gameTrack_4_index_7),
                findViewById(R.id.GoldMiner_LAY_gameTrack_4_index_8)
        };
        track5 = new RelativeLayout[] {
                findViewById(R.id.GoldMiner_LAY_gameTrack_5_index_1),
                findViewById(R.id.GoldMiner_LAY_gameTrack_5_index_2),
                findViewById(R.id.GoldMiner_LAY_gameTrack_5_index_3),
                findViewById(R.id.GoldMiner_LAY_gameTrack_5_index_4),
                findViewById(R.id.GoldMiner_LAY_gameTrack_5_index_5),
                findViewById(R.id.GoldMiner_LAY_gameTrack_5_index_6),
                findViewById(R.id.GoldMiner_LAY_gameTrack_5_index_7),
                findViewById(R.id.GoldMiner_LAY_gameTrack_5_index_8)
        };

    }

    public void moveRight() {
        int currentTrack = gameManager.getMainCharacter().getPositionX();
        int newTrack = currentTrack + 1;
        if (newTrack < gameManager.getFieldColumns()) {
            UI_changePositionOfMainCharacterIMG(currentTrack, newTrack);
            // Update the position x in the Main Character
            gameManager.getMainCharacter().setPositionX(newTrack);
        }
    }

    public void moveLeft() {
        int currentTrack = gameManager.getMainCharacter().getPositionX();
        int newTrack = currentTrack - 1;
        if (newTrack >= 0) {
            UI_changePositionOfMainCharacterIMG(currentTrack, newTrack);
            // Update the position x in the Main Character
            gameManager.getMainCharacter().setPositionX(newTrack);
        }
    }

    public void UI_changePositionOfMainCharacterIMG(int currentTrack, int newTrack) {

        RelativeLayout currentRelativeLayout = gameTracksArray[7][currentTrack];
        currentRelativeLayout.removeView(GoldMiner_IMG_character);
        RelativeLayout newRelativeLayout = gameTracksArray[7][newTrack];
        newRelativeLayout.addView(GoldMiner_IMG_character);

    }

    private void startGame() {
        GoldMiner_cardView_nextLevel.setVisibility(View.INVISIBLE);
        isGameRunning = true;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isGameRunning) {
                    return; // Stop the loop if the game is not running
                }
                handler.postDelayed(this, speedOfObjects); // Schedule the next iteration
                ObstacleViewMovement();
                if(gameManager.getNumberOfRunningObstacles() < gameManager.getMaxNumberOfGameObstacles() && isGameRunning) {
                    gameManager.addGameObstacleToRunning();
                    UI_addAllNewRunningObstacles();
                }
                if(gameManager.checkEndGame()) {
                    isGameRunning = false;
                    createNewGameFromCurrentLevel();
                }
            }
        }, speedOfObjects); // Initial delay
    }

    private void createNewGameFromCurrentLevel() {
        gameManager.setLife(gameManager.getLife() - 1);
        gameManager.setNumOfCollisions(gameManager.getNumOfCollisions() + 1);

        if(gameManager.getLife() == 0) {
            gameOver();
        }else {
            gameManager.setCurrentLevel(gameManager.getCurrentLevel() - 1);
            refreshHeartImages();
            UI_nextLevel(gameManager.nextLevel(this));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        MyBackgroundMusic.getInstance().pauseMusic();
        isGameRunning = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyBackgroundMusic.getInstance().playMusic();
        if (!isGameRunning) {
            startGame();
        }
    }

    public void ObstacleViewMovement() {
        UI_removeAllRunningObstacles();
        boolean collision = gameManager.checkCollision();
        gameManager.obstaclesMovement();
        UI_addAllRunningObstacles();

        if (collision) {
            UI_collision(gameManager.getCollisionType());
        }
    }

    public void UI_removeAllRunningObstacles() {
        for (GameObstacle gameObstacle : gameManager.getRunningObstacles()) {
            int posX = gameObstacle.getPositionX();
            int posY = gameObstacle.getPositionY();
            gameTracksArray[posY][posX].removeView(gameObstacle.getShapeableImageView());
        }

    }

    public void UI_addAllNewRunningObstacles() {

        for (GameObstacle gameObstacle : gameManager.getRunningObstacles()) {
            int posX = gameObstacle.getPositionX();
            int posY = gameObstacle.getPositionY();
            if (posY == 0) {
                gameObstacle.getShapeableImageView().setVisibility(View.VISIBLE);
                gameTracksArray[posY][posX].addView(gameObstacle.getShapeableImageView());
            }
        }
    }

    public void UI_addAllRunningObstacles() {
        for (GameObstacle gameObstacle : gameManager.getRunningObstacles()) {
            int posX = gameObstacle.getPositionX();
            int posY = gameObstacle.getPositionY();
            gameTracksArray[posY][posX].addView(gameObstacle.getShapeableImageView());
        }

    }


    public void UI_collision(int collisionType) {

        if (collisionType == 1) { // collision type 1 for bad obstacle
            refreshHeartImages();
            vibration();
            createToast("I need more Gold");
            soundPlayer.playSound(R.raw.gold_miner_lossing_music);
        }
        if (collisionType == 2) { //collision type 2 for good obstacle
            GoldMiner_LBL_money.setText(String.valueOf(gameManager.getMoney()));
            soundPlayer.playSound(R.raw.gold_miner_gold_collect_music);
        }
        if(gameManager.getMoney() >= gameManager.getTarget()){
            isGameRunning = false;
            UI_removeAllRunningObstacles();
            UI_nextLevel(gameManager.nextLevel(this));
        }


        if(gameManager.getLife() == 0){
            gameOver();
        }

    }

    public void refreshHeartImages() {

        // removing heart view in the game
        if (gameManager.getLife() >= 0) {
            GoldMiner_IMG_hearts[gameManager.getNumOfCollisions() - 1].setVisibility(View.INVISIBLE);
        }
    }

    public void createToast(String message) {
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(this, message, duration);
        toast.show();
    }

    private void UI_nextLevel(Level level){

        // updating the UI for the next level

        GoldMiner_cardView_nextLevel.setVisibility(View.VISIBLE);
        GoldMiner_LBL_target.setText(String.valueOf(level.getTarget()));
        GoldMiner_LBL_main_levelView.setText(String.valueOf(gameManager.getCurrentLevel()));
        GoldMiner_LBL_level.setText("LEVEL   " + gameManager.getCurrentLevel());
        GoldMiner_LBL_money.setText(String.valueOf(gameManager.getMoney()));
        GoldMiner_LBL_level_TNT.setText(String.valueOf(level.getNumTNT()));
        GoldMiner_LBL_level_rock.setText(String.valueOf(level.getNumRock()));
        GoldMiner_LBL_level_gold.setText(String.valueOf(level.getNumGold()));
        GoldMiner_LBL_level_moneyBag.setText(String.valueOf(level.getNumDiamond()));
    }

    public void vibration() {

        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(VIBRATIONTIMING, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            v.vibrate(VIBRATIONTIMING);
        }

    }

    private void showCardOfEndGame(boolean gameOver){
        if(gameOver){
            MAIN_GAMEOVER_CARDVIEW.setVisibility(View.VISIBLE);
        }else{
            MAIN_GAMEOVER_CARDVIEW.setVisibility(View.INVISIBLE);
        }

    }

    private void gameOver(){
        isGameRunning = false;
        GoldMiner_BTL_left.setVisibility(View.INVISIBLE);
        GoldMiner_BTL_right.setVisibility(View.INVISIBLE);
        showCardOfEndGame(true);
        MAIN_LBLCARD_FINALSCORE.setText("Final lEVEL: " + gameManager.getCurrentLevel());
        if(sensors)
            sensorsManager.stop();

    }

    private void savePlayerScoreAndGoToPlayersActivity(){
        if(MAIN_EDITTEXT_NAME.length() != 0){

            Player newPlayer = new Player().setName(MAIN_EDITTEXT_NAME.getText().toString()).setScore(gameManager.getCurrentLevel()).setLat(lat).setLng(lng);
            AllPlayers.getInstance().addPlayer(newPlayer);
            AllPlayers.getInstance().sortPlayersByScore();

            List<Player> topPlayers = AllPlayers.getInstance().getAllPlayersList().subList(0, Math.min(10, AllPlayers.getInstance().getAllPlayersList().size()));
            Gson gson = new Gson();
            String topPlayersJson = gson.toJson(topPlayers);
            Intent intent = new Intent(this, ScoreBoard.class);
            intent.putExtra("playerListJson", topPlayersJson);
            startActivity(intent);
            finish();

        }else {
            createToast("please add your name");
        }

    }
}