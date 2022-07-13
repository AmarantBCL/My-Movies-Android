package com.example.android.mymovies.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.android.mymovies.data.FavoriteMovie;
import com.example.android.mymovies.data.Movie;

@Database(entities = {Movie.class, FavoriteMovie.class}, version = 3)
public abstract class MovieDatabase extends RoomDatabase {
    public static final String DB_NAME = "movies.db";
    private static MovieDatabase database;
    private static final Object LOCK = new Object();

    public static MovieDatabase getInstance(Context context) {
        synchronized (LOCK) {
            if (database == null) {
                database = Room.databaseBuilder(context, MovieDatabase.class, DB_NAME)
                        .build();
            }
        }
        return database;
    }

    public abstract MovieDAO movieDAO();
}
