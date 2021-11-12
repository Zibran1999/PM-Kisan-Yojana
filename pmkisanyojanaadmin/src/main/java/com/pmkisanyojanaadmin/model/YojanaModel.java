package com.pmkisanyojanaadmin.model;

import java.util.Comparator;

public class YojanaModel {

    public static Comparator<YojanaModel> yojanaModelComparator = (o1, o2) -> {
        Integer id1, id2;
        id1 = Integer.valueOf(o1.getId());
        id2 = Integer.valueOf(o2.getId());
        return id1.compareTo(id2);

    };
    private final String id;
    private final String Image;
    private final String Title;
    private final String Date;
    private final String Time;
    private final String url;
    private final String pinned;

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
