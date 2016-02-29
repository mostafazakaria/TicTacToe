package com.shore.game.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shore.game.R;
import com.shore.game.entities.Player;

import java.util.ArrayList;

public class LeaderBoardRecyclerAdapter extends RecyclerView.Adapter<LeaderBoardRecyclerAdapter.LeaderBoardViewHolder> {
    private final Context mContext;
    private final ArrayList<Player> mPlayers;

    public LeaderBoardRecyclerAdapter(Context context, ArrayList<Player> players) {
        mContext = context;
        mPlayers = players;
    }

    @Override
    public LeaderBoardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_leader_board, parent, false);
        return new LeaderBoardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LeaderBoardViewHolder holder, int position) {
        Player player = mPlayers.get(position);
        holder.playerName.setText(player.getName());
        holder.playerScore.setText(String.valueOf(player.getScore()));
        holder.playerLastGameDate.setText(player.getLastGameDate().toString());
    }

    @Override
    public int getItemCount() {
        return mPlayers != null ? mPlayers.size() : 0;
    }

    public static class LeaderBoardViewHolder extends RecyclerView.ViewHolder {
        public TextView playerName;
        public TextView playerScore;
        public TextView playerLastGameDate;

        public LeaderBoardViewHolder(View itemView) {
            super(itemView);
            playerName = (TextView) itemView.findViewById(R.id.player_name);
            playerScore = (TextView) itemView.findViewById(R.id.player_score);
            playerLastGameDate = (TextView) itemView.findViewById(R.id.player_last_game_date);
        }
    }
}
