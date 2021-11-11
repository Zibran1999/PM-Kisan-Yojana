package com.pmkisanyojana.models;

public class PreviewModel {
    private final String id;
    private final String previewId;
    private final String desc;


    public PreviewModel(String id, String previewId, String desc) {
        this.id = id;
        this.previewId = previewId;
        this.desc = desc;
    }

    public String getId() {
        return id;
    }

    public String getPreviewId() {
        return previewId;
    }

    public String getDesc() {
        return desc;
    }
}
