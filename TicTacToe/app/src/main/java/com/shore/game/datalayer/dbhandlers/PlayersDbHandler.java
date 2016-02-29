package com.shore.game.datalayer.dbhandlers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.Nullable;

import com.shore.game.datalayer.DatabaseManager;
import com.shore.game.entities.Player;
import com.shore.game.utils.Log;

import java.util.ArrayList;
import java.util.Date;

public class PlayersDbHandler extends DBHandler<Player> {
    private static final String TABLE_PLAYERS = "Players";
    private static final String COL_PLAYER_ID = "id";
    private static final String COL_PLAYER_NAME = "name";
    private static final String COL_PLAYER_SCORE = "score";
    private static final String COL_PLAYER_LAST_GAME_DATE = "last_game_date";
    private static final String TAG = "PlayersDbHandler";

    @Override
    public String getCreationQuery() {
        return "CREATE TABLE IF NOT EXISTS " + TABLE_PLAYERS + " ( "
                + COL_PLAYER_ID + " TEXT PRIMARY KEY, "
                + COL_PLAYER_NAME + " TEXT, "
                + COL_PLAYER_SCORE + " INTEGER, "
                + COL_PLAYER_LAST_GAME_DATE + " TEXT); ";
    }

    @Override
    public ArrayList<String> getAlterQueries() {
        return null;
    }

    @Override
    protected String getSelectQuery() {
        return "SELECT * FROM " + TABLE_PLAYERS + " ORDER BY " + COL_PLAYER_SCORE + " DESC";
    }

    private String getSelectQuery(String id) {
        return "SELECT * FROM " + TABLE_PLAYERS + " WHERE " + COL_PLAYER_ID + " = '" + id + "'";
    }

    @Override
    protected String getDeleteQuery() {
        return "DELETE FROM " + TABLE_PLAYERS;
    }

    @Override
    public boolean saveItem(Context context, Player player) {
        if (context == null || player == null) return false;
        ContentValues row = new ContentValues();

        row.put(COL_PLAYER_ID, player.getId());
        row.put(COL_PLAYER_NAME, player.getName());
        row.put(COL_PLAYER_SCORE, player.getScore());
        row.put(COL_PLAYER_LAST_GAME_DATE, player.getLastGameDate().getTime());

        try {
            return DatabaseManager.getInstance(context).insertOrUpdate(TABLE_PLAYERS, row);
        } catch (Exception e) {
            Log.e(TAG, "Error while inserting player row", e);
        }
        return false;
    }

    @Override
    protected ArrayList<Player> parseDataSet(Cursor cursor) {
        ArrayList<Player> players = new ArrayList<>();

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                try {
                    do {
                        String id = cursor.getString(cursor.getColumnIndex(COL_PLAYER_ID));
                        String name = cursor.getString(cursor.getColumnIndex(COL_PLAYER_NAME));
                        long score = cursor.getLong(cursor.getColumnIndex(COL_PLAYER_SCORE));
                        long lastGameDate = cursor.getLong(cursor.getColumnIndex(COL_PLAYER_LAST_GAME_DATE));
                        players.add(new Player(id, name, score, new Date(lastGameDate)));
                    } while (cursor.moveToNext());
                } catch (Exception e) {
                    Log.e(TAG, "Error while retrieving players rows", e);
                } finally {
                    if (cursor != null)
                        cursor.close();
                }
            }
        }

        return players;
    }

    @Override
    protected Player parseDataset(Cursor cursor) {
        Player player = null;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                try {
                    String id = cursor.getString(cursor.getColumnIndex(COL_PLAYER_ID));
                    String name = cursor.getString(cursor.getColumnIndex(COL_PLAYER_NAME));
                    long score = cursor.getLong(cursor.getColumnIndex(COL_PLAYER_SCORE));
                    long lastGameDate = cursor.getLong(cursor.getColumnIndex(COL_PLAYER_LAST_GAME_DATE));

                    player = new Player(id, name, score, new Date(lastGameDate));
                } catch (Exception e) {
                    Log.e(TAG, "Error while retrieving player row", e);
                } finally {
                    if (cursor != null)
                        cursor.close();
                }
            }
        }
        return player;
    }

    @Nullable
    @Override
    public ArrayList<Player> getAll(Context context) {
        try {
            return parseDataSet(DatabaseManager.getInstance(context).select(getSelectQuery()));
        } catch (Exception e) {
            Log.e(TAG, "Error while retrieving players", e);
            return null;
        }
    }

    public Player getItemWithId(Context context, String playerId) {
        try {
            return parseDataset(DatabaseManager.getInstance(context).select(getSelectQuery(playerId)));
        } catch (Exception e) {
            Log.e(TAG, "Error while retrieving players", e);
            return null;
        }
    }
}
