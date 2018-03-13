package com.wordpress.hossamhassan47.bingbongcup.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.wordpress.hossamhassan47.bingbongcup.R;
import com.wordpress.hossamhassan47.bingbongcup.entities.CupPlayerDetails;
import com.wordpress.hossamhassan47.bingbongcup.entities.RoundMatchDetails;

import java.util.List;

/**
 * Created by Hossam on 3/13/2018.
 */

public class RoundMatchAdapter extends ArrayAdapter<RoundMatchDetails> {
    List<RoundMatchDetails> lstRoundMatches;

    public RoundMatchAdapter(@NonNull Context context, @NonNull List<RoundMatchDetails> objects) {
        super(context, R.layout.adapter_item_cup_player, objects);

        this.lstRoundMatches = objects;
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.adapter_item_round_match, parent, false);
        }

        final RoundMatchDetails currentItem = getItem(position);

        // Round Match Number
        TextView txtMatchNo = listItemView.findViewById(R.id.text_view_round_match_no);
        txtMatchNo.setText("#" + (position + 1));

        TextView txtPlayer1Name = listItemView.findViewById(R.id.text_view_player1_name);
        txtPlayer1Name.setText(currentItem.player1Name);

        TextView txtPlayer2Name = listItemView.findViewById(R.id.text_view_player2_name);
        txtPlayer2Name.setText(currentItem.player2Name);

        return listItemView;
    }
}
