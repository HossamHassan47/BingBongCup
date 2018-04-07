package com.wordpress.hossamhassan47.bingbongcup.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.wordpress.hossamhassan47.bingbongcup.R;
import com.wordpress.hossamhassan47.bingbongcup.dao.AppDatabase;
import com.wordpress.hossamhassan47.bingbongcup.entities.CupPlayer;
import com.wordpress.hossamhassan47.bingbongcup.entities.Player;
import com.wordpress.hossamhassan47.bingbongcup.entities.RoundMatch;

import java.util.List;

public class SetCupPlayerFragment extends DialogFragment {
    NoticeDialogListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement NoticeDialogListener");
        }
    }

    AppDatabase db;

    Spinner spinnerCupPlayer;
    int cupPlayerId = -1;
    int playerId = -1;
    int cupId = -1;
    int cupMode = 1;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_set_cup_player, null);

        spinnerCupPlayer = view.findViewById(R.id.spinner_cup_player);

        cupPlayerId = getArguments().getInt("cupPlayerId");
        playerId = getArguments().getInt("fk_playerId");
        cupId = getArguments().getInt("fk_cupId");
        cupMode = getArguments().getInt("mode", 1);

        db = AppDatabase.getAppDatabase(getActivity());
        List<Player> lstRemainingPlayers = db.playerDao().loadPlayersListForCup(cupId, cupMode);
        Player player = new Player();
        player.playerId = 0;
        player.fullName = "N/A";
        lstRemainingPlayers.add(0, player);

        ArrayAdapter<Player> adapter = new ArrayAdapter(getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                lstRemainingPlayers);

        spinnerCupPlayer.setAdapter(adapter);

        builder.setTitle("Select Player")
                .setView(view)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Player selectedPlayer = (Player) spinnerCupPlayer.getSelectedItem();

                        db = AppDatabase.getAppDatabase(getActivity());
                        CupPlayer cupPlayer = db.cupPlayerDao().loadCupPlayerById(cupPlayerId);
                        cupPlayer.fk_playerId = selectedPlayer.playerId;

                        if (cupPlayerId > 0) {
                            db.cupPlayerDao().updateCupPlayer(cupPlayer);

                            int rootMatchNo;
                            RoundMatch roundMatch;

                            // Set match player Id
                            if (cupPlayer.playerNo % 2 == 0) {
                                // Even --> set player 2
                                rootMatchNo = (cupPlayer.playerNo / 2);
                                roundMatch = db.roundMatchDao().loadRootRoundMatchByMatchNo(cupId, rootMatchNo);
                                roundMatch.player2Id = selectedPlayer.playerId;
                            } else {
                                // Odd --> set player 1
                                rootMatchNo = ((cupPlayer.playerNo + 1) / 2);
                                roundMatch = db.roundMatchDao().loadRootRoundMatchByMatchNo(cupId, rootMatchNo);
                                roundMatch.player1Id = selectedPlayer.playerId;
                            }

                            db.roundMatchDao().updateRoundMatch(roundMatch);
                        }

                        Toast.makeText(getActivity(), "Player saved successfully.", Toast.LENGTH_SHORT)
                                .show();

                        // Send the positive button event back to the host activity
                        mListener.onDialogPositiveClick(SetCupPlayerFragment.this);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SetCupPlayerFragment.this.getDialog().cancel();
                    }
                });

        // Create the AlertDialog object and return it
        return builder.create();
    }
}
