package com.shore.game.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.shore.game.R;
import com.shore.game.fragments.GameBoardFragment;
import com.shore.game.fragments.LeaderBoardDialog;
import com.shore.game.interfaces.IGameBoardCallbacks;
import com.shore.game.interfaces.ILeaderBoardCallbacks;

public class GameBoardActivity extends AppCompatActivity implements IGameBoardCallbacks, ILeaderBoardCallbacks {
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

    @Override
    public void onGameFinished() {
        LeaderBoardDialog dialog = new LeaderBoardDialog(this);
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    public void onExitGameClicked() {
        finish();
    }

    @Override
    public void onPlayAgainClicked(boolean samePlayers) {
        if (samePlayers) {
                openGameBoardFragment(getIntent());
        } else {
            GameSetupActivity.start(this);
            finish();
        }
    }
}

