package com.example.gold_miner_game.logic;


import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.gold_miner_game.R;
import com.example.gold_miner_game.model.GameObstacle;
import com.example.gold_miner_game.model.Level;
import com.example.gold_miner_game.model.mainCharacter;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

public class GameManager {

    private int life;
    private int fieldColumns;
    private int fieldRows;
    private mainCharacter mainCharacter;
    private Stack<GameObstacle> gameObstacles;
    private Set<GameObstacle> runningObstacles;
    private int numberOfRunningObstacles = 0;
    private int maxNumberOfGameObstacles = 6;
    private int numOfCollisions = 0;
    private int collisionType = 0;
    private Level[] levels;
    private final int LEVELS_COUNT = 10;
    private int currentLevel = 0;
    private int money = 0;
    private int target = 0;

    public GameManager(int life, int fieldColumns, int fieldRows) {

        this.life = life;
        this.fieldColumns = fieldColumns;
        this.fieldRows = fieldRows;
        mainCharacter = new mainCharacter();
        gameObstacles = new Stack<>();
        runningObstacles = new HashSet<>();
        levels = new Level[LEVELS_COUNT];

    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public int getFieldColumns() {
        return fieldColumns;
    }

    public void setFieldColumns(int fieldColumns) {
        this.fieldColumns = fieldColumns;
    }

    public int getFieldRows() {
        return fieldRows;
    }

    public void setFieldRows(int fieldRows) {
        this.fieldRows = fieldRows;
    }

    public mainCharacter getMainCharacter() {
        return mainCharacter;
    }

    public int getNumOfCollisions() {
        return numOfCollisions;
    }

    public void setNumOfCollisions(int numOfCollisions) {
        this.numOfCollisions = numOfCollisions;
    }

    public void setGameObstacles(Stack<GameObstacle> gameObstacles) {
        this.gameObstacles = gameObstacles;
    }

    public void setRunningObstacles(Set<GameObstacle> runningObstacles) {
        this.runningObstacles = runningObstacles;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public int getCollisionType() {
        return collisionType;
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


    public void addAllGameObstacles(Context context) {

        // EXPLAIN:
        // each step of level the game create the numbers of obstacles needed and push each obstacle to a stack
        // after this the game shuffles the stack.

        Context prefs = context.getApplicationContext();
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        int[] level = getCurrentLevelArray();

        // Map obstacle types to image resource IDs
        Map<Integer, Integer> obstacleImages = new HashMap<>();
        obstacleImages.put(1, R.drawable.gold_miner_tnt);
        obstacleImages.put(2, R.drawable.gold_miner_rock);
        obstacleImages.put(3, R.drawable.gold_miner_gold);
        obstacleImages.put(4, R.drawable.gold_miner_diamond);

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
        int[] levelArray = {level.getTarget(),level.getNumTNT(),level.getNumRock(),level.getNumGold(),level.getNumDiamond()};
        return levelArray;
    }

    public void addGameObstacleToRunning (){

        // EXPLAIN:
        // when the game is running each loop of the game i add an running obstacle to the game
        // by pulling obstacle from the stack and add to a set array
        // that handle the movement of the obstacles in the game.

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
                collisionType = 1;
                return true;
            }else if(this.mainCharacter.getPositionX() == gameObstacle.getPositionX() && this.mainCharacter.getPositionY() == (gameObstacle.getPositionY() + 1) && gameObstacle.getCurrentType() == 3){
                money += 10;
                collisionType = 2;
                return true;
            }
            else if(this.mainCharacter.getPositionX() == gameObstacle.getPositionX() && this.mainCharacter.getPositionY() == (gameObstacle.getPositionY() + 1) && gameObstacle.getCurrentType() == 4){
            money += 500;
            collisionType = 2;
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

            Level newLevel = new Level((900+(i*100)),i+4,i+9,(42 - (i*2)),3);
            levels[i] = newLevel;
        }
    }

    public boolean checkEndGame() {
        return (gameObstacles.isEmpty() && runningObstacles.isEmpty());
    }
}
