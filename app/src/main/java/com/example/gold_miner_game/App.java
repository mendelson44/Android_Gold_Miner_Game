package com.example.gold_miner_game;

import android.app.Application;
import com.example.gold_miner_game.logic.SharedPreferencesManager;
import com.example.gold_miner_game.model.MyBackgroundMusic;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferencesManager.init(getApplicationContext());
        MyBackgroundMusic.init(this);


    }
}
