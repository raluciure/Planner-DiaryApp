package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.model.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * This is the main entity we will be using to display the User Main Screen
 */
public class UserMainActivity extends AppCompatActivity {

    private User contextUser;
    private String AUTHENTICATION_TOKEN;

    private Button plannerButton;
    private Button diaryButton;
    private Button logOutButton;

    private Long calendarTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
        initializeComponents();
        initializePlannerListener();
        initializeDiaryListener();
        initializeLogOutListener();
    }

    /**
     * This method initializes all the components (buttons, listviews, services) needed
     */
    private void initializeComponents() {
        contextUser = (User) getIntent().getSerializableExtra("user");
        AUTHENTICATION_TOKEN = (String) getIntent().getSerializableExtra("authenticationToken");

        plannerButton = findViewById(R.id.user_planner_btn);
        diaryButton = findViewById(R.id.user_diary_btn);
        logOutButton = findViewById(R.id.user_logOut_btn);

        final CalendarView calendarView = findViewById(R.id.calendarView);
        calendarTime = calendarView.getDate();

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth);
            calendarTime = calendar.getTimeInMillis();
        });
    }

    /**
     * This method gets the date (type Long) and transforms it into a String
     * @return date as String
     */
    private String getCalendarViewString() {
        final Long date = calendarTime;
        final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date) + " 00:00:00";
    }

    /**
     * PLANNER button listener
     */
    private void initializePlannerListener() {
        plannerButton.setOnClickListener(v -> openPlannerActivity());
    }

    /**
     * This method is used to send the information needed to return to display the planner page
     */
    private void openPlannerActivity() {
        Intent intent = new Intent(this, CalendarEntriesActivity.class);
        intent.putExtra("user", contextUser);
        intent.putExtra("authenticationToken", AUTHENTICATION_TOKEN);
        intent.putExtra("date", getCalendarViewString());
        startActivity(intent);
    }

    /**
     * DIARY button listener
     */
    private void initializeDiaryListener() {
        diaryButton.setOnClickListener(v -> openDiaryActivity());
    }

    /**
     * This method is used to send the information needed to display the diary page
     */
    private void openDiaryActivity() {
        Intent intent = new Intent(this, DiaryEntryActivity.class);
        intent.putExtra("user", contextUser);
        intent.putExtra("authenticationToken", AUTHENTICATION_TOKEN);
        intent.putExtra("date", getCalendarViewString());
        startActivity(intent);
    }

    /**
     * LOGOUT button listener
     */
    private void initializeLogOutListener() {
        logOutButton.setOnClickListener( v -> openLogOutActivity());
    }

    /**
     * This method is used to send the information needed to return to the main screen
     */
    private void openLogOutActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}