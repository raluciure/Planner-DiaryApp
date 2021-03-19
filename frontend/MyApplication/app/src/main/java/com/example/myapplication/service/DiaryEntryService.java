package com.example.myapplication.service;

import com.example.myapplication.model.DiaryEntry;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

/**
 * This interface contains the logic of the application used for DiaryEntry
 */
public interface DiaryEntryService {
    @POST("diary-entry/")
    Call<Void> saveOrUpdate(@Body DiaryEntry diaryEntry, @Header(value = "Authorization") String value);

    @PUT("diary-entry/person-day")
    Call<DiaryEntry> getDiaryEntryByPersonAndDay(@Body DiaryEntry diaryEntry, @Header(value = "Authorization") String value);
}
