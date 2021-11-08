package com.pmkisanyojana.models;

public class YojanaPreviewModel {
    private final String id;
    private final String yojanaId;
    private final String desc;
    private final String url;


    public YojanaPreviewModel(String id, String yojanaId, String desc, String url) {
        this.id = id;
        this.yojanaId = yojanaId;
        this.desc = desc;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public String getYojanaId() {
        return yojanaId;
    }

    public String getDesc() {
        return desc;
    }

    public String getUrl() {
        return url;
    }
}
