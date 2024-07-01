package com.example.gold_miner_game.model;

public class Level {


    private int target;
    private int numTNT;
    private int numRock;
    private int numGold;
    private int numDiamond;


    public Level(int target, int numTNT, int numRock, int numGold, int numDiamond) {
        this.target = target;
        this.numTNT = numTNT;
        this.numRock = numRock;
        this.numGold = numGold;
        this.numDiamond = numDiamond;
    }

    public int getTarget() {
        return target;
    }

    public int getNumTNT() {
        return numTNT;
    }

    public int getNumRock() {
        return numRock;
    }

    public int getNumGold() {
        return numGold;
    }

    public int getNumDiamond() {return numDiamond;}

    public void setTarget(int target) {
        this.target = target;
    }

    public void setNumTNT(int numTNT) {
        this.numTNT = numTNT;
    }

    public void setNumRock(int numRock) {
        this.numRock = numRock;
    }

    public void setNumGold(int numGold) {
        this.numGold = numGold;
    }

    public void setNumDiamond(int numDiamond) {
        this.numDiamond = numDiamond;
    }
}
