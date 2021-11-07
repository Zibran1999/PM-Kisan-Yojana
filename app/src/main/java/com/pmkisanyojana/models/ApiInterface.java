package com.pmkisanyojana.models;

import retrofit2.Call;
import retrofit2.http.POST;

public interface ApiInterface {


    @POST("")
    Call<YojanaModel> getAllYojana();
}
