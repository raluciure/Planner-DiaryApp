package com.example.myapplication.activities;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.config.ErrorDialog;
import com.example.myapplication.config.RestClient;
import com.example.myapplication.model.Activity;
import com.example.myapplication.model.CalendarEntry;
import com.example.myapplication.model.Person;
import com.example.myapplication.model.User;
import com.example.myapplication.service.ActivityService;
import com.example.myapplication.service.CalendarEntryService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This is the main entity we will be using to display the Add/EditCalendarEntry GUI
 */
public class AddEditCalendarEntryActivity extends AppCompatActivity {

    private User contextUser;
    private String AUTHENTICATION_TOKEN;
    private CalendarEntryService calendarEntryService;
    private ActivityService activityService;
    private String date;
    private String dateHeader;

    private Spinner activitySpinner;
    private EditText dateFrom;
    private EditText dateTo;
    private EditText obs;
    private EditText goal;
    private Button save;

    private CalendarEntry calendarEntryDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_entry_creation);
        initializeComponents();
    }

    /**
     * This method initializes all the components (buttons, listviews, services) needed
     */
    private void initializeComponents() {
        contextUser = (User) getIntent().getSerializableExtra("user");
        AUTHENTICATION_TOKEN = (String) getIntent().getSerializableExtra("authenticationToken");
        date = (String) getIntent().getSerializableExtra("date");
        dateHeader = date.split(" ")[0];
        calendarEntryService = RestClient.createService(CalendarEntryService.class);
        activityService = RestClient.createService(ActivityService.class);
        calendarEntryDetails = (CalendarEntry) getIntent().getSerializableExtra("calendarEntry");

        activitySpinner = findViewById(R.id.activities_spinner);
        dateFrom = findViewById(R.id.calendar_entry_dateFrom);
        dateTo = findViewById(R.id.calendar_entry_dateTo);
        obs = findViewById(R.id.calendar_entry_observation);
        goal = findViewById(R.id.calendar_entry_goal);
        save = findViewById(R.id.calendar_entry_saveBtn);

        initializeDateFromListener();
        initializeDateToListener();
        initializeSpinner();
        initializeSaveListener();
        initializeData();
    }

    /**
     * This method initializes the existent data for the calendar entry
     */
    private void initializeData() {
        if(calendarEntryDetails != null) {
            dateFrom.setText(calendarEntryDetails.getDateFrom());
            dateTo.setText(calendarEntryDetails.getDateTo());
            goal.setText(calendarEntryDetails.getGoals());
            obs.setText(calendarEntryDetails.getObservation());
        }
    }

    /**
     * SAVE button listener
     */
    private void initializeSaveListener() {
        save.setOnClickListener(v -> {
            final CalendarEntry newCalendarEntry = new CalendarEntry();
            if(calendarEntryDetails != null) {
                newCalendarEntry.setId(calendarEntryDetails.getId());
            }
            newCalendarEntry.setDateFrom(dateFrom.getText().toString());
            newCalendarEntry.setDateTo(dateTo.getText().toString());
            newCalendarEntry.setGoals(goal.getText().toString());
            newCalendarEntry.setObservation(obs.getText().toString());
            final Person person = new Person();
            person.setId(contextUser.getPerson().getId());
            newCalendarEntry.setPerson(person);
            final Activity activity = new Activity();
            activity.setName(activitySpinner.getSelectedItem().toString());
            if(activity.getName().equals("Activities")) {
                TextView errorText = (TextView) activitySpinner.getSelectedView();
                errorText.setError("Error");
                errorText.setTextColor(Color.RED);
                errorText.setText("Activity not selected!");
            }
            else {
                newCalendarEntry.setActivity(activity);
                addEditCalendarEntry(newCalendarEntry);
            }
        });
    }

    /**
     * This method gets the calendar entry and sends it to the add/edit page
     */
    private void addEditCalendarEntry(final CalendarEntry calendarEntry){
        final Call<Void> calendarEntryCall = calendarEntryService.saveOrUpdate(calendarEntry, AUTHENTICATION_TOKEN);
        calendarEntryCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    ErrorDialog.showDialog(AddEditCalendarEntryActivity.this, "Error during call");
                }
                else {
                    openCalendarEntryActivity();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                ErrorDialog.showDialog(AddEditCalendarEntryActivity.this, t.getMessage());
            }
        });
    }

    /**
     * This method is used to send the information needed to see page of all the calendar entries
     */
    private void openCalendarEntryActivity() {
        Intent intent = new Intent(this, CalendarEntriesActivity.class);
        intent.putExtra("user", contextUser);
        intent.putExtra("authenticationToken", AUTHENTICATION_TOKEN);
        intent.putExtra("date", date);
        startActivity(intent);
    }

    /**
     * This method is used to initialize the spinner used to select an activity to be added in
     * calendar entry
     */
    private void initializeSpinner() {
        Call<List<Activity>> listCall = activityService.getAll(AUTHENTICATION_TOKEN);

        if(listCall == null) {
            TextView errorText = (TextView) activitySpinner.getSelectedView();
            errorText.setError("Error");
            errorText.setTextColor(Color.RED);
        }
        else {
            listCall.enqueue(new Callback<List<Activity>>() {
                @Override
                public void onResponse(Call<List<Activity>> call, Response<List<Activity>> response) {
                    if (!response.isSuccessful()) {
                        ErrorDialog.showDialog(AddEditCalendarEntryActivity.this, "Error");
                    } else {
                        final List<Activity> activities = response.body();
                        if (activities == null) {
                            ErrorDialog.showDialog(AddEditCalendarEntryActivity.this, "Error");
                        }
                        else {
                            final ArrayList<String> arrayList = new ArrayList<>();
                            arrayList.add("Activities");
                            final List<String> activityList = activities.stream().map(Activity::getName).collect(Collectors.toList());
                            arrayList.addAll(activityList);
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(AddEditCalendarEntryActivity.this, android.R.layout.simple_spinner_item, arrayList);
                            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            activitySpinner.setAdapter(arrayAdapter);
                            activitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    activitySpinner.setSelection(position);
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });
                            if (calendarEntryDetails != null) {
                                arrayList.forEach(name -> {
                                    if (name.equals(calendarEntryDetails.getActivity().getName())) {
                                        int position = arrayList.indexOf(name);
                                        activitySpinner.setSelection(position);
                                    }
                                });
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Activity>> call, Throwable t) {
                    ErrorDialog.showDialog(AddEditCalendarEntryActivity.this, t.getMessage());
                }
            });
        }
    }

    /**
     * This method is used to initialize the dateTo for a calendar entry
     */
    private void initializeDateToListener() {
        dateTo.setText(date);
        dateTo.setOnClickListener(v -> {
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(AddEditCalendarEntryActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    String minuteString;
                    if(selectedMinute < 10) {
                        minuteString = "0" + selectedMinute;
                    }
                    else {
                        minuteString = "" + selectedMinute;
                    }
                    String hourString;
                    if(selectedHour < 10) {
                        hourString = "0" + selectedHour;
                    }
                    else {
                        hourString = "" + selectedHour;
                    }
                    dateTo.setText( dateHeader + " " +  hourString + ":" + minuteString  + ":00");
                }
            }, hour, minute, true);
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();
        });
    }

    /**
     * This method is used to initialize the dateFrom for a calendar entry
     */
    private void initializeDateFromListener() {
        dateFrom.setText(date);
        dateFrom.setOnClickListener(v -> {
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(AddEditCalendarEntryActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    String minuteString;
                    if(selectedMinute < 10) {
                        minuteString = "0" + selectedMinute;
                    }
                    else {
                        minuteString = "" + selectedMinute;
                    }
                    String hourString;
                    if(selectedHour < 10) {
                        hourString = "0" + selectedHour;
                    }
                    else {
                        hourString = "" + selectedHour;
                    }
                    dateFrom.setText( dateHeader + " " +  hourString + ":" + minuteString  + ":00");
                }
            }, hour, minute, true);
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();
        });
    }
}
