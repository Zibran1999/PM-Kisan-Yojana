package com.pmkisanyojana.models;

public class NewsModel {


    private final String id;
    private final String Image;
    private final String Title;
    private final String Date;
    private final String Time;

    public NewsModel(String id, String image, String title, String date, String time) {
        this.id = id;
        Image = image;
        Title = title;
        Date = date;
        Time = time;
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


}
