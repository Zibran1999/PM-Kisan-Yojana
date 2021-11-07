package com.pmkisanyojana.activities;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.pmkisanyojana.databinding.ActivityWelcomeScreenBinding;

public class WelcomeScreenActivity extends AppCompatActivity {
    ImageView startBtn, privacyBtn, shareBtn;
    ActivityWelcomeScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWelcomeScreenBinding.inflate(getLayoutInflater());

        startBtn = binding.startBtn;
        privacyBtn = binding.privacyBtn;
        shareBtn = binding.shareBtn;



    }


}