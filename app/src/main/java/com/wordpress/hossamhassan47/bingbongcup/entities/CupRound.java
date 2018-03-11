package com.wordpress.hossamhassan47.bingbongcup.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Hossam on 3/10/2018.
 */
@Entity
public class CupRound {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public long cupId;
    public int roundNo;
    public String roundName;


}
