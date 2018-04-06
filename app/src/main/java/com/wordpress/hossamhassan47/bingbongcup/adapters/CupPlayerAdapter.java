package com.wordpress.hossamhassan47.bingbongcup.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wordpress.hossamhassan47.bingbongcup.Helper.RoundImage;
import com.wordpress.hossamhassan47.bingbongcup.R;
import com.wordpress.hossamhassan47.bingbongcup.activities.PlayerImageActivity;
import com.wordpress.hossamhassan47.bingbongcup.entities.CupPlayerDetails;

import java.util.List;

/**
 * Created by Hossam on 3/11/2018.
 */

public class CupPlayerAdapter extends ArrayAdapter<CupPlayerDetails> {
    List<CupPlayerDetails> lstPlayers;
    RoundImage roundedImage;

    public CupPlayerAdapter(@NonNull Context context, @NonNull List<CupPlayerDetails> objects) {
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

        final CupPlayerDetails currentItem = getItem(position);

        // Cup Player Number
        TextView txtPlayerNo = listItemView.findViewById(R.id.text_view_cup_player_no);
        if (position < 9) {
            txtPlayerNo.setText("0" + (position + 1));
        } else {
            txtPlayerNo.setText("" + (position + 1));
        }

        TextView txtFullName = listItemView.findViewById(R.id.txtFullName);
        TextView txtEmail = listItemView.findViewById(R.id.txtEmail);
        TextView txtMobileNo = listItemView.findViewById(R.id.text_view_mobile_no);
        ImageView imgPlayerImage = listItemView.findViewById(R.id.image_view_player_image);

        if (currentItem.player != null) {
            txtFullName.setText(currentItem.player.fullName);
            txtEmail.setText(currentItem.player.email);
            txtMobileNo.setText(currentItem.player.mobileNo);

            if (currentItem.player.imageSrc != null) {
                Bitmap bm = BitmapFactory.decodeFile(currentItem.player.imageSrc);
                roundedImage = new RoundImage(bm);
                imgPlayerImage.setImageDrawable(roundedImage);
            } else {
                imgPlayerImage.setImageResource(R.drawable.ic_face_white_48dp);
            }
        } else {
            txtFullName.setText("");
            txtEmail.setText("");
            txtMobileNo.setText("");
            imgPlayerImage.setVisibility(View.INVISIBLE);
        }

        return listItemView;
    }
}
