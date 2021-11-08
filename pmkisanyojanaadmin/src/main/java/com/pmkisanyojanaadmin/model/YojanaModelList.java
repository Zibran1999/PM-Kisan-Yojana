package com.pmkisanyojanaadmin.model;

import java.util.List;

public class YojanaModelList {

    private List<YojanaModel> data = null;

    public YojanaModelList(List<YojanaModel> data) {
        this.data = data;
    }

    public List<YojanaModel> getData() {
        return data;
    }
}
