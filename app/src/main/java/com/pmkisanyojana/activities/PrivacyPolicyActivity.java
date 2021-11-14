package com.pmkisanyojana.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

import com.pmkisanyojana.R;

public class PrivacyPolicyActivity extends AppCompatActivity {
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        webView = findViewById(R.id.policy);
        webView.loadUrl("file:///android_asset/privacy.html");
    }
}