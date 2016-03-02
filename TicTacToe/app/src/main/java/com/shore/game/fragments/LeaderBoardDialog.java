package com.shore.game.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.LoaderManager;
import android.content.DialogInterface;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.shore.game.R;
import com.shore.game.adapters.LeaderBoardRecyclerAdapter;
import com.shore.game.entities.Player;
import com.shore.game.interfaces.ILeaderBoardCallbacks;
import com.shore.game.loaders.PlayersLoader;

import java.util.ArrayList;

public class LeaderBoardDialog extends Dialog implements LoaderManager.LoaderCallbacks<ArrayList<Player>> {
    private static final int LOADER_ID = 0;
    private LeaderBoardRecyclerAdapter mAdapter;
    private ILeaderBoardCallbacks mCallbacks;
    private RecyclerView mRecyclerView;
    private ArrayList<Player> mPlayers;
    private Activity mActivity;
    private Button mPlayAgain;
    private Button mExit;


    public LeaderBoardDialog(Activity activity) {
        super(activity);
        mActivity = activity;
        if (mActivity instanceof ILeaderBoardCallbacks)
            mCallbacks = (ILeaderBoardCallbacks) mActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setContentView(R.layout.dialog_leader_board);
        initViews();
        initLoader();
    }

    private void initLoader() {
        if (mActivity != null) {
            mActivity.getLoaderManager().initLoader(LOADER_ID, null, this).forceLoad();
        }
    }

    private void setViews() {
        if (mPlayers != null) {
            mAdapter = new LeaderBoardRecyclerAdapter(mActivity, mPlayers);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    private void initViews() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mPlayAgain = (Button) findViewById(R.id.play_again_button);
        mExit = (Button) findViewById(R.id.exit_game_button);

        mPlayAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPlayAgainDialog();
            }
        });
        mExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCallbacks != null)
                    mCallbacks.onExitGameClicked();
                dismiss();
            }
        });
    }

    private void showPlayAgainDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivity);
        alertDialogBuilder.setMessage(mActivity.getString(R.string.play_again_dialog_message));
        alertDialogBuilder.setPositiveButton(mActivity.getString(R.string.option_yes),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        if (mCallbacks != null)
                            mCallbacks.onPlayAgainClicked(true);
                        dismiss();
                    }
                });

        alertDialogBuilder.setNegativeButton(mActivity.getString(R.string.option_no),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        if (mCallbacks != null)
                            mCallbacks.onPlayAgainClicked(false);
                        dismiss();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public Loader<ArrayList<Player>> onCreateLoader(int i, Bundle bundle) {
        return new PlayersLoader(mActivity);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Player>> loader, ArrayList<Player> players) {
        if (players != null) {
            mPlayers = players;
            setViews();
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Player>> loader) {

    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mCallbacks = null;
    }
}
