package com.pmkisanyojana.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.pmkisanyojana.databinding.ActivityWebViewBinding;

public class WebViewActivity extends AppCompatActivity {
    ImageView backIcon;
    WebView webView;
    TextView title;
    ActivityWebViewBinding binding;
    String data;
    Dialog dialog;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWebViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        backIcon = binding.backIcon;
        webView = binding.webView;
        title = binding.title;
        backIcon.setOnClickListener(v -> onBackPressed());
        data = getIntent().getStringExtra("url");
        webView.loadUrl(data);
        webView.getSettings().setJavaScriptEnabled(true);


    }
}