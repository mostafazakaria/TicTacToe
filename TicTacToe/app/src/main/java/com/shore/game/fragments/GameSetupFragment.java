package com.shore.game.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.shore.game.R;
import com.shore.game.interfaces.IGameSetup;

public class GameSetupFragment extends Fragment {
    private EditText mFirstPlayerEditText;
    private EditText mSecondPlayerEditText;
    private Button mGoButton;
    private IGameSetup mCallbacks;
    private String mFirstPlayerName;
    private String mSecondPlayerName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_game_setup, container, false);
        initViews(view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IGameSetup)
            mCallbacks = (IGameSetup) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    private void initViews(View view) {
        mFirstPlayerEditText = (EditText) view.findViewById(R.id.first_player_name);
        mSecondPlayerEditText = (EditText) view.findViewById(R.id.second_player_name);
        mGoButton = (Button) view.findViewById(R.id.go_button);

        mGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPlayersNamesValid())
                    startGameBoard();
            }
        });
    }

    private void startGameBoard() {
        if (mCallbacks != null)
            mCallbacks.onSetupFinished(mFirstPlayerName, mSecondPlayerName);
    }

    public boolean isPlayersNamesValid() {
        mFirstPlayerName = mFirstPlayerEditText.getText().toString();
        mSecondPlayerName = mSecondPlayerEditText.getText().toString();
        if (mFirstPlayerName == null || mFirstPlayerName.isEmpty() || mFirstPlayerName.trim().isEmpty()) {
            Toast.makeText(getActivity(), R.string.first_player_name_error, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mSecondPlayerName == null || mSecondPlayerName.isEmpty() || mSecondPlayerName.trim().isEmpty()) {
            Toast.makeText(getActivity(), R.string.second_player_name_error, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mFirstPlayerName.equals(mSecondPlayerName)) {
            Toast.makeText(getActivity(), R.string.same_players_names_error, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
