package com.wordpress.hossamhassan47.bingbongcup.entities;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Relation;

import java.util.List;

import static android.arch.persistence.room.ForeignKey.CASCADE;
import static android.arch.persistence.room.ForeignKey.NO_ACTION;

/**
 * Created by Hossam on 3/10/2018.
 */
@Entity
public class CupPlayer {
    @PrimaryKey(autoGenerate = true)
    public int cupPlayerId;
    public int fk_cupId;
    public int fk_playerId;
    public int playerNo;
    public boolean winner;
}

