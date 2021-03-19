package com.example.myapplication.service;

import com.example.myapplication.model.Role;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * This interface contains the logic of the application used for Role
 */
public interface RoleService {

    @POST("role/")
    Call<Role> saveOrUpdate(@Body Role role, @Header(value = "Authorization") String value);
}
