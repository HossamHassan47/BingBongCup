package com.wordpress.hossamhassan47.bingbongcup.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Hossam on 3/10/2018.
 */
@Entity
public class MatchGame {
    @PrimaryKey(autoGenerate = true)
    public int matchGameId;
    public int fk_roundMatchId;
    public int gameNo;
    public int player1Id;
    public int player2Id;
    public int player1Score;
    public int player2Score;
    public int winnerId;
}
