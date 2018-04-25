package com.wordpress.hossamhassan47.bingbongcup.Helper;

import android.content.Context;

import com.wordpress.hossamhassan47.bingbongcup.dao.AppDatabase;
import com.wordpress.hossamhassan47.bingbongcup.entities.Cup;
import com.wordpress.hossamhassan47.bingbongcup.entities.CupPlayerDetails;
import com.wordpress.hossamhassan47.bingbongcup.entities.CupRound;
import com.wordpress.hossamhassan47.bingbongcup.entities.MatchGameDetails;
import com.wordpress.hossamhassan47.bingbongcup.entities.RoundMatchDetails;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class HtmlHelper {
    public static String GetCupSummaryForEmailBody(Context context, Cup currentItem)
    {
        AppDatabase db = AppDatabase.getAppDatabase(context);

        // Cup Details
        String html = "<strong>Hello All,</strong><br><br>";
        html += "<strong><u>:: Cup Details ::</u></strong><br>";
        html += "<strong>Name: </strong>" + currentItem.cupName + "<br>";
        html += "<strong>Players: </strong>" + currentItem.playersCount + "<br>";
        html += "<strong>Best of: </strong>" + currentItem.gamesCount + "<br>";
        html += "<strong>Mode: </strong>" + (currentItem.mode == 1 ? "Single" : "Double") + "<br><br>";

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

        html += playersDetails;

        // Round Details
        DateFormat df = new SimpleDateFormat("E, dd-MMM-yyyy hh:mm a");

        List<CupRound> lstCupRounds = db.cupRoundDao().loadAllCupRounds(currentItem.cupId);
        for (int i = 0; i < lstCupRounds.size(); i++) {
            CupRound cupRound = lstCupRounds.get(i);

            html += "<br><strong><u>:: " + cupRound.roundName + " ::</u></strong><br>";

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
                    html += "<strong>" + matchNo + ". </strong>" +
                            (roundMatchDetails.player1Name == null ? "[TBD]" : roundMatchDetails.player1Name) + " vs. " +
                            (roundMatchDetails.player2Name == null ? "[TBD]" : roundMatchDetails.player2Name) + "<br>";
                } else if (roundMatchDetails.roundMatch.player1Id == roundMatchDetails.roundMatch.winnerId) {
                    // Player 1 Winner
                    html += "<strong>" + matchNo + ". </strong>" +
                            "<strong>" + roundMatchDetails.player1Name + "<font color='#33691E'> (Winner)</font></strong> vs. " +
                            "<strike>" + roundMatchDetails.player2Name + "</strike>"
                            + resultDetails + "<br>";
                } else if (roundMatchDetails.roundMatch.player2Id == roundMatchDetails.roundMatch.winnerId) {
                    // Player 1 Winner
                    html += "<strong>" + matchNo + ". </strong>" +
                            "<strike>" +roundMatchDetails.player1Name + "</strike> vs. " +
                            "<strong>" + roundMatchDetails.player2Name + "<font color='#33691E'> (Winner)</font></strong>"
                            + resultDetails+ "<br>";
                } else {
                    html += "<strong>" + matchNo + ". </strong>" +
                            roundMatchDetails.player1Name + " vs. " + roundMatchDetails.player2Name
                            + resultDetails+ "<br>";
                }
            }
        }

        html += "<br><br>Good Luck!<br>Bing Bong Cup<br>KEEP CALM & FAIR PLAY";
        return html;
    }
}
