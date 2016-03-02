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
import com.shore.game.interfaces.ICellCallback;
import com.shore.game.utils.Constants;

public class BoardRecyclerAdapter extends RecyclerView.Adapter<BoardRecyclerAdapter.BoardViewHolder> {
    private final Context mContext;
    private ICellCallback mCallback;
    private char[][] mBoard;

    public BoardRecyclerAdapter(Context context, char[][] board, ICellCallback callback) {
        mContext = context;
        mBoard = board;
        mCallback = callback;
    }

    @Override
    public BoardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_game_cell, parent, false);
        return new BoardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BoardViewHolder holder, int position) {
        final int row = getRow(position);
        final int column = getColumn(position);
        if (mBoard[row][column] == Constants.MARK_FIRST_PLAYER)
            holder.mMark.setBackgroundResource(R.drawable.icon_first_player);
        else if (mBoard[row][column] == Constants.MARK_SECOND_PLAYER)
            holder.mMark.setBackgroundResource(R.drawable.icon_second_player);
        else
            holder.mMark.setBackgroundResource(0);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCallback != null && (mBoard[row][column] == Constants.MARK_DEFAULT))
                    mCallback.onCellClicked(row, column);
            }
        });
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

    public void notifyBoardChanged(char[][] board) {
        mBoard = board;
        notifyDataSetChanged();
    }

    public void setCallback(ICellCallback callback) {
        mCallback = callback;
    }

    public static class BoardViewHolder extends RecyclerView.ViewHolder {
        public ImageView mMark;

        public BoardViewHolder(View itemView) {
            super(itemView);
            mMark = (ImageView) itemView.findViewById(R.id.mark);
        }
    }
}
