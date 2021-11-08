package com.pmkisanyojanaadmin.model;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST("fetch_kisan_yojana_api.php")
    Call<YojanaModelList> getAllYojana();

    @FormUrlEncoded
    @POST("upload_kisan_yojna_api.php")
    Call<MessageModel> uploadYojna(@FieldMap Map<String,String> map);

    @FormUrlEncoded
    @POST("upload_news_api.php")
    Call<MessageModel> uploadNews(@FieldMap Map<String,String> map);


}
