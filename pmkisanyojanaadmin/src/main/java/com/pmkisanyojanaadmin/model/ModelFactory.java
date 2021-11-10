package com.pmkisanyojanaadmin.model;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;


import java.util.Map;

public class ModelFactory implements ViewModelProvider.Factory {

    Application application;
    Map<String,String> yojanaMap;

    public ModelFactory(Application application, Map<String, String> map) {
        this.application = application;
        yojanaMap = map;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> aClass) {
        Log.d("checkMap",yojanaMap.get("yojanaId"));

        return (T) new YojanaViewModel(application, yojanaMap);
    }
}
