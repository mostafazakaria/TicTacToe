package com.shore.game.fragments;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shore.game.R;
import com.shore.game.adapters.BoardRecyclerAdapter;
import com.shore.game.entities.Game;
import com.shore.game.entities.Player;
import com.shore.game.interfaces.ICellCallback;
import com.shore.game.utils.Constants;

public class GameBoardFragment extends Fragment implements ICellCallback {
    private static final String ARG_FIRST_PLAYER = "arg_first_player";
    private static final String ARG_SECOND_PLAYER = "arg_second_player";
    private RecyclerView mBoardRecyclerView;
    private BoardRecyclerAdapter mBoardAdapter;
    private TextView mCurrentPlayer;
    private Game mGame;

    public static GameBoardFragment newInstance(String firstPlayer, String secondPlayer) {
        GameBoardFragment fragment = new GameBoardFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_FIRST_PLAYER, firstPlayer);
        bundle.putString(ARG_SECOND_PLAYER, secondPlayer);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_game_board, container, false);
        initGameBoard();
        initViews(view);
        setViews();
        return view;
    }

    private void initViews(View view) {
        mCurrentPlayer = (TextView) view.findViewById(R.id.current_player);
        mBoardRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mBoardRecyclerView.setHasFixedSize(true);
        mBoardRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), Game.BOARD_SIZE));
        mBoardAdapter = new BoardRecyclerAdapter(getActivity(), mGame.getBoard(), this);
        mBoardRecyclerView.setAdapter(mBoardAdapter);
    }

    private void setViews() {
        if (mGame != null)
            mCurrentPlayer.setText(mGame.getCurrentPlayer().getName() + getActivity().getString(R.string.game_turn));
    }

    private void initGameBoard() {
        if (getArguments() == null)
            return;
        String firstPlayer = getArguments().getString(ARG_FIRST_PLAYER);
        String secondPlayer = getArguments().getString(ARG_SECOND_PLAYER);
        mGame = new Game(new Player(firstPlayer, Constants.MARK_FIRST_PLAYER), new Player(secondPlayer, Constants.MARK_SECOND_PLAYER));
    }

    @Override
    public void onCellClicked(int row, int col) {
        mGame.setMove(row, col);
        if (mGame.checkBoardResult()) {
            Player winner = mGame.getWinner();
            if (winner != null) {
                Toast.makeText(getActivity(), winner.getName() + " (" + winner.getMark() + ") Winner is !!", Toast.LENGTH_LONG).show();
                mBoardAdapter.setCallback(null);
            }
        } else if (mGame.isBoardFull()) {
            mBoardAdapter.setCallback(null);
            Toast.makeText(getActivity(), " Draw !!", Toast.LENGTH_SHORT).show();
        }
        mCurrentPlayer.setText(mGame.getCurrentPlayer().getName() + getActivity().getString(R.string.game_turn));
        mBoardAdapter.notifyBoardChanged(mGame.getBoard());
    }
}
