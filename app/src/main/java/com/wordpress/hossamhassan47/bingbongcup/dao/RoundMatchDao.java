package com.wordpress.hossamhassan47.bingbongcup.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.wordpress.hossamhassan47.bingbongcup.entities.CupPlayerDetails;
import com.wordpress.hossamhassan47.bingbongcup.entities.RoundMatch;
import com.wordpress.hossamhassan47.bingbongcup.entities.RoundMatchDetails;

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

    @Query("SELECT RoundMatch.* " +
            "FROM RoundMatch " +
            "INNER JOIN CupRound ON RoundMatch.fk_roundId = CupRound.cupRoundId " +
            "WHERE RoundMatch.parentRoundNo = 0 " +
            "AND RoundMatch.matchNo = :matchNo AND CupRound.fk_cupId = :cupId")
    RoundMatch loadRootRoundMatchByMatchNo(int cupId, int matchNo);

    @Query("SELECT RoundMatch.*, " +
            "Cup.cupName, " +
            "Cup.cupId, " +
            "CupRound.roundNo, " +

            "player1.fullName as player1Name, " +
            "player2.fullName as player2Name, " +
            "player1.imageSrc as player1ImageSrc, " +
            "player2.imageSrc as player2ImageSrc, " +
            "player1.email as player1Email, " +
            "player2.email as player2Email " +
            "FROM RoundMatch " +
            "INNER JOIN CupRound on  CupRound.cupRoundId = RoundMatch.fk_roundId " +
            "INNER JOIN Cup on  Cup.cupId = CupRound.fk_cupId " +
            "LEFT OUTER JOIN Player as player1 ON RoundMatch.player1Id = player1.playerId " +
            "LEFT OUTER JOIN Player as player2 ON RoundMatch.player2Id = player2.playerId " +
            "WHERE RoundMatch.fk_roundId = :cupRoundId")
    List<RoundMatchDetails> loadRoundMatchesById(int cupRoundId);

    @Query("SELECT RoundMatch.*, " +
            "0 AS cupId, " +
            "0 AS roundNo, "+

            "player1.imageSrc as player1ImageSrc, " +
            "player2.imageSrc as player2ImageSrc, " +

            "player1.fullName as player1Name, " +
            "player2.fullName as player2Name, " +
            "player1.email as player1Email, " +
            "player2.email as player2Email " +
            "FROM RoundMatch " +
            "INNER JOIN Player as player1 ON RoundMatch.player1Id = player1.playerId " +
            "INNER JOIN Player as player2 ON RoundMatch.player2Id = player2.playerId " +
            "WHERE RoundMatch.fk_roundId = 0 ORDER BY RoundMatch.roundMatchId DESC")
    List<RoundMatchDetails> loadFriendlyMatches();


    @Query("SELECT RoundMatch.* " +
            "FROM RoundMatch " +
            "INNER JOIN CupRound on  CupRound.cupRoundId = RoundMatch.fk_roundId " +
            "WHERE CupRound.roundNo <> 2 " +
            "AND (parentRoundMatchNo1= :matchNo OR parentRoundMatchNo2= :matchNo) " +
            "AND parentRoundNo = :roundNo " +
            "AND CupRound.fk_cupId = :cupId")
    RoundMatch loadNextRoundMatch(int cupId, int roundNo, int matchNo);

    @Query("SELECT RoundMatch.* " +
            "FROM RoundMatch " +
            "INNER JOIN CupRound on  CupRound.cupRoundId = RoundMatch.fk_roundId " +
            "WHERE CupRound.roundNo = 2 " +
            "AND (parentRoundMatchNo1= :matchNo OR parentRoundMatchNo2= :matchNo) " +
            "AND parentRoundNo = :roundNo " +
            "AND CupRound.fk_cupId = :cupId")
    RoundMatch load3rdRoundMatch(int cupId, int roundNo, int matchNo);
}
