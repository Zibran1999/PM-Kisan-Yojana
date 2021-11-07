package com.pmkisanyojana.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.pmkisanyojana.databinding.ActivityWelcomeScreenBinding;
import com.pmkisanyojana.utils.CommonMethod;

public class WelcomeScreenActivity extends AppCompatActivity {
    ImageView startBtn, privacyBtn, shareBtn;
    ActivityWelcomeScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWelcomeScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        startBtn = binding.startBtn;
        privacyBtn = binding.privacyBtn;
        shareBtn = binding.shareBtn;

        startBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), HomeScreenActivity.class);
            startActivity(intent);
            finish();
        });
        shareBtn.setOnClickListener(v ->
                CommonMethod.rateApp(this));
        privacyBtn.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), PrivacyPolicyActivity.class)));
    }


}