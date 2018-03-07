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

        txtCupName = view.findViewById(R.id.txtCupName);
        txtCupName.setText(getArguments().getString("cupName"));

        // Players Count Spinner
        spinnerPlayersCount = view.findViewById(R.id.spinner_players_count);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.players_count, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPlayersCount.setAdapter(adapter);

        spinnerPlayersCount.setSelection(adapter.getPosition(getArguments().getString("playersCount")));

        // Games count spinner
        spinnerGamesCount = view.findViewById(R.id.spinner_games_count);
        ArrayAdapter<CharSequence> adapterGamesCount = ArrayAdapter.createFromResource(getActivity(),
                R.array.games_count, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGamesCount.setAdapter(adapterGamesCount);
        spinnerGamesCount.setSelection(adapterGamesCount.getPosition(getArguments().getString("gamesCount")));

        // Players Count Spinner
        spinnerCupMode = view.findViewById(R.id.spinner_cup_mode);
        ArrayAdapter<CharSequence> adapterCupMode = ArrayAdapter.createFromResource(getActivity(),
                R.array.cup_mode, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCupMode.setAdapter(adapterCupMode);
        spinnerCupMode.setSelection(getArguments().getInt("cupMode"));


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
                            db.cupDao().insertCup(cup);
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
