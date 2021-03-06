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

    @Query("SELECT * " +
            "FROM Player " +
            "WHERE playerMode = :mode " +
            "AND playerId not in (select fk_playerId from cupplayer where fk_cupId = :cupId) ORDER BY fullName")
    List<Player> loadPlayersListForCup(int cupId, int mode);

    @Query("SELECT * FROM Player where playerId IN (select fk_playerId from cupplayer where fk_cupId = :cupId) ORDER BY fullName")
    List<Player> loadCupPlayersList(int cupId);

    @Query("SELECT * FROM Player WHERE playerId = :id")
    Player loadPlayerById(int id);

    @Query("SELECT * FROM Player WHERE fullName = :playerName")
    Player isPlayerExist(String playerName);

    @Query("SELECT * FROM Player WHERE fullName = :playerName AND playerId != :id")
    Player isPlayerExist(String playerName, int id);
}
