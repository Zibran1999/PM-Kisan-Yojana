package com.pmkisanyojana.activities;

import static com.pmkisanyojana.activities.MainActivity.adRequest;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.pmkisanyojana.R;
import com.pmkisanyojana.databinding.ActivityWelcomeScreenBinding;
import com.pmkisanyojana.utils.CommonMethod;

import java.io.UnsupportedEncodingException;

public class WelcomeScreenActivity extends AppCompatActivity {

    ActivityWelcomeScreenBinding binding;

    /*ads variable*/
    AdView adView;
    /*ads variable*/

    public static int count = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWelcomeScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        MobileAds.initialize(this);
        CommonMethod.interstitialAds(WelcomeScreenActivity.this);

        binding.startBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), HomeScreenActivity.class);
            startActivity(intent);
            finish();
        });

        binding.shareBtn.setOnClickListener(v -> CommonMethod.shareApp(this));

        binding.contactBtn.setOnClickListener(v -> {
            try {
                CommonMethod.whatsApp(this);
            } catch (UnsupportedEncodingException | PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        });

        adView = binding.adView;
        adView.loadAd(adRequest);
        adView.setVisibility(View.VISIBLE);

    }


}