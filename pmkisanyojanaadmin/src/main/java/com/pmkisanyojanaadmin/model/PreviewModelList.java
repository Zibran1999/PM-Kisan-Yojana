package com.pmkisanyojanaadmin.model;

import java.util.List;

public class PreviewModelList {

    private List<PreviewModel> data =null;

    public PreviewModelList(List<PreviewModel> data) {
        this.data = data;
    }

    public List<PreviewModel> getData() {
        return data;
    }
}
