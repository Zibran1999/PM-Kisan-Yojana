package com.pmkisanyojana.models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.pmkisanyojana.activities.ui.main.PageViewModel;

import java.util.Map;

public class ModelFactory implements ViewModelProvider.Factory {

    Application application;
    Map<String,String> map;

    public ModelFactory(Application application, Map<String, String> map) {
        this.application = application;
        this.map = map;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> aClass) {
        return (T) new PageViewModel(application,map);
    }
}
