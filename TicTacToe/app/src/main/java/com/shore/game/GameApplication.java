package com.shore.game;

import android.app.Application;

import com.shore.game.datalayer.DatabaseManager;
import com.shore.game.utils.Log;

public class GameApplication extends Application {
    private static final String TAG = "GameApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        initDatabase();
    }

    private void initDatabase() {
        try {
            DatabaseManager.getInstance(getApplicationContext());
        } catch (Exception e) {
            Log.e(TAG, "Fail to initialize database manager", e);
        }
    }
}
