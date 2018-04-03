package com.wordpress.hossamhassan47.bingbongcup.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Hossam on 3/3/2018.
 */
@Entity
public class Player {
    @PrimaryKey(autoGenerate = true)
    public int playerId;
    public String fullName;
    public String email;
    public String imageٍSrc;
    public String mobileNo;
    public int playerMode; // 1 for single 2 for double

    public String toString()
    {
        return fullName;
    }
}
