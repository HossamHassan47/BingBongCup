package com.wordpress.hossamhassan47.bingbongcup.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.wordpress.hossamhassan47.bingbongcup.entities.MatchGame;
import com.wordpress.hossamhassan47.bingbongcup.entities.MatchGameDetails;
import com.wordpress.hossamhassan47.bingbongcup.entities.RoundMatchDetails;

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

    @Query("SELECT * FROM MatchGame WHERE fk_roundMatchId = :roundMatchId ORDER BY gameNo")
    List<MatchGame> loadAllMatchGames(int roundMatchId);

    @Query("SELECT * FROM MatchGame WHERE matchGameId = :id")
    MatchGame loadMatchGameById(int id);

    @Query("SELECT MatchGame.*, " +
            "Cup.cupName, " +
            "CupRound.roundNo, " +
            "player1.playerId as matchPlayer1Id, " +
            "player1.fullName as player1Name, " +
            "player2.playerId as matchPlayer2Id, " +
            "player2.fullName as player2Name " +
            "FROM MatchGame " +
            "INNER JOIN RoundMatch ON RoundMatch.roundMatchId = MatchGame.fk_roundMatchId " +
            "INNER JOIN CupRound on  CupRound.cupRoundId = RoundMatch.fk_roundId " +
            "INNER JOIN Cup on  Cup.cupId = CupRound.fk_cupId " +
            "INNER JOIN Player as player1 ON RoundMatch.player1Id = player1.playerId " +
            "INNER JOIN Player as player2 ON RoundMatch.player2Id = player2.playerId " +
            "WHERE MatchGame.fk_roundMatchId = :roundMatchId")
    List<MatchGameDetails> loadMatchGameDetailsByRoundMatchId(int roundMatchId);

    @Query("SELECT MatchGame.*, " +
            "Cup.cupName, " +
            "CupRound.roundNo, " +
            "player1.playerId as matchPlayer1Id, " +
            "player1.fullName as player1Name, " +
            "player2.playerId as matchPlayer2Id, " +
            "player2.fullName as player2Name " +
            "FROM MatchGame " +
            "INNER JOIN RoundMatch ON RoundMatch.roundMatchId = MatchGame.fk_roundMatchId " +
            "INNER JOIN CupRound on  CupRound.cupRoundId = RoundMatch.fk_roundId " +
            "INNER JOIN Cup on  Cup.cupId = CupRound.fk_cupId " +
            "INNER JOIN Player as player1 ON RoundMatch.player1Id = player1.playerId " +
            "INNER JOIN Player as player2 ON RoundMatch.player2Id = player2.playerId " +
            "WHERE MatchGame.matchGameId = :matchGameId")
    MatchGameDetails loadMatchGameDetailsById(int matchGameId);
}
