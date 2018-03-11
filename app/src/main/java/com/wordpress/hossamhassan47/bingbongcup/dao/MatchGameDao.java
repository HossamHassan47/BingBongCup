package com.wordpress.hossamhassan47.bingbongcup.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.wordpress.hossamhassan47.bingbongcup.entities.MatchGame;

import java.util.List;

/**
 * Created by Hossam on 3/10/2018.
 */
@Dao
public interface MatchGameDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertMatchGame(MatchGame matchGame);

    @Update
    int updateMatchGame(MatchGame matchGame);

    @Delete
    void deleteMatchGame(MatchGame matchGame);

    @Query("SELECT * FROM MatchGame ORDER BY gameNo")
    List<MatchGame> loadAllMatchGames();

    @Query("SELECT * FROM MatchGame WHERE id = :id")
    MatchGame loadMatchGameById(int id);
}
