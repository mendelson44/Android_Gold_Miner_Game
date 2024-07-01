package com.example.gold_miner_game.model;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.Objects;
import java.util.Random;

public class GameObstacle extends Character {

    private ShapeableImageView shapeableImageView;
    private int currentType;


    public GameObstacle(ShapeableImageView shapeableImageView, int currentType ) {
        super(0, 0);
        this.shapeableImageView = shapeableImageView;
        this.currentType = currentType;
    }


    @Override
    public int hashCode() {
        return Objects.hash(this.getPositionX(), this.getPositionY());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        GameObstacle obstacle = (GameObstacle) obj;
        return this.getPositionX() == obstacle.getPositionX() && this.getPositionY() == obstacle.getPositionY();
    }

    public ShapeableImageView getShapeableImageView() {
        return shapeableImageView;
    }

    public GameObstacle setShapeableImageView(ShapeableImageView shapeableImageView) {
        this.shapeableImageView = shapeableImageView;
        return this;
    }

    public int getCurrentType() {
        return currentType;
    }

    public GameObstacle setCurrentType(int currentType) {
        this.currentType = currentType;
        return this;
    }
}
