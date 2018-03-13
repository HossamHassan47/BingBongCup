package com.wordpress.hossamhassan47.bingbongcup.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wordpress.hossamhassan47.bingbongcup.dao.AppDatabase;
import com.wordpress.hossamhassan47.bingbongcup.entities.Player;
import com.wordpress.hossamhassan47.bingbongcup.R;

import java.util.List;

/**
 * Created by Hossam on 3/3/2018.
 */

public class PlayerAdapter extends ArrayAdapter<Player> {

    List<Player> lstPlayers;

    public PlayerAdapter(@NonNull Context context, @NonNull List<Player> objects) {
        super(context, R.layout.adapter_item_player, objects);

        this.lstPlayers = objects;
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.adapter_item_player, parent, false);
        }

        final Player currentItem = getItem(position);

        TextView txtFullName = listItemView.findViewById(R.id.txtFullName);
        txtFullName.setText(currentItem.fullName);

        TextView txtEmail = listItemView.findViewById(R.id.txtEmail);
        txtEmail.setText(currentItem.email);

        //ImageView imgPlayerImage = (ImageView) listItemView.findViewById(R.cupPlayerId.imgPlayerImage);
        //imgPlayerImage.setImageResource(R.drawable.family_older_brother);

        // Delete
        ImageView btnDelete = listItemView.findViewById(R.id.btnDeletePlayer);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Confirm")
                        .setMessage("Are you sure you want to delete " + currentItem.fullName + "?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                AppDatabase db = AppDatabase.getAppDatabase(getContext());
                                db.playerDao().deletePlayer(currentItem);

                                lstPlayers.remove(position);

                                notifyDataSetChanged();

                                Toast.makeText(getContext(), currentItem.fullName + " deleted successfully.", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
        return listItemView;
    }
}

