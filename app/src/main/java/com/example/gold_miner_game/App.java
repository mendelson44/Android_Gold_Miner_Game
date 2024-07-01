package com.example.gold_miner_game;

import android.app.Application;
import com.example.gold_miner_game.logic.SharedPreferencesManager;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferencesManager.init(getApplicationContext());
    }
}
