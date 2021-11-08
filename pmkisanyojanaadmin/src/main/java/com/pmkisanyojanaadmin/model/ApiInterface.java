package com.pmkisanyojanaadmin.model;

import retrofit2.Call;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST("fetch_kisan_yojana_api.php")
    Call<YojanaModelList> getAllYojana();
}
