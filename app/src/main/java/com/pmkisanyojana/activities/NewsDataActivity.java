package com.pmkisanyojana.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.pmkisanyojana.R;
import com.pmkisanyojana.activities.ui.main.PageViewModel;
import com.pmkisanyojana.databinding.ActivityNewsDataBinding;
import com.pmkisanyojana.models.ModelFactory;
import com.pmkisanyojana.models.NewsPreveiwModel;
import com.pmkisanyojana.utils.CommonMethod;

import java.util.HashMap;
import java.util.Map;

public class NewsDataActivity extends AppCompatActivity {
    ImageView newsImg, backIcon;
    TextView newsTitle, newsDesc;
    String id, img, desc, title;
    ActivityNewsDataBinding binding;
    PageViewModel pageViewModel;
    Map<String, String> map = new HashMap<>();
    LottieAnimationView lottieAnimationView;
    Dialog dialog;
    String finalEnglishString, finalHindiString;

    @SuppressLint("NonConstantResourceId")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        binding = ActivityNewsDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        newsImg = binding.newsImg;
        newsTitle = binding.newsTitle;
        newsDesc = binding.newsDesc;
        backIcon = binding.backIcon;
        lottieAnimationView = binding.lottieHome;
        id = getIntent().getStringExtra("id");
        dialog = CommonMethod.getDialog(this);
        dialog.show();

        map.put("newsId", id);
        MaterialButtonToggleGroup materialButtonToggleGroup = binding.materialButtonToggleGroup;
        materialButtonToggleGroup.setVisibility(View.GONE);
        Button hindi, english;
        hindi = binding.hindiPreview;
        english = binding.englishPreview;
        backIcon.setOnClickListener(v -> onBackPressed());
        pageViewModel = new ViewModelProvider(this, new ModelFactory(this.getApplication(), map)).get(PageViewModel.class);
        pageViewModel.getNewsPreviewData().observe(this, newsPreviewModelList -> {
            dialog.show();
            if (!newsPreviewModelList.getData().isEmpty()) {
                String hindiString = null;
                String englishString = null;
                for (NewsPreveiwModel m : newsPreviewModelList.getData()) {
                    if (m.getNewsId().equals(id)) {
                        lottieAnimationView.setVisibility(View.GONE);
                        hindi.setBackgroundColor(Color.parseColor("#0C61F1"));
                        hindi.setTextColor(Color.WHITE);
                        String replaceString = m.getDesc().replaceAll("<.*?>", "");
                        String removeNumeric = replaceString.replaceAll("[0-9]", "");
                        Log.d("both data", removeNumeric.trim());

                        for (char c : removeNumeric.trim().toCharArray()) {
                            if (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.DEVANAGARI) {
                                hindiString = m.getDesc();
                                break;
                            } else {
                                if (englishString == null) {
                                    englishString = m.getDesc();
                                    materialButtonToggleGroup.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }
                }
                finalEnglishString = englishString;
                finalHindiString = hindiString;

                if (finalHindiString != null || finalEnglishString != null) {
                    lottieAnimationView.setVisibility(View.GONE);
                    newsDesc.setVisibility(View.VISIBLE);
                    newsTitle.setVisibility(View.VISIBLE);
                    newsImg.setVisibility(View.VISIBLE);
                    title = getIntent().getStringExtra("title");
                    img = getIntent().getStringExtra("img");
                    Glide.with(this).load("https://gedgetsworld.in/PM_Kisan_Yojana/News_Images/" + img).into(newsImg);
                    newsTitle.setText(title);
                } else {
                    newsImg.setVisibility(View.GONE);
                    newsTitle.setVisibility(View.GONE);
                    newsDesc.setVisibility(View.GONE);
                }

                if (finalHindiString != null) {
                    newsDesc.setText(finalHindiString);
                } else {
                    newsDesc.setText(finalEnglishString);
                    materialButtonToggleGroup.setVisibility(View.GONE);
                }

                dialog.dismiss();
                english.setBackgroundColor(0);
                english.setTextColor(Color.BLACK);
                materialButtonToggleGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {

                    switch (checkedId) {
                        case R.id.hindiPreview:
                            english.setBackgroundColor(0);
                            english.setTextColor(Color.BLACK);
                            hindi.setBackgroundColor(Color.parseColor("#0C61F1"));
                            hindi.setTextColor(Color.WHITE);
                            newsDesc.setText(finalHindiString);
                            newsDesc.setVisibility(View.VISIBLE);
                            break;
                        case R.id.englishPreview:
                            english.setBackgroundColor(Color.parseColor("#0C61F1"));
                            english.setTextColor(Color.WHITE);
                            hindi.setBackgroundColor(0);
                            hindi.setTextColor(Color.BLACK);
                            newsDesc.setText(finalEnglishString);
                            newsDesc.setVisibility(View.VISIBLE);
                            break;
                        default:
                    }
                });

            } else {
                dialog.dismiss();
                newsImg.setVisibility(View.GONE);
                newsTitle.setVisibility(View.GONE);
                newsDesc.setVisibility(View.GONE);
                lottieAnimationView.setVisibility(View.VISIBLE);
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
        finish();
        overridePendingTransition(0, 0);
    }
}