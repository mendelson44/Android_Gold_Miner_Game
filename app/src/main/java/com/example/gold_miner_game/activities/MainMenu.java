package com.example.gold_miner_game.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import com.example.gold_miner_game.R;
import com.example.gold_miner_game.model.AllPlayers;
import com.example.gold_miner_game.model.Player;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainMenu extends AppCompatActivity {

    private AppCompatButton GoldMiner_Main_Menu_BTN_mode_slow;
    private AppCompatButton GoldMiner_Main_Menu_BTN_mode_fast;
    private AppCompatButton GoldMiner_Main_Menu_BTN_mode_sensors;
    private AppCompatButton GoldMiner_Main_Menu_BTN_startGame;
    private AppCompatButton GoldMiner_Main_Menu_BTN_scoreBoard;
    private boolean  sensors = false;
    private boolean slow;

    //location of the user:
    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    //for the player location:
    private double lat;
    private double lng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        locationPermission();
        findViewsById();
        GoldMiner_Main_Menu_BTN_mode_slow.setOnClickListener(v -> slowButtonClicked());
        GoldMiner_Main_Menu_BTN_mode_fast.setOnClickListener(v -> fastButtonClicked());
        GoldMiner_Main_Menu_BTN_mode_sensors.setOnClickListener(v -> sensorsButtonClicked());
        GoldMiner_Main_Menu_BTN_startGame.setOnClickListener(v -> startGame());
        GoldMiner_Main_Menu_BTN_scoreBoard.setOnClickListener(v -> view_Score_Board());
    }

    private void findViewsById(){


        GoldMiner_Main_Menu_BTN_mode_slow = findViewById(R.id.GoldMiner_Main_Menu_BTN_mode_slow);
        GoldMiner_Main_Menu_BTN_mode_fast = findViewById(R.id.GoldMiner_Main_Menu_BTN_mode_fast);
        GoldMiner_Main_Menu_BTN_mode_sensors = findViewById(R.id.GoldMiner_Main_Menu_BTN_mode_sensors);
        GoldMiner_Main_Menu_BTN_startGame = findViewById(R.id.GoldMiner_Main_Menu_BTN_startGame);
        GoldMiner_Main_Menu_BTN_scoreBoard = findViewById(R.id.GoldMiner_Main_Menu_BTN_scoreBoard);

    }

    private void slowButtonClicked() {
        slow = true;
        GoldMiner_Main_Menu_BTN_mode_slow.setTextColor(ContextCompat.getColor(this, R.color.menu_buttons_color_clicked));
        GoldMiner_Main_Menu_BTN_mode_fast.setTextColor(ContextCompat.getColor(this, R.color.white));
    }

    private void fastButtonClicked() {
        slow = false;
        GoldMiner_Main_Menu_BTN_mode_fast.setTextColor(ContextCompat.getColor(this, R.color.menu_buttons_color_clicked));
        GoldMiner_Main_Menu_BTN_mode_slow.setTextColor(ContextCompat.getColor(this, R.color.white));
    }

    private void sensorsButtonClicked() {
        if(sensors) {
            sensors = false;
            GoldMiner_Main_Menu_BTN_mode_sensors.setTextColor(ContextCompat.getColor(this, R.color.white));
        }else {
            sensors = true;
            GoldMiner_Main_Menu_BTN_mode_sensors.setTextColor(ContextCompat.getColor(this, R.color.menu_buttons_color_clicked));
        }
    }

    private void startGame(){
        JSONObject valuesToMainActivity = new JSONObject();
        try {
            valuesToMainActivity.put("sensors", sensors);
            valuesToMainActivity.put("slow", slow);
            valuesToMainActivity.put("lat", lat);
            valuesToMainActivity.put("lng", lng);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        String jsonString = valuesToMainActivity.toString();

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("jsonString", jsonString);
        startActivity(intent);

    }


    private void locationPermission() {
        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request the permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }else{

            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                // Got last known location. In some rare situations, this can be null.
                                lat = location.getLatitude();
                                lng = location.getLongitude();

                                //Log.d("lat and lon", "lat" + lat + "lon" + lon);
                            }
                        }
                    });
        }

    }

    private void view_Score_Board(){

        ArrayList<Player> allPlayers;
        AllPlayers.getInstance().sortPlayersByScore();
        allPlayers = AllPlayers.getInstance().getAllPlayersList();

        // Get the first 10 players after sorting
        List<Player> topPlayers = allPlayers.subList(0, Math.min(10, allPlayers.size()));

        Gson gson = new Gson();
        String topPlayersJson = gson.toJson(topPlayers);
        Intent intent = new Intent(this, ScoreBoard.class);
        intent.putExtra("playerListJson", topPlayersJson);
        startActivity(intent);

    }
}
