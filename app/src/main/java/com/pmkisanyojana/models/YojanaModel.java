package com.pmkisanyojana.models;

public class YojanaModel {


    private final String id;
    private final String Image;
    private final String Title;
    private final String Date;
    private final String Time;
    private final String url;

    public YojanaModel(String id, String image, String title, String date, String time, String url) {
        this.id = id;
        Image = image;
        Title = title;
        Date = date;
        Time = time;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public String getImage() {
        return Image;
    }

    public String getTitle() {
        return Title;
    }

    public String getDate() {
        return Date;
    }

    public String getTime() {
        return Time;
    }

    public String getUrl() {
        return url;
    }
}
