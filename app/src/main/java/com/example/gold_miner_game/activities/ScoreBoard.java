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
    private FrameLayout playerBoard_FRAME_list;
    private FrameLayout playerBoard_FRAME_map;




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
        getSupportFragmentManager().beginTransaction().add(R.id.playerBoard_FRAME_list,listFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.playerBoard_FRAME_map,mapFragment).commit();

    }

    private void findViews(){
        playerBoard_FRAME_list = findViewById(R.id.playerBoard_FRAME_list);
        playerBoard_FRAME_map = findViewById(R.id.playerBoard_FRAME_map);
    }
}
