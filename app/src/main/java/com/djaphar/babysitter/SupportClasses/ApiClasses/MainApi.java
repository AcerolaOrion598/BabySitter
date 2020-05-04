package com.djaphar.babysitter.SupportClasses.ApiClasses;

import com.djaphar.babysitter.SupportClasses.LocalDataClasses.User;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface MainApi {

    @FormUrlEncoded
    @POST("token/educator")
    Call<User> requestLogin(@Field("username") String username, @Field("password") String password);
}
