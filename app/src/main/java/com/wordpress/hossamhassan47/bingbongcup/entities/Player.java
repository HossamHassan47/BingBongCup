package com.wordpress.hossamhassan47.bingbongcup.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Hossam on 3/3/2018.
 */
@Entity
public class Player {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String fullName;
    public String email;
    //public String imagePath;
}
