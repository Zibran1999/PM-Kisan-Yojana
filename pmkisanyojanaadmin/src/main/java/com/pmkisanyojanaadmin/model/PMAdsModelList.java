package com.pmkisanyojanaadmin.model;

import java.util.List;

public class PMAdsModelList {
    List<PMAdsModel> data = null;

    public PMAdsModelList(List<PMAdsModel> data) {
        this.data = data;
    }

    public List<PMAdsModel> getData() {
        return data;
    }
}
