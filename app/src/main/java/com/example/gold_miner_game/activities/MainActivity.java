package com.example.gold_miner_game.activities;

import android.content.Context;
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
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;

import com.example.gold_miner_game.R;
import com.example.gold_miner_game.logic.GameManager;
import com.example.gold_miner_game.model.GameObstacle;
import com.example.gold_miner_game.model.Level;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {


    private RelativeLayout[][] gameTracksArray;
    private RelativeLayout[] track1;
    private RelativeLayout[] track2;
    private RelativeLayout[] track3;

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
    private boolean isGameRunning = true;
    private int speedOfObjects = 850;
    private final int VIBRATIONTIMING = 300;
    private final int FIELD_GAME_COLUMNS = 3;
    private final int FIELD_GAME_ROWS = 7;
    private final int LIVES = 3;
    private Handler handler = new Handler(Looper.getMainLooper());
    private GameManager gameManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        findViews();
        gameTracksArray = getAllGameTracks(FIELD_GAME_ROWS,FIELD_GAME_COLUMNS);
        GoldMiner_BTL_left.setOnClickListener(view -> moveLeft());
        GoldMiner_BTL_right.setOnClickListener(view -> moveRight());
        gameManager = new GameManager(LIVES,FIELD_GAME_COLUMNS,FIELD_GAME_ROWS);
        gameManager.generateLevels();
        UI_nextLevel(gameManager.nextLevel(this));
        GoldMiner_BTL_level_continue.setOnClickListener(view -> startGame());
    }

    private RelativeLayout[][] getAllGameTracks(int rows, int columns) {

        RelativeLayout[][] relativeLayouts = new RelativeLayout[rows][columns];
        for (int i = 0; i < rows; i++) {
            relativeLayouts[i][0] = track1[i];
            relativeLayouts[i][1] = track2[i];
            relativeLayouts[i][2] = track3[i];
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
                findViewById(R.id.GoldMiner_LAY_gameTrack_1_index_7)
        };
        track2 = new RelativeLayout[] {
                findViewById(R.id.GoldMiner_LAY_gameTrack_2_index_1),
                findViewById(R.id.GoldMiner_LAY_gameTrack_2_index_2),
                findViewById(R.id.GoldMiner_LAY_gameTrack_2_index_3),
                findViewById(R.id.GoldMiner_LAY_gameTrack_2_index_4),
                findViewById(R.id.GoldMiner_LAY_gameTrack_2_index_5),
                findViewById(R.id.GoldMiner_LAY_gameTrack_2_index_6),
                findViewById(R.id.GoldMiner_LAY_gameTrack_2_index_7)
        };
        track3 = new RelativeLayout[] {
                findViewById(R.id.GoldMiner_LAY_gameTrack_3_index_1),
                findViewById(R.id.GoldMiner_LAY_gameTrack_3_index_2),
                findViewById(R.id.GoldMiner_LAY_gameTrack_3_index_3),
                findViewById(R.id.GoldMiner_LAY_gameTrack_3_index_4),
                findViewById(R.id.GoldMiner_LAY_gameTrack_3_index_5),
                findViewById(R.id.GoldMiner_LAY_gameTrack_3_index_6),
                findViewById(R.id.GoldMiner_LAY_gameTrack_3_index_7)
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

        RelativeLayout currentRelativeLayout = gameTracksArray[6][currentTrack];
        currentRelativeLayout.removeView(GoldMiner_IMG_character);
        RelativeLayout newRelativeLayout = gameTracksArray[6][newTrack];
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
                    createNewGameFromLevel1();
                }
            }
        }, speedOfObjects); // Initial delay
    }

    private void createNewGameFromLevel1() {
        gameManager.setCurrentLevel(0);
        gameManager.setLife(LIVES);
        gameManager.setNumOfCollisions(0);
        UI_resetHeartsImages();
        UI_nextLevel(gameManager.nextLevel(this));
    }

    @Override
    protected void onStop() {
        super.onStop();
        isGameRunning = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isGameRunning) {
            isGameRunning = true;
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
        }
        if (collisionType == 2) { //collision type 2 for good obstacle
            GoldMiner_LBL_money.setText(String.valueOf(gameManager.getMoney()));

        }
        if(gameManager.getMoney() >= gameManager.getTarget()){
            isGameRunning = false;
            UI_removeAllRunningObstacles();
            UI_nextLevel(gameManager.nextLevel(this));
        }


        if(gameManager.getLife() == 0){
            isGameRunning = false;
            UI_removeAllRunningObstacles();
            createNewGameFromLevel1();
        }

    }

    private void UI_resetHeartsImages() {

        for (int i = 0 ; i < LIVES ; i++) {
            GoldMiner_IMG_hearts[i].setVisibility(View.VISIBLE);
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
        GoldMiner_LBL_level.setText("Level :   " + gameManager.getCurrentLevel());
        GoldMiner_LBL_money.setText(String.valueOf(gameManager.getMoney()));
        GoldMiner_LBL_level_TNT.setText(String.valueOf(level.getNumTNT()));
        GoldMiner_LBL_level_rock.setText(String.valueOf(level.getNumRock()));
        GoldMiner_LBL_level_gold.setText(String.valueOf(level.getNumGold()));
        GoldMiner_LBL_level_moneyBag.setText(String.valueOf(level.getNumMoneyBag()));
        //this.setSpeedOfObjects(speedOfObjects - 10*gameManager.getCurrentLevel());
    }

    public void setSpeedOfObjects(int speedOfObjects) {
        this.speedOfObjects = speedOfObjects;
    }

    public void vibration() {

        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(VIBRATIONTIMING, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            v.vibrate(VIBRATIONTIMING);
        }

    }
}