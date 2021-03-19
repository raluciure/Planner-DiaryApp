package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.adapter.CalendarEntryListAdapter;
import com.example.myapplication.config.ErrorDialog;
import com.example.myapplication.config.RestClient;
import com.example.myapplication.model.CalendarEntry;
import com.example.myapplication.model.Person;
import com.example.myapplication.model.User;
import com.example.myapplication.service.CalendarEntryService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This is the main entity we will be using to display the list of calendar entries in GUI
 */
public class CalendarEntriesActivity extends AppCompatActivity {
    private User contextUser;
    private String AUTHENTICATION_TOKEN;
    private String date;

    private ListView lvCalendarEntry;
    private CalendarEntryListAdapter calendarEntryListAdapter;
    private List<CalendarEntry> calendarEntryList;

    private FloatingActionButton moreButton;
    private FloatingActionButton addButton;
    private FloatingActionButton returnButton;
    private FloatingActionButton viewButton;

    private TextView returnActionText;
    private TextView addActionText;
    private TextView viewActionText;

    private Boolean isAllFabsVisible;

    private CalendarEntryService calendarEntryService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_entry_list);
        initializeComponents();
    }

    /**
     * This method initializes all the components (buttons, listviews, services) needed
     */
    private void initializeComponents() {
        contextUser = (User) getIntent().getSerializableExtra("user");
        AUTHENTICATION_TOKEN = (String) getIntent().getSerializableExtra("authenticationToken");
        calendarEntryService = RestClient.createService(CalendarEntryService.class);
        date = (String) getIntent().getSerializableExtra("date");

        lvCalendarEntry = (ListView) findViewById(R.id.listview_calendar_entry);
        moreButton = findViewById(R.id.more_fab);
        addButton = findViewById(R.id.add_activity_fab);
        returnButton = findViewById(R.id.return_fab);
        viewButton = findViewById(R.id.view_completed_fab);

        returnActionText = findViewById(R.id.add_return_action_text);
        addActionText = findViewById(R.id.add_activity_action_text);
        viewActionText = findViewById(R.id.add_view_action_text);

        addButton.setVisibility(View.GONE);
        returnButton.setVisibility(View.GONE);
        viewButton.setVisibility(View.GONE);
        addActionText.setVisibility(View.GONE);
        returnActionText.setVisibility(View.GONE);
        viewActionText.setVisibility(View.GONE);
        isAllFabsVisible = false;

        initializeActivityList();
        initializeMoreListener();
        initializeAddListener();
        initializeReturnListener();
        initializeViewListener();
    }

    /**
     * This method gets the calendar entries from the data base and displays them in GUI
     */
    private void initializeActivityList() {
        final CalendarEntry calendarEntry = new CalendarEntry();
        calendarEntry.setDateFrom(date);
        final Person person = new Person();
        person.setId(contextUser.getPerson().getId());
        calendarEntry.setPerson(person);
        final Call<List<CalendarEntry>> getAllCall = calendarEntryService.getCalendarEntriesByPersonAndDay(calendarEntry,AUTHENTICATION_TOKEN);
        getAllCall.enqueue(new Callback<List<CalendarEntry>>() {
            @Override
            public void onResponse(Call<List<CalendarEntry>> call, Response<List<CalendarEntry>> response) {
                if (!response.isSuccessful()) {
                    ErrorDialog.showDialog(CalendarEntriesActivity.this, "Error");
                }
                else {
                    final List<CalendarEntry> calendarEntries = response.body();
                    if(response.body() == null) {
                        ErrorDialog.showDialog(CalendarEntriesActivity.this, "Error");
                    }
                    else {
                        calendarEntryList = calendarEntries;
                        calendarEntryListAdapter = new CalendarEntryListAdapter(getApplicationContext(), calendarEntryList);
                        lvCalendarEntry.setAdapter(calendarEntryListAdapter);
                        lvCalendarEntry.setOnItemClickListener((parent, view, position, id) -> {
                            final CalendarEntry calendarEntry = calendarEntryList.get(position);
                            openCalendarEntryDetailsActivity(calendarEntry);
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<List<CalendarEntry>> call, Throwable t) {
                ErrorDialog.showDialog(CalendarEntriesActivity.this, t.getMessage());
            }
        });
    }

    /**
     * This method is used to send the information needed to display a calendar entry from the list
     * @param calendarEntry
     */
    private void openCalendarEntryDetailsActivity(CalendarEntry calendarEntry) {
        Intent intent = new Intent(this, CalendarEntryDetails.class);
        intent.putExtra("user", contextUser);
        intent.putExtra("calendarEntry", calendarEntry);
        intent.putExtra("authenticationToken", AUTHENTICATION_TOKEN);
        intent.putExtra("date", date);
        startActivity(intent);
    }

    /**
     * MORE button listener
     */
    private void initializeMoreListener() {
        moreButton.setOnClickListener(
                view -> {
                    if (!isAllFabsVisible) {
                        addButton.show();
                        returnButton.show();
                        viewButton.show();
                        addActionText.setVisibility(View.VISIBLE);
                        returnActionText.setVisibility(View.VISIBLE);
                        viewActionText.setVisibility(View.VISIBLE);

                        isAllFabsVisible = true;
                    } else {
                        addButton.hide();
                        returnButton.hide();
                        viewButton.hide();
                        addActionText.setVisibility(View.GONE);
                        returnActionText.setVisibility(View.GONE);
                        viewActionText.setVisibility(View.GONE);

                        isAllFabsVisible = false;
                    }
                });
    }

    /**
     * ADD button listener
     */
    private void initializeAddListener(){
        addButton.setOnClickListener( v -> openAddActivity());
    }

    /**
     * This method is used to send the information needed to display the add activity page
     */
    private void openAddActivity() {
        Intent intent = new Intent(this, AddEditCalendarEntryActivity.class);
        intent.putExtra("user", contextUser);
        intent.putExtra("authenticationToken", AUTHENTICATION_TOKEN);
        intent.putExtra("date", date);
        startActivity(intent);
    }

    /**
     * RETURN button listener
     */
    private void initializeReturnListener() {
        returnButton.setOnClickListener( v -> openReturnActivity());
    }

    /**
     * This method is used to send the information needed to display the previous page
     */
    private void openReturnActivity() {
        Intent intent = new Intent(this, UserMainActivity.class);
        intent.putExtra("user", contextUser);
        intent.putExtra("authenticationToken", AUTHENTICATION_TOKEN);
        startActivity(intent);
    }

    /**
     * VIEW button listener
     */
    private void initializeViewListener() {
        viewButton.setOnClickListener( v -> openCompletedEntriesActivity());
    }

    /**
     * This method is used to send the information needed to display the completed calendar entries
     * page
     */
    private void openCompletedEntriesActivity() {
        Intent intent = new Intent(this, CompletedEntryActivity.class);
        intent.putExtra("user", contextUser);
        intent.putExtra("authenticationToken", AUTHENTICATION_TOKEN);
        intent.putExtra("date", date);
        startActivity(intent);
    }
}
