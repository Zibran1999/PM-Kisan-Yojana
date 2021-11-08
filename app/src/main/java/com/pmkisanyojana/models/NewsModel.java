package com.pmkisanyojana.models;

import java.util.Comparator;

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

    public static Comparator<NewsModel> newsModelComparator = new Comparator<NewsModel>() {
        @Override
        public int compare(NewsModel o1, NewsModel o2) {
            Integer time1, time2, date1, date2;
            time1 = Integer.valueOf(o1.getTime());
            time2 = Integer.valueOf(o2.getTime());
            date1 = Integer.valueOf(o1.getDate());
            date2 = Integer.valueOf(o2.getDate());

            Integer resultDate = date1.compareTo(date2);
            return resultDate;
        }
    };
}
