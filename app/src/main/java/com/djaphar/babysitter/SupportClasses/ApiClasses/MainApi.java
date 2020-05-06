package com.djaphar.babysitter.SupportClasses.ApiClasses;

import com.djaphar.babysitter.SupportClasses.LocalDataClasses.User;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MainApi {

    @FormUrlEncoded
    @POST("token/educator/")
    Call<User> requestLogin(@Field("username") String username, @Field("password") String password);

    @GET("children")
    Call<ArrayList<Child>> requestChildrenList(@HeaderMap Map<String, String> headers);

    @GET("children/{id}/")
    Call<Child> requestSingleChild(@HeaderMap Map<String, String> headers, @Path("id") String id);

    @POST("children/")
    Call<Void> requestCreateChild(@HeaderMap Map<String, String> headers, @Body Child child);

    @GET("foods")
    Call<ArrayList<Food>> requestMyFoods(@HeaderMap Map<String, String> headers);
}
