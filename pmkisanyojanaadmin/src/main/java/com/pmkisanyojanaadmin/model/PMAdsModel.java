package com.pmkisanyojanaadmin.model;

import com.google.gson.annotations.SerializedName;

public class PMAdsModel {
    String id, banner, interstitial,appOpen;
    @SerializedName("native")
    String nativeADs;

    public PMAdsModel(String id, String banner, String interstitial, String appOpen, String nativeADs) {
        this.id = id;
        this.banner = banner;
        this.interstitial = interstitial;
        this.appOpen = appOpen;
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

    public String getAppOpen() {
        return appOpen;
    }

    public String getNativeADs() {
        return nativeADs;
    }
}
