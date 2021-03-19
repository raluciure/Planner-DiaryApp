package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.adapter.ActivityListAdapter;
import com.example.myapplication.config.ErrorDialog;
import com.example.myapplication.config.RestClient;
import com.example.myapplication.model.Activity;
import com.example.myapplication.model.User;
import com.example.myapplication.service.ActivityService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This is the main entity we will be using to display the list of activities in GUI
 */
public class ActivitiesActivity extends AppCompatActivity {
    private User contextUser;
    private String AUTHENTICATION_TOKEN;

    private ListView lvActivity;
    private ActivityListAdapter activityListAdapter;
    private List<Activity> activityList;

    private FloatingActionButton moreButton;
    private FloatingActionButton addButton;
    private FloatingActionButton returnButton;

    private TextView returnActionText;
    private TextView addActionText;

    Boolean isAllFabsVisible;

    private ActivityService activityService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activities_list);
        initializeComponents();
    }

    /**
     * This method initializes all the components (buttons, listviews, services) needed
     */
    private void initializeComponents() {
        contextUser = (User) getIntent().getSerializableExtra("user");
        AUTHENTICATION_TOKEN = (String) getIntent().getSerializableExtra("authenticationToken");
        activityService = RestClient.createService(ActivityService.class);

        lvActivity = (ListView) findViewById(R.id.listview_activity);
        moreButton = findViewById(R.id.more_fab);
        addButton = findViewById(R.id.add_activity_fab);
        returnButton = findViewById(R.id.return_fab);

        returnActionText = findViewById(R.id.add_return_action_text);
        addActionText = findViewById(R.id.add_activity_action_text);

        addButton.setVisibility(View.GONE);
        returnButton.setVisibility(View.GONE);
        addActionText.setVisibility(View.GONE);
        returnActionText.setVisibility(View.GONE);
        isAllFabsVisible = false;

        initializeActivityList();
        initializeMoreListener();
        initializeAddListener();
        initializeReturnListener();
    }

    /**
     * This method gets the activities from the data base and displays them in GUI
     */
    private void initializeActivityList() {
        final Call<List<Activity>> getAllCall = activityService.getAll(AUTHENTICATION_TOKEN);
        getAllCall.enqueue(new Callback<List<Activity>>() {
            @Override
            public void onResponse(Call<List<Activity>> call, Response<List<Activity>> response) {
                if (!response.isSuccessful()) {
                    ErrorDialog.showDialog(ActivitiesActivity.this, "Error");
                } else {
                    final List<Activity> activities = response.body();
                    if (response.body() == null) {
                        ErrorDialog.showDialog(ActivitiesActivity.this, "Error");
                    } else {
                        activityList = activities;
                        activityListAdapter = new ActivityListAdapter(getApplicationContext(), activityList);
                        lvActivity.setAdapter(activityListAdapter);
                        lvActivity.setOnItemClickListener((parent, view, position, id) -> {
                            final Activity activity = activityList.get(position);
                            openEditActivity(activity);
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Activity>> call, Throwable t) {
                ErrorDialog.showDialog(ActivitiesActivity.this, t.getMessage());
            }
        });
    }

    /**
     * This method is used to send the information needed to display an activity from the list
     * @param activity
     */
    private void openEditActivity(final Activity activity) {
        Intent intent = new Intent(this, ActivityAddEditActivity.class);
        intent.putExtra("user", contextUser);
        intent.putExtra("activityEdit", activity);
        intent.putExtra("authenticationToken", AUTHENTICATION_TOKEN);
        startActivity(intent);
    }

    /**
     * Listener for MORE button
     */
    private void initializeMoreListener() {
        moreButton.setOnClickListener(
                view -> {
                    if (!isAllFabsVisible) {
                        addButton.show();
                        returnButton.show();
                        addActionText.setVisibility(View.VISIBLE);
                        returnActionText.setVisibility(View.VISIBLE);

                        isAllFabsVisible = true;
                    } else {
                        addButton.hide();
                        returnButton.hide();
                        addActionText.setVisibility(View.GONE);
                        returnActionText.setVisibility(View.GONE);

                        isAllFabsVisible = false;
                    }
                });
    }

    /**
     * Listener for ADD button
     */
    private void initializeAddListener(){
        addButton.setOnClickListener( v -> openAddActivity());
    }

    /**
     * This method is used to send the information needed to add a new activity
     */
    private void openAddActivity() {
        Intent intent = new Intent(this, ActivityAddEditActivity.class);
        intent.putExtra("user", contextUser);
        intent.putExtra("authenticationToken", AUTHENTICATION_TOKEN);
        startActivity(intent);
    }

    /**
     * Listener for RETURN button
     */
    private void initializeReturnListener() {
        returnButton.setOnClickListener( v -> openReturnActivity());
    }

    /**
     * This method is used to send the information needed to return to the previous page
     */
    private void openReturnActivity() {
        Intent intent = new Intent(this, AdminMainActivity.class);
        intent.putExtra("user", contextUser);
        intent.putExtra("authenticationToken", AUTHENTICATION_TOKEN);
        startActivity(intent);
    }
}
