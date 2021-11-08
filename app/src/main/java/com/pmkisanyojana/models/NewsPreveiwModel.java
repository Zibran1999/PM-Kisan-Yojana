package com.pmkisanyojana.models;

public class NewsPreveiwModel {
    private final String id;
    private final String newsId;
    private final String desc;

    public NewsPreveiwModel(String id, String newsId, String desc) {
        this.id = id;
        this.newsId = newsId;
        this.desc = desc;
    }

    public String getId() {
        return id;
    }

    public String getNewsId() {
        return newsId;
    }

    public String getDesc() {
        return desc;
    }
}
