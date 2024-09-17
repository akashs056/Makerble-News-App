package com.example.makerablenewsapp.roomdb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.makerablenewsapp.Models.Article;

import java.util.List;

@Dao
public interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Article article);

    @Delete
    void delete(Article article);


    @Query("SELECT * FROM ARTICLES")
    List<Article> getAllArticles();
}
