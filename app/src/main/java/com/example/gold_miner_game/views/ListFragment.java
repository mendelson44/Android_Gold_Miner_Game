package com.example.gold_miner_game.views;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.gold_miner_game.R;
import com.example.gold_miner_game.logic.CallBack_playerLocation;
import com.example.gold_miner_game.logic.CallBack_playerScoreClicked;
import com.example.gold_miner_game.logic.PlayerAdapter;
import com.example.gold_miner_game.model.Player;

import java.util.ArrayList;


public class ListFragment extends Fragment {


    private CallBack_playerScoreClicked callBackPlayerScoreClicked;
    private RecyclerView ListFragment_RCV_PLAYERBOARD;
    private ArrayList<Player> playerList;


    public ListFragment(ArrayList<Player> playerList) {
        this.playerList = playerList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list, container, false);
        findViews(view);
        initViews();

        return view;
    }



    private void playerClicked(double lat, double lon ) {
        if(callBackPlayerScoreClicked != null) {
            Log.d("TAG", "playerClicked: IM HERE" + lat + lon);
            callBackPlayerScoreClicked.playerScoreClicked(lat, lon);
        }

    }
    private void initViews() {
        PlayerAdapter playerAdapter = new PlayerAdapter(getActivity().getApplicationContext(),playerList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        ListFragment_RCV_PLAYERBOARD.setLayoutManager(linearLayoutManager);
        ListFragment_RCV_PLAYERBOARD.setAdapter(playerAdapter);

        playerAdapter.setPlayerLocation(new CallBack_playerLocation(){
            @Override
            public void getLatLonFromPlayerClicked(Player p, int position) {

                playerClicked(p.getLat(), p.getLng());

            }
        });


    }
    private void findViews(View view){
        ListFragment_RCV_PLAYERBOARD = view.findViewById(R.id.ListFragment_RCV_PLAYERBOARD);
    }

    public void setCallBackPlayerScoreClicked(CallBack_playerScoreClicked callBackPlayerScoreClicked) {
        this.callBackPlayerScoreClicked = callBackPlayerScoreClicked;
    }

    public void setPlayerList(ArrayList<Player> playerList) {
        this.playerList = playerList;


    }
}