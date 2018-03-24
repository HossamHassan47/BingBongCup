package com.wordpress.hossamhassan47.bingbongcup.entities;

import android.arch.persistence.room.Embedded;

/**
 * Created by Hossam on 3/13/2018.
 */

public class RoundMatchDetails {

    @Embedded
    public RoundMatch roundMatch;

    public String player1Name;
    public String player2Name;
    public String player1Email;
    public String player2Email;

    public int cupId;
    public String cupName;

    public int roundNo;
}
