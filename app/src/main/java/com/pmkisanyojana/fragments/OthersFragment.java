package com.pmkisanyojana.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.pmkisanyojana.activities.YojanaDataActivity;
import com.pmkisanyojana.activities.ui.main.PageViewModel;
import com.pmkisanyojana.adapters.YojanaAdapter;
import com.pmkisanyojana.databinding.FragmentOthersBinding;
import com.pmkisanyojana.models.YojanaModel;
import com.pmkisanyojana.utils.CommonMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OthersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OthersFragment extends Fragment implements YojanaAdapter.YojanaInterface {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    FirebaseAnalytics mFirebaseAnalytics;
    RecyclerView homeRV, pinnedRv;
    YojanaAdapter othersAdapter, pinnedAdapter;
    AdView adView;
    FragmentOthersBinding binding;
    PageViewModel pageViewModel;
    List<YojanaModel> models = new ArrayList<>();
    String yojanaId = "true";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OthersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OthersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OthersFragment newInstance(String param1, String param2) {
        OthersFragment fragment = new OthersFragment();
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
        binding = FragmentOthersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        homeRV = binding.HomeRV;
        pinnedRv = binding.pinnedRV;
        MobileAds.initialize(root.getContext());
        adView = binding.adViewHome;
        CommonMethod.getBannerAds(requireActivity(), adView);


        LinearLayoutManager layoutManager = new LinearLayoutManager(root.getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        homeRV.setLayoutManager(layoutManager);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(root.getContext());
        layoutManager1.setOrientation(RecyclerView.VERTICAL);
        pinnedRv.setLayoutManager(layoutManager1);

        setOthersData(root.getContext());
        binding.swipeRefresh.setOnRefreshListener(() -> {
            setOthersData(root.getContext());
            binding.swipeRefresh.setRefreshing(false);

        });
        return binding.getRoot();
    }

    private void setOthersData(Context context) {
        List<YojanaModel> pinnedModels = new ArrayList<>();
        List<YojanaModel> othersModelList = new ArrayList<>();

        othersAdapter = new YojanaAdapter(context, this);
        pinnedAdapter = new YojanaAdapter(context, this);

        homeRV.setAdapter(othersAdapter);
        pinnedRv.setAdapter(pinnedAdapter);
        pinnedRv.setVisibility(View.VISIBLE);

        pageViewModel.getOthersData().observe(requireActivity(), othersData -> {
            othersModelList.clear();
            pinnedModels.clear();
            models.clear();
            if (!othersData.getData().isEmpty()) {
                othersModelList.addAll(othersData.getData());
                models.addAll(othersData.getData());
                for (YojanaModel model : othersModelList) {
                    if (yojanaId.equals(model.getPinned())) {
                        pinnedModels.add(new YojanaModel(model.getId(), model.getImage(), model.getTitle(), model.getDate(), model.getTime(), model.getUrl(), model.getPinned()));
                        models.remove(model);
                        Log.d("piined ", pinnedModels.toString());
                    }
                }
                pinnedAdapter.updateYojanaList(pinnedModels);
                othersAdapter.updateYojanaList(models);


            }
        });

    }

    @Override
    public void onItemClicked(YojanaModel yojanaModel) {

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireActivity());
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, yojanaModel.getId());
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, yojanaModel.getTitle());
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, yojanaModel.getImage());
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        Intent intent = new Intent(getContext(), YojanaDataActivity.class);
        intent.putExtra("id", yojanaModel.getId());
        intent.putExtra("title", yojanaModel.getTitle());
        intent.putExtra("url", yojanaModel.getUrl());
        startActivity(intent);
    }
}