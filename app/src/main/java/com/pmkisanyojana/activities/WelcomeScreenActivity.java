package com.pmkisanyojana.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.pmkisanyojana.databinding.ActivityWelcomeScreenBinding;
import com.pmkisanyojana.utils.CommonMethod;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class WelcomeScreenActivity extends AppCompatActivity {

    public static int count = 1;
    ActivityWelcomeScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWelcomeScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        MobileAds.initialize(this);
        CommonMethod.interstitialAds(WelcomeScreenActivity.this);
        CommonMethod.getBannerAds(this, binding.adView);
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

    }


}