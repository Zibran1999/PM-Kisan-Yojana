package com.pmkisanyojana.models;

import java.util.List;

public class PreviewModelList {
     List<PreviewModel> data = null;


    public PreviewModelList(List<PreviewModel> data) {
        this.data = data;
    }

    public List<PreviewModel> getData() {
        return data;
    }
}
