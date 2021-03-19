package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.config.ErrorDialog;
import com.example.myapplication.config.RestClient;
import com.example.myapplication.model.CalendarEntry;
import com.example.myapplication.model.User;
import com.example.myapplication.service.CalendarEntryService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This is the main entity we will be using to display the CalendarEntry details in GUI
 */
public class CalendarEntryDetails extends AppCompatActivity {

    private User contextUser;
    private String AUTHENTICATION_TOKEN;
    private String date;

    private CalendarEntry calendarEntryDetails;

    private CalendarEntryService calendarEntryService;

    private EditText name;
    private EditText desc;
    private EditText dateFrom;
    private EditText dateTo;
    private EditText obs;
    private EditText goal;
    private Button complete;
    private Button edit;
    private Button delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_calendar_entry);
        initializeComponents();
    }

    /**
     * This method initializes all the components (buttons, listviews, services) needed
     */
    private void initializeComponents() {
        calendarEntryService = RestClient.createService(CalendarEntryService.class);

        contextUser = (User) getIntent().getSerializableExtra("user");
        AUTHENTICATION_TOKEN = (String) getIntent().getSerializableExtra("authenticationToken");
        date = (String) getIntent().getSerializableExtra("date");
        calendarEntryDetails = (CalendarEntry) getIntent().getSerializableExtra("calendarEntry");

        name = findViewById(R.id.editTextTextPersonName11);
        desc = findViewById(R.id.editTextTextMultiLine5);
        dateFrom = findViewById(R.id.calendar_entry_dateFrom);
        dateTo = findViewById(R.id.editTextDate6);
        obs = findViewById(R.id.editTextTextMultiLine6);
        goal = findViewById(R.id.editTextTextMultiLine7);
        complete = findViewById(R.id.button);
        edit = findViewById(R.id.calendar_entry_saveBtn);
        delete = findViewById(R.id.button2);

        initializeData();
        initializeCompleteListener();
        initializeDeleteListener();
        initializeEditListener();
    }

    /**
     * This method initializes the existent data for the calendar entry
     */
    private void initializeData() {
        name.setText(calendarEntryDetails.getActivity().getName());
        desc.setText(calendarEntryDetails.getActivity().getDescription());
        dateFrom.setText(calendarEntryDetails.getDateFrom().split(" ")[1]);
        dateTo.setText(calendarEntryDetails.getDateTo().split(" ")[1]);
        obs.setText(calendarEntryDetails.getObservation());
        goal.setText(calendarEntryDetails.getGoals());
    }

    /**
     * EDIT button listener
     */
    private void initializeEditListener() {
        edit.setOnClickListener(v -> {
            openAddEditActivity();
        });
    }

    /**
     * This method is used to send the information needed to see the AddEditCalendarEntry page
     */
    private void openAddEditActivity() {
        Intent intent = new Intent(this, AddEditCalendarEntryActivity.class);
        intent.putExtra("user", contextUser);
        intent.putExtra("calendarEntry", calendarEntryDetails);
        intent.putExtra("authenticationToken", AUTHENTICATION_TOKEN);
        intent.putExtra("date", date);
        startActivity(intent);
    }

    /**
     * DELETE button listener
     */
    private void initializeDeleteListener() {
        delete.setOnClickListener(v -> {
            final Call<Void> deleteById = calendarEntryService.deleteById(calendarEntryDetails.getId(), AUTHENTICATION_TOKEN);
            deleteById.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (!response.isSuccessful()) {
                        ErrorDialog.showDialog(CalendarEntryDetails.this, "Error");
                    }
                    else {
                        openCalendarEntriesActivity();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    ErrorDialog.showDialog( CalendarEntryDetails.this, t.getMessage());
                }
            });
        });
    }

    /**
     * This method is used to send the information needed to see the calendar entries page
     */
    private void openCalendarEntriesActivity() {
        Intent intent = new Intent(this, CalendarEntriesActivity.class);
        intent.putExtra("user", contextUser);
        intent.putExtra("authenticationToken", AUTHENTICATION_TOKEN);
        intent.putExtra("date", date);
        startActivity(intent);
    }

    /**
     * COMPLETE button listener
     */
    private void initializeCompleteListener() {
        complete.setOnClickListener(v -> {
            openCompleteEntryCreationActivity();
        });
    }

    /**
     * This method is used to send the information needed to see the CompleteCalendarEntry page
     */
    private void openCompleteEntryCreationActivity() {
        Intent intent = new Intent(this, CompletedEntryCreationActivity.class);
        intent.putExtra("user", contextUser);
        intent.putExtra("authenticationToken", AUTHENTICATION_TOKEN);
        intent.putExtra("calendarEntry", calendarEntryDetails);
        intent.putExtra("date", date);
        startActivity(intent);
    }
}
