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
    WebView webView;
    TextView title;
    ActivityWebViewBinding binding;
    String data;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWebViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        webView = binding.webView;
        title = binding.title;
        binding.backIcon.setOnClickListener(v -> onBackPressed());
        data = getIntent().getStringExtra("url");
        webView.loadUrl(data);
        webView.getSettings().setJavaScriptEnabled(true);


    }
}