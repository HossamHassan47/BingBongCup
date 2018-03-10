package com.wordpress.hossamhassan47.bingbongcup.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.wordpress.hossamhassan47.bingbongcup.entities.CupPlayer;

import java.util.List;

/**
 * Created by Hossam on 3/10/2018.
 */
@Dao
public interface CupPlayerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCupPlayer(CupPlayer cupPlayer);

    @Update
    void updateCupPlayer(CupPlayer cupPlayer);

    @Delete
    void deleteCupPlayer(CupPlayer cupPlayer);

    @Query("SELECT * FROM CupPlayer ORDER BY playerNo")
    List<CupPlayer> loadAllCupPlayers();

    @Query("SELECT * FROM CupPlayer WHERE id = :id")
    CupPlayer loadCupPlayerById(int id);
}
