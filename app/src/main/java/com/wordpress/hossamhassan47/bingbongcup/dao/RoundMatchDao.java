package com.wordpress.hossamhassan47.bingbongcup.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.wordpress.hossamhassan47.bingbongcup.entities.RoundMatch;

import java.util.List;

/**
 * Created by Hossam on 3/10/2018.
 */
@Dao
public interface RoundMatchDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertRoundMatch(RoundMatch roundMatch);

    @Update
    int updateRoundMatch(RoundMatch roundMatch);

    @Delete
    void deleteRoundMatch(RoundMatch roundMatch);

    @Query("SELECT * FROM RoundMatch ORDER BY matchNo")
    List<RoundMatch> loadAllRoundMatches();

    @Query("SELECT * FROM RoundMatch WHERE roundMatchId = :id")
    RoundMatch loadRoundMatchById(int id);
}
