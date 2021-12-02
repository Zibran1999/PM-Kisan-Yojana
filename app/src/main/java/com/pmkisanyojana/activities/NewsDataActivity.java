package com.pmkisanyojana.activities;

import static com.pmkisanyojana.utils.CommonMethod.mInterstitialAd;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.pmkisanyojana.activities.ui.main.PageViewModel;
import com.pmkisanyojana.databinding.ActivityNewsDataBinding;
import com.pmkisanyojana.models.ModelFactory;
import com.pmkisanyojana.models.PreviewModel;
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
//    String finalEnglishString, finalHindiString;

    /*ads variable*/
    AdView adView;
    AdRequest adRequest;
    /*ads variable*/

    @SuppressLint("NonConstantResourceId")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         binding = ActivityNewsDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        CommonMethod.interstitialAds(this);
        newsImg = binding.newsImg;
        newsTitle = binding.newsTitle;
        newsDesc = binding.newsDesc;
        backIcon = binding.backIcon;
        lottieAnimationView = binding.lottieHome;
        id = getIntent().getStringExtra("id");
        dialog = CommonMethod.getDialog(this);
        dialog.show();
        MobileAds.initialize(this);
        adRequest = new AdRequest.Builder().build();
        adView = binding.adViewNews;
        adView.loadAd(adRequest);
        adView.setVisibility(View.VISIBLE);

        new Handler().postDelayed(() -> {
            if (mInterstitialAd != null) {
                mInterstitialAd.show(this);
            } else {
                Log.d("TAG", "The interstitial ad wasn't ready yet.");
            }
        }, 2000);

        map.put("previewId", id);
//        MaterialButtonToggleGroup materialButtonToggleGroup = binding.materialButtonToggleGroup;
//        materialButtonToggleGroup.setVisibility(View.GONE);
//        Button hindi, english;
//        hindi = binding.hindiPreview;
//        english = binding.englishPreview;
        newsTitle.setVisibility(View.VISIBLE);
        newsImg.setVisibility(View.VISIBLE);
        newsDesc.setVisibility(View.VISIBLE);
        backIcon.setOnClickListener(v -> onBackPressed());
        pageViewModel = new ViewModelProvider(this, new ModelFactory(this.getApplication(), map)).get(PageViewModel.class);
        pageViewModel.getPreviewData().observe(this, previewModelList -> {
            dialog.show();
            if (!previewModelList.getData().isEmpty()) {
                dialog.dismiss();
                title = getIntent().getStringExtra("title");
                    img = getIntent().getStringExtra("img");
                Glide.with(this).load("https://gedgetsworld.in/PM_Kisan_Yojana/News_Images/" + img).into(newsImg);
                newsTitle.setText(title);
//                String hindiString = null;
//                String englishString = null;
                for (PreviewModel m : previewModelList.getData()) {
                    if (m.getPreviewId().equals(id)) {

                        newsDesc.setText(m.getDesc());
                            newsDesc.setVisibility(View.VISIBLE);


//                        lottieAnimationView.setVisibility(View.GONE);
//                        hindi.setBackgroundColor(Color.parseColor("#009637"));
//                        hindi.setTextColor(Color.WHITE);
//                        String replaceString = m.getDesc().replaceAll("<.*?>", "");
//                        String removeNumeric = replaceString.replaceAll("[0-9]", "");
//                        Log.d("both data", removeNumeric.trim());
//
//                        for (char c : removeNumeric.trim().toCharArray()) {
//                            if (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.DEVANAGARI) {
//                                hindiString = m.getDesc();
//                                break;
//                            } else {
//                                if (englishString == null) {
//                                    englishString = m.getDesc();
//                                    materialButtonToggleGroup.setVisibility(View.VISIBLE);
//                                }
//                            }
//                        }
                    }
                }
//                finalEnglishString = englishString;
//                finalHindiString = hindiString;
//
//                if (finalHindiString != null || finalEnglishString != null) {
//                    lottieAnimationView.setVisibility(View.GONE);
//                    newsDesc.setVisibility(View.VISIBLE);
//                    newsTitle.setVisibility(View.VISIBLE);
//                    newsImg.setVisibility(View.VISIBLE);
//                    title = getIntent().getStringExtra("title");
//                    img = getIntent().getStringExtra("img");
//
//                } else {
//                    newsImg.setVisibility(View.GONE);
//                    newsTitle.setVisibility(View.GONE);
//                    newsDesc.setVisibility(View.GONE);
//                }
//
//                if (finalHindiString != null) {
//                    newsDesc.setText(finalHindiString);
//                } else {
//                    newsDesc.setText(finalEnglishString);
//                    materialButtonToggleGroup.setVisibility(View.GONE);
//                }
//
//                dialog.dismiss();
//                english.setBackgroundColor(0);
//                english.setTextColor(Color.BLACK);
//                materialButtonToggleGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
//
//                    switch (checkedId) {
//                        case R.id.hindiPreview:
//                            english.setBackgroundColor(0);
//                            english.setTextColor(Color.BLACK);
//                            hindi.setBackgroundColor(Color.parseColor("#009637"));
//                            hindi.setTextColor(Color.WHITE);
//                            newsDesc.setText(finalHindiString);
//                            newsDesc.setVisibility(View.VISIBLE);
//                            break;
//                        case R.id.englishPreview:
//                            english.setBackgroundColor(Color.parseColor("#009637"));
//                            english.setTextColor(Color.WHITE);
//                            hindi.setBackgroundColor(0);
//                            hindi.setTextColor(Color.BLACK);
//                            newsDesc.setText(finalEnglishString);
//                            newsDesc.setVisibility(View.VISIBLE);
//                            break;
//                        default:
//                    }
//                });

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