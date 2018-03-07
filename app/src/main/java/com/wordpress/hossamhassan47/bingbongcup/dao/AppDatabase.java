package com.wordpress.hossamhassan47.bingbongcup.dao;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.wordpress.hossamhassan47.bingbongcup.entities.Player;

/**
 * Created by Hossam on 3/3/2018.
 */
@Database(entities = {Player.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase{
    public abstract PlayerDao playerDao();
    private static AppDatabase INSTANCE;

    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "user-database")
                            // allow queries on the main thread.
                            // Don't do this on a real app! See PersistenceBasicSample for an example.
                            .allowMainThreadQueries()
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
