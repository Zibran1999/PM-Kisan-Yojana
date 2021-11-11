package com.pmkisanyojanaadmin.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.pmkisanyojanaadmin.R;
import com.pmkisanyojanaadmin.adapter.NewsAdapter;
import com.pmkisanyojanaadmin.adapter.YojanaAdapter;
import com.pmkisanyojanaadmin.model.NewsModel;
import com.pmkisanyojanaadmin.model.PageViewModel;
import com.pmkisanyojanaadmin.model.YojanaModel;

public class EditAndDeleteActivity extends AppCompatActivity implements YojanaAdapter.YojanaInterface, NewsAdapter.NewsInterface {
    Button editAndDeleteYojana, editAndDeleteNews;
    Dialog editDeleteDialog;
    YojanaAdapter yojanaAdapter;
    PageViewModel pageViewModel;
    RecyclerView recyclerView;
    TextView title;
    MaterialAlertDialogBuilder builder;
    ImageView backIcon;
    NewsAdapter newsAdapter;
    Dialog loadingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_and_delete);
        editAndDeleteYojana = findViewById(R.id.edit_yojana);
        editAndDeleteNews = findViewById(R.id.edit_news);
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
        editAndDeleteYojana.setOnClickListener(v -> {
            showDialog(this, "Yojana");
        });
        editAndDeleteNews.setOnClickListener(v -> {
            showDialog(this, "News");
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void showDialog(Context context, String s) {
        editDeleteDialog = new Dialog(context);
        editDeleteDialog.setContentView(R.layout.edit_and_delete_dialog);
        editDeleteDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        editDeleteDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.item_bg));
        editDeleteDialog.setCancelable(false);
        editDeleteDialog.show();
        title = editDeleteDialog.findViewById(R.id.dialog_title);
        backIcon = editDeleteDialog.findViewById(R.id.back_icon);
        backIcon.setOnClickListener(v -> {
            editDeleteDialog.dismiss();
        });
        recyclerView = editDeleteDialog.findViewById(R.id.edit_delete_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        title.setText(s);

        if (s.equals("Yojana")) {
            loadingDialog.show();
            yojanaAdapter = new YojanaAdapter(context, this);
            recyclerView.setAdapter(yojanaAdapter);
            pageViewModel.geYojanaList().observe(this, yojanaModel -> {

                if (yojanaModel != null) {
                    yojanaAdapter.updateYojanaList(yojanaModel.getData());
                }
                loadingDialog.dismiss();
            });
        } else {
            loadingDialog.show();
            newsAdapter = new NewsAdapter(context, this);
            recyclerView.setAdapter(newsAdapter);
            pageViewModel.getNews().observe(this, newsModelList -> {

                if (newsModelList != null) {
                    newsAdapter.updateNewsList(newsModelList.getData());
                }
                loadingDialog.dismiss();
            });
        }
    }
    @Override
    public void onItemClicked(YojanaModel yojanaModel) {
        builder.setPositiveButton("Delete", (dialog, which) -> {
            Toast.makeText(EditAndDeleteActivity.this, "Yojana", Toast.LENGTH_SHORT).show();
        });
        builder.setNeutralButton("Edit", (dialog, which) -> {
            Intent intent = new Intent(getApplicationContext(),EditActivity.class);
            intent.putExtra("intentId","yojana");
            intent.putExtra("id",yojanaModel.getId());
            intent.putExtra("image","https://gedgetsworld.in/PM_Kisan_Yojana/Kisan_Yojana_Images/"+yojanaModel.getImage());
            intent.putExtra("image2",yojanaModel.getImage());
            intent.putExtra("title",yojanaModel.getTitle());
            intent.putExtra("link",yojanaModel.getUrl());
            startActivity(intent);
        });
        builder.show();
    }

    @Override
    public void onItemClicked(NewsModel newsModel) {
        builder.setPositiveButton("Delete", (dialog, which) -> {
            Toast.makeText(EditAndDeleteActivity.this, "News", Toast.LENGTH_SHORT).show();

        });

        builder.setNeutralButton("Edit", (dialog, which) -> {
            Intent intent = new Intent(getApplicationContext(),EditActivity.class);
            intent.putExtra("intentId","news");
            intent.putExtra("id",newsModel.getId());
            intent.putExtra("image","https://gedgetsworld.in/PM_Kisan_Yojana/News_Images/"+newsModel.getImage());
            intent.putExtra("image2",newsModel.getImage());
            intent.putExtra("title",newsModel.getTitle());
            startActivity(intent);
        });
        builder.show();
    }
}