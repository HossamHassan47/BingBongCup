package com.wordpress.hossamhassan47.bingbongcup.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by Hossam on 3/10/2018.
 */
@Entity(foreignKeys = {@ForeignKey(entity = Cup.class,
        parentColumns = "id",
        childColumns = "cupId",
        onDelete = CASCADE)})
public class CupPlayer {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public long cupId;
    public int playerId;
    public int playerNo;
    public boolean winner;
}
