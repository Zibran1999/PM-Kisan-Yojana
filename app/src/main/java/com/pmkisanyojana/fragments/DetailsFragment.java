package com.pmkisanyojana.fragments;

import static com.pmkisanyojana.utils.CommonMethod.mInterstitialAd;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.pmkisanyojana.R;
import com.pmkisanyojana.activities.WebViewActivity;
import com.pmkisanyojana.activities.YojanaDataActivity;
import com.pmkisanyojana.activities.ui.main.PageViewModel;
import com.pmkisanyojana.databinding.FragmentDetailsBinding;
import com.pmkisanyojana.models.ModelFactory;
import com.pmkisanyojana.models.PreviewModel;
import com.pmkisanyojana.utils.CommonMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    WebView webView;
    int count = 1;
    String id;
    PageViewModel pageViewModel;
    MaterialButton visitSiteBtn;
    Map<String, String> map = new HashMap<>();
    String finalEnglishString, finalHindiString;
    FragmentDetailsBinding binding;
    Dialog dialog;
    LottieAnimationView lottieAnimationView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailsFragment newInstance(String param1, String param2) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            MobileAds.initialize(requireActivity());
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint({"SetJavaScriptEnabled", "NonConstantResourceId"})
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDetailsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        dialog = CommonMethod.getDialog(requireActivity());


        webView = binding.webView;
        WebSettings webSettings = webView.getSettings();
        webSettings.getLoadsImagesAutomatically();
        webSettings.setJavaScriptEnabled(true);
        dialog.show();

        visitSiteBtn = binding.visitSiteBtn;
        lottieAnimationView = binding.lottieAnimationEmpty;
        lottieAnimationView.setVisibility(View.GONE);

        visitSiteBtn.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), WebViewActivity.class);
            intent.putExtra("url", requireActivity().getIntent().getStringExtra("url"));
            startActivity(intent);
        });

        id = requireActivity().getIntent().getStringExtra("id");
        map.put("previewId", id);

        MaterialButtonToggleGroup materialButtonToggleGroup = binding.materialButtonToggleGroup;
        materialButtonToggleGroup.setVisibility(View.GONE);
        Button hindi, english;
        hindi = binding.hindiPreview;
        english = binding.englishPreview;
        List<PreviewModel> previewModels = new ArrayList<>();

        MobileAds.initialize(requireActivity());
        CommonMethod.interstitialAds(requireActivity());


        pageViewModel = new ViewModelProvider(this, new ModelFactory(requireActivity().getApplication(), map)).get(PageViewModel.class);
        pageViewModel.getPreviewData().observe(requireActivity(), previewModelList -> {
            previewModels.clear();
            previewModels.addAll(previewModelList.getData());
            if (!previewModels.isEmpty()) {

                CommonMethod.getBannerAds(requireActivity(), binding.adViewData);
                lottieAnimationView.setVisibility(View.GONE);
                String hindiString = null;
                String englishString = null;
                for (PreviewModel m : previewModelList.getData()) {
                    if (m.getPreviewId().equals(id)) {
                        hindi.setBackgroundColor(Color.parseColor("#009637"));
                        hindi.setTextColor(Color.WHITE);

                        String replaceString = m.getDesc().replaceAll("<.*?>", "");
                        String removeNumeric = replaceString.replaceAll("[0-9]", "");

                        for (char c : removeNumeric.trim().toCharArray()) {
                            if (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.DEVANAGARI) {
                                hindiString = m.getDesc();
                                break;
                            } else {

                                if (englishString == null) {
                                    englishString = m.getDesc();
                                    materialButtonToggleGroup.setVisibility(View.VISIBLE);
                                }
                            }
                        }


                    }
                }

                finalEnglishString = englishString;
                finalHindiString = hindiString;
                webView.setVisibility(View.VISIBLE);
                if (finalHindiString != null) {
                    webView.loadData(finalHindiString, "text/html", "UTF-8");

                } else {
                    webView.loadData(finalEnglishString, "text/html", "UTF-8");
                    materialButtonToggleGroup.setVisibility(View.GONE);
                }
                Log.d("pos", String.valueOf(YojanaDataActivity.count));
                binding.titleTv.setText(requireActivity().getIntent().getStringExtra("title"));
                dialog.dismiss();

                binding.titleTv.setVisibility(View.VISIBLE);
                if (requireActivity().getIntent().getStringExtra("url").equals("null")) {
                    visitSiteBtn.setVisibility(View.GONE);
                } else
                    visitSiteBtn.setVisibility(View.VISIBLE);

                english.setBackgroundColor(0);
                english.setTextColor(Color.BLACK);
                materialButtonToggleGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {

                    switch (checkedId) {
                        case R.id.hindiPreview:
                            CommonMethod.getBannerAds(requireActivity(), binding.adViewData);
                            english.setBackgroundColor(0);
                            english.setTextColor(Color.BLACK);
                            hindi.setBackgroundColor(Color.parseColor("#009637"));
                            hindi.setTextColor(Color.WHITE);
                            webView.loadDataWithBaseURL(null, finalHindiString, "text/html", "UTF-8", null);
                            webView.setVisibility(View.VISIBLE);
                            break;
                        case R.id.englishPreview:
                            CommonMethod.getBannerAds(requireActivity(), binding.adViewData);
                            english.setBackgroundColor(Color.parseColor("#009637"));
                            english.setTextColor(Color.WHITE);
                            hindi.setBackgroundColor(0);
                            hindi.setTextColor(Color.BLACK);
                            webView.loadDataWithBaseURL(null, finalEnglishString, "text/html", "UTF-8", null);
                            webView.setVisibility(View.VISIBLE);
                            break;
                        default:
                    }
                });


            } else {
                lottieAnimationView.setVisibility(View.VISIBLE);
                visitSiteBtn.setVisibility(View.GONE);
                binding.titleTv.setVisibility(View.GONE);
                webView.setVisibility(View.GONE);

                dialog.dismiss();
            }

        });

        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setSupportMultipleWindows(true);
        webView.getSettings().setJavaScriptEnabled(true);


        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onCreateWindow(WebView view, boolean dialog, boolean userGesture, android.os.Message resultMsg) {

                if (mInterstitialAd != null) {
                    mInterstitialAd.show(requireActivity());
                    mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            // Called when fullscreen content is dismissed.
                            WebView.HitTestResult result = view.getHitTestResult();
                            String data = result.getExtra();
                            Context context = view.getContext();
                            Intent browserIntent = new Intent(requireActivity(), WebViewActivity.class);
                            browserIntent.putExtra("url", data);
                            context.startActivity(browserIntent);
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                            // Called when fullscreen content failed to show.
                            Log.d("TAG", "The ad failed to show.");
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            // Called when fullscreen content is shown.
                            // Make sure to set your reference to null so you don't
                            // show it a second time.
                            mInterstitialAd = null;
                            Log.d("TAG", "The ad was shown.");
                        }
                    });
                } else {
                    CommonMethod.interstitialAds(requireActivity());

                    WebView.HitTestResult result = view.getHitTestResult();
                    String data = result.getExtra();
                    Context context = view.getContext();
                    Intent browserIntent = new Intent(requireActivity(), WebViewActivity.class);
                    browserIntent.putExtra("url", data);
                    context.startActivity(browserIntent);
                }


                return false;
            }
        });
        return root;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        YojanaDataActivity.count--;
    }
}