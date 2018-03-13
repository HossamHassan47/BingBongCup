package com.wordpress.hossamhassan47.bingbongcup.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.wordpress.hossamhassan47.bingbongcup.entities.Player;

import java.util.List;

/**
 * Created by Hossam on 3/3/2018.
 */
@Dao
public interface PlayerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertPlayer(Player player);

    @Update
    int updatePlayer(Player player);

    @Delete
    void deletePlayer(Player player);

    @Query("SELECT * FROM Player ORDER BY fullName")
    List<Player> loadAllPlayers();

    @Query("SELECT * FROM Player WHERE playerId = :id")
    Player loadPlayerById(int id);
}
