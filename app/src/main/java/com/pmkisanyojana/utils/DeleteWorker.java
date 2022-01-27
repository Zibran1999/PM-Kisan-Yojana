package com.pmkisanyojana.utils;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.pmkisanyojana.models.ApiInterface;
import com.pmkisanyojana.models.ApiWebServices;
import com.pmkisanyojana.models.MessageModel;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeleteWorker extends Worker {

    Map<String, String> map = new HashMap<>();
    private String id, img;

    public DeleteWorker(@NonNull Context context, @NonNull WorkerParameters workerParams, String statusId, String statusImg) {
        super(context, workerParams);
        id = statusId;
        img = statusImg;

    }

    @NonNull
    @Override
    public Result doWork() {
        map.put("statusId", id);
        map.put("statusImg", "User_Status_Images/" + img);
        deleteMyStatus(map);
        return Result.success();
    }

    private void deleteMyStatus(Map<String, String> map) {
        ApiInterface apiInterface = ApiWebServices.getApiInterface();
        Call<MessageModel> call = apiInterface.deleteMyStatus(map);
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(@NonNull Call<MessageModel> call, @NonNull Response<MessageModel> response) {

                assert response.body() != null;
                if (response.isSuccessful()) {

                    Log.d("deleteStatus", response.body().getMessage());

                } else {
                    Log.d("StatusError", response.body().getMessage());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MessageModel> call, @NonNull Throwable t) {
                Log.d("deleteStatusError", t.getMessage());


            }
        });

    }

}
