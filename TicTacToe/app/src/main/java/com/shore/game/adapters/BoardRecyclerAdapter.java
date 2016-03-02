package com.shore.game.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.shore.game.R;
import com.shore.game.entities.Game;
import com.shore.game.interfaces.ICellCallbacks;
import com.shore.game.utils.Constants;

import java.util.HashSet;
import java.util.Set;

public class BoardRecyclerAdapter extends RecyclerView.Adapter<BoardRecyclerAdapter.BoardViewHolder> {
    private final Context mContext;
    private ICellCallbacks mCallback;
    private char[][] mBoard;
    private Set<Integer> mAnimatedPositions;

    public BoardRecyclerAdapter(Context context, char[][] board, ICellCallbacks callback) {
        mContext = context;
        mBoard = board;
        mCallback = callback;
        mAnimatedPositions = new HashSet<>();
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
        if (mBoard[row][column] == Constants.MARK_FIRST_PLAYER) {
            holder.mMark.setBackgroundResource(R.drawable.icon_first_player);
        } else if (mBoard[row][column] == Constants.MARK_SECOND_PLAYER) {
            holder.mMark.setBackgroundResource(R.drawable.icon_second_player);
        } else {
            holder.mMark.setBackgroundResource(0);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCallback != null && (mBoard[row][column] == Constants.MARK_DEFAULT))
                    mCallback.onCellClicked(row, column);
            }
        });
        if (mAnimatedPositions.contains(position)) {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.cell_animation);
            holder.mMark.startAnimation(animation);
        }
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

    public void notifyBoardChanged(char[][] board, Set<Integer> animatedPositions) {
        mBoard = board;
        if (animatedPositions != null)
            mAnimatedPositions = animatedPositions;
        notifyDataSetChanged();
    }

    public void setCallback(ICellCallbacks callback) {
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
