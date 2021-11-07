package com.pmkisanyojana.activities.ui.main;

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

import com.pmkisanyojana.adapters.YojanaAdapter;
import com.pmkisanyojana.databinding.FragmentHomeScreenBinding;
import com.pmkisanyojana.models.YojanaModel;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment implements YojanaAdapter.YojanaInterface {

    private static final String ARG_SECTION_NUMBER = "section_number";
    int pos = 1;
    RecyclerView homeRV;
    YojanaAdapter yojanaAdapter;
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
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        binding = FragmentHomeScreenBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        homeRV = binding.HomeRV;
        LinearLayoutManager layoutManager = new LinearLayoutManager(root.getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        homeRV.setLayoutManager(layoutManager);


        if (pos == 1) {
            yojanaAdapter = new YojanaAdapter(root.getContext(), this);
            homeRV.setAdapter(yojanaAdapter);
            pageViewModel.geYojanaList().observe(requireActivity(), yojanaModel -> {

                if (yojanaModel != null) {
                    Log.d("onResponse",yojanaModel.getData().get(0).getYojanaTitle());
                    yojanaAdapter.updateYojanaList(yojanaModel.getData());

                }

            });


        } else if (pos == 2) {


        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemClicked(YojanaModel yojanaModel) {

    }
}