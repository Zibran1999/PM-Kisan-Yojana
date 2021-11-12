package com.pmkisanyojanaadmin.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.Map;

public class YojanaViewModel extends AndroidViewModel {

    private final YojanaRepository yojanaRepository;
    Map<String, String> map;

    public YojanaViewModel(@NonNull Application application, Map<String, String> map) {
        super(application);
        this.map = map;
        yojanaRepository = YojanaRepository.getInstance();

    }

    public LiveData<PreviewModelList> getPreviewData() {
        return yojanaRepository.getPreviewLiveData(map);
    }



}
