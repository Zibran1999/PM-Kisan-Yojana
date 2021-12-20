package com.pmkisanyojana.models;

import java.util.List;

public class ProfileModelList {
    private List<ProfileModel> data = null;

    public ProfileModelList(List<ProfileModel> data) {
        this.data = data;
    }

    public List<ProfileModel> getData() {
        return data;
    }
}
