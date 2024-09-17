package com.example.makerablenewsapp.Fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.makerablenewsapp.Models.Article;
import com.example.makerablenewsapp.R;
import com.example.makerablenewsapp.roomdb.ArticleDatabase;

public class Readmore extends Fragment {
    private TextView titleTextView, authorTextView, publishedAtTextView, contentTextView;
    private ImageView image,save,back;
    private Article article;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_readmore, container, false);

        titleTextView = view.findViewById(R.id.title);
        authorTextView = view.findViewById(R.id.author);
        publishedAtTextView = view.findViewById(R.id.publishedAt);
        contentTextView = view.findViewById(R.id.description);
        image = view.findViewById(R.id.image);
        save = view.findViewById(R.id.save);
        back = view.findViewById(R.id.back_btn);

        back.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        if (getArguments() != null) {
            article = getArguments().getParcelable("article");
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(() -> {
                    ArticleDatabase db = ArticleDatabase.getInstance(getContext());
                boolean isCurrentlySaved = article.isSaved();
                article.setSaved(!isCurrentlySaved); // Toggle the saved status
                if (isCurrentlySaved) {
                    db.articleDao().delete(article); // Remove from database if already saved
                    ((AppCompatActivity) getContext()).runOnUiThread(() ->
                            Toast.makeText(getContext(), "Article removed from bookmarks", Toast.LENGTH_SHORT).show());
                } else {
                    db.articleDao().insert(article); // Insert into database if not saved
                    ((AppCompatActivity) getContext()).runOnUiThread(() ->
                            Toast.makeText(getContext(), "Article saved", Toast.LENGTH_SHORT).show());
                }

                ((AppCompatActivity) getContext()).runOnUiThread(() -> {
                    if (article.isSaved()) {
                        save.setImageResource(R.drawable.full_save);
                    } else {
                        save.setImageResource(R.drawable.save);
                    }
                });
                }).start();
            }
        });

        // Get the article details passed as arguments
        if (getArguments() != null) {
            // Set the article details to the views
            titleTextView.setText(article.getTitle());
            authorTextView.setText(article.getAuthor());
            publishedAtTextView.setText(article.getPublishedAt());
            contentTextView.setText(article.getContent());
            Glide.with(this)
                    .load(article.getUrlToImage())
                    .into(image);
            if (article.isSaved()) {
                save.setImageResource(R.drawable.full_save);
            } else {
                save.setImageResource(R.drawable.save);
            }
        }

        return view;
    }
}