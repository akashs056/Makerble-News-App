package com.example.makerablenewsapp.Adapter;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.makerablenewsapp.Models.Article;
import com.example.makerablenewsapp.R;
import com.example.makerablenewsapp.Fragments.Readmore;
import com.example.makerablenewsapp.roomdb.ArticleDatabase;

import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {
    private final Context context;
    private final List<Article> articleList;

    public ArticleAdapter(Context context, List<Article> articleList) {
        this.context = context;
        this.articleList = articleList;
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.news_card, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        Article article = articleList.get(position);
        holder.titleTextView.setText(article.getTitle());
        holder.description.setText(article.getDescription());
        holder.source.setText(article.getSource().getName());
        Glide.with(context).load(article.getUrlToImage()).into(holder.imageView);

        holder.readmore.setOnClickListener(v -> {
            Readmore readMoreFragment = new Readmore();

            Bundle args = new Bundle();
            args.putParcelable("article", article);
            readMoreFragment.setArguments(args);


            // Replace fragment (Assuming context is an Activity)
            FragmentTransaction transaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, readMoreFragment);
            transaction.addToBackStack("readmore_fragment"); // Add to back stack to handle back navigation
            transaction.commit();
        });
        holder.save.setOnClickListener(v -> {
            ArticleDatabase db = ArticleDatabase.getInstance(context);
            new Thread(() -> {
                boolean isCurrentlySaved = article.isSaved();
                article.setSaved(!isCurrentlySaved); // Toggle the saved status

                if (isCurrentlySaved) {
                    db.articleDao().delete(article); // Remove from database if already saved
                    ((AppCompatActivity) context).runOnUiThread(() ->
                            Toast.makeText(context, "Article removed from bookmarks", Toast.LENGTH_SHORT).show());
                } else {
                    db.articleDao().insert(article); // Insert into database if not saved
                    ((AppCompatActivity) context).runOnUiThread(() ->
                            Toast.makeText(context, "Article saved", Toast.LENGTH_SHORT).show());
                }

                // Update the button icon to reflect the new saved status
                ((AppCompatActivity) context).runOnUiThread(() -> {
                    if (article.isSaved()) {
                        holder.save.setImageResource(R.drawable.full_save);
                    } else {
                        holder.save.setImageResource(R.drawable.save);
                    }
                });
                // You might want to display a Toast on the UI thread

            }).start();
        });
        ((AppCompatActivity) context).runOnUiThread(() -> {
            if (article.isSaved()) {
                holder.save.setImageResource(R.drawable.full_save);
            } else {
                holder.save.setImageResource(R.drawable.save);
            }
        });
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    public static class ArticleViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView description;
        TextView source;
        ImageView imageView;
        Button readmore;
        ImageView save;

        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.title);
            imageView = itemView.findViewById(R.id.image);
            description = itemView.findViewById(R.id.description);
            source = itemView.findViewById(R.id.source);
            readmore = itemView.findViewById(R.id.read_more);
            save = itemView.findViewById(R.id.save);
        }
    }
}
