package com.pmkisanyojana.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.ironsource.mediationsdk.IronSource;
import com.pmkisanyojana.activities.YojanaDataActivity;
import com.pmkisanyojana.activities.ui.main.PageViewModel;
import com.pmkisanyojana.adapters.YojanaAdapter;
import com.pmkisanyojana.databinding.FragmentYojanaBinding;
import com.pmkisanyojana.models.YojanaModel;
import com.pmkisanyojana.utils.AdsViewModel;
import com.pmkisanyojana.utils.CommonMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link YojanaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class YojanaFragment extends Fragment implements YojanaAdapter.YojanaInterface {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RecyclerView homeRV, pinnedRv;
    String yojanaId = "true";
    YojanaAdapter yojanaAdapter;
    FragmentYojanaBinding binding;
    FirebaseAnalytics mFirebaseAnalytics;
    PageViewModel pageViewModel;
    List<YojanaModel> models = new ArrayList<>();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public YojanaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment YojanaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static YojanaFragment newInstance(String param1, String param2) {
        YojanaFragment fragment = new YojanaFragment();
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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentYojanaBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        homeRV = binding.HomeRV;
        pinnedRv = binding.pinnedRV;
        CommonMethod.getBannerAds(requireActivity(), binding.adViewYojana);
        LinearLayoutManager layoutManager = new LinearLayoutManager(root.getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        homeRV.setLayoutManager(layoutManager);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(root.getContext());
        layoutManager1.setOrientation(RecyclerView.VERTICAL);
        pinnedRv.setLayoutManager(layoutManager1);
        setYojanaData(requireActivity());
        binding.swipeRefresh.setOnRefreshListener(() -> {
            setYojanaData(requireActivity());
            CommonMethod.getBannerAds(requireActivity(), binding.adViewYojana);
            binding.swipeRefresh.setRefreshing(false);

        });
        return binding.getRoot();
    }

    private void setYojanaData(Activity context) {
        List<YojanaModel> yojanaModels = new ArrayList<>();
        List<YojanaModel> yojanaModelList = new ArrayList<>();
        yojanaAdapter = new YojanaAdapter(context, this);
        YojanaAdapter adapter = new YojanaAdapter(context, this);
        homeRV.setAdapter(yojanaAdapter);
        pinnedRv.setAdapter(adapter);
        pinnedRv.setVisibility(View.VISIBLE);

        pageViewModel.geYojanaList().observe(requireActivity(), yojanaModel -> {
            Log.d("yojana", yojanaModel.getData().toString());
            yojanaModelList.clear();
            yojanaModels.clear();
            models.clear();
            if (!yojanaModel.getData().isEmpty()) {
                yojanaModelList.addAll(yojanaModel.getData());
                models.addAll(yojanaModel.getData());
                for (YojanaModel model : yojanaModelList) {
                    if (yojanaId.equals(model.getPinned())) {
                        yojanaModels.add(new YojanaModel(model.getId(), model.getImage(), model.getTitle(), model.getDate(), model.getTime(), model.getUrl(), model.getPinned()));
                        models.remove(model);
                    }
                }
                adapter.updateYojanaList(yojanaModels);
                yojanaAdapter.updateYojanaList(models);

            }
        });

        ViewCompat.setNestedScrollingEnabled(homeRV, false);
        ViewCompat.setNestedScrollingEnabled(pinnedRv, false);

    }


    @Override
    public void onItemClicked(YojanaModel yojanaModel, int position) {

        AdsViewModel.destroyBanner();
        CommonMethod.interstitialAds(requireActivity());
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireActivity());
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, yojanaModel.getId());
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, yojanaModel.getTitle());
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "YOJANA Fragment");
        mFirebaseAnalytics.logEvent("Clicked_Yojana_Items", bundle);

        Intent intent = new Intent(getContext(), YojanaDataActivity.class);
        intent.putExtra("id", yojanaModel.getId());
        intent.putExtra("title", yojanaModel.getTitle());
        intent.putExtra("url", yojanaModel.getUrl());
        intent.putExtra("pos", position);
        startActivity(intent);
        Log.d("TAG", "The interstitial ad wasn't ready yet.");

    }
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