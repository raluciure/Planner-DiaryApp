package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.adapter.CompletedEntryListAdapter;
import com.example.myapplication.config.ErrorDialog;
import com.example.myapplication.config.RestClient;
import com.example.myapplication.model.CompletedEntry;
import com.example.myapplication.model.Person;
import com.example.myapplication.model.User;
import com.example.myapplication.service.CompletedEntryService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This is the main entity we will be using to display the list of completed entries in GUI
 */
public class CompletedEntryActivity extends AppCompatActivity {

    private User contextUser;
    private String AUTHENTICATION_TOKEN;
    private String date;

    private ListView lvCalendarEntry;
    private CompletedEntryListAdapter completedEntryListAdapter;
    private List<CompletedEntry> completedEntryList;

    private FloatingActionButton moreButton;
    private FloatingActionButton returnButton;
    private FloatingActionButton viewButton;

    private TextView returnActionText;
    private TextView viewActionText;

    private Boolean isAllFabsVisible;

    private CompletedEntryService completedEntryService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.completed_entry_list);
        initializeComponents();
    }

    /**
     * This method initializes all the components (buttons, listviews, services) needed
     */
    private void initializeComponents() {
        contextUser = (User) getIntent().getSerializableExtra("user");
        AUTHENTICATION_TOKEN = (String) getIntent().getSerializableExtra("authenticationToken");
        completedEntryService = RestClient.createService(CompletedEntryService.class);
        date = (String) getIntent().getSerializableExtra("date");

        lvCalendarEntry = (ListView) findViewById(R.id.listview_calendar_entry);
        moreButton = findViewById(R.id.more_fab);
        returnButton = findViewById(R.id.return_fab);
        viewButton = findViewById(R.id.add_activity_fab);

        returnActionText = findViewById(R.id.add_return_action_text);
        viewActionText = findViewById(R.id.add_user_action_text);

        returnButton.setVisibility(View.GONE);
        viewButton.setVisibility(View.GONE);
        returnActionText.setVisibility(View.GONE);
        viewActionText.setVisibility(View.GONE);
        isAllFabsVisible = false;

        initializeActivityList();
        initializeMoreListener();
        initializeReturnListener();
        initializeViewListener();
    }

    /**
     * This method gets the completed entries from the data base and displays them in GUI
     */
    private void initializeActivityList() {
        final CompletedEntry completedEntry = new CompletedEntry();
        completedEntry.setDateFrom(date);
        final Person person = new Person();
        person.setId(contextUser.getPerson().getId());
        completedEntry.setPerson(person);
        final Call<List<CompletedEntry>> getAllCall = completedEntryService.getCompletedEntriesByPersonAndDay(completedEntry, AUTHENTICATION_TOKEN);
        getAllCall.enqueue(new Callback<List<CompletedEntry>>() {
            @Override
            public void onResponse(Call<List<CompletedEntry>> call, Response<List<CompletedEntry>> response) {
                if (!response.isSuccessful()) {
                    ErrorDialog.showDialog(CompletedEntryActivity.this, "Error");
                }
                else {
                    final List<CompletedEntry> completedEntries = response.body();
                    if(response.body() == null) {
                        ErrorDialog.showDialog(CompletedEntryActivity.this, "Error");
                    }
                    else {
                        completedEntryList = completedEntries;
                        completedEntryListAdapter = new CompletedEntryListAdapter(getApplicationContext(), completedEntries);
                        lvCalendarEntry.setAdapter(completedEntryListAdapter);
                        lvCalendarEntry.setOnItemClickListener((parent, view, position, id) -> {
                            final CompletedEntry completedEntry1 = completedEntryList.get(position);
                            openCompletedEntryDetailsActivity(completedEntry1);
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<List<CompletedEntry>> call, Throwable t) {
                ErrorDialog.showDialog(CompletedEntryActivity.this, t.getMessage());
            }
        });
    }

    /**
     * This method is used to send the information needed to display a completed entry from the list
     * @param completedEntry
     */
    private void openCompletedEntryDetailsActivity(CompletedEntry completedEntry) {
        Intent intent = new Intent(this, CompletedEntryDetailsActivity.class);
        intent.putExtra("user", contextUser);
        intent.putExtra("completedEntry", completedEntry);
        intent.putExtra("authenticationToken", AUTHENTICATION_TOKEN);
        startActivity(intent);
    }

    /**
     * MORE button listenr
     */
    private void initializeMoreListener() {
        moreButton.setOnClickListener(
                view -> {
                    if (!isAllFabsVisible) {
                        returnButton.show();
                        viewButton.show();
                        returnActionText.setVisibility(View.VISIBLE);
                        viewActionText.setVisibility(View.VISIBLE);

                        isAllFabsVisible = true;
                    } else {
                        returnButton.hide();
                        viewButton.hide();
                        returnActionText.setVisibility(View.GONE);
                        viewActionText.setVisibility(View.GONE);

                        isAllFabsVisible = false;
                    }
                });
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
     * This method is used to send the information needed to display the calendar entries page
     */
    private void openCompletedEntriesActivity() {
        Intent intent = new Intent(this, CalendarEntriesActivity.class);
        intent.putExtra("user", contextUser);
        intent.putExtra("authenticationToken", AUTHENTICATION_TOKEN);
        intent.putExtra("date", date);
        startActivity(intent);
    }
}
