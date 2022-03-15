package com.pmkisanyojana.utils;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.ironsource.mediationsdk.ISBannerSize;
import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.IronSourceBannerLayout;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.sdk.BannerListener;

import io.paperdb.Paper;

public class AdsViewModel implements LifecycleObserver {
    private final Activity activity;
    private  static IronSourceBannerLayout banner;
    RelativeLayout container;

    public AdsViewModel(Activity activity, RelativeLayout container) {
        this.activity = activity;
        this.container = container;
    }

    public void showBannerAd(Context context, RelativeLayout container) {
        // Iron Source Banner Ads
        String id = Paper.book().read(Prevalent.openAppAds);
        IronSource.init((Activity) context, id.trim());
        IronSource.setMetaData("Facebook_IS_CacheFlag", "IMAGE");
        banner = IronSource.createBanner((Activity) context, ISBannerSize.BANNER);
        container.addView(banner);

        if (banner != null) {
            // set the banner listener
            banner.setBannerListener(new BannerListener() {
                @Override
                public void onBannerAdLoaded() {
                    Log.d(TAG, "onBannerAdLoaded");
                    // since banner container was "gone" by default, we need to make it visible as soon as the banner is ready
                    container.setVisibility(View.VISIBLE);
                }

                @Override
                public void onBannerAdLoadFailed(IronSourceError error) {
                    Log.d(TAG, "onBannerAdLoadFailed" + " " + error);
                    activity.runOnUiThread(container::removeAllViews);
                }

                @Override
                public void onBannerAdClicked() {
                    Log.d(TAG, "onBannerAdClicked");
                }

                @Override
                public void onBannerAdScreenPresented() {
                    Log.d(TAG, "onBannerAdScreenPresented");
                }

                @Override
                public void onBannerAdScreenDismissed() {
                    Log.d(TAG, "onBannerAdScreenDismissed");
                }

                @Override
                public void onBannerAdLeftApplication() {
                    Log.d(TAG, "onBannerAdLeftApplication");
                }
            });

            // load ad into the created banner
            IronSource.loadBanner(banner);
        } else {
            Toast.makeText(context, "IronSource.createBanner returned null", Toast.LENGTH_LONG).show();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        IronSource.onResume(activity);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        IronSource.onPause(activity);


    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        showBannerAd(activity,container);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
    }
    public static void destroyBanner(){
        IronSource.destroyBanner(banner);
    }
}
