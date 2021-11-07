package com.pmkisanyojana.activities.ui.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.pmkisanyojana.models.YojanaModel;
import com.pmkisanyojana.models.YojanaRepository;

public class PageViewModel extends AndroidViewModel {
    private final YojanaRepository yojanaRepository;

    public PageViewModel(@NonNull Application application) {
        super(application);
        yojanaRepository = YojanaRepository.getInstance();
    }

    public LiveData<YojanaModel> geYojanaList() {
        return yojanaRepository.getYojanaModelLiveData();
    }
}