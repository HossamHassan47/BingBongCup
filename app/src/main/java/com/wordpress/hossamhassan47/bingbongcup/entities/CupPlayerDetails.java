package com.wordpress.hossamhassan47.bingbongcup.entities;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

/**
 * Created by Hossam on 3/13/2018.
 */

public class CupPlayerDetails {
    @Embedded
    public CupPlayer cupPlayer;

    @Embedded
    public Player player;
}
