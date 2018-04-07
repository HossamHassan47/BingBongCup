package com.wordpress.hossamhassan47.bingbongcup.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.wordpress.hossamhassan47.bingbongcup.entities.Cup;

import java.util.List;

/**
 * Created by Hossam on 3/7/2018.
 */

@Dao
public interface CupDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertCup(Cup cup);

    @Update
    int updateCup(Cup cup);

    @Delete
    void deleteCup(Cup cup);

    @Query("SELECT * FROM Cup ORDER BY creationDate")
    List<Cup> loadAllCups();

    @Query("SELECT * FROM Cup WHERE cupId = :id")
    Cup loadCupById(int id);

    @Query("SELECT * FROM Cup WHERE cupName = :cupName")
    Cup isCupExist(String cupName);

    @Query("SELECT * FROM Cup WHERE cupName = :cupName AND cupId != :id")
    Cup isCupExist(String cupName, int id);
}
