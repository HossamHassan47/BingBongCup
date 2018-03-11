package com.wordpress.hossamhassan47.bingbongcup.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.wordpress.hossamhassan47.bingbongcup.R;
import com.wordpress.hossamhassan47.bingbongcup.dao.AppDatabase;
import com.wordpress.hossamhassan47.bingbongcup.entities.Cup;
import com.wordpress.hossamhassan47.bingbongcup.entities.CupPlayer;
import com.wordpress.hossamhassan47.bingbongcup.entities.CupRound;
import com.wordpress.hossamhassan47.bingbongcup.entities.MatchGame;
import com.wordpress.hossamhassan47.bingbongcup.entities.RoundMatch;

/**
 * Created by Hossam on 3/7/2018.
 */

public class AddCupFragment extends DialogFragment {

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

    EditText txtCupName;
    Spinner spinnerPlayersCount;
    Spinner spinnerGamesCount;
    Spinner spinnerCupMode;
    int cupId = -1;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_cup, null);

        cupId = getArguments().getInt("id");

        boolean addMode = cupId <= 0;

        txtCupName = view.findViewById(R.id.txtCupName);
        txtCupName.setText(getArguments().getString("cupName"));

        // Players Count Spinner
        spinnerPlayersCount = view.findViewById(R.id.spinner_players_count);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.players_count, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPlayersCount.setAdapter(adapter);

        spinnerPlayersCount.setSelection(adapter.getPosition(getArguments().getString("playersCount")));
        spinnerPlayersCount.setEnabled(addMode);

        // Games count spinner
        spinnerGamesCount = view.findViewById(R.id.spinner_games_count);
        ArrayAdapter<CharSequence> adapterGamesCount = ArrayAdapter.createFromResource(getActivity(),
                R.array.games_count, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGamesCount.setAdapter(adapterGamesCount);
        spinnerGamesCount.setSelection(adapterGamesCount.getPosition(getArguments().getString("gamesCount")));

        spinnerGamesCount.setEnabled(addMode);

        // Players Count Spinner
        spinnerCupMode = view.findViewById(R.id.spinner_cup_mode);
        ArrayAdapter<CharSequence> adapterCupMode = ArrayAdapter.createFromResource(getActivity(),
                R.array.cup_mode, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCupMode.setAdapter(adapterCupMode);
        spinnerCupMode.setSelection(getArguments().getInt("cupMode"));
        spinnerCupMode.setEnabled(addMode);

        builder.setTitle("Cup Details")
                .setView(view)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AppDatabase db = AppDatabase.getAppDatabase(getActivity());

                        Cup cup;
                        if (cupId > 0) {
                            cup = db.cupDao().loadCupById(cupId);
                        } else {
                            cup = new Cup();
                        }

                        cup.name = txtCupName.getText().toString();
                        cup.playersCount = Integer.parseInt(spinnerPlayersCount.getSelectedItem().toString());
                        cup.gamesCount = Integer.parseInt(spinnerGamesCount.getSelectedItem().toString());

                        cup.mode = spinnerCupMode.getSelectedItemPosition() + 1;

                        if (cupId > 0) {
                            db.cupDao().updateCup(cup);
                        } else {

                            long cupId = db.cupDao().insertCup(cup);

                            // Prepare cup details
                            // 1. Players
                            for (int i = 0; i < cup.playersCount; i++) {
                                CupPlayer cupPlayer = new CupPlayer();
                                cupPlayer.cupId = cupId;
                                cupPlayer.playerNo = i + 1;
                                db.cupPlayerDao().insertCupPlayer(cupPlayer);
                            }

                            // 2. Rounds
                            int roundNo = cup.roundsCount;
                            while (roundNo >= 1) {
                                CupRound cupRound = new CupRound();
                                cupRound.cupId = cupId;
                                cupRound.roundNo = roundNo;

                                if (roundNo == 2) {
                                    cupRound.roundName = "3rd";
                                } else if (roundNo == 1) {
                                    cupRound.roundName = "Final";
                                } else {
                                    cupRound.roundName = "Round-" + roundNo;
                                }

                                long roundId = db.cupRoundDao().insertCupRound(cupRound);

                                // 3. Matches
                                for (int j = 1; j <= cupRound.roundNo / 2; j++) {
                                    RoundMatch roundMatch = new RoundMatch();
                                    roundMatch.roundId = roundId;
                                    roundMatch.matchNo = j;
                                    roundMatch.numberOfGames = cup.gamesCount;

                                   long matchId = db.roundMatchDao().insertRoundMatch(roundMatch);

                                    // 4. Games
                                    for (int i = 1; i <= roundMatch.numberOfGames; i++) {
                                        // Insert match game
                                        MatchGame matchGame = new MatchGame();
                                        matchGame.roundMatchId = matchId;
                                        matchGame.gameNo = i;
                                        matchGame.player1Score = 0;
                                        matchGame.player2Score = 0;

                                        db.matchGameDao().insertMatchGame(matchGame);
                                    } // End Games

                                } // End Matches

                                roundNo = roundNo / 2;
                            } // End Rounds
                        }

                        Toast.makeText(getActivity(), cup.name + " saved successfully.", Toast.LENGTH_SHORT)
                                .show();
                        // Send the positive button event back to the host activity
                        mListener.onDialogPositiveClick(AddCupFragment.this);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AddCupFragment.this.getDialog().cancel();
                    }
                });

        // Create the AlertDialog object and return it
        return builder.create();
    }
}
