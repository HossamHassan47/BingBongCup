package com.wordpress.hossamhassan47.bingbongcup.adapters;

import android.app.Activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wordpress.hossamhassan47.bingbongcup.R;
import com.wordpress.hossamhassan47.bingbongcup.activities.CupDetailsActivity;
import com.wordpress.hossamhassan47.bingbongcup.activities.FriendlyMatchesActivity;
import com.wordpress.hossamhassan47.bingbongcup.dao.AppDatabase;
import com.wordpress.hossamhassan47.bingbongcup.entities.MatchGameDetails;
import com.wordpress.hossamhassan47.bingbongcup.entities.RoundMatch;
import com.wordpress.hossamhassan47.bingbongcup.entities.RoundMatchDetails;
import com.wordpress.hossamhassan47.bingbongcup.fragments.SetMatchDateFragment;
import com.wordpress.hossamhassan47.bingbongcup.fragments.SetMatchTimeFragment;

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
        super(context, R.layout.adapter_item_round_match, objects);

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

        boolean isDone = (currentItem.roundMatch.winnerId > 0);

        // Round Match Number
        TextView txtMatchNo = listItemView.findViewById(R.id.text_view_round_match_no);
        if (position < 9) {
            txtMatchNo.setText("0" + (position + 1));
        } else {
            txtMatchNo.setText("" + (position + 1));
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

        // Delete
        ImageView btnDelete = listItemView.findViewById(R.id.image_view_delete_match);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Confirm")
                        .setMessage("Are you sure you want to delete this match?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                AppDatabase db = AppDatabase.getAppDatabase(getContext());
                                db.roundMatchDao().deleteRoundMatch(currentItem.roundMatch);

                                lstRoundMatches.remove(position);

                                notifyDataSetChanged();

                                Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
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

        if (currentItem.roundMatch.fk_roundId > 0) {
            btnDelete.setVisibility(View.GONE);
        }

        // Winning Game Count
        List<MatchGameDetails> lstMatchGameDetails = AppDatabase
                .getAppDatabase(getContext())
                .matchGameDao()
                .loadMatchGameDetailsByRoundMatchId(currentItem.roundMatch.roundMatchId);

        LinearLayout linearLayoutPlayer1WinningGameCount = listItemView.findViewById(R.id.linear_layout_player_1_winning_game_count);
        LinearLayout linearLayoutPlayer2WinningGameCount = listItemView.findViewById(R.id.linear_layout_player_2_winning_game_count);

        if (linearLayoutPlayer1WinningGameCount.getChildCount() > 0) {
            linearLayoutPlayer1WinningGameCount.removeAllViews();
        }

        if (linearLayoutPlayer2WinningGameCount.getChildCount() > 0) {
            linearLayoutPlayer2WinningGameCount.removeAllViews();
        }

        int player1Count = 0;
        int player2Count = 0;
        for (int i = 0; i < lstMatchGameDetails.size(); i++) {
            ImageView imageView = new ImageView(getContext());
            imageView.setImageResource(R.drawable.ic_star_border_white_24dp);

            if (lstMatchGameDetails.get(i).matchGame.winnerId == currentItem.roundMatch.player1Id) {
                linearLayoutPlayer1WinningGameCount.addView(imageView);
                player1Count++;
            } else if (lstMatchGameDetails.get(i).matchGame.winnerId == currentItem.roundMatch.player2Id) {
                linearLayoutPlayer2WinningGameCount.addView(imageView);
                player2Count++;
            }
        }

        final int winnerId = (player1Count > player2Count) ? currentItem.roundMatch.player1Id : currentItem.roundMatch.player2Id;
        final int loserId = (player1Count > player2Count) ? currentItem.roundMatch.player2Id : currentItem.roundMatch.player1Id;
        final boolean isDraw = (player1Count == player2Count);

        int defaultColor = Color.parseColor("#FFFFFF");
        int winnerColor = Color.parseColor("#C6FF00");
        if (player1Count > player2Count) {
            txtPlayer1Name.setTextColor(winnerColor);
            txtPlayer1Name.setPaintFlags(txtPlayer1Name.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));

            txtPlayer2Name.setTextColor(defaultColor);
            txtPlayer2Name.setPaintFlags(txtPlayer2Name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else if (player2Count > player1Count) {
            txtPlayer2Name.setTextColor(winnerColor);
            txtPlayer2Name.setPaintFlags(txtPlayer2Name.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));

            txtPlayer1Name.setTextColor(defaultColor);
            txtPlayer1Name.setPaintFlags(txtPlayer1Name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            txtPlayer2Name.setTextColor(defaultColor);
            txtPlayer2Name.setPaintFlags(txtPlayer2Name.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));

            txtPlayer1Name.setTextColor(defaultColor);
            txtPlayer1Name.setPaintFlags(txtPlayer1Name.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }


        // Set match done
        ImageView imageViewSetMatchDone = listItemView.findViewById(R.id.image_view_set_match_done);
        imageViewSetMatchDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDraw) {
                    Toast.makeText(getContext(), "Please set match winner then try again.", Toast.LENGTH_SHORT).show();
                    return;
                }

                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Confirm")
                        .setMessage("Are you sure you want to mark this match Done? Note: You can't edit it anymore.")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Set match winner & loser
                                AppDatabase db = AppDatabase.getAppDatabase(getContext());

                                RoundMatch currentRoundMatch = db.roundMatchDao()
                                        .loadRoundMatchById(currentItem.roundMatch.roundMatchId);
                                currentRoundMatch.winnerId = winnerId;
                                currentRoundMatch.loserId = loserId;

                                db.roundMatchDao().updateRoundMatch(currentRoundMatch);

                                if (currentItem.roundMatch.fk_roundId > 0) {
                                    // Not Friendly
                                    // Set next round match player
                                    RoundMatch nextRoundMatch = db.roundMatchDao()
                                            .loadNextRoundMatch(currentItem.cupId, currentItem.roundNo, currentItem.roundMatch.matchNo);

                                    if (nextRoundMatch != null) {
                                        if (currentItem.roundMatch.matchNo % 2 == 0) {
                                            nextRoundMatch.player2Id = winnerId;
                                        } else {
                                            nextRoundMatch.player1Id = winnerId;
                                        }

                                        db.roundMatchDao().updateRoundMatch(nextRoundMatch);
                                    }

                                    if (currentItem.roundNo == 4) {
                                        // Set 3rd place match for losers
                                        RoundMatch roundMatch3rd = db.roundMatchDao()
                                                .load3rdRoundMatch(currentItem.cupId, currentItem.roundNo, currentItem.roundMatch.matchNo);

                                        if (roundMatch3rd != null) {
                                            if (currentItem.roundMatch.matchNo % 2 == 0) {
                                                roundMatch3rd.player2Id = loserId;
                                            } else {
                                                roundMatch3rd.player1Id = loserId;
                                            }

                                            db.roundMatchDao().updateRoundMatch(roundMatch3rd);
                                        }
                                    }
                                }

                                if (currentItem.roundMatch.fk_roundId > 0) {
                                    ((CupDetailsActivity) getContext()).SetViewPager();
                                } else {
                                    ((FriendlyMatchesActivity) getContext()).loadFriendlyMatches();
                                }

                                Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
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

        if (isDone) {
            imageViewSetMatchDate.setVisibility(View.GONE);
            imageViewSetMatchDone.setVisibility(View.GONE);
        } else {
            imageViewSetMatchDate.setVisibility(View.VISIBLE);
            imageViewSetMatchDone.setVisibility(View.VISIBLE);
        }

        // Send Match Email
        ImageView imageViewSendEmail = listItemView.findViewById(R.id.image_view_send_match_email);
        imageViewSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (currentItem.player1Name == null) {
                    Toast.makeText(getContext(), "Player 1 is required.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (currentItem.player2Name == null) {
                    Toast.makeText(getContext(), "Player 2 is required.", Toast.LENGTH_SHORT).show();
                    return;
                }

                String emailSubject;
                String emailBody;
                String title;

                if (currentItem.roundMatch.winnerId > 0) {
                    // Send Result
                    title = "Send match result...";
                    if (currentItem.roundMatch.fk_roundId > 0) {
                        emailSubject = "Bing Bong Cup - Match Result";
                    } else {
                        emailSubject = "Bing Bong Cup - Friendly Match Result";
                    }

                    emailBody = getMatchResultEmailBody(currentItem);

                } else {
                    if (currentItem.roundMatch.matchDate == null) {
                        Toast.makeText(getContext(), "Please set match date first.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Send Schedule
                    title = "Send match schedule...";
                    if (currentItem.roundMatch.fk_roundId > 0) {
                        emailSubject = "Bing Bong Cup - Match Schedule";
                    } else {
                        emailSubject = "Bing Bong Cup - Friendly Match Schedule";
                    }

                    emailBody = getMatchScheduleEmailBody(currentItem);
                }

                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/html");
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{currentItem.player1Email, currentItem.player2Email});
                i.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
                i.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(emailBody));

                try {
                    getContext().startActivity(Intent.createChooser(i, title));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return listItemView;
    }

    private String getMatchScheduleEmailBody(RoundMatchDetails roundMatchDetails) {
        DateFormat df = new SimpleDateFormat("E, dd-MMM-yyyy hh:mm a");

        String emailBody = "<strong>Hello,</strong><br><br>";

        if (roundMatchDetails.roundMatch.fk_roundId > 0) {
            // Not friendly
            emailBody += "<p>Kindly be informed that you have a scheduled match on <strong>"
                    + df.format(roundMatchDetails.roundMatch.matchDate) + "</strong></p>";

            emailBody += "<strong>Cup Name: </strong>" + roundMatchDetails.cupName + "<br>";
            emailBody += "<strong>Round: </strong>#" + roundMatchDetails.roundNo + "<br>";
            emailBody += "<strong>Match: </strong>#" + roundMatchDetails.roundMatch.matchNo + "<br>";
        } else {
            emailBody += "<p>Kindly be informed that you have a scheduled friendly match on <strong>"
                    + df.format(roundMatchDetails.roundMatch.matchDate) + "</strong></p>";

        }

        emailBody += "<strong>Player 1: </strong>" + roundMatchDetails.player1Name + "<br>";
        emailBody += "<strong>Player 2: </strong>" + roundMatchDetails.player2Name + "<br><br>";
        emailBody += "Good Luck!<br>Bing Bong Cup<br>KEEP CALM & FAIR PLAY";

        return emailBody;
    }

    private String getMatchResultEmailBody(RoundMatchDetails roundMatchDetails) {
        DateFormat df = new SimpleDateFormat("E, dd-MMM-yyyy hh:mm a");

        // Result Details
        List<MatchGameDetails> lstMatchGameDetails = AppDatabase.getAppDatabase(getContext()).matchGameDao()
                .loadMatchGameDetailsByRoundMatchId(roundMatchDetails.roundMatch.roundMatchId);

        String resultDetails = "<strong><u>:: Result Details ::</u></strong><br>";

        int countPlayer1 = 0;
        int countPlayer2 = 0;
        for (int i = 0; i < lstMatchGameDetails.size(); i++) {
            MatchGameDetails matchGameDetails = lstMatchGameDetails.get(i);
            if (matchGameDetails.matchGame.player1Score > matchGameDetails.matchGame.player2Score) {
                countPlayer1++;
                resultDetails += "<strong>Game#" + matchGameDetails.matchGame.gameNo + ". </strong>"
                        + "<strong>" + matchGameDetails.player1Name + " (<font color='#33691E'>" + matchGameDetails.matchGame.player1Score + "</font>)</strong>, "
                        + matchGameDetails.player2Name + " (<font color='#C62828'>" + matchGameDetails.matchGame.player2Score + "</font>)<br>";

            } else if (matchGameDetails.matchGame.player2Score > matchGameDetails.matchGame.player1Score) {
                countPlayer2++;
                resultDetails += "<strong>Game#" + matchGameDetails.matchGame.gameNo + ". </strong>"
                        + matchGameDetails.player1Name + " (<font color='#C62828'>" + matchGameDetails.matchGame.player1Score + "</font>), "
                        + "<strong>" + matchGameDetails.player2Name + " (<font color='#33691E'>" + matchGameDetails.matchGame.player2Score + "</font>)</strong><br>";

            } else {
                resultDetails += "<strong>Game#" + matchGameDetails.matchGame.gameNo + ". </strong>"
                        + matchGameDetails.player1Name + " (" + matchGameDetails.matchGame.player1Score + "), "
                        + matchGameDetails.player2Name + " (" + matchGameDetails.matchGame.player2Score + ")<br>";
            }

        }

        String emailBody = "<strong>Hello,</strong><br><br>";


        if (roundMatchDetails.roundMatch.fk_roundId > 0) {
            emailBody += "<p>Kindly find below your match final result.</p>";

            emailBody += "<strong><u>:: Match Details ::</u></strong><br>";

            emailBody += "<strong>Cup Name: </strong>" + roundMatchDetails.cupName + "<br>";

            String roundNo = "";
            if (roundMatchDetails.roundNo == 2) {
                roundNo = "3rd";
            } else if (roundMatchDetails.roundNo == 1) {
                roundNo = "Final";
            } else {
                roundNo = "#" + roundMatchDetails.roundNo;
            }

            emailBody += "<strong>Round: </strong>" + roundNo + "<br>";
            if (roundMatchDetails.roundNo > 2) {
                emailBody += "<strong>Match: </strong>#" + roundMatchDetails.roundMatch.matchNo + "<br>";
            }
        } else {
            emailBody += "<p>Kindly find below your friendly match final result.</p>";
            emailBody += "<strong><u>:: Match Details ::</u></strong><br>";
        }

        if (roundMatchDetails.roundMatch.matchDate != null) {
            emailBody += "<strong>Match Date: </strong>" + df.format(roundMatchDetails.roundMatch.matchDate) + "<br>";
        }

        if (countPlayer1 > countPlayer2) {
            emailBody += "<strong>Player 1: </strong>" + roundMatchDetails.player1Name + "<strong><font color='#33691E'> (Winner)</font></strong><br>";
            emailBody += "<strong>Player 2: </strong>" + roundMatchDetails.player2Name + "<br><br>";
        } else {
            emailBody += "<strong>Player 1: </strong>" + roundMatchDetails.player1Name + "<br>";
            emailBody += "<strong>Player 2: </strong>" + roundMatchDetails.player2Name + "<strong><font color='#33691E'> (Winner)</font></strong><br><br>";
        }

        emailBody += resultDetails;

        emailBody += "<br><br>Good Luck!<br>Bing Bong Cup<br>KEEP CALM & FAIR PLAY";

        return emailBody;
    }
}
