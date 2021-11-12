package com.pmkisanyojanaadmin.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.pmkisanyojanaadmin.R;
import com.pmkisanyojanaadmin.adapter.NewsAdapter;
import com.pmkisanyojanaadmin.adapter.YojanaAdapter;
import com.pmkisanyojanaadmin.databinding.ActivityShowItemBinding;
import com.pmkisanyojanaadmin.model.NewsModel;
import com.pmkisanyojanaadmin.model.PageViewModel;
import com.pmkisanyojanaadmin.model.YojanaModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ShowItemActivity extends AppCompatActivity implements YojanaAdapter.YojanaInterface, NewsAdapter.NewsInterface {

    SwipeRefreshLayout swipeRefreshLayout;
    List<YojanaModel> yojanaModels = new ArrayList<>();
    List<NewsModel> newsModels = new ArrayList<>();
    YojanaAdapter yojanaAdapter;
    PageViewModel pageViewModel;
    RecyclerView recyclerView;
    TextView title;
    MaterialAlertDialogBuilder builder;
    ImageView backIcon;
    NewsAdapter newsAdapter;
    Dialog loadingDialog;
    ActivityShowItemBinding binding;
    String s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShowItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        s = getIntent().getStringExtra("title");

        Objects.requireNonNull(getSupportActionBar()).setTitle(s);

        pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);
        builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("What you want to do?")
                .setMessage("Edit or Delete")
                .setNegativeButton("CANCEL", (dialog1, which) -> {

                });

        //****Loading Dialog****/
        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading);
        loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.item_bg));
        loadingDialog.setCancelable(false);
        //**Loading Dialog****/
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        recyclerView = findViewById(R.id.edit_delete_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        if (s.equals("Yojana")) {
            loadingDialog.show();
            yojanaAdapter = new YojanaAdapter(this, "Yojana", this);
            recyclerView.setAdapter(yojanaAdapter);
            fetchYojana();
            swipeRefreshLayout.setOnRefreshListener(() -> {
                fetchYojana();
                swipeRefreshLayout.setRefreshing(false);
            });

        } else if (s.equals("News")) {
            loadingDialog.show();
            newsAdapter = new NewsAdapter(this, this);
            recyclerView.setAdapter(newsAdapter);
            fetchNews();
            swipeRefreshLayout.setOnRefreshListener(() -> {
                fetchNews();
                swipeRefreshLayout.setRefreshing(false);
            });

        } else if (s.equals("Others")) {

            fetchOthers();
            swipeRefreshLayout.setOnRefreshListener(() -> {
                fetchOthers();
                swipeRefreshLayout.setRefreshing(false);
            });
        }


    }

    private void fetchOthers() {
        loadingDialog.show();
        yojanaAdapter = new YojanaAdapter(this, "others", yojanaModel -> {
            builder.setNeutralButton("Edit", (dialog, which) -> {
                Intent intent = new Intent(getApplicationContext(), EditActivity.class);
                intent.putExtra("intentId", "others");
                intent.putExtra("id", yojanaModel.getId());
                intent.putExtra("image", "https://gedgetsworld.in/PM_Kisan_Yojana/Kisan_Yojana_Images/" + yojanaModel.getImage());
                intent.putExtra("image2", yojanaModel.getImage());
                intent.putExtra("title", yojanaModel.getTitle());
                intent.putExtra("link", yojanaModel.getUrl());
                startActivity(intent);
            });
            builder.show();

        });
        recyclerView.setAdapter(yojanaAdapter);
        pageViewModel.getAllOthers().observe(this, yojanaModel -> {
            if (yojanaModel != null) {
                yojanaModels.clear();
                yojanaModels.addAll(yojanaModel.getData());
                yojanaAdapter.updateYojanaList(yojanaModels);
            }
            loadingDialog.dismiss();
        });

    }

    private void fetchNews() {
        pageViewModel.getNews().observe(this, newsModelList -> {

            if (newsModelList != null) {
                newsModels.clear();
                newsModels.addAll(newsModelList.getData());
                newsAdapter.updateNewsList(newsModels);
            }
            loadingDialog.dismiss();
        });
    }

    private void fetchYojana() {
        pageViewModel.geYojanaList().observe(this, yojanaModel -> {

            if (yojanaModel != null) {
                yojanaModels.clear();
                yojanaModels.addAll(yojanaModel.getData());
                yojanaAdapter.updateYojanaList(yojanaModels);
            }
            loadingDialog.dismiss();
        });
    }

    @Override
    public void onItemClicked(YojanaModel yojanaModel) {
        builder.setPositiveButton("Delete", (dialog, which) -> {
            Toast.makeText(this, "Yojana", Toast.LENGTH_SHORT).show();
        });
        builder.setNeutralButton("Edit", (dialog, which) -> {
            Intent intent = new Intent(getApplicationContext(), EditActivity.class);
            intent.putExtra("intentId", "yojana");
            intent.putExtra("id", yojanaModel.getId());
            intent.putExtra("image", "https://gedgetsworld.in/PM_Kisan_Yojana/Kisan_Yojana_Images/" + yojanaModel.getImage());
            intent.putExtra("image2", yojanaModel.getImage());
            intent.putExtra("title", yojanaModel.getTitle());
            intent.putExtra("link", yojanaModel.getUrl());
            startActivity(intent);
        });
        builder.show();
    }

    @Override
    public void onItemClicked(NewsModel newsModel) {
        builder.setPositiveButton("Delete", (dialog, which) -> {
            Toast.makeText(this, "News", Toast.LENGTH_SHORT).show();

        });

        builder.setNeutralButton("Edit", (dialog, which) -> {
            Intent intent = new Intent(getApplicationContext(), EditActivity.class);
            intent.putExtra("intentId", "news");
            intent.putExtra("id", newsModel.getId());
            intent.putExtra("image", "https://gedgetsworld.in/PM_Kisan_Yojana/News_Images/" + newsModel.getImage());
            intent.putExtra("image2", newsModel.getImage());
            intent.putExtra("title", newsModel.getTitle());
            startActivity(intent);
        });
        builder.show();
    }
}