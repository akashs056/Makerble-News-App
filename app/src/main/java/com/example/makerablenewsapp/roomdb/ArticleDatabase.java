package com.example.makerablenewsapp.roomdb;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import android.content.Context;

import com.example.makerablenewsapp.Models.Article;
import com.example.makerablenewsapp.utils.Converters;

@Database(entities = {Article.class}, version = 1,exportSchema = false)
@TypeConverters(Converters.class) // Add this line
public abstract class ArticleDatabase extends RoomDatabase {
    private static ArticleDatabase instance;
    public abstract ArticleDao articleDao();

    public static synchronized ArticleDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            ArticleDatabase.class, "article_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
