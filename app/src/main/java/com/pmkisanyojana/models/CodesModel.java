package com.pmkisanyojana.models;

public class
CodesModel {
    String userName, images;
    String id, yojanaName, yojanaAmount;

    public CodesModel(String userName, String images, String id, String yojanaName, String yojanaAmount) {
        this.userName = userName;
        this.images = images;
        this.id = id;
        this.yojanaName = yojanaName;
        this.yojanaAmount = yojanaAmount;
    }

    public String getUserName() {
        return userName;
    }

    public String getImages() {
        return images;
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
