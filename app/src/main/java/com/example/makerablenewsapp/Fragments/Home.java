package com.example.makerablenewsapp.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.makerablenewsapp.Adapter.ArticleAdapter;
import com.example.makerablenewsapp.Models.Article;
import com.example.makerablenewsapp.Models.NewsResponse;
import com.example.makerablenewsapp.R;
import com.example.makerablenewsapp.api.APIService;
import com.example.makerablenewsapp.api.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class Home extends Fragment {
    private RecyclerView recyclerView;
    private ArticleAdapter articleAdapter;
    TextView text;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        text = view.findViewById(R.id.text);

        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        Call<NewsResponse> call = apiService.getArticles("ALL", "2024-08-17", "publishedAt", "1a494bad36fb46c8975516c364b1a6db");

        call.enqueue(new Callback<NewsResponse>() {
            public void onResponse(Call<NewsResponse> call, retrofit2.Response<NewsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Article> articles = response.body().getArticles();
                    articleAdapter = new ArticleAdapter(getContext(), articles);
                    recyclerView.setAdapter(articleAdapter);
                    text.setVisibility(View.GONE);
                } else {
                    Toast.makeText(getContext(), "Failed to retrieve data", Toast.LENGTH_SHORT).show();
                    text.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                text.setVisibility(View.VISIBLE);
            }
        });
        return view;
    }
}