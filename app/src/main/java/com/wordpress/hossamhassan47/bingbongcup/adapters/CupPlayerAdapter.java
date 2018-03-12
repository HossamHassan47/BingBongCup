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
import com.wordpress.hossamhassan47.bingbongcup.entities.CupPlayer;
import com.wordpress.hossamhassan47.bingbongcup.entities.Player;

import java.util.List;

/**
 * Created by Hossam on 3/11/2018.
 */

public class CupPlayerAdapter extends ArrayAdapter<Player> {
    List<Player> lstPlayers;

    public CupPlayerAdapter(@NonNull Context context, @NonNull List<Player> objects) {
        super(context, R.layout.adapter_item_cup_player, objects);

        this.lstPlayers = objects;
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.adapter_item_cup_player, parent, false);
        }

        final Player currentItem = getItem(position);

        // Cup Player Number
        TextView txtPlayerNo = listItemView.findViewById(R.id.text_view_cup_player_no);
        txtPlayerNo.setText("#" + (position + 1));

        // Player Name
        TextView txtFullName = listItemView.findViewById(R.id.txtFullName);
        txtFullName.setText(currentItem.fullName);

        // Player Email
        TextView txtEmail = listItemView.findViewById(R.id.txtEmail);
        txtEmail.setText(currentItem.email);

        return listItemView;
    }
}
