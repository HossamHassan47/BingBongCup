package com.wordpress.hossamhassan47.bingbongcup.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wordpress.hossamhassan47.bingbongcup.activities.MainActivity;
import com.wordpress.hossamhassan47.bingbongcup.activities.PlayerImageActivity;
import com.wordpress.hossamhassan47.bingbongcup.activities.PlayersActivity;
import com.wordpress.hossamhassan47.bingbongcup.dao.AppDatabase;
import com.wordpress.hossamhassan47.bingbongcup.entities.Player;
import com.wordpress.hossamhassan47.bingbongcup.R;
import com.wordpress.hossamhassan47.bingbongcup.fragments.AddCupFragment;
import com.wordpress.hossamhassan47.bingbongcup.fragments.AddPlayerFragment;

import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.support.v4.app.ActivityCompat.startActivityForResult;

/**
 * Created by Hossam on 3/3/2018.
 */

public class PlayerAdapter extends ArrayAdapter<Player> {

    List<Player> lstPlayers;
    ImageView imgPlayerImage;

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
        TextView txtPlayerNo = listItemView.findViewById(R.id.text_view_player_no);
        if (position < 9) {
            txtPlayerNo.setText("0" + (position + 1));
        } else {
            txtPlayerNo.setText("" + (position + 1));
        }

        ImageView iconPlayerMode = listItemView.findViewById(R.id.image_view_player_mode);
        iconPlayerMode.setImageResource(((currentItem.playerMode == 1) ? R.drawable.ic_person_white_24dp
                : R.drawable.ic_people_white_24dp));

        TextView txtFullName = listItemView.findViewById(R.id.txtFullName);
        txtFullName.setText(currentItem.fullName);

        TextView txtEmail = listItemView.findViewById(R.id.txtEmail);
        txtEmail.setText(currentItem.email);

        TextView txtMobileNo = listItemView.findViewById(R.id.text_view_mobile_no);
        txtMobileNo.setText(currentItem.mobileNo);

        imgPlayerImage = listItemView.findViewById(R.id.image_view_player_icon);
        imgPlayerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), PlayerImageActivity.class);
                i.putExtra("playerId", currentItem.playerId);
                i.putExtra("imageSrc", currentItem.imageSrc);

                getContext().startActivity(i);
            }
        });

        // Edit
        ImageView btnEditPlayer = listItemView.findViewById(R.id.image_view_edit_player);
        btnEditPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create and show the dialog.
                Bundle bundle = new Bundle();
                bundle.putString("fullName", currentItem.fullName);
                bundle.putString("email", currentItem.email);
                bundle.putString("mobileNo", currentItem.mobileNo);
                bundle.putInt("playerMode", currentItem.playerMode - 1);
                bundle.putInt("cupPlayerId", currentItem.playerId);

                AddPlayerFragment playerFragment = new AddPlayerFragment();
                playerFragment.setArguments(bundle);

                playerFragment.show(((FragmentActivity) getContext()).getSupportFragmentManager(), "dialog_AddPlayer");
            }
        });

        // Delete
        ImageView btnDelete = listItemView.findViewById(R.id.image_view_delete_player);
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

