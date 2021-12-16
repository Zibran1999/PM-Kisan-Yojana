package com.pmkisanyojana.activities;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.pmkisanyojana.R;
import com.pmkisanyojana.databinding.ActivityYojanaDataBinding;
import com.pmkisanyojana.fragments.DetailsFragment;
import com.pmkisanyojana.fragments.QuizFragment;
import com.pmkisanyojana.fragments.StatusFragment;

import java.util.ArrayList;
import java.util.List;

public class YojanaDataActivity extends AppCompatActivity {

    ActivityYojanaDataBinding binding;
    YojanaDataActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityYojanaDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity =this;

        initViews();


    }

    private void initViews() {
        setUpViewPager(binding.viewPager);
        binding.tabs.setupWithViewPager(binding.viewPager);
        binding.backIcon.setOnClickListener(v -> onBackPressed());

    }

    private void setUpViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(activity.getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.addFragment(new DetailsFragment(),"Details");
        adapter.addFragment(new StatusFragment(),"Status");
        adapter.addFragment(new QuizFragment(),"Quiz");
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(1);


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