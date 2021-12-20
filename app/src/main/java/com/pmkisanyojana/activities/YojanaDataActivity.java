package com.pmkisanyojana.activities;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

import static com.pmkisanyojana.fragments.StatusFragment.setImage;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.pmkisanyojana.databinding.ActivityYojanaDataBinding;
import com.pmkisanyojana.fragments.DetailsFragment;
import com.pmkisanyojana.fragments.QuizFragment;
import com.pmkisanyojana.fragments.StatusFragment;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;
import java.util.List;

public class YojanaDataActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 100;
    private static final int STORAGE_REQUEST = 200;
    ActivityYojanaDataBinding binding;
    YojanaDataActivity activity;
    String[] cameraPermission;
    String[] storagePermission;
    public static Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityYojanaDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity = this;

        initViews();

        // allowing permissions of gallery and camera
        cameraPermission = new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
    }

    private void initViews() {
        setUpViewPager(binding.viewPager);
        binding.tabs.setupWithViewPager(binding.viewPager);
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
                uri = result.getUri();
                setImage(uri,this);
                Glide.with(this).load(uri).into(StatusFragment.chooseImg);
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

}