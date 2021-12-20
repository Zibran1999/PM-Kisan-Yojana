package com.pmkisanyojana.models;

import com.google.gson.annotations.SerializedName;

public class ProfileModel {
    @SerializedName("id")
    private final String userId;
    @SerializedName("Image")
    private final String userImage;
    @SerializedName("Name")
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
