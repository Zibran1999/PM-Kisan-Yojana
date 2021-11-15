package com.pmkisanyojana.activities;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.pmkisanyojana.BuildConfig;
import com.pmkisanyojana.R;
import com.pmkisanyojana.activities.ui.main.PlaceholderFragment;
import com.pmkisanyojana.activities.ui.main.SectionsPagerAdapter;
import com.pmkisanyojana.databinding.ActivityHomeScreenBinding;
import com.pmkisanyojana.utils.CommonMethod;
import com.pmkisanyojana.utils.MyReceiver;

import java.io.UnsupportedEncodingException;

public class HomeScreenActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final String BroadCastStringForAction = "checkingInternet";
    TextView versionCode;
    ActivityHomeScreenBinding binding;
    private static final float END_SCALE = 0.7f;
    int count = 1;
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
    ImageView navMenu;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ConstraintLayout categoryContainer;
    SectionsPagerAdapter sectionsPagerAdapter;
    ViewPager viewPager;
    private IntentFilter intentFilter;

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void Set_Visibility_ON() {
        binding.lottieHome.setVisibility(View.GONE);
        binding.tvNotConnected.setVisibility(View.GONE);
        binding.viewPager.setVisibility(View.VISIBLE);
        binding.tabs.setVisibility(View.VISIBLE);
        enableNavItems();
        if (count == 2) {
            viewPager.setAdapter(sectionsPagerAdapter);
            TabLayout tabs = binding.tabs;
            tabs.setupWithViewPager(viewPager);
            navigationDrawer();
        }
    }

    private void Set_Visibility_OFF() {
        binding.lottieHome.setVisibility(View.VISIBLE);
        binding.tvNotConnected.setVisibility(View.VISIBLE);
        binding.viewPager.setVisibility(View.GONE);
        binding.tabs.setVisibility(View.GONE);
        disableNavItems();
    }


    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
             binding = ActivityHomeScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        versionCode = binding.versionCode;
        sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        viewPager = binding.viewPager;
        navigationView = binding.navigation;
        navMenu = binding.navMenu;
        drawerLayout = binding.drawerLayout;

        binding.lottieWhatsapp.setOnClickListener(view -> {
            try {
                CommonMethod.whatsApp(this);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
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


        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);

        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
            String version = pInfo.versionName;
            versionCode.setText("Version : " + version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void navigationDrawer() {
        navigationView = findViewById(R.id.navigation);
        navigationView.bringToFront();
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(HomeScreenActivity.this);
        navigationView.setCheckedItem(R.id.nav_home);
        categoryContainer = findViewById(R.id.container_lay);

        navigationView.setCheckedItem(R.id.nav_home);
        navMenu.setOnClickListener(v -> {
            if (drawerLayout.isDrawerVisible(GravityCompat.START))
                drawerLayout.closeDrawer(GravityCompat.START);
            else drawerLayout.openDrawer(GravityCompat.START);
        });
        animateNavigationDrawer();
    }

    private void animateNavigationDrawer() {
        drawerLayout.setScrimColor(Color.parseColor("#DEE4EA"));
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                // Scale the View based on current slide offset
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                categoryContainer.setScaleX(offsetScale);
                categoryContainer.setScaleY(offsetScale);

                // Translate the View, accounting for the scaled width
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = categoryContainer.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                categoryContainer.setTranslationX(xTranslation);
            }
        });
    }

    public boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                startActivity(new Intent(getApplicationContext(), HomeScreenActivity.class));
                overridePendingTransition(0, 0);
                finish();
                break;
            case R.id.nav_contact:
                try {
                    CommonMethod.whatsApp(getApplicationContext());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.nav_rate:
                CommonMethod.rateApp(getApplicationContext());
                break;
            case R.id.nav_privacy:
                startActivity(new Intent(HomeScreenActivity.this, PrivacyPolicyActivity.class));
                break;
            case R.id.nav_share:
               shareApp(this);
                break;
            default:
        }
        return true;
    }

    public void disableNavItems() {
        Menu navMenu = navigationView.getMenu();
        MenuItem nav_insta = navMenu.findItem(R.id.nav_share);
        nav_insta.setEnabled(false);

        MenuItem nav_policy = navMenu.findItem(R.id.nav_privacy);
        nav_policy.setEnabled(false);

        MenuItem nav_home = navMenu.findItem(R.id.nav_home);
        nav_home.setEnabled(false);

        MenuItem nav_rate = navMenu.findItem(R.id.nav_rate);
        nav_rate.setEnabled(false);

        MenuItem nav_contact = navMenu.findItem(R.id.nav_contact);
        nav_contact.setEnabled(false);
    }
    public static void shareApp(Context context) {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name);
            String shareMessage = "\nLet me recommend you this application\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            context.startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void enableNavItems() {
        Menu navMenu = navigationView.getMenu();
        MenuItem nav_insta = navMenu.findItem(R.id.nav_share);
        nav_insta.setEnabled(true);

        MenuItem nav_policy = navMenu.findItem(R.id.nav_privacy);
        nav_policy.setEnabled(true);

        MenuItem nav_home = navMenu.findItem(R.id.nav_home);
        nav_home.setEnabled(true);

        MenuItem nav_rate = navMenu.findItem(R.id.nav_rate);
        nav_rate.setEnabled(true);

        MenuItem nav_contact = navMenu.findItem(R.id.nav_contact);
        nav_contact.setEnabled(true);
    }

    @Override
    public void onBackPressed() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle(R.string.app_name)
                .setIcon(R.mipmap.ic_launcher)
                .setMessage("Do You Really Want To Exit?\nAlso Rate Us 5 Star.")
                .setNeutralButton("CANCEL", (dialog, which) -> {
                });
        builder.setNegativeButton("RATE APP", (dialog, which) -> CommonMethod.rateApp(getApplicationContext()))
                .setPositiveButton("OK!!", (dialog, which) -> {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                    moveTaskToBack(true);
                    System.exit(0);

                });
        builder.show();
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