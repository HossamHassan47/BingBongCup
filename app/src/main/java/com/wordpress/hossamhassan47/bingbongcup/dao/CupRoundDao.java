package com.wordpress.hossamhassan47.bingbongcup.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.wordpress.hossamhassan47.bingbongcup.entities.CupRound;

import java.util.List;

/**
 * Created by Hossam on 3/10/2018.
 */
@Dao
public interface CupRoundDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertCupRound(CupRound cupRound);

    @Update
    int updateCupRound(CupRound cupRound);

    @Delete
    void deleteCupRound(CupRound cupRound);

    @Query("SELECT * FROM CupRound ORDER BY roundNo")
    List<CupRound> loadAllCupRounds();

    @Query("SELECT * FROM CupRound WHERE fk_cupId = :id")
    CupRound loadCupRoundById(int id);
}
