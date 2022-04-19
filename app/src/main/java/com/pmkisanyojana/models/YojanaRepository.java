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
    private final MutableLiveData<PreviewModelList> previewModelListMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<YojanaModelList> otherLiveData = new MutableLiveData<>();
    private final MutableLiveData<QuizModelList> quizModelListMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<ProfileModelList> profileLiveData = new MutableLiveData<>();
    private final MutableLiveData<ContestCodeModel> contestCodeMutableLiveData = new MutableLiveData<>();




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

    public LiveData<PreviewModelList> getPreviewLiveData(Map<String, String> map) {
        Call<PreviewModelList> call = apiInterface.getPreview(map);
        call.enqueue(new Callback<PreviewModelList>() {
            @Override
            public void onResponse(@NonNull Call<PreviewModelList> call, @NonNull Response<PreviewModelList> response) {
                if (response.isSuccessful()) {
                    previewModelListMutableLiveData.setValue(response.body());
                } else {
                    Log.d("onResponse", response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<PreviewModelList> call, @NonNull Throwable t) {
                Log.d("onResponse error", t.getMessage());

            }
        });
        return previewModelListMutableLiveData;
    }


    public LiveData<YojanaModelList> getOthersLiveData() {
        Call<YojanaModelList> call = apiInterface.getOthersData();
        call.enqueue(new Callback<YojanaModelList>() {
            @Override
            public void onResponse(@NonNull Call<YojanaModelList> call, @NonNull Response<YojanaModelList> response) {

                if (response.isSuccessful()) {
                    otherLiveData.setValue(response.body());
                } else {
                    Log.d("onResponse", response.message());

                }
            }

            @Override
            public void onFailure(@NonNull Call<YojanaModelList> call, @NonNull Throwable t) {
                Log.d("onResponse error", t.getMessage());

            }
        });
        return otherLiveData;
    }

    public LiveData<QuizModelList> getQuizQuestions(){
        Call<QuizModelList> call = apiInterface.fetchQuizQuestions();
        call.enqueue(new Callback<QuizModelList>() {
            @Override
            public void onResponse(@NonNull Call<QuizModelList> call, @NonNull Response<QuizModelList> response) {
                if (response.isSuccessful()) {
                    quizModelListMutableLiveData.setValue(response.body());
                } else {
                    Log.d("onResponse", response.message());

                }
            }

            @Override
            public void onFailure(@NonNull Call<QuizModelList> call, @NonNull Throwable t) {
                Log.d("onResponse error", t.getMessage());

            }
        });

        return quizModelListMutableLiveData;
    }

    public LiveData<ProfileModelList> getProfileLiveData(Map<String,String> map) {
        Call<ProfileModelList> call = apiInterface.getProfileData(map);
        call.enqueue(new Callback<ProfileModelList>() {
            @Override
            public void onResponse(@NonNull Call<ProfileModelList> call, @NonNull Response<ProfileModelList> response) {
                if (response.isSuccessful()) {
                    profileLiveData.setValue(response.body());
                } else {
                    Log.d("onResponse", response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProfileModelList> call, @NonNull Throwable t) {
                Log.d("onResponse error", t.getMessage());
            }
        });

        return profileLiveData;
    }

    public MutableLiveData<ContestCodeModel> getCodeModelMutableLiveData() {
        Call<ContestCodeModel> call = apiInterface.getContestData();
        call.enqueue(new Callback<ContestCodeModel>() {
            @Override
            public void onResponse(@NonNull Call<ContestCodeModel> call, @NonNull Response<ContestCodeModel> response) {

                if (response.isSuccessful()) {
                    contestCodeMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ContestCodeModel> call, @NonNull Throwable t) {

            }
        });
        return contestCodeMutableLiveData;
    }

}
