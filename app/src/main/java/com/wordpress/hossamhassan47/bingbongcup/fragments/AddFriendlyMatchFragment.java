package com.wordpress.hossamhassan47.bingbongcup.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.wordpress.hossamhassan47.bingbongcup.R;
import com.wordpress.hossamhassan47.bingbongcup.dao.AppDatabase;
import com.wordpress.hossamhassan47.bingbongcup.entities.MatchGame;
import com.wordpress.hossamhassan47.bingbongcup.entities.RoundMatch;

/**
 * Created by Hossam on 3/27/2018.
 */

public class AddFriendlyMatchFragment extends DialogFragment {
    NoticeDialogListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    Spinner spinnerPlayer1;
    Spinner spinnerPlayer2;
    Spinner spinnerGamesCount;
    Spinner spinnerMode;
    int matchId = -1;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_friendly_match, null);

        matchId = getArguments().getInt("matchId");

        boolean addMode = matchId <= 0;

        // Players Count Spinner
        ArrayAdapter<CharSequence> adapterPlayers = ArrayAdapter.createFromResource(getActivity(),
                R.array.players_count, android.R.layout.simple_spinner_item);
        adapterPlayers.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerPlayer1 = view.findViewById(R.id.spinner_player_1);
        spinnerPlayer1.setAdapter(adapterPlayers);
        spinnerPlayer1.setSelection(adapterPlayers.getPosition(getArguments().getString("player1")));
        spinnerPlayer1.setEnabled(addMode);

        // Player 2
        spinnerPlayer2 = view.findViewById(R.id.spinner_cup_mode);
        spinnerPlayer2.setAdapter(adapterPlayers);
        spinnerPlayer2.setSelection(adapterPlayers.getPosition(getArguments().getString("player2")));
        spinnerPlayer2.setEnabled(addMode);

        // Games count spinner
        spinnerGamesCount = view.findViewById(R.id.spinner_games_count);
        ArrayAdapter<CharSequence> adapterGamesCount = ArrayAdapter.createFromResource(getActivity(),
                R.array.games_count, android.R.layout.simple_spinner_item);
        adapterGamesCount.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGamesCount.setAdapter(adapterGamesCount);
        spinnerGamesCount.setSelection(adapterGamesCount.getPosition(getArguments().getString("gamesCount")));

        spinnerGamesCount.setEnabled(addMode);

        // Mode
        spinnerMode = view.findViewById(R.id.spinner_cup_mode);
        ArrayAdapter<CharSequence> adapterMode = ArrayAdapter.createFromResource(getActivity(),
                R.array.cup_mode, android.R.layout.simple_spinner_item);
        adapterMode.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMode.setAdapter(adapterMode);

        spinnerMode.setSelection(getArguments().getInt("mode"));
        spinnerMode.setEnabled(addMode);

        builder.setTitle("Match Details")
                .setView(view)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AppDatabase db = AppDatabase.getAppDatabase(getActivity());

                        RoundMatch roundMatch;
                        if (matchId > 0) {
                            roundMatch = db.roundMatchDao().loadRoundMatchById(matchId);
                        } else {
                            roundMatch = new RoundMatch();
                        }

                        roundMatch.player1Id = Integer.parseInt(spinnerPlayer1.getSelectedItem().toString());
                        roundMatch.player2Id = Integer.parseInt(spinnerPlayer2.getSelectedItem().toString());
                        roundMatch.numberOfGames = Integer.parseInt(spinnerGamesCount.getSelectedItem().toString());

                        if (matchId > 0) {
                            db.roundMatchDao().updateRoundMatch(roundMatch);
                        } else {

                            int roundMatchId = (int) db.roundMatchDao().insertRoundMatch(roundMatch);

                            for (int i = 1; i <= roundMatch.numberOfGames; i++) {
                                // Insert match game
                                MatchGame matchGame = new MatchGame();
                                matchGame.fk_roundMatchId = roundMatchId;
                                matchGame.gameNo = i;
                                matchGame.player1Score = 0;
                                matchGame.player2Score = 0;

                                int gameId = (int) db.matchGameDao().insertMatchGame(matchGame);
                                Log.i("AddMatchFragment", "Game Added with Id: " + gameId);
                            }
                        }

                        // Send the positive button event back to the host activity
                        Toast.makeText(getActivity(), "Save done", Toast.LENGTH_SHORT).show();

                        mListener.onDialogPositiveClick(AddFriendlyMatchFragment.this);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AddFriendlyMatchFragment.this.getDialog().cancel();
                    }
                });

        // Create the AlertDialog object and return it
        return builder.create();
    }
}
