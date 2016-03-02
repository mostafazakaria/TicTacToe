package com.shore.game.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.shore.game.R;
import com.shore.game.adapters.BoardRecyclerAdapter;
import com.shore.game.controllers.PlayersController;
import com.shore.game.entities.Game;
import com.shore.game.entities.Player;
import com.shore.game.interfaces.ICellCallbacks;
import com.shore.game.interfaces.IGameBoardCallbacks;
import com.shore.game.utils.Constants;

import java.util.Set;

public class GameBoardFragment extends Fragment implements ICellCallbacks {
    private static final String ARG_FIRST_PLAYER = "arg_first_player";
    private static final String ARG_SECOND_PLAYER = "arg_second_player";
    private static final long SHOW_LEADER_BOARD_DELAY = 3000;
    private BoardRecyclerAdapter mBoardAdapter;
    private RecyclerView mBoardRecyclerView;
    private IGameBoardCallbacks mCallbacks;
    private TextView mCurrentPlayer;
    private Game mGame;
    private Handler mHandler;
    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (mCallbacks != null)
                mCallbacks.onGameFinished();
        }
    };

    public static GameBoardFragment newInstance(String firstPlayer, String secondPlayer) {
        GameBoardFragment fragment = new GameBoardFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_FIRST_PLAYER, firstPlayer);
        bundle.putString(ARG_SECOND_PLAYER, secondPlayer);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IGameBoardCallbacks)
            mCallbacks = (IGameBoardCallbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
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
        if (mGame != null) {
            mCurrentPlayer.setText(mGame.getCurrentPlayer().getName() + getActivity().getString(R.string.game_turn));
        }
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
        Set<Integer> winnerPositions = null;
        mGame.setMove(row, col);
        if (mGame.checkBoardResult()) {
            Player winner = mGame.getWinner();
            winnerPositions = mGame.getWinnerPositions();
            if (winner != null) {
                displayGameResult("Winner is " + winner.getName() + "!!");
                mBoardAdapter.setCallback(null);
                saveGameResult(winner);
                showLeaderBoard();
            }
        } else if (mGame.isBoardFull()) {
            displayGameResult("Game is Draw !!");
            mBoardAdapter.setCallback(null);
            saveGameResult(null);
            showLeaderBoard();
        } else {
            setViews();
        }
        mBoardAdapter.notifyBoardChanged(mGame.getBoard(), winnerPositions);
    }

    private void showLeaderBoard() {
        mHandler = new Handler();
        mHandler.postDelayed(mRunnable, SHOW_LEADER_BOARD_DELAY);
    }

    private void saveGameResult(Player winner) {
        if (winner == null) {
            mGame.getFirstPlayer().setScore(mGame.getFirstPlayer().getScore() + Constants.DRAW_POINTS);
            mGame.getSecondPlayer().setScore(mGame.getSecondPlayer().getScore() + Constants.DRAW_POINTS);
        } else if (mGame.getFirstPlayer().getId().equals(winner.getId())) {
            mGame.getFirstPlayer().setScore(mGame.getFirstPlayer().getScore() + Constants.WIN_POINTS);
        } else if (mGame.getSecondPlayer().getId().equals(winner.getId())) {
            mGame.getSecondPlayer().setScore(mGame.getSecondPlayer().getScore() + Constants.WIN_POINTS);
        }
        PlayersController.getInstance(getActivity()).savePlayer(mGame.getFirstPlayer());
        PlayersController.getInstance(getActivity()).savePlayer(mGame.getSecondPlayer());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mHandler != null)
            mHandler.removeCallbacksAndMessages(null);
    }

    public void displayGameResult(String gameResult) {
        mCurrentPlayer.setText(gameResult);
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.cell_animation);
        mCurrentPlayer.startAnimation(animation);
    }
}

