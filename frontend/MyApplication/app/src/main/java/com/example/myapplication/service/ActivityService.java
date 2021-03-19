package com.example.myapplication.service;


import com.example.myapplication.model.Activity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * This interface contains the logic of the application used for Activity
 */
public interface ActivityService {
    @POST("activity/")
    Call<Void> saveOrUpdate(@Body Activity activity, @Header(value = "Authorization") String value);

    @GET("activity/")
    Call<List<Activity>> getAll(@Header(value = "Authorization") String value);

    @GET("activity/person/{personId}")
    Call<List<Activity>> getByPersonId(@Path("personId") Long personId, @Header(value = "Authorization") String value);

    @DELETE("activity/{id}")
    Call<Void> deleteById(@Path("id") Long id, @Header(value = "Authorization") String value);

}
