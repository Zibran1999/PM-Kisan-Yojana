package com.pmkisanyojana.models;

import java.util.List;

public class NewsPreviewModelList {

    private List<NewsPreveiwModel> data =null;

    public NewsPreviewModelList(List<NewsPreveiwModel> data) {
        this.data = data;
    }

    public List<NewsPreveiwModel> getData() {
        return data;
    }
}
