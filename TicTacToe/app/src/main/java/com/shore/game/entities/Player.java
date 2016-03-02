package com.shore.game.entities;

public class Player {
    private String mId;
    private String mName;
    private long mScore;
    private char mMark;

    public Player(String name, char mark) {
        this(String.valueOf(name.hashCode()), name, 0, mark);
    }

    public Player(String id, String name, long score) {
        mId = id;
        mName = name;
        mScore = score;
    }

    public Player(String id, String name, long score, char mark) {
        mId = id;
        mName = name;
        mScore = score;
        mMark = mark;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public long getScore() {
        return mScore;
    }

    public void setScore(long score) {
        mScore = score;
    }

    public char getMark() {
        return mMark;
    }

    public void setMark(char mark) {
        mMark = mark;
    }
}
