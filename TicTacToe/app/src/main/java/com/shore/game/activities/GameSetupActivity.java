package com.shore.game.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.shore.game.R;
import com.shore.game.interfaces.IGameSetupCallbacks;

public class GameSetupActivity extends AppCompatActivity implements IGameSetupCallbacks {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_setup);
    }

    @Override
    public void onSetupFinished(String firstPlayerName, String secondPlayerName) {
        GameBoardActivity.start(this, firstPlayerName, secondPlayerName);
        finish();
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, GameSetupActivity.class);
        context.startActivity(intent);
    }
}
