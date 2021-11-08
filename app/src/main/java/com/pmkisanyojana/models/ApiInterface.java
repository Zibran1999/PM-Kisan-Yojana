package com.pmkisanyojana.models;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {


    @POST("fetch_kisan_yojana_api.php")
    Call<YojanaModelList> getAllYojana();

    @POST("fetch_news_api.php")
    Call<NewsModelList> getAllNews();

    @FormUrlEncoded
    @POST("fetch_yojana_preview_data.php")
    Call<YojanaPreviewModelList> getYojanaPreview(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("fetch_news_preview_data.php")
    Call<NewsPreviewModelList> getNewsPreview(@FieldMap Map<String, String> map);

}
