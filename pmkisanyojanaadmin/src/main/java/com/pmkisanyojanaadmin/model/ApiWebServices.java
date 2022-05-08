package com.pmkisanyojanaadmin.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiWebServices { 

    static Gson gson = new GsonBuilder()
            .setLenient()
            .create();
    private static final String base_url = "https://gedgetsworld.in/PM_Kisan_Yojana/";
    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(base_url)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

    public static ApiInterface getApiInterface() {
        return retrofit.create(ApiInterface.class);
    }
}
