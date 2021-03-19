package com.example.myapplication.service;

import com.example.myapplication.model.CompletedEntry;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

/**
 * This interface contains the logic of the application used for CompletedEntry
 */
public interface CompletedEntryService {

    @POST("completed-entry/")
    Call<CompletedEntry> saveOrUpdate(@Body CompletedEntry completedEntry, @Header(value = "Authorization") String value);

    @PUT("completed-entry/person-day")
    Call<List<CompletedEntry>> getCompletedEntriesByPersonAndDay(@Body CompletedEntry completedEntry, @Header(value = "Authorization") String value);
}
