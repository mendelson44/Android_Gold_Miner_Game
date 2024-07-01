package com.example.gold_miner_game.model;

import com.example.gold_miner_game.logic.SharedPreferencesManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AllPlayers {

    private ArrayList<Player> allPlayersList = new ArrayList<>();
    private static AllPlayers instance = null;
    private int numberOfBestPlayersToShow = 10;


    public AllPlayers(){
        this.allPlayersList = loadAllPlayerFromSharedPreferences();
        if(allPlayersList == null)
            this.allPlayersList = new ArrayList<Player>();
    }

    public static AllPlayers getInstance() {
        if (instance == null)
            instance = new AllPlayers();
        return instance;
    }

    public void addPlayer(Player player){
        this.allPlayersList.add(player);
        saveAllPlayersToSharedPreferences();

    }

    public ArrayList<Player> getAllPlayersList() {
        return allPlayersList;
    }

    public AllPlayers setAllPlayersList(ArrayList<Player> allPlayersList) {
        this.allPlayersList = allPlayersList;
        return this;
    }

    public int getNumberOfBestPlayersToShow() {
        return numberOfBestPlayersToShow;
    }

    public AllPlayers setNumberOfBestPlayersToShow(int numberOfBestPlayersToShow) {
        this.numberOfBestPlayersToShow = numberOfBestPlayersToShow;
        return this;
    }

    @Override
    public String toString() {
        return "AllPlayers{" +
                "allPlayersList=" + allPlayersList +
                ", numberOfBestPlayersToShow=" + numberOfBestPlayersToShow +
                '}';
    }

    private void saveAllPlayersToSharedPreferences(){
        Gson gson = new Gson();
        String playersJson = gson.toJson(this.allPlayersList);
        SharedPreferencesManager.getInstance().putString("all_players", playersJson);

    }

    private ArrayList<Player> loadAllPlayerFromSharedPreferences(){
        String playersJson = SharedPreferencesManager.getInstance().getString("all_players", "");
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Player>>(){}.getType();
        return gson.fromJson(playersJson, type);
    }

    public void sortPlayersByScore() {
        Collections.sort(allPlayersList, new Comparator<Player>() {
            @Override
            public int compare(Player player1, Player player2) {
                return Integer.compare(player2.getScore(), player1.getScore());
            }
        });
    }


}
