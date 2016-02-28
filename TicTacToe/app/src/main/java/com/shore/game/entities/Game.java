package com.shore.game.entities;

import com.shore.game.utils.Constants;

public class Game {
    public static final int BOARD_SIZE = 3;
    private Player mFirstPlayer;
    private Player mSecondPlayer;
    private char mBoard[][];
    private int mWinner;

    public Game(Player firstPlayer, Player secondPlayer) {
        mFirstPlayer = firstPlayer;
        mSecondPlayer = secondPlayer;
        mWinner = -1;
        mBoard = new char[BOARD_SIZE][BOARD_SIZE];
        initBoard();
    }

    private void initBoard() {
        for (int i = 0; i < mBoard.length; i++) {
            for (int j = 0; j < mBoard[i].length; j++) {
                mBoard[i][j] = Constants.MARK_DEFAULT;
            }
        }
    }

    public Player getFirstPlayer() {
        return mFirstPlayer;
    }

    public void setFirstPlayer(Player firstPlayer) {
        mFirstPlayer = firstPlayer;
    }

    public Player getSecondPlayer() {
        return mSecondPlayer;
    }

    public void setSecondPlayer(Player secondPlayer) {
        mSecondPlayer = secondPlayer;
    }

    public char[][] getBoard() {
        return mBoard;
    }

    public void setBoard(char[][] board) {
        mBoard = board;
    }
}
