package com.shore.game.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.shore.game.R;
import com.shore.game.controllers.PlayersController;
import com.shore.game.entities.Player;
import com.shore.game.fragments.GameBoardFragment;

public class GameBoardActivity extends AppCompatActivity {
    private final static String EXTRA_FIRST_PLAYER_NAME = "extra_first_player_name";
    private final static String EXTRA_SECOND_PLAYER_NAME = "extra_second_player_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_board);
        openGameBoardFragment(getIntent());
    }

    private void openGameBoardFragment(Intent intent) {
        if (intent == null)
            return;
        String firstPlayer = intent.getStringExtra(EXTRA_FIRST_PLAYER_NAME);
        String secondPlayer = intent.getStringExtra(EXTRA_SECOND_PLAYER_NAME);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, GameBoardFragment.newInstance(firstPlayer, secondPlayer)).commit();
    }

    public static void start(Context context, String firstPlayerName, String secondPlayerName) {
        Intent intent = new Intent(context, GameBoardActivity.class);
        intent.putExtra(EXTRA_FIRST_PLAYER_NAME, firstPlayerName);
        intent.putExtra(EXTRA_SECOND_PLAYER_NAME, secondPlayerName);
        context.startActivity(intent);
    }
}

