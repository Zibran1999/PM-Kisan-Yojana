package com.pmkisanyojana.models;

import java.util.List;

public class StatusViewModelList {
    List<StatusViewModel> data = null;

    public StatusViewModelList(List<StatusViewModel> data) {
        this.data = data;
    }

    public List<StatusViewModel> getData() {
        return data;
    }
}
