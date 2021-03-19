package com.example.myapplication.service;

import com.example.myapplication.config.BasicToken;
import com.example.myapplication.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * This interface contains the logic of the application used for User
 */
public interface UserService {
    @POST("user/")
    Call<Void> saveOrUpdate(@Body User user, @Header(value = "Authorization") String value);

    @POST("user/create-user")
    Call<Void> createUser(@Body User user);

    @POST("user/login")
    Call<BasicToken> login(@Body User user);

    @GET("user/")
    Call<List<User>> getAll(@Header(value = "Authorization") String value);

    @GET("user/{id}")
    Call<User> getById(@Path("id") Long id, @Header(value = "Authorization") String value);
}
