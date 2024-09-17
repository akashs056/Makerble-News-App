package com.example.makerablenewsapp.api;

import com.example.makerablenewsapp.Models.NewsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIService {
    @GET("v2/everything")
    Call<NewsResponse> getArticles(@Query("q") String query,
                                   @Query("from") String from,
                                   @Query("sortBy") String sortBy,
                                   @Query("apiKey") String apiKey);
}