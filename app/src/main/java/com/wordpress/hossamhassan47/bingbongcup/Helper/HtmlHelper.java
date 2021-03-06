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
    public static String GetCupSummaryForEmailBody(Context context, Cup currentItem) {
        AppDatabase db = AppDatabase.getAppDatabase(context);

        // Cup Details
        String html = "<html><body>";
        html += "<strong>Hello All,</strong><br><br>";
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
                            "<strike>" + roundMatchDetails.player1Name + "</strike> vs. " +
                            "<strong>" + roundMatchDetails.player2Name + "<font color='#33691E'> (Winner)</font></strong>"
                            + resultDetails + "<br>";
                } else {
                    html += "<strong>" + matchNo + ". </strong>" +
                            roundMatchDetails.player1Name + " vs. " + roundMatchDetails.player2Name
                            + resultDetails + "<br>";
                }
            }
        }

        html += "<br><br>Good Luck!<br>Bing Bong Cup<br>KEEP CALM & FAIR PLAY";
        html += "</body></html>";
        return html;
    }

    public static String GetCupSummaryForPDF(Context context, Cup currentItem) {
        AppDatabase db = AppDatabase.getAppDatabase(context);

        // Cup Details
        String html = "<html><body>";
        html += "<h2>Bing Bong Cup - " + currentItem.cupName + " Summary</h2>";
        html += "<h3>:: Cup Info ::</h3>";
        html += "<ul><li>Name: " + currentItem.cupName + "</li>";
        html += "<li>Players: " + currentItem.playersCount + "</li>";
        html += "<li>Best of: " + currentItem.gamesCount + "</li>";
        html += "<li>Mode: " + (currentItem.mode == 1 ? "Single" : "Double") + "</li></ul>";

        // Player Details
        List<CupPlayerDetails> lstPlayers = db.cupPlayerDao().getPlayersByCupId(currentItem.cupId);

        html += "<h3>:: Cup Players ::</h3>";
        html += "<ol>";
        for (int i = 0; i < lstPlayers.size(); i++) {
            CupPlayerDetails cupPlayer = lstPlayers.get(i);
            if (cupPlayer.player != null) {
                html += "<li>" + cupPlayer.player.fullName + " \"" + cupPlayer.player.email + "\"</li>";
            } else {
                html += "<li>[TBD]</li>";
            }
        }
        html += "</ol>";

        List<CupRound> lstCupRounds = db.cupRoundDao().loadAllCupRounds(currentItem.cupId);
        for (int i = 0; i < lstCupRounds.size(); i++) {
            CupRound cupRound = lstCupRounds.get(i);

            html += "<h3>:: " + cupRound.roundName + " ::</h3>";

            List<RoundMatchDetails> lstRoundMatches = db.roundMatchDao().loadRoundMatchesById(cupRound.cupRoundId);

            String styleTd = " style='padding:4px; border: 1px solid black;'";
            for (int j = 0; j < lstRoundMatches.size(); j++) {
                RoundMatchDetails roundMatchDetails = lstRoundMatches.get(j);
                List<MatchGameDetails> lstMatchGameDetails = db.matchGameDao()
                        .loadMatchGameDetailsByRoundMatchId(roundMatchDetails.roundMatch.roundMatchId);

                String dr1 = "<tr><td rowspan='2' " + styleTd + ">" + roundMatchDetails.roundMatch.matchNo + "</td>";
                String dr2 = "<tr>";

                if (roundMatchDetails.player1Name == null || roundMatchDetails.player2Name == null) {
                    dr1 += "<td " + styleTd + ">" + (roundMatchDetails.player1Name == null ? "[TBD]" : roundMatchDetails.player1Name) + "</td>";
                    dr2 += "<td " + styleTd + ">" + (roundMatchDetails.player2Name == null ? "[TBD]" : roundMatchDetails.player2Name) + "</td>";
                } else if (roundMatchDetails.roundMatch.player1Id == roundMatchDetails.roundMatch.winnerId) {
                    // Player 1 Winner
                    dr1 += "<td " + styleTd + "><strong>" + roundMatchDetails.player1Name + "<font color='#33691E'> (Winner)</font></strong></td>";
                    dr2 += "<td " + styleTd + "><strike>" + roundMatchDetails.player2Name + "</strike></td>";
                } else if (roundMatchDetails.roundMatch.player2Id == roundMatchDetails.roundMatch.winnerId) {
                    // Player 1 Winner
                    dr1 += "<td " + styleTd + "><strike>" + roundMatchDetails.player1Name + "</strike></td>";
                    dr2 += "<td " + styleTd + "><strong>" + roundMatchDetails.player2Name + "<font color='#33691E'> (Winner)</font></strong></td>";
                } else {
                    dr1 += "<td " + styleTd + ">" + roundMatchDetails.player1Name + "</td>";
                    dr2 += "<td " + styleTd + ">" + roundMatchDetails.player2Name + "</td>";
                }

                for (int m = 0; m < lstMatchGameDetails.size(); m++) {
                    MatchGameDetails matchGameDetails = lstMatchGameDetails.get(m);

                    if (matchGameDetails.matchGame.player1Score > matchGameDetails.matchGame.player2Score) {
                        dr1 += "<td " + styleTd + "><strong><font color='#33691E'>" + matchGameDetails.matchGame.player1Score + "</font></strong></td>";
                        dr2 += "<td " + styleTd + "><font color='#C62828'>" + matchGameDetails.matchGame.player2Score + "</font></td>";
                    } else if (matchGameDetails.matchGame.player2Score > matchGameDetails.matchGame.player1Score) {
                        dr1 += "<td " + styleTd + "><font color='#C62828'>" + matchGameDetails.matchGame.player1Score + "</font></td>";
                        dr2 += "<td " + styleTd + "><strong><font color='#33691E'>" + matchGameDetails.matchGame.player2Score + "</font></strong></td>";

                    } else {
                        dr1 += "<td " + styleTd + ">" + matchGameDetails.matchGame.player1Score + "</td>";
                        dr2 += "<td " + styleTd + ">" + matchGameDetails.matchGame.player2Score + "</td>";
                    }
                }

                dr1 += "</tr>";
                dr2 += "</tr>";
                html += "<table style='border:1px solid black;'>" + dr1 + dr2 + "</table></br>";
            }
        }

        html += "<br>Good Luck!<br>Bing Bong Cup<br>KEEP CALM & FAIR PLAY";
        html += "</body></html>";
        return html;
    }

    public static String GetMatchResultForPDF(Context context, RoundMatchDetails roundMatchDetails) {
        AppDatabase db = AppDatabase.getAppDatabase(context);

        // Cup Details
        String html = "<html><body>";
        html += "<h2>Bing Bong Cup - Match Result</h2>";

        if (roundMatchDetails.roundMatch.fk_roundId > 0) {
            html += "<h3>:: Match Details ::</h3>";

            html += "<ul><li>Cup Name: " + roundMatchDetails.cupName + "</li>";

            String roundNo = "";
            if (roundMatchDetails.roundNo == 2) {
                roundNo = "3rd";
            } else if (roundMatchDetails.roundNo == 1) {
                roundNo = "Final";
            } else {
                roundNo = "#" + roundMatchDetails.roundNo;
            }

            html += "<li>Round: " + roundNo + "</li>";
            if (roundMatchDetails.roundNo > 2) {
                html += "<li>Match: #" + roundMatchDetails.roundMatch.matchNo + "</li>";
            }
        } else {
            html += "<h3>:: Friendly Match Details ::</h3>";
            html += "<ul>";
        }

        if (roundMatchDetails.roundMatch.matchDate != null) {
            DateFormat df = new SimpleDateFormat("E, dd-MMM-yyyy hh:mm a");
            html += "<li>Match Date: " + df.format(roundMatchDetails.roundMatch.matchDate) + "</li>";
        }

            html += "<li>Player 1: " + roundMatchDetails.player1Name + "</li>";
            html += "<li>Player 2: " + roundMatchDetails.player2Name + "</li></ul>";



        String styleTd = " style='padding:4px; border: 1px solid black;'";
        List<MatchGameDetails> lstMatchGameDetails = db.matchGameDao()
                .loadMatchGameDetailsByRoundMatchId(roundMatchDetails.roundMatch.roundMatchId);

        String dr1 = "<tr>";
        String dr2 = "<tr>";

        if (roundMatchDetails.player1Name == null || roundMatchDetails.player2Name == null) {
            dr1 += "<td " + styleTd + ">" + (roundMatchDetails.player1Name == null ? "[TBD]" : roundMatchDetails.player1Name) + "</td>";
            dr2 += "<td " + styleTd + ">" + (roundMatchDetails.player2Name == null ? "[TBD]" : roundMatchDetails.player2Name) + "</td>";
        } else if (roundMatchDetails.roundMatch.player1Id == roundMatchDetails.roundMatch.winnerId) {
            // Player 1 Winner
            dr1 += "<td " + styleTd + "><strong>" + roundMatchDetails.player1Name + "<font color='#33691E'> (Winner)</font></strong></td>";
            dr2 += "<td " + styleTd + "><strike>" + roundMatchDetails.player2Name + "</strike></td>";
        } else if (roundMatchDetails.roundMatch.player2Id == roundMatchDetails.roundMatch.winnerId) {
            // Player 1 Winner
            dr1 += "<td " + styleTd + "><strike>" + roundMatchDetails.player1Name + "</strike></td>";
            dr2 += "<td " + styleTd + "><strong>" + roundMatchDetails.player2Name + "<font color='#33691E'> (Winner)</font></strong></td>";
        } else {
            dr1 += "<td " + styleTd + ">" + roundMatchDetails.player1Name + "</td>";
            dr2 += "<td " + styleTd + ">" + roundMatchDetails.player2Name + "</td>";
        }

        for (int m = 0; m < lstMatchGameDetails.size(); m++) {
            MatchGameDetails matchGameDetails = lstMatchGameDetails.get(m);

            if (matchGameDetails.matchGame.player1Score > matchGameDetails.matchGame.player2Score) {
                dr1 += "<td " + styleTd + "><strong><font color='#33691E'>" + matchGameDetails.matchGame.player1Score + "</font></strong></td>";
                dr2 += "<td " + styleTd + "><font color='#C62828'>" + matchGameDetails.matchGame.player2Score + "</font></td>";
            } else if (matchGameDetails.matchGame.player2Score > matchGameDetails.matchGame.player1Score) {
                dr1 += "<td " + styleTd + "><font color='#C62828'>" + matchGameDetails.matchGame.player1Score + "</font></td>";
                dr2 += "<td " + styleTd + "><strong><font color='#33691E'>" + matchGameDetails.matchGame.player2Score + "</font></strong></td>";

            } else {
                dr1 += "<td " + styleTd + ">" + matchGameDetails.matchGame.player1Score + "</td>";
                dr2 += "<td " + styleTd + ">" + matchGameDetails.matchGame.player2Score + "</td>";
            }
        }

        dr1 += "</tr>";
        dr2 += "</tr>";
        html += "<table style='border:1px solid black;'>" + dr1 + dr2 + "</table></br>";


        html += "<br>Good Luck!<br>Bing Bong Cup<br>KEEP CALM & FAIR PLAY";
        html += "</body></html>";
        return html;
    }
}
