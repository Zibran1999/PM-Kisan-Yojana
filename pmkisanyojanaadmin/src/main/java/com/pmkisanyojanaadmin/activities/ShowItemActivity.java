package com.pmkisanyojanaadmin.activities;

import static android.media.CamcorderProfile.get;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.pmkisanyojanaadmin.R;
import com.pmkisanyojanaadmin.adapter.NewsAdapter;
import com.pmkisanyojanaadmin.adapter.YojanaAdapter;
import com.pmkisanyojanaadmin.databinding.ActivityShowItemBinding;
import com.pmkisanyojanaadmin.model.ApiInterface;
import com.pmkisanyojanaadmin.model.ApiWebServices;
import com.pmkisanyojanaadmin.model.MessageModel;
import com.pmkisanyojanaadmin.model.NewsModel;
import com.pmkisanyojanaadmin.model.PageViewModel;
import com.pmkisanyojanaadmin.model.YojanaModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowItemActivity extends AppCompatActivity implements YojanaAdapter.YojanaInterface, NewsAdapter.NewsInterface {

    SwipeRefreshLayout swipeRefreshLayout;
    List<YojanaModel> yojanaModels = new ArrayList<>();
    List<NewsModel> newsModels = new ArrayList<>();
    YojanaAdapter yojanaAdapter;
    PageViewModel pageViewModel;
    RecyclerView recyclerView;
    ItemTouchHelper.SimpleCallback simpleCallback;
    TextView title;
    MaterialAlertDialogBuilder builder;
    ImageView backIcon;
    NewsAdapter newsAdapter;
    Dialog loadingDialog;
    ActivityShowItemBinding binding;
    String intentId,itemId,imgPath;
    ApiInterface apiInterface;
    Map<String,String> map = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShowItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        apiInterface = ApiWebServices.getApiInterface();
        intentId = getIntent().getStringExtra("title");

        Objects.requireNonNull(getSupportActionBar()).setTitle(intentId);

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

        simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }


            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Log.d("ffffffffff",newsModels.get(viewHolder.getAdapterPosition()).getImage());
                switch (intentId) {
                    case "News":
                        itemId = newsModels.get(viewHolder.getAdapterPosition()).getId();
                        imgPath = newsModels.get(viewHolder.getAdapterPosition()).getImage();
                        Log.d("ggggggggg",imgPath);
                        newsModels.remove(viewHolder.getAdapterPosition());
                        newsAdapter.updateNewsList(newsModels);

                        map.put("id", itemId);
                        map.put("title", "News");
                        map.put("img", "News_Images/"+imgPath);
                        deleteItem(map);

                        break;
                    case "Yojana":
                        itemId = yojanaModels.get(viewHolder.getAdapterPosition()).getId();
                        imgPath = yojanaModels.get(viewHolder.getAdapterPosition()).getImage();
                        yojanaModels.remove(viewHolder.getAdapterPosition());
                        yojanaAdapter.updateYojanaList(yojanaModels);

                        map.put("id", itemId);
                        map.put("title", "Yojana");
                        map.put("img", "Kisan_Yojana_Images/"+imgPath);
                        deleteItem(map);

                        break;
                    case "Others":
                        itemId = yojanaModels.get(viewHolder.getAdapterPosition()).getId();
                        imgPath = yojanaModels.get(viewHolder.getAdapterPosition()).getImage();
                        yojanaModels.remove(viewHolder.getAdapterPosition());
                        yojanaAdapter.updateYojanaList(yojanaModels);

                        map.put("id", itemId);
                        map.put("title", "Others");
                        map.put("img", "Kisan_Yojana_Images/"+imgPath);
                        deleteItem(map);

                        break;
                }
            }
        };
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);

        if (intentId.equals("Yojana")) {
            loadingDialog.show();
            yojanaAdapter = new YojanaAdapter(this, "Yojana", this);
            recyclerView.setAdapter(yojanaAdapter);
            fetchYojana();
            swipeRefreshLayout.setOnRefreshListener(() -> {
                fetchYojana();
                swipeRefreshLayout.setRefreshing(false);
            });

        } else if (intentId.equals("News")) {
            loadingDialog.show();
            newsAdapter = new NewsAdapter(this, this);
            recyclerView.setAdapter(newsAdapter);
            fetchNews();
            swipeRefreshLayout.setOnRefreshListener(() -> {
                fetchNews();
                swipeRefreshLayout.setRefreshing(false);
            });

        } else if (intentId.equals("Others")) {

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

    private void deleteItem(Map<String, String> map) {
        Call<MessageModel> call = apiInterface.deleteItems(map);
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(@NonNull Call<MessageModel> call, @NonNull Response<MessageModel> response) {
                assert response.body() != null;
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), response.body().getError(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MessageModel> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
                Log.d("onResponse", t.getMessage());
            }
        });
    }
}