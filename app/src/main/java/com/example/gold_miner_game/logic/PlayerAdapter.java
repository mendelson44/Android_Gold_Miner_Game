package com.example.gold_miner_game.logic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gold_miner_game.R;
import com.example.gold_miner_game.model.Player;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> {

    private Context context;
    private ArrayList<Player> allPlayersList;
    private CallBack_playerLocation playerLocation;
    Map<Integer, Integer> placeMedalImages = new HashMap<>();


    //callback

    public PlayerAdapter(Context context, ArrayList<Player> allPlayersList) {
        this.context = context;
        this.allPlayersList = allPlayersList;
        placeMedalImages.put(1, R.drawable.gold_miner_first_place_trophy);
        placeMedalImages.put(2, R.drawable.gold_miner_second_place_trophy);
        placeMedalImages.put(3, R.drawable.gold_miner_third_place_trophy);
        placeMedalImages.put(4, R.drawable.gold_miner_medal);

    }

    public PlayerAdapter setPlayerLocation(CallBack_playerLocation playerLocation) {
        this.playerLocation = playerLocation;
        return this;
    }

    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_player_score_board, parent, false);
        return new PlayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerViewHolder holder, int position) {
            Player player = getItem(position);
            if((position) < 3)
                holder.player_IMG_rank.setImageResource(placeMedalImages.get(position + 1));
            else
                holder.player_IMG_rank.setImageResource(placeMedalImages.get(4));
            holder.player_LBL_name.setText(player.getName());
            holder.player_LBL_score.setText(String.valueOf(player.getScore()));

    }

    @Override
    public int getItemCount() {
        return allPlayersList == null ? 0 : allPlayersList.size();

    }

    private Player getItem(int position) {

        return allPlayersList.get(position);
    }

    public class PlayerViewHolder extends RecyclerView.ViewHolder {

        private ShapeableImageView player_IMG_rank;
        private MaterialTextView player_LBL_name;
        private MaterialTextView player_LBL_score;
        private ShapeableImageView player_IMG_LOCATION;



        public PlayerViewHolder(@NonNull View itemView) {
            super(itemView);

            player_IMG_rank = itemView.findViewById(R.id.player_IMG_rank);
            player_LBL_name = itemView.findViewById(R.id.player_LBL_name);
            player_LBL_score = itemView.findViewById(R.id.player_LBL_score);
            player_IMG_LOCATION = itemView.findViewById(R.id.player_IMG_LOCATION);
            player_IMG_LOCATION.setOnClickListener(v -> {

                if(playerLocation!=null){
                    playerLocation.getLatLonFromPlayerClicked(getItem(getAdapterPosition()),getAdapterPosition());
                }

            });
        }

    }
}
