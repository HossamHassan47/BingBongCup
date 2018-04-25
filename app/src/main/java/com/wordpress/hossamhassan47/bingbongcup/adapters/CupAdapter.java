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

                // Cup Details
                String emailBody = "<strong>Hello All,</strong><br><br>";
                emailBody += "<strong><u>:: Cup Details ::</u></strong><br>";
                emailBody += "<strong>Name: </strong>" + currentItem.cupName + "<br>";
                emailBody += "<strong>Players: </strong>" + currentItem.playersCount + "<br>";
                emailBody += "<strong>Best of: </strong>" + currentItem.gamesCount + "<br>";
                emailBody += "<strong>Mode: </strong>" + (currentItem.mode == 1 ? "Single" : "Double") + "<br><br>";

                // Player Details
                List<CupPlayerDetails> lstPlayers = db.cupPlayerDao().getPlayersByCupId(currentItem.cupId);

                String[] playerEmails = new String[lstPlayers.size()];
                String playersDetails = "<strong><u>:: Players ::</u></strong><br>";
                for (int i = 0; i < lstPlayers.size(); i++) {
                    CupPlayerDetails cupPlayer = lstPlayers.get(i);

                    String playerNo = (i < 9) ? "0" + (i + 1) : "" + (i + 1);

                    if (cupPlayer.player != null) {
                        playerEmails[i] = cupPlayer.player.email;
                        playersDetails += "<strong>" + playerNo + ". " + cupPlayer.player.fullName + "</strong> \""
                                + cupPlayer.player.email + "\"<br>";
                    } else {
                        playerEmails[i] = "";
                        playersDetails += "<strong>" + playerNo + ". [TBD]</strong><br>";
                    }
                }

                emailBody += playersDetails;

                // Round Details
                DateFormat df = new SimpleDateFormat("E, dd-MMM-yyyy hh:mm a");

                List<CupRound> lstCupRounds = db.cupRoundDao().loadAllCupRounds(currentItem.cupId);
                for (int i = 0; i < lstCupRounds.size(); i++) {
                    CupRound cupRound = lstCupRounds.get(i);

                    emailBody += "<br><strong><u>:: " + cupRound.roundName + " ::</u></strong><br>";

                    List<RoundMatchDetails> lstRoundMatches = db.roundMatchDao().loadRoundMatchesById(cupRound.cupRoundId);

                    for (int j = 0; j < lstRoundMatches.size(); j++) {
                        RoundMatchDetails roundMatchDetails = lstRoundMatches.get(j);
                        String matchNo = (j < 9) ? "0" + (j + 1) : "" + (j + 1);

                        // Get Result Details
                        List<MatchGameDetails> lstMatchGameDetails = db.matchGameDao()
                                .loadMatchGameDetailsByRoundMatchId(roundMatchDetails.roundMatch.roundMatchId);
                        String resultDetails = " | ";
                        for (int m = 0; m < lstMatchGameDetails.size(); m++) {
                            MatchGameDetails matchGameDetails = lstMatchGameDetails.get(m);
                            if (matchGameDetails.matchGame.player1Score > matchGameDetails.matchGame.player2Score) {
                                resultDetails += "(<strong><font color='#33691E'>" + matchGameDetails.matchGame.player1Score + "</font></strong>, "
                                        + " <font color='#C62828'>" + matchGameDetails.matchGame.player2Score + "</font>) ";

                            } else if (matchGameDetails.matchGame.player2Score > matchGameDetails.matchGame.player1Score) {
                                resultDetails += "(<font color='#C62828'>" + matchGameDetails.matchGame.player1Score + "</font>, "
                                        + "<strong><font color='#33691E'>" + matchGameDetails.matchGame.player2Score + "</font></strong>) ";

                            } else {
                                resultDetails += "(" + matchGameDetails.matchGame.player1Score
                                        + ", " + matchGameDetails.matchGame.player2Score + ") ";
                            }
                        }

                        //roundMatchDetails.roundMatch.winnerId
                        if (roundMatchDetails.player1Name == null || roundMatchDetails.player2Name == null) {
                            emailBody += "<strong>" + matchNo + ". </strong>" +
                                    (roundMatchDetails.player1Name == null ? "[TBD]" : roundMatchDetails.player1Name) + " vs. " +
                                    (roundMatchDetails.player2Name == null ? "[TBD]" : roundMatchDetails.player2Name) + "<br>";
                        } else if (roundMatchDetails.roundMatch.player1Id == roundMatchDetails.roundMatch.winnerId) {
                            // Player 1 Winner
                            emailBody += "<strong>" + matchNo + ". </strong>" +
                                    "<strong>" + roundMatchDetails.player1Name + "<font color='#33691E'> (Winner)</font></strong> vs. " +
                                    "<strike>" + roundMatchDetails.player2Name + "</strike>"
                                    + resultDetails + "<br>";
                        } else if (roundMatchDetails.roundMatch.player2Id == roundMatchDetails.roundMatch.winnerId) {
                            // Player 1 Winner
                            emailBody += "<strong>" + matchNo + ". </strong>" +
                                    "<strike>" +roundMatchDetails.player1Name + "</strike> vs. " +
                                    "<strong>" + roundMatchDetails.player2Name + "<font color='#33691E'> (Winner)</font></strong>"
                                    + resultDetails+ "<br>";
                        } else {
                            emailBody += "<strong>" + matchNo + ". </strong>" +
                                    roundMatchDetails.player1Name + " vs. " + roundMatchDetails.player2Name
                                    + resultDetails+ "<br>";
                        }
                    }
                }

                emailBody += "<br><br>Good Luck!<br>Bing Bong Cup<br>KEEP CALM & FAIR PLAY";

                String emailSubject = "Bing Bong Cup - " + currentItem.cupName + " Summary";
                String title = "Send cup summary...";

                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/html");
                i.putExtra(Intent.EXTRA_EMAIL, playerEmails);
                i.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
                i.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(emailBody));


                PdfConverter converter = PdfConverter.getInstance();
                File file = new File(pdfSummaryPath);
                String htmlString = Html.fromHtml(emailBody).toString();
                converter.convert(getContext(), htmlString, file);
// By now the pdf has been printed in the fil
                i.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:/"+pdfSummaryPath));

                //if(createPdf()){
                  //  i.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:/"+pdfSummaryPath));

                    Log.i("pdf", "file:/"+pdfSummaryPath);
                //}

                try {
                    getContext().startActivity(Intent.createChooser(i, title));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return listItemView;
    }

    String pdfSummaryPath = getContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)+"/BingBongCupSummary.pdf";
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private boolean createPdf(){
        // create a new document
        PdfDocument document = new PdfDocument();

        // crate a page description
        PdfDocument.PageInfo pageInfo =
                new PdfDocument.PageInfo.Builder(100, 100, 1).create();

        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();
        paint.setColor(Color.RED);

        canvas.drawCircle(50, 50, 30, paint);

        // finish the page
        document.finishPage(page);

        // Create Page 2
        pageInfo = new PdfDocument.PageInfo.Builder(500, 500, 2).create();
        page = document.startPage(pageInfo);
        canvas = page.getCanvas();
        paint = new Paint();
        paint.setColor(Color.BLUE);
        canvas.drawCircle(200, 200, 100, paint);
        document.finishPage(page);

        // write the document content
        String targetPdf = pdfSummaryPath;
        File filePath = new File(targetPdf);
        try {
            filePath.createNewFile(); // if file already exists will do nothing
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        try {
            document.writeTo(new FileOutputStream(filePath, false));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        finally {
            // close the document
            document.close();
        }
        return true;
    }

}
