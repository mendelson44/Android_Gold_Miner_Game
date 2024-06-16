package com.example.gold_miner_game.logic;


import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.gold_miner_game.R;
import com.example.gold_miner_game.model.GameObstacle;
import com.example.gold_miner_game.model.Level;
import com.example.gold_miner_game.model.mainCharacter;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

public class GameManager {

    private int life = 3;
    private int fieldColumns = 3;
    private int fieldRows = 7;
    private mainCharacter mainCharacter;
    private Stack<GameObstacle> gameObstacles;
    private Set<GameObstacle> runningObstacles;
    private int numberOfRunningObstacles = 0;
    private int maxNumberOfGameObstacles = 4;
    private int numOfCollisions = 0;
    private int collisionType = 0;
    private Level[] levels;
    private final int LEVELS_COUNT = 10;
    private int currentLevel = 0;
    private int money = 0;
    private int target = 0;

    public GameManager() {

        mainCharacter = new mainCharacter();
        gameObstacles = new Stack<>();
        runningObstacles = new HashSet<>();
        levels = new Level[LEVELS_COUNT];

    }

    public int getLife() {
        return life;
    }

    public GameManager setLife(int life) {
        this.life = life;
        return this;
    }


    public int getFieldColumns() {
        return fieldColumns;
    }

    public GameManager setFieldColumns(int fieldColumns) {
        this.fieldColumns = fieldColumns;
        return this;
    }

    public int getFieldRows() {
        return fieldRows;
    }

    public GameManager setFieldRows(int fieldRows) {
        this.fieldRows = fieldRows;
        return this;
    }

    public mainCharacter getMainCharacter() {
        return mainCharacter;
    }


    public int getNumOfCollisions() {
        return numOfCollisions;
    }

    public int getMoney() {
        return money;
    }

    public GameManager setMoney(int money) {
        this.money = money;
        return this;
    }

    public void setGameObstacles(Stack<GameObstacle> gameObstacles) {
        this.gameObstacles = gameObstacles;
    }

    public void setRunningObstacles(Set<GameObstacle> runningObstacles) {
        this.runningObstacles = runningObstacles;
    }

    public int getTarget() {
        return target;
    }

    public GameManager setTarget(int target) {
        this.target = target;
        return this;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public int getCollisionType() {
        return collisionType;
    }

    public void addAllGameObstacles(Context context) {
        Context prefs = context.getApplicationContext();
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                256,
                256);

        int[] level = getCurrentLevelArray();

        // Map obstacle types to image resource IDs
        Map<Integer, Integer> obstacleImages = new HashMap<>();
        obstacleImages.put(1, R.drawable.gold_miner_tnt);
        obstacleImages.put(2, R.drawable.gold_miner_rock);
        obstacleImages.put(3, R.drawable.gold_miner_gold);
        obstacleImages.put(4, R.drawable.gold_miner_moneybag);

        for (int i = 1; i < level.length; i++) {
            for (int j = 0; j < level[i]; j++) {
                ShapeableImageView image = new ShapeableImageView(prefs); // Use context directly here
                // Set image resource based on obstacle type
                int imageResource = obstacleImages.getOrDefault(i, 0); // Default to 0 if not found
                if (imageResource != 0) {
                    image.setImageResource(imageResource);
                }

                image.setLayoutParams(layoutParams);

                GameObstacle gameObstacle = new GameObstacle(image, i);
                gameObstacles.push(gameObstacle);

            }
        }
    }

    private int[] getCurrentLevelArray() {
        Level level = levels[currentLevel];
        int[] levelArray = {level.getTarget(),level.getNumTNT(),level.getNumRock(),level.getNumGold(),level.getNumMoneyBag()};
        return levelArray;
    }

    public void addGameObstacleToRunning (){

        if(gameObstacles.isEmpty())
            return;
        GameObstacle gameObstacle = gameObstacles.pop();
        gameObstacle.setPositionX(randomNumber(fieldColumns));
        runningObstacles.add(gameObstacle);
        numberOfRunningObstacles++;
    }

    private int randomNumber(int max) {
        // Create a Random number
        Random random = new Random();
        int randomNumber = random.nextInt(max);
        return randomNumber;
    }



    public int getNumberOfRunningObstacles() {
        return numberOfRunningObstacles;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public void setNumberOfRunningObstacles(int numberOfRunningObstacles) {
        this.numberOfRunningObstacles = numberOfRunningObstacles;
    }

    public Stack<GameObstacle> getGameObstacles() {
        return gameObstacles;
    }

    public Set<GameObstacle> getRunningObstacles() {
        return runningObstacles;
    }

    public int getMaxNumberOfGameObstacles() {
        return maxNumberOfGameObstacles;
    }

    public void obstaclesMovement() {
        Iterator<GameObstacle> iterator = runningObstacles.iterator();
        while (iterator.hasNext()) {
            GameObstacle gameObstacle = iterator.next();
            if (gameObstacle.getPositionY() != fieldRows - 2) {
                gameObstacle.setPositionY(gameObstacle.getPositionY() + 1);
            } else {
                gameObstacle.getShapeableImageView().setVisibility(View.GONE);
                iterator.remove(); // Safely remove using the iterator
                numberOfRunningObstacles--;
            }
        }
    }

    public boolean checkCollision() {
        for (GameObstacle gameObstacle : runningObstacles) {
            if (this.mainCharacter.getPositionX() == gameObstacle.getPositionX() && this.mainCharacter.getPositionY() == (gameObstacle.getPositionY() + 1) && (gameObstacle.getCurrentType() == 1 || gameObstacle.getCurrentType() == 2) ){
                numOfCollisions++;
                life--;
                collisionType= 1;
                return true;
            }else if(this.mainCharacter.getPositionX() == gameObstacle.getPositionX() && this.mainCharacter.getPositionY() == (gameObstacle.getPositionY() + 1) && gameObstacle.getCurrentType() == 3){
                money += 5;
                collisionType= 3;
                return true;
            }
            else if(this.mainCharacter.getPositionX() == gameObstacle.getPositionX() && this.mainCharacter.getPositionY() == (gameObstacle.getPositionY() + 1) && gameObstacle.getCurrentType() == 4){
            money += 1000;
            collisionType= 3;
            return true;
            }
        }
        return false;
    }

    public void shuffleObjectsGame() {

        Collections.shuffle(gameObstacles);
    }

    public Level nextLevel(Context context) {
        currentLevel++;
        setTarget(levels[currentLevel].getTarget());
        setMoney(0);
        setGameObstacles(new Stack<>());
        setRunningObstacles(new HashSet<>());
        setNumberOfRunningObstacles(0);
        addAllGameObstacles(context);
        shuffleObjectsGame();
        return levels[currentLevel];
    }

    public void generateLevels() {

        for (int i = 0 ; i < LEVELS_COUNT ; i++) {

            Level newLevel = new Level(999+i,5,10,10,2);
            levels[i] = newLevel;        }

    }

    public boolean checkEndGame() {
        return (gameObstacles.isEmpty() && runningObstacles.isEmpty());
    }
}
