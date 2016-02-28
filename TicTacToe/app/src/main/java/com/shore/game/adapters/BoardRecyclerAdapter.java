package com.shore.game.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shore.game.R;
import com.shore.game.entities.Game;

public class BoardRecyclerAdapter extends RecyclerView.Adapter<BoardRecyclerAdapter.BoardViewHolder> {
    private final Context mContext;
    private final char[][] mBoard;

    public BoardRecyclerAdapter(Context context, char[][] board) {
        mContext = context;
        mBoard = board;
    }

    @Override
    public BoardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_game_cell, parent, false);
        return new BoardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BoardViewHolder holder, int position) {
        holder.mMark.setText(String.valueOf(mBoard[getRow(position)][getColumn(position)]));
    }

    private int getColumn(int position) {
        return position % Game.BOARD_SIZE;
    }

    private int getRow(int position) {
        return position / Game.BOARD_SIZE;
    }

    @Override
    public int getItemCount() {
        return Game.BOARD_SIZE * Game.BOARD_SIZE;
    }

    public static class BoardViewHolder extends RecyclerView.ViewHolder {
        public TextView mMark;

        public BoardViewHolder(View itemView) {
            super(itemView);
            mMark = (TextView) itemView.findViewById(R.id.mark);
        }
    }
}
