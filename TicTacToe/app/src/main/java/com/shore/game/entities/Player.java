package com.shore.game.entities;

import java.util.Date;

public class Player {
    private String mId;
    private String mName;
    private long mScore;
    private Date mLastGameDate;
    private char mMark;

    public Player(String name, char mark) {
        this(String.valueOf(name.hashCode()), name, 0, new Date(), mark);
    }

    public Player(String id, String name, long score, Date lastGameDate) {
        mId = id;
        mName = name;
        mScore = score;
        mLastGameDate = lastGameDate;
    }

    public Player(String id, String name, long score, Date lastGameDate, char mark) {
        mId = id;
        mName = name;
        mScore = score;
        mLastGameDate = lastGameDate;
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

    public Date getLastGameDate() {
        return mLastGameDate;
    }

    public void setLastGameDate(Date lastGameDate) {
        mLastGameDate = lastGameDate;
    }

    public char getMark() {
        return mMark;
    }

    public void setMark(char mark) {
        mMark = mark;
    }
}
