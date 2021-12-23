package com.pmkisanyojana.models;

import com.google.gson.annotations.SerializedName;

public class ProfileModel {
    @SerializedName("profileID")
    private final String userId;
    @SerializedName("profileImage")
    private final String userImage;
    @SerializedName("profileName")
    private final String userName;

    public ProfileModel(String userId, String userImage, String userName) {
        this.userId = userId;
        this.userImage = userImage;
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserImage() {
        return userImage;
    }

    public String getUserName() {
        return userName;
    }
}
