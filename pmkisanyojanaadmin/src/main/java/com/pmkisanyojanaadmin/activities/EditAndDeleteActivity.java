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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.pmkisanyojanaadmin.R;
import com.pmkisanyojanaadmin.adapter.NewsAdapter;
import com.pmkisanyojanaadmin.adapter.YojanaAdapter;
import com.pmkisanyojanaadmin.databinding.ActivityEditAndDeleteBinding;
import com.pmkisanyojanaadmin.model.NewsModel;
import com.pmkisanyojanaadmin.model.PageViewModel;
import com.pmkisanyojanaadmin.model.YojanaModel;

import java.util.ArrayList;
import java.util.List;

public class EditAndDeleteActivity extends AppCompatActivity  {

    Button editAndDeleteYojana, editAndDeleteNews;
    Dialog editDeleteDialog;


    ActivityEditAndDeleteBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditAndDeleteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        editAndDeleteYojana = findViewById(R.id.edit_yojana);
        editAndDeleteNews = findViewById(R.id.edit_news);




        editAndDeleteYojana.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(),ShowItemActivity.class);
            intent.putExtra("title","Yojana");
            startActivity(intent);
        });
        editAndDeleteNews.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(),ShowItemActivity.class);
            intent.putExtra("title","News");
            startActivity(intent);
        });
        binding.editOthers.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(),ShowItemActivity.class);
            intent.putExtra("title","Others");
            startActivity(intent);
        });
    }





}