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

import com.example.makerablenewsapp.Adapter.ArticleAdapter;
import com.example.makerablenewsapp.Models.Article;
import com.example.makerablenewsapp.R;
import com.example.makerablenewsapp.roomdb.ArticleDatabase;

import java.util.ArrayList;
import java.util.List;

public class BookMarks extends Fragment {
    private RecyclerView recyclerView;
    private ArticleAdapter articleAdapter;
    private ArticleDatabase articleDatabase;
    List<Article> articles = new ArrayList<>(); // Use ArrayList instead of emptyList()
    TextView text;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_book_marks, container, false);
        recyclerView = view.findViewById(R.id.bookmark_rv);
        text = view.findViewById(R.id.text);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        articleDatabase = ArticleDatabase.getInstance(getContext());

        new Thread(() -> {
            // Fetch articles from the database
            articles = articleDatabase.articleDao().getAllArticles();
            // Update the UI on the main thread

            getActivity().runOnUiThread(() -> {
                if (articleAdapter == null) {
                    articleAdapter = new ArticleAdapter(getContext(), articles);
                    recyclerView.setAdapter(articleAdapter);
                    articleAdapter.notifyDataSetChanged();
                } else {
                    articleAdapter.notifyDataSetChanged();
                }

                if (articles.isEmpty()) {
                    text.setVisibility(View.VISIBLE);
                } else {
                    text.setVisibility(View.GONE);
                }
            });
        }).start();

        return view;
    }


}