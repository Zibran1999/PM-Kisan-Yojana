package com.pmkisanyojanaadmin.model;

import java.util.Comparator;

public class YojanaModel {

    public static Comparator<YojanaModel> yojanaModelComparator = (o1, o2) -> {
        Integer time1, time2;
        return   o1.getDate().compareTo(o2.getDate());
    };
    private final String id;
    private final String Image;
    private final String Title;
    private final String Date;
    private final String Time;

    public YojanaModel(String id, String image, String title, String date, String time) {
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
