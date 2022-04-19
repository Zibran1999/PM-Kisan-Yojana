package com.pmkisanyojana.activities;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;
import static com.pmkisanyojana.fragments.StatusFragment.setImage;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.ironsource.mediationsdk.IronSource;
import com.pmkisanyojana.databinding.ActivityYojanaDataBinding;
import com.pmkisanyojana.fragments.DetailsFragment;
import com.pmkisanyojana.fragments.QuizFragment;
import com.pmkisanyojana.fragments.StatusFragment;
import com.pmkisanyojana.utils.AdsViewModel;
import com.pmkisanyojana.utils.CommonMethod;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;
import java.util.List;

public class YojanaDataActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 100;
    private static final int STORAGE_REQUEST = 200;
    public static Uri uri;
    ActivityYojanaDataBinding binding;
    YojanaDataActivity activity;
    String[] cameraPermission;
    public  static int count=1;
    String[] storagePermission;
    public static int pos =0;
    FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityYojanaDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity = this;
        CommonMethod.interstitialAds(this);
        pos= getIntent().getIntExtra("pos",0);

        initViews();


        // allowing permissions of gallery and camera
        cameraPermission = new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE};

        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    mFirebaseAnalytics = FirebaseAnalytics.getInstance(YojanaDataActivity.this);
                    Bundle bundle = new Bundle();
                  //  bundle.putString(FirebaseAnalytics.Param.ITEM_ID, yojanaModel.getId());
                    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Status Stories");
                    bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Status Fragment");
                    mFirebaseAnalytics.logEvent("Status", bundle);

                } else if (position == 2) {
                    mFirebaseAnalytics = FirebaseAnalytics.getInstance(YojanaDataActivity.this);
                    Bundle bundle = new Bundle();
                    //  bundle.putString(FirebaseAnalytics.Param.ITEM_ID, yojanaModel.getId());
                    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Quiz");
                    bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Quiz Fragment");
                    mFirebaseAnalytics.logEvent("Quiz", bundle);

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initViews() {
        setUpViewPager(binding.viewPager);
        binding.tabs.setupWithViewPager(binding.viewPager);
//        AdsViewModel adsViewModel = new AdsViewModel(this,binding.adView);
//        getLifecycle().addObserver(adsViewModel);
        binding.backIcon.setOnClickListener(v -> onBackPressed());

    }

    private void setUpViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(activity.getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.addFragment(new DetailsFragment(), "Details");
        adapter.addFragment(new StatusFragment(), "Status");
        adapter.addFragment(new QuizFragment(), "Quiz");
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(1);
    }


    // Requesting camera and gallery
    // permission if not given
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_REQUEST: {
                if (grantResults.length > 0) {
                    boolean camera_accepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageaccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (camera_accepted && writeStorageaccepted) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(this, "Please Enable Camera and Storage Permissions", Toast.LENGTH_LONG).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST: {
                if (grantResults.length > 0) {
                    boolean writeStorageaccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (writeStorageaccepted) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(this, "Please Enable Storage Permissions", Toast.LENGTH_LONG).show();
                    }
                }
            }
            break;
        }
    }

    // Here we will pick image from gallery or camera
    private void pickFromGallery() {
        CropImage.activity().start(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                assert result != null;
                uri = result.getUri();
                setImage(uri, this);
                Glide.with(this).load(uri).into(StatusFragment.userImageView);
            }
        }
    }


    static class ViewPagerAdapter extends FragmentPagerAdapter {

        List<Fragment> fragmentList = new ArrayList<>();
        List<String> stringList = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            stringList.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return stringList.get(position);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AdsViewModel.destroyBanner();

    }

    @Override
    protected void onResume() {
        super.onResume();
        IronSource.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        IronSource.onPause(this);
    }
}