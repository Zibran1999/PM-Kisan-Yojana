package com.pmkisanyojana.activities;

import static androidx.lifecycle.Lifecycle.Event.ON_START;
import static com.pmkisanyojana.utils.CommonMethod.mInterstitialAd;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Half;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.pmkisanyojana.databinding.ActivityWelcomeScreenBinding;
import com.pmkisanyojana.utils.AppOpenManager;
import com.pmkisanyojana.utils.CommonMethod;
import com.pmkisanyojana.utils.MyApp;
import com.pmkisanyojana.utils.Prevalent;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Date;

import io.paperdb.Paper;

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
            binding.lottieFlower.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    binding.lottieFlower.setVisibility(View.GONE);

                    if (CommonMethod.mInterstitialAd != null) {
                        CommonMethod.mInterstitialAd.show(WelcomeScreenActivity.this);
                        CommonMethod.mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when fullscreen content is dismissed.
                                startActivity(new Intent(getApplicationContext(),HomeScreenActivity.class));
                                finish();
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                // Called when fullscreen content failed to show.
                                Log.d("TAG", "The ad failed to show.");
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when fullscreen content is shown.
                                // Make sure to set your reference to null so you don't
                                // show it a second time.
                                CommonMethod.mInterstitialAd = null;
                                Log.d("TAG", "The ad was shown.");
                            }
                        });
                    } else {
                        CommonMethod.interstitialAds(WelcomeScreenActivity.this);
                        startActivity(new Intent(getApplicationContext(),HomeScreenActivity.class));
                        finish();
                        Log.d("TAG", "The interstitial ad wasn't ready yet.");
                    }

                    // new AppOpenManager(MyApp.mInstance, Paper.book().read(Prevalent.openAppAds),getApplicationContext());

                }
            },3000);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        moveTaskToBack(true);
        System.exit(0);
    }


}