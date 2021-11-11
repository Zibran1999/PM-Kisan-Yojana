package com.pmkisanyojana.activities.ui.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.pmkisanyojana.models.NewsModelList;
import com.pmkisanyojana.models.YojanaModelList;
import com.pmkisanyojana.models.PreviewModelList;
import com.pmkisanyojana.models.YojanaRepository;

import java.util.Map;

public class PageViewModel extends AndroidViewModel {
    private final YojanaRepository yojanaRepository;
    Map<String, String> map;

    public PageViewModel(@NonNull Application application) {
        super(application);
        yojanaRepository = YojanaRepository.getInstance();
    }

    public PageViewModel(@NonNull Application application, Map<String, String> map) {
        super(application);
        this.map = map;
        yojanaRepository = YojanaRepository.getInstance();

    }

    public LiveData<YojanaModelList> geYojanaList() {
        return yojanaRepository.getYojanaModelLiveData();
    }

    public LiveData<YojanaModelList> getOthersData() {
        return yojanaRepository.getOthersLiveData();
    }

    public LiveData<NewsModelList> getNews() {
        return yojanaRepository.getNewsLiveData();
    }

    public LiveData<PreviewModelList> getPreviewData() {
        return yojanaRepository.getPreviewLiveData(map);
    }


}