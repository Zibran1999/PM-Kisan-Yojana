package com.pmkisanyojana.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.pmkisanyojana.R;
import com.pmkisanyojana.databinding.ActivityMainBinding;
import com.pmkisanyojana.utils.AppOpenManager;
import com.pmkisanyojana.utils.MyApp;
import com.pmkisanyojana.utils.MyReceiver;


public class MainActivity extends AppCompatActivity {
    public static final String BroadCastStringForAction = "checkingInternet";
    FirebaseAnalytics firebaseAnalytics;
    int REQUEST_CODE = 11;
    int count = 1;
    ActivityMainBinding binding;
    public BroadcastReceiver receiver = new BroadcastReceiver() {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        inAppUpdate();

        intentFilter = new IntentFilter();
        intentFilter.addAction(BroadCastStringForAction);
        Intent serviceIntent = new Intent(this, MyReceiver.class);
        startService(serviceIntent);
        binding.tvNotConnected.setVisibility(View.GONE);
        if (isOnline(getApplicationContext())) {
            Set_Visibility_ON();
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

    private void Set_Visibility_ON() {
        binding.tvNotConnected.setVisibility(View.GONE);
        binding.noNetwork.setVisibility(View.GONE);
        binding.spIcon.setVisibility(View.VISIBLE);
        binding.lottieLoading.setVisibility(View.VISIBLE);
        binding.spBg.setBackground(ContextCompat.getDrawable(this, R.drawable.sp_bg));


//      appOpenManager =  new AppOpenManager(this.getApplication(), Paper.book().read(Prevalent.openAppAds));

        if (count == 2) {
                    new Handler().postDelayed(() -> {
                        startActivity(new Intent(getApplicationContext(), WelcomeScreenActivity.class));
                        finish();
                    }, 4000);



        }
    }


    private void Set_Visibility_OFF() {
        binding.tvNotConnected.setVisibility(View.VISIBLE);
        binding.noNetwork.setVisibility(View.VISIBLE);
        binding.lottieLoading.setVisibility(View.GONE);
        binding.spIcon.setVisibility(View.GONE);
        binding.spBg.setBackgroundColor(0);
    }

    private void inAppUpdate() {
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(MainActivity.this);

        // Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    // This example applies an immediate update. To apply a flexible update
                    // instead, pass in AppUpdateType.FLEXIBLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                // Request the update.
                try {
                    appUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo, AppUpdateType.IMMEDIATE, MainActivity.this, REQUEST_CODE);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            Toast.makeText(MainActivity.this, "Downloading Started", Toast.LENGTH_SHORT).show();
            if (resultCode != RESULT_OK) {
                Log.d("DDDDD", "Downloading Failed" + resultCode);
            }
        }
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
}