package com.pmkisanyojana.models;

import java.util.List;

public class StatusModelList {

    List<StatusModel> data = null;

    public StatusModelList(List<StatusModel> data) {
        this.data = data;
    }

    public List<StatusModel> getData() {
        return data;
    }
}
