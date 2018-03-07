package com.wordpress.hossamhassan47.bingbongcup.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.wordpress.hossamhassan47.bingbongcup.dao.AppDatabase;
import com.wordpress.hossamhassan47.bingbongcup.entities.Player;
import com.wordpress.hossamhassan47.bingbongcup.R;

/**
 * Created by Hossam on 3/3/2018.
 */

public class AddPlayerFragment extends DialogFragment {

    NoticeDialogListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    EditText txtFullName;
    EditText txtEmail;
    int playerId = -1;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_user, null);

        txtFullName = (EditText) view.findViewById(R.id.txtFullName);
        txtEmail = (EditText) view.findViewById(R.id.txtEmail);

        txtFullName.setText(getArguments().getString("fullName"));
        txtEmail.setText(getArguments().getString("email"));

        playerId = getArguments().getInt("id");

        builder.setTitle("Player Details")
                .setView(view)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AppDatabase db = AppDatabase.getAppDatabase(getActivity());

                        Player player;
                        if (playerId > 0) {
                            player = db.playerDao().loadPlayerById(playerId);
                        } else {
                            player = new Player();
                        }

                        player.fullName = txtFullName.getText().toString();
                        player.email = txtEmail.getText().toString();

                        if (playerId > 0) {
                            db.playerDao().updatePlayer(player);
                        } else {
                            db.playerDao().insertPlayer(player);
                        }

                        Toast.makeText(getActivity(), player.fullName + " saved successfully.", Toast.LENGTH_SHORT)
                                .show();
                        // Send the positive button event back to the host activity
                        mListener.onDialogPositiveClick(AddPlayerFragment.this);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AddPlayerFragment.this.getDialog().cancel();
                    }
                });

        // Create the AlertDialog object and return it
        return builder.create();
    }
}
