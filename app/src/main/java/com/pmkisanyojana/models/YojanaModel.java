package com.pmkisanyojana.models;

public class YojanaModel {

    private final String id;
    private final String yojanaImage;
    private final String yojanaTitle;
    private final String yojanaDate;
    private final String yojanaTime;

    public YojanaModel(String id, String yojanaImage, String yojanaTitle, String yojanaDate, String yojanaTime) {
        this.id = id;
        this.yojanaImage = yojanaImage;
        this.yojanaTitle = yojanaTitle;
        this.yojanaDate = yojanaDate;
        this.yojanaTime = yojanaTime;
    }

    public String getId() {
        return id;
    }

    public String getYojanaImage() {
        return yojanaImage;
    }

    public String getYojanaTitle() {
        return yojanaTitle;
    }

    public String getYojanaDate() {
        return yojanaDate;
    }

    public String getYojanaTime() {
        return yojanaTime;
    }
}
