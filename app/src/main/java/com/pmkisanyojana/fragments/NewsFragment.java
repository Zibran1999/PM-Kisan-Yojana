package com.pmkisanyojana.fragments;

import android.app.Activity;
import android.app.Dialog;
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

import com.google.firebase.analytics.FirebaseAnalytics;
import com.ironsource.mediationsdk.IronSource;
import com.pmkisanyojana.activities.NewsDataActivity;
import com.pmkisanyojana.activities.ui.main.PageViewModel;
import com.pmkisanyojana.adapters.NewsAdapter;
import com.pmkisanyojana.databinding.FragmentNewsBinding;
import com.pmkisanyojana.models.NewsModel;
import com.pmkisanyojana.utils.CommonMethod;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFragment extends Fragment implements NewsAdapter.NewsInterface {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    FirebaseAnalytics mFirebaseAnalytics;

    Dialog dialog;
    RecyclerView homeRV;
    NewsAdapter newsAdapter;
    FragmentNewsBinding binding;
    PageViewModel pageViewModel;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NewsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsFragment newInstance(String param1, String param2) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);
        dialog = CommonMethod.getDialog(requireActivity());
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNewsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        homeRV = binding.HomeRV;
        LinearLayoutManager layoutManager = new LinearLayoutManager(root.getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        homeRV.setLayoutManager(layoutManager);
        dialog.show();
        CommonMethod.getBannerAds(requireActivity(), binding.adViewNews);
        CommonMethod.getBannerAds(requireActivity(), binding.adViewBottom);
        setNewsData(requireActivity());
        binding.swipeRefresh.setOnRefreshListener(() -> {
            setNewsData(requireActivity());
            binding.swipeRefresh.setRefreshing(false);

        });
        return binding.getRoot();
    }


    private void setNewsData(Activity context) {
        newsAdapter = new NewsAdapter(context, this);
        homeRV.setAdapter(newsAdapter);

        pageViewModel.getNews().observe(requireActivity(), newsModelList -> {

            if (!newsModelList.getData().isEmpty()) {
                newsAdapter.updateNewsList(newsModelList.getData());
                dialog.dismiss();
                CommonMethod.getBannerAds(requireActivity(), binding.adViewNews);
                CommonMethod.getBannerAds(requireActivity(), binding.adViewBottom);

            }

        });

    }

    @Override
    public void onItemClicked(NewsModel newsModel, int position) {
        CommonMethod.interstitialAds(requireActivity());

        Intent intent = new Intent(getContext(), NewsDataActivity.class);
        intent.putExtra("id", newsModel.getId());
        intent.putExtra("title", newsModel.getTitle());
        intent.putExtra("img", newsModel.getImage());
        intent.putExtra("pos", position);
        startActivity(intent);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireActivity());

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, newsModel.getId());
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, newsModel.getTitle());
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "NEWS LIST");
        mFirebaseAnalytics.logEvent("Clicked_News_Items", bundle);
        Log.d("TAG", "The ad was dismissed.");    }

    @Override
    public void onPause() {
        super.onPause();
        IronSource.onPause(requireActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        IronSource.onResume(requireActivity());
    }
}