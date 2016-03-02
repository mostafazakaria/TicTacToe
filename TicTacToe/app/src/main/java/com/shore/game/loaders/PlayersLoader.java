package com.shore.game.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.shore.game.controllers.PlayersController;
import com.shore.game.entities.Player;

import java.util.ArrayList;

public class PlayersLoader extends AsyncTaskLoader<ArrayList<Player>> {
    public PlayersLoader(Context context) {
        super(context.getApplicationContext());
    }

    @Override
    public ArrayList<Player> loadInBackground() {
        return PlayersController.getInstance(getContext()).getPlayers();
    }
}
