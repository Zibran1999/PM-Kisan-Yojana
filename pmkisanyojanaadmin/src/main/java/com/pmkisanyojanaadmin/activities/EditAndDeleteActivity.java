package com.pmkisanyojanaadmin.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.pmkisanyojanaadmin.R;
import com.pmkisanyojanaadmin.databinding.ActivityEditAndDeleteBinding;
import com.pmkisanyojanaadmin.model.PageViewModel;

public class EditAndDeleteActivity extends AppCompatActivity {

    Button editAndDeleteYojana, editAndDeleteNews, editQuizBtn;
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
            Intent intent = new Intent(getApplicationContext(), ShowItemActivity.class);
            intent.putExtra("title", "Yojana");
            startActivity(intent);
        });
        editAndDeleteNews.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ShowItemActivity.class);
            intent.putExtra("title", "News");
            startActivity(intent);
        });
        binding.editOthers.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ShowItemActivity.class);
            intent.putExtra("title", "Others");
            startActivity(intent);
        });
        binding.editQuizQuestions.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ShowItemActivity.class);
            intent.putExtra("title", "Quiz");
            startActivity(intent);
        });
    }


}