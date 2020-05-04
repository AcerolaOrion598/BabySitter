package com.djaphar.babysitter.SupportClasses.ApiClasses;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiBuilder {

    public static MainApi getMainApi() {
        return new Retrofit
                .Builder()
                .baseUrl("http://82.148.31.234:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MainApi.class);
    }
}
