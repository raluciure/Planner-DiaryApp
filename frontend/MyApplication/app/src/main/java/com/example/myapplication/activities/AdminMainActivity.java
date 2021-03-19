package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.config.RestClient;
import com.example.myapplication.model.User;
import com.example.myapplication.service.ActivityService;
import com.example.myapplication.service.CategoryService;
import com.example.myapplication.service.UserService;

/**
 * This is the main entity we will be using to display the Add/EditCalendarEntry GUI
 */
public class AdminMainActivity extends AppCompatActivity {

    private User contextUser;
    private String AUTHENTICATION_TOKEN;

    private Button usersButton;
    private Button activitiesButton;
    private Button categoriesButton;
    private Button logOutButton;

    private UserService userService;
    private ActivityService activityService;
    private CategoryService categoryService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_main_screen);
        initializeComponents();
    }

    /**
     * This method initializes all the components (buttons, listviews, services) needed
     */
    private void initializeComponents() {
        userService = RestClient.createService(UserService.class);
        activityService = RestClient.createService(ActivityService.class);
        categoryService = RestClient.createService(CategoryService.class);

        contextUser = (User) getIntent().getSerializableExtra("user");
        AUTHENTICATION_TOKEN = (String) getIntent().getSerializableExtra("authenticationToken");

        usersButton = findViewById(R.id.admin_main_users_btn);
        activitiesButton = findViewById(R.id.admin_main_activities_btn);
        categoriesButton = findViewById(R.id.admin_main_categories_btn);
        logOutButton = findViewById(R.id.admin_log_outBtn);

        initializeUsersListener();
        initializeCategoriesListener();
        initializeActivitiesListener();
        initializeLogOutListener();
    }

    /**
     * USERS button listener
     */
    private void initializeUsersListener() {
        usersButton.setOnClickListener(v -> openUsersActivity());
    }

    /**
     * This method is used to send the information needed to see page of all users
     */
    private void openUsersActivity() {
        Intent intent = new Intent(this, UsersActivity.class);
        intent.putExtra("user", contextUser);
        intent.putExtra("authenticationToken", AUTHENTICATION_TOKEN);
        startActivity(intent);
    }

    /**
     * CATEGORIES button listener
     */
    private void initializeCategoriesListener() {
        categoriesButton.setOnClickListener(v -> openCategoriesActivity());
    }

    /**
     * This method is used to send the information needed to see page of all categories
     */
    private void openCategoriesActivity() {
        Intent intent = new Intent(this, CategoriesActivity.class);
        intent.putExtra("user", contextUser);
        intent.putExtra("authenticationToken", AUTHENTICATION_TOKEN);
        startActivity(intent);
    }

    /**
     * ACTIVITIES button listener
     */
    private void initializeActivitiesListener(){
        activitiesButton.setOnClickListener( v -> openActivitiesActivity());
    }

    /**
     * This method is used to send the information needed to see page of all activities
     */
    private void openActivitiesActivity() {
        Intent intent = new Intent(this, ActivitiesActivity.class);
        intent.putExtra("user", contextUser);
        intent.putExtra("authenticationToken", AUTHENTICATION_TOKEN);
        startActivity(intent);
    }

    /**
     * LOGOUT button listener
     */
    private void initializeLogOutListener() {
        logOutButton.setOnClickListener( v -> openMainScreenActivity());
    }

    /**
     * This method is used to send the information needed to return to the main screen
     */
    private void openMainScreenActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}
