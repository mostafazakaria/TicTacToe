package com.shore.game.controllers;

import android.content.Context;

import com.shore.game.datalayer.dbhandlers.PlayersDbHandler;
import com.shore.game.entities.Player;

import java.util.ArrayList;

/**
 * A singleton instance controller for game players
 */
public class PlayersController {
    private static PlayersController sPlayersController;
    private PlayersDbHandler mPlayersDbHandler;
    private Context mContext;

    private PlayersController(Context context) {
        mContext = context.getApplicationContext();
        mPlayersDbHandler = new PlayersDbHandler();
    }

    public static PlayersController getInstance(Context context) {
        synchronized (PlayersController.class) {
            if (sPlayersController == null)
                sPlayersController = new PlayersController(context);
        }
        return sPlayersController;
    }

    public Player getPlayer(String playerName) {
        if (playerName == null)
            return null;
        String playerId = String.valueOf(playerName.hashCode());
        return mPlayersDbHandler.getItemWithId(mContext, playerId);
    }

    public boolean savePlayer(Player player) {
        return player != null && mPlayersDbHandler.saveItem(mContext, player);
    }

    public ArrayList<Player> getPlayers() {
        return mPlayersDbHandler.getAll(mContext);
    }
}
