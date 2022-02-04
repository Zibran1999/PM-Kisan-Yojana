package com.pmkisanyojanaadmin.model;

import com.google.gson.annotations.SerializedName;

public class AdsModel {
    String id, banner, interstitial;
    @SerializedName("native")
    String nativeADs;

    public AdsModel(String id, String banner, String interstitial, String nativeADs) {
        this.id = id;
        this.banner = banner;
        this.interstitial = interstitial;
        this.nativeADs = nativeADs;
    }

    public String getId() {
        return id;
    }

    public String getBanner() {
        return banner;
    }

    public String getInterstitial() {
        return interstitial;
    }

    public String getNativeADs() {
        return nativeADs;
    }
}
