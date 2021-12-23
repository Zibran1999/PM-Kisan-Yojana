package com.pmkisanyojana.models;

public class MyStatusModel {

    String id, userName,image,time;

    public MyStatusModel(String id, String userName, String image, String time) {
        this.id = id;
        this.userName = userName;
        this.image = image;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getImage() {
        return image;
    }

    public String getTime() {
        return time;
    }
}
