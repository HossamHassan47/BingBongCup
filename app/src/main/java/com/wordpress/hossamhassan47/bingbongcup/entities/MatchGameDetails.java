package com.wordpress.hossamhassan47.bingbongcup.entities;

import android.arch.persistence.room.Embedded;

/**
 * Created by Hossam on 3/24/2018.
 */

public class MatchGameDetails {
    @Embedded
    public MatchGame matchGame;

    public int matchPlayer1Id;
    public int matchPlayer2Id;
    public String player1Name;
    public String player2Name;
    public String player1ImageSrc;
    public String player2ImageSrc;

    public String cupName;
    public int roundNo;
}
