package com.pmkisanyojana.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.pmkisanyojana.R;
import com.pmkisanyojana.databinding.AdLayoutBinding;
import com.pmkisanyojana.models.NewsModel;
import com.pmkisanyojana.utils.Prevalent;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_VIEW = 0;
    private static final int AD_VIEW = 1;
    private static final int ITEM_FEED_COUNT = 5;
    private final List<NewsModel> newsModelList = new ArrayList<>();
    Activity context;
    NewsInterface newsInterface;
    AdLoader adLoader;

    public NewsAdapter(Activity context, NewsInterface newsInterface) {
        this.context = context;
        this.newsInterface = newsInterface;
    }


    @Override
    public int getItemViewType(int position) {
        if ((position + 1) % ITEM_FEED_COUNT == 0) {
            return AD_VIEW;
        }
        return ITEM_VIEW;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == ITEM_VIEW) {
            View view = LayoutInflater.from(context).inflate(R.layout.yojna_layout, parent, false);
            return new ItemViewHolder(view);
        } else if (viewType == AD_VIEW) {
            View view = LayoutInflater.from(context).inflate(R.layout.ad_layout, parent, false);
            return new AdViewHolder(view);
        } else return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder.getItemViewType() == ITEM_VIEW) {
            int pos = position - Math.round(position / ITEM_FEED_COUNT);
            ((ItemViewHolder) holder).newsTitle.setText(newsModelList.get(pos).getTitle());
            ((ItemViewHolder) holder).newsTitle.setTextSize(17);
            Glide.with(context).load("https://gedgetsworld.in/PM_Kisan_Yojana/News_Images/" + newsModelList.get(pos).getImage()).into(((ItemViewHolder) holder).newsImage);
            holder.itemView.setOnClickListener(v -> newsInterface.onItemClicked(newsModelList.get(pos), pos));


        } else if (holder.getItemViewType() == AD_VIEW) {
            ((AdViewHolder) holder).bindAdData();
        }

    }

    @Override
    public int getItemCount() {
        if (newsModelList.size() > 0) {
            return newsModelList.size() + Math.round(newsModelList.size() / ITEM_FEED_COUNT);
        }
        return 0;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateNewsList(List<NewsModel> newsModels) {
        newsModelList.clear();
        newsModelList.addAll(newsModels);
        notifyDataSetChanged();
    }

    public interface NewsInterface {

        void onItemClicked(NewsModel newsModel, int position);
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView newsTitle;
        ImageView newsImage;

        public ItemViewHolder(@NonNull View itemViewType) {
            super(itemViewType);
            newsTitle = itemViewType.findViewById(R.id.yojanaTitle);
            newsImage = itemViewType.findViewById(R.id.yojnaImage);
        }
    }

    public class AdViewHolder extends RecyclerView.ViewHolder {
        AdLayoutBinding binding;

        public AdViewHolder(@NonNull View itemAdView2) {
            super(itemAdView2);
            binding = AdLayoutBinding.bind(itemAdView2);
        }

        private void bindAdData() {
            AdLoader.Builder builder = new AdLoader.Builder(context, Paper.book().read(Prevalent.nativeAds))
                    .forNativeAd(nativeAd -> {
                        NativeAdView nativeAdView = (NativeAdView) context.getLayoutInflater().inflate(R.layout.native_ad_layout, null);
                        populateNativeADView(nativeAd, nativeAdView);
                        binding.adLayout.removeAllViews();
                        binding.adLayout.addView(nativeAdView);
                    });

            adLoader = builder.withAdListener(new AdListener() {

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    adLoader.loadAd(new AdRequest.Builder().build());
                }
            }).build();

            adLoader.loadAd(new AdRequest.Builder().build());

        }

        private void populateNativeADView(NativeAd nativeAd, NativeAdView adView) {
            // Set the media view.
//            adView.setMediaView(adView.findViewById(R.id.ad_media));

            // Set other ad assets.
            adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
            adView.setBodyView(adView.findViewById(R.id.ad_body));
            adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
            adView.setIconView(adView.findViewById(R.id.ad_app_icon));
            adView.setPriceView(adView.findViewById(R.id.ad_price));
            adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
            adView.setStoreView(adView.findViewById(R.id.ad_store));
            adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

            // The headline and mediaContent are guaranteed to be in every UnifiedNativeAd.
            ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
//            adView.getMediaView().setMediaContent(nativeAd.getMediaContent());

            // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
            // check before trying to display them.
            if (nativeAd.getBody() == null) {
                adView.getBodyView().setVisibility(View.INVISIBLE);
            } else {
                adView.getBodyView().setVisibility(View.VISIBLE);
                ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
            }

            if (nativeAd.getCallToAction() == null) {
                adView.getCallToActionView().setVisibility(View.INVISIBLE);
            } else {
                adView.getCallToActionView().setVisibility(View.VISIBLE);
                ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
            }

            if (nativeAd.getIcon() == null) {
                adView.getIconView().setVisibility(View.GONE);
            } else {
                ((ImageView) adView.getIconView()).setImageDrawable(
                        nativeAd.getIcon().getDrawable());
                adView.getIconView().setVisibility(View.VISIBLE);
            }

            if (nativeAd.getPrice() == null) {
                adView.getPriceView().setVisibility(View.INVISIBLE);
            } else {
                adView.getPriceView().setVisibility(View.VISIBLE);
                ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
            }

            if (nativeAd.getStore() == null) {
                adView.getStoreView().setVisibility(View.INVISIBLE);
            } else {
                adView.getStoreView().setVisibility(View.VISIBLE);
                ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
            }

            if (nativeAd.getStarRating() == null) {
                adView.getStarRatingView().setVisibility(View.INVISIBLE);
            } else {
                ((RatingBar) adView.getStarRatingView()).setRating(nativeAd.getStarRating().floatValue());
                adView.getStarRatingView().setVisibility(View.VISIBLE);
            }

            if (nativeAd.getAdvertiser() == null) {
                adView.getAdvertiserView().setVisibility(View.INVISIBLE);
            } else {
                ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
                adView.getAdvertiserView().setVisibility(View.VISIBLE);
            }

            // This method tells the Google Mobile Ads SDK that you have finished populating your
            // native ad view with this native ad.
            adView.setNativeAd(nativeAd);
        }
    }
}
