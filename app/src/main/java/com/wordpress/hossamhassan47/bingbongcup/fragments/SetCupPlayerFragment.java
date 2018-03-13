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

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_set_cup_player, null);

        spinnerCupPlayer = view.findViewById(R.id.spinner_cup_player);

        cupPlayerId = getArguments().getInt("cupPlayerId");
        playerId = getArguments().getInt("fk_playerId");

        db = AppDatabase.getAppDatabase(getActivity());
        ArrayAdapter<Player> adapter = new ArrayAdapter(getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                db.playerDao().loadAllPlayers());

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
                        }

                        Toast.makeText(getActivity(), selectedPlayer.fullName + " saved successfully.", Toast.LENGTH_SHORT)
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
