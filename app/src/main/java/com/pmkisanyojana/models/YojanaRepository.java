package com.pmkisanyojana.models;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class YojanaRepository {

    private static ApiInterface apiInterface;
    private static YojanaRepository yojanaRepository;
    private final MutableLiveData<YojanaModel> yojanaModelLiveData = new MutableLiveData<>();

    public YojanaRepository() {
        apiInterface = ApiWebServices.getApiInterface();
    }

    public static YojanaRepository getInstance() {
        if (yojanaRepository == null) {
            yojanaRepository = new YojanaRepository();
        }
        return yojanaRepository;
    }

    public LiveData<YojanaModel> getYojanaModelLiveData() {
        Call<YojanaModel> call = apiInterface.getAllYojana();
        call.enqueue(new Callback<YojanaModel>() {
            @Override
            public void onResponse(@NonNull Call<YojanaModel> call, @NonNull Response<YojanaModel> response) {

                if (response.isSuccessful()) {
                    yojanaModelLiveData.setValue(response.body());
                }else {
                    Log.d("onResponse",response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<YojanaModel> call, @NonNull Throwable t) {
                Log.d("onResponse error",t.getMessage());

            }
        });
        return yojanaModelLiveData;
    }
}
