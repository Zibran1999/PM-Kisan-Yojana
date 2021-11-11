package com.pmkisanyojanaadmin.model;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST("fetch_kisan_yojana_api.php")
    Call<YojanaModelList> getAllYojana();

    @POST("fetch_others_api.php")
    Call<YojanaModelList> getAllOthers();

    @POST("fetch_news_api.php")
    Call<NewsModelList> getAllNews();

    @FormUrlEncoded
    @POST("upload_kisan_yojna_api.php")
    Call<MessageModel> uploadYojana(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("upload_others_api.php")
    Call<MessageModel> uploadOthers(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("upload_news_api.php")
    Call<MessageModel> uploadNews(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("upload_preview_data.php")
    Call<MessageModel> uploadPreviewData(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("fetch_preview_data.php")
    Call<PreviewModelList> getPreview(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("update_news_api.php")
    Call<MessageModel> updateNews(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("update_yojana_api.php")
    Call<MessageModel> updateYojana(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("update_yojana_preview_api.php")
    Call<MessageModel> updateYojanaPreview(@FieldMap Map<String, String> map);

}
