package com.example.myapplication.service;

import com.example.myapplication.model.Person;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * This interface contains the logic of the application used for Person
 */
public interface PersonService {
    @POST("person/")
    Call<Person> saveOrUpdate(@Body Person person, @Header(value = "Authorization") String value);
}
