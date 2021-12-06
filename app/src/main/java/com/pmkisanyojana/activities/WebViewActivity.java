package com.pmkisanyojana.activities;

import static com.pmkisanyojana.utils.CommonMethod.mInterstitialAd;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.pmkisanyojana.databinding.ActivityWebViewBinding;
import com.pmkisanyojana.utils.CommonMethod;

public class WebViewActivity extends AppCompatActivity {
    WebView webView;
    TextView title;
    ActivityWebViewBinding binding;
    String data;
    Dialog dialog;

    /*ads variable*/
//    AdView adView;
//    AdRequest adRequest;
    /*ads variable*/

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWebViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        MobileAds.initialize(this);
        CommonMethod.interstitialAds(this);
//        adRequest = new AdRequest.Builder().build();
        webView = binding.webView;
        title = binding.title;
        binding.backIcon.setOnClickListener(v -> onBackPressed());
        data = getIntent().getStringExtra("url");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);


        WebSettings webSettings = webView.getSettings();
        webSettings.getLoadsImagesAutomatically();



//        adView = binding.adViewWebView;
//        adView.loadAd(adRequest);
//        adView.setVisibility(View.VISIBLE);

//        new Handler().postDelayed(() -> {
//            if (mInterstitialAd != null) {
//                mInterstitialAd.show(this);
//            } else {
//                Log.d("TAG", "The interstitial ad wasn't ready yet.");
//            }
//        }, 3000);


        dialog = CommonMethod.getDialog(this);
        dialog.show();

        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i("TAG", "Processing webview url click...");
                view.loadUrl(url);
                return true;
            }
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                //super.onReceivedSslError(view, handler, error);
                handler.proceed();//skip ssl error
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

                Log.e("TAG", "Error: " + description);
                Toast.makeText(getApplicationContext(), "Oh no! " + description, Toast.LENGTH_SHORT).show();

            }
        });
        new Handler().postDelayed(() -> {
            dialog.dismiss();

        }, 3000);

        webView.loadUrl(data);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()){
            webView.goBack();
        }else {
            super.onBackPressed();
        }
    }
}