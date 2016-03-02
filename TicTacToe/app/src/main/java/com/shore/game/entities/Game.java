package com.shore.game.entities;

import com.shore.game.utils.Constants;

import java.util.HashSet;
import java.util.Set;

public class Game {
    public static final int BOARD_SIZE = 3;
    private Player mFirstPlayer;
    private Player mSecondPlayer;
    private char mBoard[][];
    private int mWinner;
    private int mCurrentPlayer;
    private Set<Integer> mWinnerPositions;

    public Game(Player firstPlayer, Player secondPlayer) {
        mFirstPlayer = firstPlayer;
        mSecondPlayer = secondPlayer;
        mWinner = -1;
        mCurrentPlayer = -1;
        mBoard = new char[BOARD_SIZE][BOARD_SIZE];
        mWinnerPositions = new HashSet<>();
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
        return (scanBoardHorizontally() || scanBoardVertically() || scanBoardLeftDiagonal() || scanBoardRightDiagonal());
    }

    private boolean scanBoardHorizontally() {
        boolean firstPlayerRow = true;
        boolean secondPlayerRow = true;
        for (int i = 0; i < mBoard.length; i++) {
            for (int j = 0; j < mBoard[i].length; j++) {
                firstPlayerRow &= mBoard[i][j] == Constants.MARK_FIRST_PLAYER;
                secondPlayerRow &= mBoard[i][j] == Constants.MARK_SECOND_PLAYER;
            }
            if (firstPlayerRow) {
                mWinner = 0;
            } else if (secondPlayerRow) {
                mWinner = 1;
            }
            if (firstPlayerRow || secondPlayerRow) {
                mWinnerPositions.add(i * BOARD_SIZE);
                mWinnerPositions.add(i * BOARD_SIZE + 1);
                mWinnerPositions.add(i * BOARD_SIZE + 2);
                return true;
            }
            firstPlayerRow = secondPlayerRow = true;
        }
        return false;
    }

    private boolean scanBoardVertically() {
        boolean firstPlayerCol = true;
        boolean secondPlayerCol = true;
        for (int i = 0; i < mBoard.length; i++) {
            int j;
            for (j = 0; j < mBoard[i].length; j++) {
                firstPlayerCol &= mBoard[j][i] == Constants.MARK_FIRST_PLAYER;
                secondPlayerCol &= mBoard[j][i] == Constants.MARK_SECOND_PLAYER;
            }
            if (firstPlayerCol) {
                mWinner = 0;
            } else if (secondPlayerCol) {
                mWinner = 1;
            }
            if (firstPlayerCol || secondPlayerCol) {
                mWinnerPositions.add(i);
                mWinnerPositions.add(BOARD_SIZE + i);
                mWinnerPositions.add(2 * BOARD_SIZE + i);
                return true;
            }
            firstPlayerCol = secondPlayerCol = true;
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
        } else if (secondPlayerDiagonal) {
            mWinner = 1;
        }
        if (firstPlayerDiagonal || secondPlayerDiagonal) {
            mWinnerPositions.add(0);
            mWinnerPositions.add(4);
            mWinnerPositions.add(8);
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
        } else if (secondPlayerDiagonal) {
            mWinner = 1;
        }
        if (firstPlayerDiagonal || secondPlayerDiagonal) {
            mWinnerPositions.add(2);
            mWinnerPositions.add(4);
            mWinnerPositions.add(6);
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
        if (mCurrentPlayer == 0 || mCurrentPlayer == -1) {
            mBoard[row][col] = mFirstPlayer.getMark();
            mCurrentPlayer = 1;
        } else if (mCurrentPlayer == 1) {
            mBoard[row][col] = mSecondPlayer.getMark();
            mCurrentPlayer = 0;
        }
    }

    public Player getCurrentPlayer() {
        return (mCurrentPlayer == 0 || mCurrentPlayer == -1) ? mFirstPlayer : mSecondPlayer;
    }

    public Set<Integer> getWinnerPositions() {
        return mWinnerPositions;
    }
}
