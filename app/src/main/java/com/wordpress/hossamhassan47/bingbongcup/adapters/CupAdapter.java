package com.wordpress.hossamhassan47.bingbongcup.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.print.PdfConverter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wordpress.hossamhassan47.bingbongcup.Helper.HtmlHelper;
import com.wordpress.hossamhassan47.bingbongcup.R;
import com.wordpress.hossamhassan47.bingbongcup.dao.AppDatabase;
import com.wordpress.hossamhassan47.bingbongcup.entities.Cup;
import com.wordpress.hossamhassan47.bingbongcup.entities.CupPlayerDetails;
import com.wordpress.hossamhassan47.bingbongcup.entities.CupRound;
import com.wordpress.hossamhassan47.bingbongcup.entities.MatchGameDetails;
import com.wordpress.hossamhassan47.bingbongcup.entities.Player;
import com.wordpress.hossamhassan47.bingbongcup.entities.RoundMatchDetails;
import com.wordpress.hossamhassan47.bingbongcup.fragments.AddCupFragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

        // Round Match Number
        TextView txtCupNo = listItemView.findViewById(R.id.text_view_round_cup_no);
        if (position < 9) {
            txtCupNo.setText("0" + (position + 1));
        } else {
            txtCupNo.setText("" + (position + 1));
        }

        // Name
        TextView txtCupName = listItemView.findViewById(R.id.txtCupName);
        txtCupName.setText(currentItem.cupName);

        // Players Count
        TextView txtPlayersCount = listItemView.findViewById(R.id.text_view_players_count);
        txtPlayersCount.setText(currentItem.playersCount + "");

        // Cup Mode
        ImageView iconCupMode = listItemView.findViewById(R.id.iconCupMode);
        iconCupMode.setImageResource(((currentItem.mode == 1) ? R.drawable.ic_person_white_24dp : R.drawable.ic_people_white_24dp));

        TextView txtGamesCount = listItemView.findViewById(R.id.text_view_games_count);
        txtGamesCount.setText("" + currentItem.gamesCount);

        // Edit
        ImageView btnEditCup = listItemView.findViewById(R.id.image_view_edit_cup);
        btnEditCup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create and show the dialog.
                Bundle bundle = new Bundle();
                bundle.putInt("cupPlayerId", currentItem.cupId);
                bundle.putString("cupName", currentItem.cupName);
                bundle.putString("playersCount", String.valueOf(currentItem.playersCount));
                bundle.putString("gamesCount", String.valueOf(currentItem.gamesCount));
                bundle.putInt("cupMode", currentItem.mode - 1);

                AddCupFragment fragment = new AddCupFragment();
                fragment.setArguments(bundle);

                FragmentManager fm = ((FragmentActivity) getContext()).getSupportFragmentManager();
                fragment.show(fm, "dialog_AddCup");
            }
        });

        // Delete
        ImageView btnDelete = listItemView.findViewById(R.id.image_view_delete_cup);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Confirm")
                        .setMessage("Are you sure you want to delete " + currentItem.cupName + " cup?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                AppDatabase db = AppDatabase.getAppDatabase(getContext());
                                db.cupDao().deleteCup(currentItem);

                                lstCups.remove(position);

                                notifyDataSetChanged();

                                Toast.makeText(getContext(), currentItem.cupName + " deleted successfully.", Toast.LENGTH_SHORT)
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

        // Send Summary
        ImageView btnSendCupSummary = listItemView.findViewById(R.id.image_view_send_cup_summary);
        btnSendCupSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppDatabase db = AppDatabase.getAppDatabase(getContext());

                // Player Details
                List<CupPlayerDetails> lstPlayers = db.cupPlayerDao().getPlayersByCupId(currentItem.cupId);

                String[] playerEmails = new String[lstPlayers.size()];
                for (int i = 0; i < lstPlayers.size(); i++) {
                    CupPlayerDetails cupPlayer = lstPlayers.get(i);

                    if (cupPlayer.player != null) {
                        playerEmails[i] = cupPlayer.player.email;
                    } else {
                        playerEmails[i] = "";
                    }
                }

                String emailBody = HtmlHelper.GetCupSummaryForEmailBody(getContext(), currentItem);
                String emailSubject = "Bing Bong Cup - " + currentItem.cupName + " Summary";
                String title = "Send cup summary...";

                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/html");
                i.putExtra(Intent.EXTRA_EMAIL, playerEmails);
                i.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
                i.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(emailBody));

                // Generate PDF
                PdfConverter converter = PdfConverter.getInstance();
                String pdfSummaryPath = getContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
                        + "/" + emailBody + ".pdf";
                File file = new File(pdfSummaryPath);
                String htmlString = Html.fromHtml(emailBody).toString();
                converter.convert(getContext(), htmlString, file);

                i.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:/" + pdfSummaryPath));

                Log.i("pdf", "file:/" + pdfSummaryPath);

                try {
                    getContext().startActivity(Intent.createChooser(i, title));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return listItemView;
    }
}
