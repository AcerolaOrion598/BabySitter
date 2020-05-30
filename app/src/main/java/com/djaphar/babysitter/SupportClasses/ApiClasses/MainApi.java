package com.djaphar.babysitter.SupportClasses.ApiClasses;

import com.djaphar.babysitter.SupportClasses.LocalDataClasses.User;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MainApi {

    @FormUrlEncoded
    @POST("token/educator/")
    Call<User> requestLogin(@Field("username") String username, @Field("password") String password);

    @GET("children/")
    Call<ArrayList<Child>> requestChildrenList(@HeaderMap Map<String, String> headers);

    @GET("children/{id}/")
    Call<Child> requestSingleChild(@HeaderMap Map<String, String> headers, @Path("id") String id);

    @POST("children/")
    Call<Void> requestCreateChild(@HeaderMap Map<String, String> headers, @Body Child child);

    @PUT("children/{id}/")
    Call<Child> requestUpdateChild(@HeaderMap Map<String, String> headers, @Path("id") String id, @Body Child child);

    @DELETE("children/{id}/")
    Call<Void> requestDeleteChild(@HeaderMap Map<String, String> headers, @Path("id") String id);

    @GET("events/")
    Call<Event> requestEvent(@HeaderMap Map<String, String> headers, @Query("child_id") String child_id, @Query("date") String date);

    @POST("events/")
    Call<Void> requestUpdateEvent(@HeaderMap Map<String, String> headers, @Body Event event);

    @GET("foods/")
    Call<ArrayList<Food>> requestMyFoods(@HeaderMap Map<String, String> headers);

    @POST("foods/")
    Call<Void> requestCreateFood(@HeaderMap Map<String,String> headers, @Body Food food);

    @DELETE("foods/{id}/")
    Call<Void> requestDeleteFood(@HeaderMap Map<String, String> headers, @Path("id") String id);

    @POST("photos/")
    Call<Void> requestUpdatePicture(@HeaderMap Map<String, String> headers, @Body UpdatePictureModel updatePictureModel);

    @HTTP(method = "DELETE", path = "photos/", hasBody = true)
    Call<Child> requestDeletePicture(@HeaderMap Map<String, String> headers, @Body UpdatePictureModel updatePictureModel);

    @GET("bill/")
    Call<ArrayList<Bill>> requestMyBills(@HeaderMap Map<String, String> headers);

    @POST("bill/")
    Call<Void> requestCreateBill(@HeaderMap Map<String, String> headers, @Body BillPostModel billPostModel);

    @GET("bill/{bill_id}/")
    Call<Bill> requestSingleBill(@HeaderMap Map<String, String> headers, @Path("bill_id") String bill_id);

    @PUT("bill/{bill_id}/")
    Call<Void> requestUpdateBill(@HeaderMap Map<String, String> headers, @Path("bill_id") String bill_id, @Body BillPostModel billPostModel);

    @DELETE("bill/{bill_id}/")
    Call<Void> requestDeleteBill(@HeaderMap Map<String, String> headers, @Path("bill_id") String bill_id);

    @GET("gallery/")
    Call<ArrayList<GalleryPicture>> requestMyGallery(@HeaderMap Map<String, String> headers);

    @POST("gallery/")
    Call<Void> requestAddGalleryPicture(@HeaderMap Map<String, String> headers, @Body UpdatePictureModel updatePictureModel);

    @DELETE("gallery/gallery_id/")
    Call<Void> requestDeleteGalleryPicture(@HeaderMap Map<String, String> headers, @Query("gallery_id") String gallery_id);
}
