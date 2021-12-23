package com.pmkisanyojana.models;

import java.util.List;

public class MyStatusModelList {
    List<MyStatusModel> data = null;

    public MyStatusModelList(List<MyStatusModel> data) {
        this.data = data;
    }

    public List<MyStatusModel> getData() {
        return data;
    }
}
