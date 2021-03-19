package com.example.myapplication.service;

import com.example.myapplication.model.Category;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * This interface contains the logic of the application used for Category
 */
public interface CategoryService {
    @POST("category/")
    Call<Void> saveOrUpdate(@Body Category category, @Header(value = "Authorization") String value);

    @GET("category/")
    Call<List<Category>> getAll(@Header(value = "Authorization") String value);

    @DELETE("category/{id}")
    Call<Category> deleteById(@Path("id") Long id, @Header(value = "Authorization") String value);
}
