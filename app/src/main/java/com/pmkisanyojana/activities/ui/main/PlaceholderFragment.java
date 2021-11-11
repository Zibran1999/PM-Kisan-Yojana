package com.pmkisanyojana.activities.ui.main;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pmkisanyojana.activities.DataActivity;
import com.pmkisanyojana.activities.NewsDataActivity;
import com.pmkisanyojana.adapters.NewsAdapter;
import com.pmkisanyojana.adapters.YojanaAdapter;
import com.pmkisanyojana.databinding.FragmentHomeScreenBinding;
import com.pmkisanyojana.models.NewsModel;
import com.pmkisanyojana.models.YojanaModel;
import com.pmkisanyojana.utils.CommonMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment implements YojanaAdapter.YojanaInterface, NewsAdapter.NewsInterface {

    private static final String ARG_SECTION_NUMBER = "section_number";
    int pos = 1;
    RecyclerView homeRV, pinnedRv;
    YojanaAdapter yojanaAdapter;
    NewsAdapter newsAdapter;
    Dialog dialog;
    String yojanaId = "12";
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
        pinnedRv = binding.pinnedRV;

        LinearLayoutManager layoutManager = new LinearLayoutManager(root.getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        homeRV.setLayoutManager(layoutManager);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(root.getContext());
        layoutManager1.setOrientation(RecyclerView.VERTICAL);
        pinnedRv.setLayoutManager(layoutManager1);


        if (pos == 1) {
            dialog.show();
            setYojanaData(root.getContext());
            binding.swipeRefresh.setOnRefreshListener(() -> {
                setYojanaData(root.getContext());
                binding.swipeRefresh.setRefreshing(false);
            });

        } else if (pos == 2) {
            setNewsData(root.getContext());
            binding.swipeRefresh.setOnRefreshListener(() -> {
                setNewsData(root.getContext());
                binding.swipeRefresh.setRefreshing(false);

            });

        } else if (pos == 3) {
            setOthersData(root.getContext());
            binding.swipeRefresh.setOnRefreshListener(() -> {
                setOthersData(root.getContext());
                binding.swipeRefresh.setRefreshing(false);

            });
        }

        return root;
    }

    private void setOthersData(Context context) {
        YojanaAdapter adapter = new YojanaAdapter(context, this);
        homeRV.setAdapter(adapter);

        pageViewModel.getOthersData().observe(requireActivity(), othersData -> {
            if (othersData.getData() != null) {
                adapter.updateYojanaList(othersData.getData());
            } else {
                Toast.makeText(getActivity(), "Data not found", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        });

    }

    private void setNewsData(Context context) {
        newsAdapter = new NewsAdapter(context, this);
        homeRV.setAdapter(newsAdapter);
        pageViewModel.getNews().observe(requireActivity(), newsModelList -> {

            if (newsModelList.getData() != null) {
                newsAdapter.updateNewsList(newsModelList.getData());
            } else {
                Toast.makeText(getActivity(), "Data not found", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        });

    }

    private void setYojanaData(Context context) {
        List<YojanaModel> yojanaModels = new ArrayList<>();
        List<YojanaModel> yojanaModelList = new ArrayList<>();
        yojanaAdapter = new YojanaAdapter(context, this);
        YojanaAdapter adapter = new YojanaAdapter(context, this);
        homeRV.setAdapter(yojanaAdapter);
        pinnedRv.setAdapter(adapter);
        pinnedRv.setVisibility(View.VISIBLE);

        pageViewModel.geYojanaList().observe(requireActivity(), yojanaModel -> {

            if (yojanaModel.getData() != null) {
                yojanaModelList.clear();
                yojanaModelList.addAll(yojanaModel.getData());
                for (int i = 0; i < yojanaModel.getData().size(); i++) {
                    if (yojanaId.equals(yojanaModel.getData().get(i).getId())) {
                        yojanaModels.clear();
                        yojanaModels.add(new YojanaModel(yojanaModel.getData().get(i).getId(), yojanaModel.getData().get(i).getImage(), yojanaModel.getData().get(i).getTitle(), yojanaModel.getData().get(i).getDate(), yojanaModel.getData().get(i).getTime(), yojanaModel.getData().get(i).getUrl()));
                        yojanaModelList.remove(i);
                        break;
                    }

                }
                adapter.updateYojanaList(yojanaModels);
                yojanaAdapter.updateYojanaList(yojanaModelList);


            } else {
                Toast.makeText(getActivity(), "Data not found", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        });

        ViewCompat.setNestedScrollingEnabled(homeRV, false);
        ViewCompat.setNestedScrollingEnabled(pinnedRv, false);

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