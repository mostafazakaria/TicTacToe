package com.shore.game.entities;

import com.shore.game.utils.Constants;

public class Game {
    public static final int BOARD_SIZE = 3;
    private Player mFirstPlayer;
    private Player mSecondPlayer;
    private char mBoard[][];
    private int mWinner;
    private int mCurrenPlayer;

    public Game(Player firstPlayer, Player secondPlayer) {
        mFirstPlayer = firstPlayer;
        mSecondPlayer = secondPlayer;
        mWinner = -1;
        mCurrenPlayer = -1;
        mBoard = new char[BOARD_SIZE][BOARD_SIZE];
        initBoard();
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

    private void initBoard() {
        for (int i = 0; i < mBoard.length; i++) {
            for (int j = 0; j < mBoard[i].length; j++) {
                mBoard[i][j] = Constants.MARK_DEFAULT;
            }
        }
    }

    public boolean checkBoardResult() {
        if (scanBoardDimensions())
            return true;
        return scanBoardLeftDiagonal() || scanBoardRightDiagonal();
    }

    private boolean scanBoardDimensions() {
        boolean firstPlayerRow = true;
        boolean secondPlayerRow = true;
        boolean firstPlayerCol = true;
        boolean secondPlayerCol = true;
        for (int i = 0; i < mBoard.length; i++) {
            for (int j = 0; j < mBoard[i].length; j++) {
                firstPlayerRow &= mBoard[i][j] == Constants.MARK_FIRST_PLAYER;
                secondPlayerRow &= mBoard[i][j] == Constants.MARK_SECOND_PLAYER;
                firstPlayerCol &= mBoard[j][i] == Constants.MARK_FIRST_PLAYER;
                secondPlayerCol &= mBoard[j][i] == Constants.MARK_SECOND_PLAYER;
            }
            if (firstPlayerRow || firstPlayerCol) {
                mWinner = 0;
                return true;
            } else if (secondPlayerRow || secondPlayerCol) {
                mWinner = 1;
                return true;
            }
            firstPlayerRow = firstPlayerCol = secondPlayerRow = secondPlayerCol = true;
        }
        return false;
    }

    public Player getWinner() {
        if (mWinner == 0)
            return mFirstPlayer;
        else if (mWinner == 1)
            return mSecondPlayer;
        return null;
    }

    private boolean scanBoardLeftDiagonal() {
        boolean firstPlayerDiagonal = true;
        boolean secondPlayerDiagonal = true;
        for (int i = 0; i < mBoard.length; i++) {
            firstPlayerDiagonal &= mBoard[i][i] == Constants.MARK_FIRST_PLAYER;
            secondPlayerDiagonal &= mBoard[i][i] == Constants.MARK_SECOND_PLAYER;
        }
        if (firstPlayerDiagonal) {
            mWinner = 0;
            return true;
        } else if (secondPlayerDiagonal) {
            mWinner = 1;
            return true;
        }
        return false;
    }

    private boolean scanBoardRightDiagonal() {
        boolean firstPlayerDiagonal = true;
        boolean secondPlayerDiagonal = true;
        for (int i = 0; i < mBoard.length; i++) {
            firstPlayerDiagonal &= mBoard[i][mBoard.length - i - 1] == Constants.MARK_FIRST_PLAYER;
            secondPlayerDiagonal &= mBoard[i][mBoard.length - i - 1] == Constants.MARK_SECOND_PLAYER;
        }
        if (firstPlayerDiagonal) {
            mWinner = 0;
            return true;
        } else if (secondPlayerDiagonal) {
            mWinner = 1;
            return true;
        }
        return false;
    }

    public boolean isBoardFull() {
        for (int i = 0; i < mBoard.length; i++) {
            for (int j = 0; j < mBoard[i].length; j++) {
                if (mBoard[i][j] == Constants.MARK_DEFAULT)
                    return false;
            }
        }
        return true;
    }

    public void setMove(int row, int col) {
        if (mCurrenPlayer == 0 || mCurrenPlayer == -1) {
            mBoard[row][col] = mFirstPlayer.getMark();
            mCurrenPlayer = 1;
        } else if (mCurrenPlayer == 1) {
            mBoard[row][col] = mSecondPlayer.getMark();
            mCurrenPlayer = 0;
        }
    }
}
