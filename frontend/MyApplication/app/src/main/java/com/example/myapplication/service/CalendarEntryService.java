package com.example.myapplication.service;

import com.example.myapplication.model.CalendarEntry;
import com.example.myapplication.model.CompletedEntry;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * This interface contains the logic of the application used for CalendarEntry
 */
public interface CalendarEntryService {
    @POST("calendar-entry/")
    Call<Void> saveOrUpdate(@Body CalendarEntry calendarEntry, @Header(value = "Authorization") String value);

    @POST("calendar-entry/complete-activity")
    Call<CompletedEntry> completeActivity(@Body CompletedEntry completedEntry, @Header(value = "Authorization") String value);

    @PUT("calendar-entry/person-day")
    Call<List<CalendarEntry>> getCalendarEntriesByPersonAndDay(@Body CalendarEntry calendarEntry, @Header(value = "Authorization") String value);

    @DELETE("calendar-entry/{id}")
    Call<Void> deleteById(@Path("id") Long id, @Header(value = "Authorization") String value);
}
