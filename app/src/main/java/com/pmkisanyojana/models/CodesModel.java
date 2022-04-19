package com.pmkisanyojana.models;

import com.google.gson.annotations.SerializedName;

public class
CodesModel {
    String userName,userImage;
    String id, yojanaName,yojanaAmount;

    public CodesModel(String userName, String userImage, String id, String yojanaName, String yojanaAmount) {
        this.userName = userName;
        this.userImage = userImage;
        this.id = id;
        this.yojanaName = yojanaName;
        this.yojanaAmount = yojanaAmount;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserImage() {
        return userImage;
    }

    public String getId() {
        return id;
    }

    public String getYojanaName() {
        return yojanaName;
    }

    public String getYojanaAmount() {
        return yojanaAmount;
    }
}
