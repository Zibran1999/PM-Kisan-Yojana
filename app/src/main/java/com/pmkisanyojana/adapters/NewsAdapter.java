package com.pmkisanyojana.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pmkisanyojana.R;
import com.pmkisanyojana.models.NewsModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    Context context;
    NewsInterface newsInterface;
    private final List<NewsModel> newsModelList = new ArrayList<>();

    public NewsAdapter(Context context, NewsInterface newsInterface) {
        this.context = context;
        this.newsInterface = newsInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.yojna_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.newsTitle.setText(newsModelList.get(position).getTitle());
        Glide.with(context).load(newsModelList.get(position).getImage()).into(holder.newsImage);
        holder.itemView.setOnClickListener(v -> newsInterface.onItemClicked(newsModelList.get(position)));

    }

    @Override
    public int getItemCount() {
        return newsModelList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateNewsList(List<NewsModel> newsModels) {
        newsModelList.clear();
        newsModelList.addAll(newsModels);
        Collections.sort(newsModelList,NewsModel.newsModelComparator);
        notifyDataSetChanged();
    }

    public interface NewsInterface {

        void onItemClicked(NewsModel newsModel);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView newsTitle;
        ImageView newsImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            newsTitle = itemView.findViewById(R.id.yojanaTitle);
            newsImage = itemView.findViewById(R.id.yojnaImage);
        }
    }
}
