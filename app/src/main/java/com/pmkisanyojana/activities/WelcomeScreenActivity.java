package com.pmkisanyojana.activities;

import static com.pmkisanyojana.utils.CommonMethod.mInterstitialAd;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.pmkisanyojana.databinding.ActivityWelcomeScreenBinding;
import com.pmkisanyojana.utils.CommonMethod;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class WelcomeScreenActivity extends AppCompatActivity {

    public static int count = 1;
    ActivityWelcomeScreenBinding binding;
    FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWelcomeScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        MobileAds.initialize(this);
        CommonMethod.interstitialAds(WelcomeScreenActivity.this);
        CommonMethod.getBannerAds(this, binding.adView);
        binding.startBtn.setOnClickListener(v -> {

            mFirebaseAnalytics = FirebaseAnalytics.getInstance(WelcomeScreenActivity.this);
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Welcome Button");
            mFirebaseAnalytics.logEvent("Clicked_start_Button", bundle);

            Intent intent = new Intent(getApplicationContext(), HomeScreenActivity.class);
            startActivity(intent);
            finish();
        });

        binding.shareBtn.setOnClickListener(v -> CommonMethod.shareApp(this));

        binding.contactBtn.setOnClickListener(v -> {
            final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        });

    }


}