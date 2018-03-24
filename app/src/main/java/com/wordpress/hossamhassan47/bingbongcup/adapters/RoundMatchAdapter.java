package com.wordpress.hossamhassan47.bingbongcup.adapters;

import android.app.Activity;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wordpress.hossamhassan47.bingbongcup.R;
import com.wordpress.hossamhassan47.bingbongcup.entities.RoundMatchDetails;
import com.wordpress.hossamhassan47.bingbongcup.fragments.SetMatchDateFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Hossam on 3/13/2018.
 */

public class RoundMatchAdapter extends ArrayAdapter<RoundMatchDetails> {
    List<RoundMatchDetails> lstRoundMatches;
    DateFormat df = new SimpleDateFormat("E, dd-MMM-yyyy hh:mm a");

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
        if (position < 9) {
            txtMatchNo.setText("#0" + (position + 1));
        } else {
            txtMatchNo.setText("#" + (position + 1));
        }

        // Match Date
        TextView txtMatchDate = listItemView.findViewById(R.id.text_view_match_date_time);
        if (currentItem.roundMatch.matchDate != null) {
            txtMatchDate.setText(df.format(currentItem.roundMatch.matchDate));
        } else {
            txtMatchDate.setText("");
        }

        // Player Name
        TextView txtPlayer1Name = listItemView.findViewById(R.id.text_view_player1_name);
        txtPlayer1Name.setText(currentItem.player1Name);

        TextView txtPlayer2Name = listItemView.findViewById(R.id.text_view_player2_name);
        txtPlayer2Name.setText(currentItem.player2Name);

        // Set match date
        ImageView imageViewSetMatchDate = listItemView.findViewById(R.id.image_view_set_match_date);
        imageViewSetMatchDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create and show the dialog.
                Bundle bundle = new Bundle();
                bundle.putInt("roundMatchId", currentItem.roundMatch.roundMatchId);
                if (currentItem.roundMatch.matchDate != null) {
                    bundle.putString("matchDate_Day", (String) android.text.format.DateFormat.format("dd", currentItem.roundMatch.matchDate));
                    bundle.putString("matchDate_Month", (String) android.text.format.DateFormat.format("MM", currentItem.roundMatch.matchDate));
                    bundle.putString("matchDate_Year", (String) android.text.format.DateFormat.format("yyyy", currentItem.roundMatch.matchDate));
                    bundle.putString("matchDate_Hours", (String) android.text.format.DateFormat.format("HH", currentItem.roundMatch.matchDate));
                    bundle.putString("matchDate_Minutes", (String) android.text.format.DateFormat.format("mm", currentItem.roundMatch.matchDate));
                } else {
                    bundle.putString("matchDate_Day", "-1");
                }

                SetMatchDateFragment setMatchDateFragment = new SetMatchDateFragment();
                setMatchDateFragment.setArguments(bundle);

                FragmentManager fm = ((FragmentActivity) getContext()).getSupportFragmentManager();
                setMatchDateFragment.show(fm, "dialog_SetRoundMatchDate");
            }
        });

        // Set match done
        ImageView imageViewSetMatchDone = listItemView.findViewById(R.id.image_view_set_match_done);
        imageViewSetMatchDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
            }
        });

        // Send Match Email
        ImageView imageViewSendEmail = listItemView.findViewById(R.id.image_view_send_match_email);
        imageViewSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentItem.roundMatch.matchDate == null) {
                    Toast.makeText(getContext(), "Please set match date first.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (currentItem.player1Name == null) {
                    Toast.makeText(getContext(), "Player 1 is required.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (currentItem.player2Name == null) {
                    Toast.makeText(getContext(), "Player 2 is required.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/html");
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{currentItem.player1Email, currentItem.player2Email});
                i.putExtra(Intent.EXTRA_SUBJECT, "Bing Bong Cup - Scheduled Match");
                i.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(GetEmailBody(currentItem)));

                try {
                    getContext().startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return listItemView;
    }

    private String GetEmailBody(RoundMatchDetails roundMatchDetails) {
        String emailBody = "<strong>Hello,</strong><br><br>";
        emailBody += "<p>Kindly be informed that you have a scheduled match on <strong>"
                + roundMatchDetails.roundMatch.matchDate.toString() + "</strong></p>";
        emailBody += "<strong><u>:: Match Details ::</u></strong><br>";
        emailBody += "<strong>Cup Name: </strong>" + roundMatchDetails.cupName + "<br>";
        emailBody += "<strong>Round: </strong>#" + roundMatchDetails.roundNo + "<br>";
        emailBody += "<strong>Match: </strong>#" + roundMatchDetails.roundMatch.matchNo + "<br>";
        emailBody += "<strong>Player 1: </strong>" + roundMatchDetails.player1Name + "<br>";
        emailBody += "<strong>Player 2: </strong>" + roundMatchDetails.player2Name + "<br><br>";
        emailBody += "Good Luck!<br>Bing Bong Cup<br>KEEP CALM & FAIR PLAY";

        return emailBody;
    }
}
