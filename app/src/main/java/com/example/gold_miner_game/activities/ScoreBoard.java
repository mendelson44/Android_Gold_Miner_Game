package com.example.gold_miner_game.activities;


import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gold_miner_game.R;
import com.example.gold_miner_game.logic.CallBack_playerScoreClicked;
import com.example.gold_miner_game.model.Player;
import com.example.gold_miner_game.views.ListFragment;
import com.example.gold_miner_game.views.MapFragment;
import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;

import java.util.ArrayList;

public class ScoreBoard extends AppCompatActivity {


    private ListFragment listFragment;
    private MapFragment mapFragment;
    private FrameLayout GoldMiner_frameList_score_board;
    private FrameLayout GoldMiner_frameMap_score_board;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_board);

        findViews();

        String playerListJson = getIntent().getStringExtra("playerListJson");
        Gson gson = new Gson();
        ArrayList<Player> playerList = gson.fromJson(playerListJson, new TypeToken<ArrayList<Player>>(){}.getType());
        Log.d("InFragment", "getDataFromLastActivity: " + playerList.toString());


        listFragment = new ListFragment(playerList);
        listFragment.setCallBackPlayerScoreClicked(new CallBack_playerScoreClicked() {
            @Override
            public void playerScoreClicked(double lat, double lng) {
                mapFragment.zoom(lat, lng);
            }
        });


        mapFragment = new MapFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.GoldMiner_frameList_score_board,listFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.GoldMiner_frameMap_score_board,mapFragment).commit();

    }

    private void findViews(){
        GoldMiner_frameList_score_board = findViewById(R.id.GoldMiner_frameList_score_board);
        GoldMiner_frameMap_score_board = findViewById(R.id.GoldMiner_frameMap_score_board);
    }
}
