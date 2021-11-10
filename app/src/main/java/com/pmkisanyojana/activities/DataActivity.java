package com.pmkisanyojana.activities;

import static com.pmkisanyojana.activities.HomeScreenActivity.BroadCastStringForAction;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.pmkisanyojana.R;
import com.pmkisanyojana.activities.ui.main.PageViewModel;
import com.pmkisanyojana.databinding.ActivityDataBinding;
import com.pmkisanyojana.models.ModelFactory;
import com.pmkisanyojana.models.YojanaPreviewModel;
import com.pmkisanyojana.utils.CommonMethod;
import com.pmkisanyojana.utils.MyReceiver;

import java.util.HashMap;
import java.util.Map;

public class DataActivity extends AppCompatActivity {

    WebView webView;
    TextView title;
    int count = 1;
    String id;
    PageViewModel pageViewModel;
    Map<String, String> map = new HashMap<>();
    LottieAnimationView lottieAnimationView;

    MaterialButton visitSiteBtn;
    ActivityDataBinding binding;
    public BroadcastReceiver receiver = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(BroadCastStringForAction)) {
                if (intent.getStringExtra("online_status").equals("true")) {

                    Set_Visibility_ON();
                    count++;
                } else {
                    Set_Visibility_OFF();
                }
            }
        }
    };
    private IntentFilter intentFilter;
    Dialog dialog;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint({"SetJavaScriptEnabled", "NonConstantResourceId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        binding = ActivityDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        webView = binding.webView;
        title = binding.title;
        visitSiteBtn = binding.visitSiteBtn;
        lottieAnimationView = binding.lottieAnimationEmpty;
        dialog = CommonMethod.getDialog(this);

        visitSiteBtn.setText(String.format("%s visit Site", getIntent().getStringExtra("title")));
        visitSiteBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);
            intent.putExtra("url", getIntent().getStringExtra("url"));
            startActivity(intent);
        });
        binding.backIcon.setOnClickListener(v -> onBackPressed());
        title.setText(getIntent().getStringExtra("title"));
        id = getIntent().getStringExtra("id");
        map.put("yojanaId", id);
        MaterialButtonToggleGroup materialButtonToggleGroup = binding.materialButtonToggleGroup;
        materialButtonToggleGroup.setVisibility(View.GONE);
        Button hindi, english;
        hindi = binding.hindiPreview;
        english = binding.englishPreview;
        pageViewModel = new ViewModelProvider(this, new ModelFactory(this.getApplication(), map)).get(PageViewModel.class);
        pageViewModel.getYojanaPreviewData().observe(this, yojanaPreviewModelList -> {
dialog.show();
            if (!yojanaPreviewModelList.getData().isEmpty()) {
                String hindiString = null;
                String englishString = null;
                for (YojanaPreviewModel m : yojanaPreviewModelList.getData()) {
                    if (m.getYojanaId().equals(id)) {
                        hindi.setBackgroundColor(Color.parseColor("#009637"));
                        hindi.setTextColor(Color.WHITE);


                        String replaceString = m.getDesc().replaceAll("<.*?>", "");
                        String removeNumeric = replaceString.replaceAll("[0-9]", "");
                        Log.d("both data", removeNumeric.trim());

                        for (char c : removeNumeric.trim().toCharArray()) {
                            if (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.DEVANAGARI) {
                                hindiString = m.getDesc();
                                Log.d("hindi", hindiString);
                                break;
                            } else {

                                if (englishString == null) {
                                    englishString = m.getDesc();
                                    Log.d("english", englishString);
                                    materialButtonToggleGroup.setVisibility(View.VISIBLE);

                                }

                            }

                        }
                    }
                }
                String finalEnglishString = englishString;
                String finalHindiString = hindiString;
                webView.setVisibility(View.VISIBLE);
                if (finalHindiString != null) {
                    webView.loadData(finalHindiString, "text/html", "UTF-8");

                } else {
                    webView.loadData(finalEnglishString, "text/html", "UTF-8");
                    materialButtonToggleGroup.setVisibility(View.GONE);
                }
                dialog.dismiss();

                english.setBackgroundColor(0);
                english.setTextColor(Color.BLACK);
                materialButtonToggleGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {

                    switch (checkedId) {
                        case R.id.hindiPreview:
                            english.setBackgroundColor(0);
                            english.setTextColor(Color.BLACK);
                            hindi.setBackgroundColor(Color.parseColor("#009637"));
                            hindi.setTextColor(Color.WHITE);
                            webView.loadDataWithBaseURL(null, finalHindiString, "text/html", "UTF-8", null);
                            webView.setVisibility(View.VISIBLE);
                            break;
                        case R.id.englishPreview:
                            english.setBackgroundColor(Color.parseColor("#009637"));
                            english.setTextColor(Color.WHITE);
                            hindi.setBackgroundColor(0);
                            hindi.setTextColor(Color.BLACK);
                            webView.loadDataWithBaseURL(null, finalEnglishString, "text/html", "UTF-8", null);
                            webView.setVisibility(View.VISIBLE);
                            break;
                        default:
                    }
                });

            } else {
                dialog.dismiss();
                lottieAnimationView.setVisibility(View.VISIBLE);
                visitSiteBtn.setVisibility(View.GONE);

            }
        });

        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setSupportMultipleWindows(true);
        webView.getSettings().setJavaScriptEnabled(true);


        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onCreateWindow(WebView view, boolean dialog, boolean userGesture, android.os.Message resultMsg) {
                WebView.HitTestResult result = view.getHitTestResult();
                String data = result.getExtra();
                Context context = view.getContext();
                Intent browserIntent = new Intent(getApplicationContext(), WebViewActivity.class);
                browserIntent.putExtra("url", data);
                context.startActivity(browserIntent);
                return false;
            }
        });

        intentFilter = new IntentFilter();
        intentFilter.addAction(BroadCastStringForAction);
        Intent serviceIntent = new Intent(this, MyReceiver.class);
        startService(serviceIntent);
        if (isOnline(getApplicationContext())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Set_Visibility_ON();
            }
        } else {
            Set_Visibility_OFF();
        }
    }

    public boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void Set_Visibility_ON() {
        binding.lottieHome.setVisibility(View.GONE);
        binding.tvNotConnected.setVisibility(View.GONE);

    }

    private void Set_Visibility_OFF() {
        binding.lottieHome.setVisibility(View.VISIBLE);
        binding.tvNotConnected.setVisibility(View.VISIBLE);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
        finish();
        overridePendingTransition(0, 0);
    }
}