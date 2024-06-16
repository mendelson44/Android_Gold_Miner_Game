package com.example.gold_miner_game.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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


    private RelativeLayout[][] relativeLayouts;
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
    private LinearLayoutCompat GoldMiner_LAY_main_gameTrack;
    private LinearLayoutCompat GoldMiner_LAY_gameTrack_1;
    private LinearLayoutCompat GoldMiner_LAY_gameTrack_2;
    private LinearLayoutCompat GoldMiner_LAY_gameTrack_3;
    private GameManager gameManager;
    private Handler handler = new Handler(Looper.getMainLooper());
    private boolean isGameRunning = true;
    private int timeCounter = 0;
    private int speedOfObjects = 850;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        findViews();
        relativeLayouts = getAllRelativeLayouts1();
        GoldMiner_BTL_left.setOnClickListener(view -> moveLeft());
        GoldMiner_BTL_right.setOnClickListener(view -> moveRight());
        gameManager = new GameManager();
        gameManager.generateLevels();
        UI_nextLevel(gameManager.nextLevel(this));
        GoldMiner_BTL_level_continue.setOnClickListener(view -> startGame());
    }

    private RelativeLayout[][] getAllRelativeLayouts() {

        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 3; j++) {
                View track = GoldMiner_LAY_main_gameTrack.getChildAt(j);
                if (track instanceof LinearLayoutCompat) {
                    View childView = ((LinearLayoutCompat) track).getChildAt(i);
                    if (childView instanceof RelativeLayout) {
                        relativeLayouts[i][j] = (RelativeLayout) childView;

                    }
                }
            }
        }
        return relativeLayouts;
    }
    private RelativeLayout[][] getAllRelativeLayouts1() {

        RelativeLayout[][] relativeLayouts = new RelativeLayout[7][3];
        for (int i = 0; i < 7; i++) {
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

        GoldMiner_LAY_main_gameTrack = findViewById(R.id.GoldMiner_LAY_main_gameTrack);
        GoldMiner_LAY_gameTrack_1 = findViewById(R.id.GoldMiner_LAY_gameTrack_1);
        GoldMiner_LAY_gameTrack_2 = findViewById(R.id.GoldMiner_LAY_gameTrack_2);
        GoldMiner_LAY_gameTrack_3 = findViewById(R.id.GoldMiner_LAY_gameTrack_3);

    }

    public void moveRight() {
        int currentTrack = gameManager.getMainCharacter().getPositionX();
        int newTrack = currentTrack + 1;


        if (newTrack < gameManager.getFieldColumns()) {
            changeLayoutOfMainCharacterIMG(currentTrack, newTrack);
            // Update the lane in the game manager
            gameManager.getMainCharacter().setPositionX(newTrack);
        }
    }

    public void moveLeft() {

        int currentTrack = gameManager.getMainCharacter().getPositionX();
        int newTrack = currentTrack - 1;


        if (newTrack >= 0) {
            changeLayoutOfMainCharacterIMG(currentTrack, newTrack);
            // Update the lane in the game manager
            gameManager.getMainCharacter().setPositionX(newTrack);
        }
    }

    public void changeLayoutOfMainCharacterIMG(int currentTrack, int newTrack) {

        RelativeLayout currentRelativeLayout = relativeLayouts[6][currentTrack];
        currentRelativeLayout.removeView(GoldMiner_IMG_character);
        RelativeLayout newRelativeLayout = relativeLayouts[6][newTrack];
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
                timeCounter++;
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

    public void addScore() {

        GoldMiner_LBL_money.setText(String.valueOf(gameManager.getMoney()));

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
            relativeLayouts[posY][posX].removeView(gameObstacle.getShapeableImageView());
        }

    }

    public void UI_addAllNewRunningObstacles() {

        for (GameObstacle gameObstacle : gameManager.getRunningObstacles()) {
            int posX = gameObstacle.getPositionX();
            int posY = gameObstacle.getPositionY();
            if (posY == 0) {
                gameObstacle.getShapeableImageView().setVisibility(View.VISIBLE);
                relativeLayouts[posY][posX].addView(gameObstacle.getShapeableImageView());
            }
        }
    }

    public void UI_addAllRunningObstacles() {
        for (GameObstacle gameObstacle : gameManager.getRunningObstacles()) {
            int posX = gameObstacle.getPositionX();
            int posY = gameObstacle.getPositionY();
            relativeLayouts[posY][posX].addView(gameObstacle.getShapeableImageView());
        }

    }


    public void UI_collision(int collisionType) {

        if (collisionType == 1) {
            refreshHeartImages();
            createToast("I need more Gold");
        }
        if (collisionType == 3) {
            addScore();

        }
        if(gameManager.getMoney() >= gameManager.getTarget()){
            isGameRunning = false;
            UI_removeAllRunningObstacles();
            UI_nextLevel(gameManager.nextLevel(this));
        }


        if(gameManager.getLife() == 0){
            //gameManager.setLife(3);
        }

    }

    public void refreshHeartImages() {

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

        GoldMiner_cardView_nextLevel.setVisibility(View.VISIBLE);
        GoldMiner_LBL_target.setText(String.valueOf(level.getTarget()));
        GoldMiner_LBL_main_levelView.setText(String.valueOf(gameManager.getCurrentLevel()));
        GoldMiner_LBL_level.setText("Level :   " + gameManager.getCurrentLevel());
        GoldMiner_LBL_money.setText(String.valueOf(gameManager.getMoney()));
        GoldMiner_LBL_level_TNT.setText(String.valueOf(level.getNumTNT()));
        GoldMiner_LBL_level_rock.setText(String.valueOf(level.getNumRock()));
        GoldMiner_LBL_level_gold.setText(String.valueOf(level.getNumGold()));
        GoldMiner_LBL_level_moneyBag.setText(String.valueOf(level.getNumMoneyBag()));
        this.setSpeedOfObjects(speedOfObjects - 10*gameManager.getCurrentLevel());
    }

    public void setSpeedOfObjects(int speedOfObjects) {
        this.speedOfObjects = speedOfObjects;
    }
}