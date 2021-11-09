package com.pmkisanyojana.activities.ui.main;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.pmkisanyojana.R;
import com.pmkisanyojana.activities.DataActivity;
import com.pmkisanyojana.activities.NewsDataActivity;
import com.pmkisanyojana.adapters.NewsAdapter;
import com.pmkisanyojana.adapters.YojanaAdapter;
import com.pmkisanyojana.databinding.FragmentHomeScreenBinding;
import com.pmkisanyojana.models.NewsModel;
import com.pmkisanyojana.models.YojanaModel;
import com.pmkisanyojana.utils.CommonMethod;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment implements YojanaAdapter.YojanaInterface, NewsAdapter.NewsInterface {

    private static final String ARG_SECTION_NUMBER = "section_number";
    int pos = 1;
    RecyclerView homeRV;
    YojanaAdapter yojanaAdapter;
    NewsAdapter newsAdapter;
    Dialog dialog;
    SwipeRefreshLayout swipeRefreshLayout;
    LottieAnimationView lottieAnimationView;
    private PageViewModel pageViewModel;
    private FragmentHomeScreenBinding binding;

    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
            pos = index;
        }
        dialog = CommonMethod.getDialog(getContext());
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        binding = FragmentHomeScreenBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        homeRV = binding.HomeRV;
        swipeRefreshLayout = binding.swipeRefresh;
        lottieAnimationView = binding.lottieAnimation;

        LinearLayoutManager layoutManager = new LinearLayoutManager(root.getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        homeRV.setLayoutManager(layoutManager);


        if (pos == 1) {
            dialog.show();
            setYojanaData(root.getContext());
            swipeRefreshLayout.setOnRefreshListener(() -> {
                setYojanaData(root.getContext());
                swipeRefreshLayout.setRefreshing(false);
            });

        } else if (pos == 2) {
            setNewsData(root.getContext());
            swipeRefreshLayout.setOnRefreshListener(() -> {
                setNewsData(root.getContext());
                swipeRefreshLayout.setRefreshing(false);

            });

        }

        return root;
    }

    private void setNewsData(Context context) {
        newsAdapter = new NewsAdapter(context, this);
        homeRV.setAdapter(newsAdapter);
        pageViewModel.getNews().observe(requireActivity(), newsModelList -> {

            if (newsModelList != null) {
                newsAdapter.updateNewsList(newsModelList.getData());
            } else {
                homeRV.setVisibility(View.GONE);
                lottieAnimationView.setVisibility(View.VISIBLE);
            }
            dialog.dismiss();
        });

    }

    private void setYojanaData(Context context) {
        yojanaAdapter = new YojanaAdapter(context, this);
        homeRV.setAdapter(yojanaAdapter);
        pageViewModel.geYojanaList().observe(requireActivity(), yojanaModel -> {

            if (yojanaModel != null) {
                yojanaAdapter.updateYojanaList(yojanaModel.getData());

            } else {
                homeRV.setVisibility(View.GONE);
                lottieAnimationView.setVisibility(View.VISIBLE);
            }
            dialog.dismiss();
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemClicked(YojanaModel yojanaModel) {

        Intent intent = new Intent(getContext(), DataActivity.class);
        intent.putExtra("id", yojanaModel.getId());
        intent.putExtra("title", yojanaModel.getTitle());
        intent.putExtra("url", yojanaModel.getUrl());
        startActivity(intent);

    }

    @Override
    public void onItemClicked(NewsModel newsModel) {
        Intent intent = new Intent(getContext(), NewsDataActivity.class);
        intent.putExtra("id", newsModel.getId());
        intent.putExtra("title", newsModel.getTitle());
        intent.putExtra("img", newsModel.getImage());
        startActivity(intent);
    }
}