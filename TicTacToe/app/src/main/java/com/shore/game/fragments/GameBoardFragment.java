package com.shore.game.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shore.game.R;
import com.shore.game.adapters.BoardRecyclerAdapter;
import com.shore.game.entities.Game;
import com.shore.game.entities.Player;
import com.shore.game.utils.Constants;

public class GameBoardFragment extends Fragment {
    private RecyclerView mBoardRecyclerView;
    private BoardRecyclerAdapter mBoardAdapter;
    private Game mGame;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_game_board, container, false);
        initGameBoard();
        initViews(view);
        return view;
    }

    private void initGameBoard() {
        mGame = new Game(new Player("Mostafa", Constants.MARK_FIRST_PLAYER), new Player("Zeko", Constants.MARK_SECOND_PLAYER));
    }

    private void initViews(View view) {
        mBoardRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mBoardRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), Game.BOARD_SIZE));
        mBoardAdapter = new BoardRecyclerAdapter(getActivity(), mGame.getBoard());
        mBoardRecyclerView.setAdapter(mBoardAdapter);
    }
}
