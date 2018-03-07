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

import com.wordpress.hossamhassan47.bingbongcup.R;
import com.wordpress.hossamhassan47.bingbongcup.dao.AppDatabase;
import com.wordpress.hossamhassan47.bingbongcup.entities.Cup;
import com.wordpress.hossamhassan47.bingbongcup.entities.Player;

import java.util.List;

/**
 * Created by Hossam on 3/7/2018.
 */

public class CupAdapter extends ArrayAdapter<Cup> {
    List<Cup> lstCups;

    public CupAdapter(@NonNull Context context, @NonNull List<Cup> objects) {
        super(context, R.layout.adapter_item_cup, objects);

        this.lstCups = objects;
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.adapter_item_cup, parent, false);
        }

        final Cup currentItem = getItem(position);

        // Name
        TextView txtCupName = listItemView.findViewById(R.id.txtCupName);
        txtCupName.setText(currentItem.name);

        // Players Count
        TextView txtPlayersCount = listItemView.findViewById(R.id.text_view_players_count);
        txtPlayersCount.setText(currentItem.playersCount + " Players");

        TextView txtGamesCount = listItemView.findViewById(R.id.text_view_games_count);
        txtGamesCount.setText("Best of " + currentItem.gamesCount);

        TextView txtCupMode = listItemView.findViewById(R.id.text_view_cup_mode);
        txtCupMode.setText(((currentItem.mode == 1) ? "Single" : "Double"));

        // Delete
        ImageView btnDelete = listItemView.findViewById(R.id.btnDeleteCup);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Confirm")
                        .setMessage("Are you sure you want to delete " + currentItem.name + " cup?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                AppDatabase db = AppDatabase.getAppDatabase(getContext());
                                db.cupDao().deleteCup(currentItem);

                                lstCups.remove(position);

                                notifyDataSetChanged();

                                Toast.makeText(getContext(), currentItem.name + " deleted successfully.", Toast.LENGTH_SHORT)
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
