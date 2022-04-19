package com.pmkisanyojana.models;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface ApiInterface {


    @POST("fetch_kisan_yojana_api.php")
    Call<YojanaModelList> getAllYojana();

    @POST("fetch_news_api.php")
    Call<NewsModelList> getAllNews();

    @POST("fetch_others_api.php")
    Call<YojanaModelList> getOthersData();

    @POST("fetch_quiz_questions.php")
    Call<QuizModelList> fetchQuizQuestions();



    @FormUrlEncoded
    @POST("fetch_preview_data.php")
    Call<PreviewModelList> getPreview(@FieldMap Map<String, String> map);


    @FormUrlEncoded
    @POST("fetch_user_profile_api.php")
    Call<ProfileModelList> getProfileData(@FieldMap Map<String, String> map);




    @FormUrlEncoded
    @POST("deleteMyStatus.php")
    Call<MessageModel> deleteMyStatus(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("pm_kisan_ads_id_fetch.php")
    Call<AdsModelList> fetchAds(@Field("id") String id);
    @FormUrlEncoded
    @POST()
    Call<MessageModel> getContestRes(@FieldMap Map<String, String> map, @Url String url);

    @GET("fetch_contest_code_data.php")
    Call<ContestCodeModel> getContestData();

}
