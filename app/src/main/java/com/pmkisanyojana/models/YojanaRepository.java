package com.pmkisanyojana.models;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class YojanaRepository {

    private static ApiInterface apiInterface;
    private static YojanaRepository yojanaRepository;
    private final MutableLiveData<YojanaModelList> yojanaModelLiveData = new MutableLiveData<>();
    private final MutableLiveData<NewsModelList> newsModelLiveData = new MutableLiveData<>();
    private final MutableLiveData<YojanaPreviewModelList> yojanaPreviewModelListMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<NewsPreviewModelList> newsPreviewModelListMutableLiveData = new MutableLiveData<>();


    public YojanaRepository() {
        apiInterface = ApiWebServices.getApiInterface();
    }

    public static YojanaRepository getInstance() {
        if (yojanaRepository == null) {
            yojanaRepository = new YojanaRepository();
        }
        return yojanaRepository;
    }

    public LiveData<YojanaModelList> getYojanaModelLiveData() {
        Call<YojanaModelList> call = apiInterface.getAllYojana();
        call.enqueue(new Callback<YojanaModelList>() {
            @Override
            public void onResponse(@NonNull Call<YojanaModelList> call, @NonNull Response<YojanaModelList> response) {

                if (response.isSuccessful()) {
                    yojanaModelLiveData.setValue(response.body());
                } else {
                    Log.d("onResponse", response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<YojanaModelList> call, @NonNull Throwable t) {
                Log.d("onResponse error", t.getMessage());

            }
        });
        return yojanaModelLiveData;
    }

    public LiveData<NewsModelList> getNewsLiveData() {
        Call<NewsModelList> call = apiInterface.getAllNews();
        call.enqueue(new Callback<NewsModelList>() {
            @Override
            public void onResponse(@NonNull Call<NewsModelList> call, @NonNull Response<NewsModelList> response) {

                if (response.isSuccessful()) {
                    newsModelLiveData.setValue(response.body());
                } else {
                    Log.d("onResponse", response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<NewsModelList> call, @NonNull Throwable t) {
                Log.d("onResponse error", t.getMessage());

            }
        });
        return newsModelLiveData;
    }

    public LiveData<YojanaPreviewModelList> getYojanaPreviewLiveData(Map<String, String> map) {
        Call<YojanaPreviewModelList> call = apiInterface.getYojanaPreview(map);
        call.enqueue(new Callback<YojanaPreviewModelList>() {
            @Override
            public void onResponse(@NonNull Call<YojanaPreviewModelList> call, @NonNull Response<YojanaPreviewModelList> response) {
                if (response.isSuccessful()) {
                    yojanaPreviewModelListMutableLiveData.setValue(response.body());
                } else {
                    Log.d("onResponse", response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<YojanaPreviewModelList> call, @NonNull Throwable t) {
                Log.d("onResponse error", t.getMessage());

            }
        });
        return yojanaPreviewModelListMutableLiveData;
    }

    public LiveData<NewsPreviewModelList> getNewsPreviewLiveData(Map<String, String> map) {
        Call<NewsPreviewModelList> call = apiInterface.getNewsPreview(map);
        call.enqueue(new Callback<NewsPreviewModelList>() {
            @Override
            public void onResponse(@NonNull Call<NewsPreviewModelList> call, @NonNull Response<NewsPreviewModelList> response) {
                if (response.isSuccessful()) {
                    newsPreviewModelListMutableLiveData.setValue(response.body());
                } else {
                    Log.d("onResponse", response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<NewsPreviewModelList> call, @NonNull Throwable t) {
                Log.d("onResponse error", t.getMessage());

            }
        });
        return newsPreviewModelListMutableLiveData;
    }


}
