package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.config.ErrorDialog;
import com.example.myapplication.config.RestClient;
import com.example.myapplication.model.CalendarEntry;
import com.example.myapplication.model.CompletedEntry;
import com.example.myapplication.model.User;
import com.example.myapplication.service.CalendarEntryService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This is the main entity we will be using to display the creation page of a completed entry
 */
public class CompletedEntryCreationActivity extends AppCompatActivity {

    private User contextUser;
    private String AUTHENTICATION_TOKEN;
    private String date;
    private CalendarEntry calendarEntryDetails;

    private RatingBar ratingBar;
    private SeekBar seekBar;
    private EditText notes;
    private Button button;

    private CompletedEntry completedEntry;

    private CalendarEntryService calendarEntryService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.completed_entry_creation);
        initializeComponents();
    }

    /**
     * This method initializes all the components (buttons, listviews, services) needed
     */
    private void initializeComponents() {
        contextUser = (User) getIntent().getSerializableExtra("user");
        AUTHENTICATION_TOKEN = (String) getIntent().getSerializableExtra("authenticationToken");
        date = (String) getIntent().getSerializableExtra("date");
        calendarEntryDetails = (CalendarEntry) getIntent().getSerializableExtra("calendarEntry");
        calendarEntryService = RestClient.createService(CalendarEntryService.class);

        ratingBar = findViewById(R.id.ratingBar);
        seekBar = findViewById(R.id.seekBar2);
        notes = findViewById(R.id.editTextTextMultiLine3);
        button = findViewById(R.id.button8);

        initializeButton();
    }

    /**
     * This method initializes the existent data for the calendar entry
     */
    private void initializeData() {
        completedEntry = new CompletedEntry();
        completedEntry.setId(calendarEntryDetails.getId());
        completedEntry.setActivity(calendarEntryDetails.getActivity());
        completedEntry.setPerson(calendarEntryDetails.getPerson());
        completedEntry.setDateFrom(calendarEntryDetails.getDateFrom());
        completedEntry.setDateTo(calendarEntryDetails.getDateTo());
        completedEntry.setFinalNotes(notes.getText().toString());
        completedEntry.setCompletion((long) seekBar.getProgress());
        completedEntry.setRating((double) ratingBar.getRating());
    }

    /**
     * CREATE button listener
     */
    private void initializeButton() {
        button.setOnClickListener(v -> {
            initializeData();
            final Call<CompletedEntry> completedEntryCall = calendarEntryService.completeActivity(completedEntry, AUTHENTICATION_TOKEN);
            completedEntryCall.enqueue(new Callback<CompletedEntry>() {
                @Override
                public void onResponse(Call<CompletedEntry> call, Response<CompletedEntry> response) {
                    if (!response.isSuccessful()) {
                        ErrorDialog.showDialog(CompletedEntryCreationActivity.this, "Error during call");
                    }
                    else {
                        if (response.body() != null) {
                            openCompletedEntriesActivity();
                        }
                    }
                }

                @Override
                public void onFailure(Call<CompletedEntry> call, Throwable t) {
                    ErrorDialog.showDialog(CompletedEntryCreationActivity.this, t.getMessage());
                }
            });
        });
    }
     /**
     * This method is used to send the information needed to see page of all the completed calendar
      *  entries
     */
    private void openCompletedEntriesActivity() {
        Intent intent = new Intent(this, CompletedEntryActivity.class);
        intent.putExtra("user", contextUser);
        intent.putExtra("authenticationToken", AUTHENTICATION_TOKEN);
        intent.putExtra("date", date);
        startActivity(intent);
    }
}
