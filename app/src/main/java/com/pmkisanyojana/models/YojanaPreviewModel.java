package com.pmkisanyojana.models;

public class YojanaPreviewModel {
    private final String id;
    private final String yojanaId;
    private final String desc;


    public YojanaPreviewModel(String id, String yojanaId, String desc) {
        this.id = id;
        this.yojanaId = yojanaId;
        this.desc = desc;
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
}
