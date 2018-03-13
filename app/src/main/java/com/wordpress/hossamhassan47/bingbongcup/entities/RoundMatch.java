package com.wordpress.hossamhassan47.bingbongcup.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.util.Date;

/**
 * Created by Hossam on 3/10/2018.
 */
@Entity
public class RoundMatch {
    @PrimaryKey(autoGenerate = true)
    public int roundMatchId;
    public int fk_roundId;
    public int player1Id;
    public int player2Id;
    public int winnerId;
    public int loserId;
    public int player1PendingMatchId;
    public int player2PendingMatchId;
    public int matchNo;
    @TypeConverters({TimestampConverter.class})
    public Date matchDate;
    public int numberOfGames;
}
