package com.wordpress.hossamhassan47.bingbongcup.dao;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.wordpress.hossamhassan47.bingbongcup.entities.Cup;
import com.wordpress.hossamhassan47.bingbongcup.entities.CupPlayer;
import com.wordpress.hossamhassan47.bingbongcup.entities.CupRound;
import com.wordpress.hossamhassan47.bingbongcup.entities.MatchGame;
import com.wordpress.hossamhassan47.bingbongcup.entities.Player;
import com.wordpress.hossamhassan47.bingbongcup.entities.RoundMatch;

/**
 * Created by Hossam on 3/3/2018.
 */
@Database(entities = {Player.class, Cup.class, CupPlayer.class, CupRound.class, RoundMatch.class, MatchGame.class},
        version = 5)
public abstract class AppDatabase extends RoomDatabase{
    public abstract PlayerDao playerDao();
    public abstract CupDao cupDao();
    public abstract CupPlayerDao cupPlayerDao();
    public abstract CupRoundDao cupRoundDao();
    public abstract RoundMatchDao roundMatchDao();
    public abstract MatchGameDao matchGameDao();

    private static AppDatabase INSTANCE;

    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "ping-pong-cup-database")
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
