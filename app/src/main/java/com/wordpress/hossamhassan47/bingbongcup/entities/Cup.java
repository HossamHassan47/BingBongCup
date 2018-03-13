package com.wordpress.hossamhassan47.bingbongcup.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.util.Date;


/**
 * Created by Hossam on 3/7/2018.
 */
@Entity
public class Cup {
    @PrimaryKey(autoGenerate = true)
    public int cupId;
    public String cupName;

    @TypeConverters({TimestampConverter.class})
    public Date creationDate;

    public int playersCount; // 2, 4, 8, 16, 32, 64
    public int roundsCount;
    public int matchesCount;
    public int gamesCount; // 1, 3, 5, 7, 9
    public int mode; // 1 for single, 2 for double
}