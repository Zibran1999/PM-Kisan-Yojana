package com.pmkisanyojana.models;

public class YojanaModel {


    private final String id;
    private final String Image;
    private final String Title;
    private final String Date;
    private final String Time;
    private final String url;
    private  final  String pinned;

    public YojanaModel(String id, String image, String title, String date, String time, String url, String pinned) {
        this.id = id;
        Image = image;
        Title = title;
        Date = date;
        Time = time;
        this.url = url;
        this.pinned = pinned;
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

    public String getPinned() {
        return pinned;
    }
}
