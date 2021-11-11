package com.pmkisanyojanaadmin.model;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


import java.util.Map;

public class PageViewModel extends AndroidViewModel {
    private final YojanaRepository yojanaRepository;
    Map<String, String> map;

    public PageViewModel(@NonNull Application application) {
        super(application);
        yojanaRepository = YojanaRepository.getInstance();
    }

    public LiveData<YojanaModelList> geYojanaList() {
        return yojanaRepository.getYojanaModelLiveData();
    }

    public LiveData<NewsModelList> getNews() {
        return yojanaRepository.getNewsLiveData();
    }

    public LiveData<YojanaModelList> getAllOthers() {
        return yojanaRepository.getOtherModelLiveData();
    }

}