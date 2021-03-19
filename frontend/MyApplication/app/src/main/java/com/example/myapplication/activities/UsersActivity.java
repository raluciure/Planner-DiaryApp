package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.adapter.UserListAdapter;
import com.example.myapplication.config.ErrorDialog;
import com.example.myapplication.config.RestClient;
import com.example.myapplication.model.User;
import com.example.myapplication.service.UserService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This is the main entity we will be using to display all the users in GUI
 */
public class UsersActivity extends AppCompatActivity {

    private User contextUser;
    private String AUTHENTICATION_TOKEN;

    private ListView lvUser;
    private UserListAdapter userListAdapter;
    private List<User> userList;

    private FloatingActionButton moreButton;
    private FloatingActionButton addButton;
    private FloatingActionButton returnButton;

    private TextView returnActionText;
    private TextView addActionText;

    Boolean isAllFabsVisible;

    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.users_list);
        initializeComponents();
    }

    /**
     * This method initializes all the components (buttons, listviews, services) needed
     */
    private void initializeComponents() {
        contextUser = (User) getIntent().getSerializableExtra("user");
        AUTHENTICATION_TOKEN = (String) getIntent().getSerializableExtra("authenticationToken");
        userService = RestClient.createService(UserService.class);

        lvUser = (ListView) findViewById(R.id.listview_user);
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
     * This method gets the users from the data base and displays them in GUI
     */
    private void initializeActivityList() {
        final Call<List<User>> getAllCall = userService.getAll(AUTHENTICATION_TOKEN);
        getAllCall.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (!response.isSuccessful()) {
                    ErrorDialog.showDialog(UsersActivity.this, "Error");
                }
                else {
                    final List<User> users = response.body();
                    if(response.body() == null) {
                        ErrorDialog.showDialog(UsersActivity.this, "Error");
                    }
                    else {
                        userList = users;
                        userListAdapter = new UserListAdapter(getApplicationContext(), userList);
                        lvUser.setAdapter(userListAdapter);
                        lvUser.setOnItemClickListener((parent, view, position, id) -> {
                            final Long userId = userList.get(position).getId();
                            openUserDetailsActivity(userId);
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                ErrorDialog.showDialog(UsersActivity.this, t.getMessage());
            }
        });
    }

    /**
     * This method is used to send the information needed to display a user from the list
     * @param userId
     */
    private void openUserDetailsActivity(final Long userId) {
        Intent intent = new Intent(this, UserDetailsActivity.class);
        intent.putExtra("user", contextUser);
        intent.putExtra("userId", userId);
        intent.putExtra("authenticationToken", AUTHENTICATION_TOKEN);
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
     * ADD button listener
     */
    private void initializeAddListener(){
        addButton.setOnClickListener( v -> openAddActivity());
    }

    /**
     * This method is used to send the information needed to add a new user
     */
    private void openAddActivity() {
        Intent intent = new Intent(this, EditUserActivity.class);
        intent.putExtra("user", contextUser);
        intent.putExtra("authenticationToken", AUTHENTICATION_TOKEN);
        startActivity(intent);
    }

    /**
     * RETURN button listener
     */
    private void initializeReturnListener() {
        returnButton.setOnClickListener( v -> openReturnActivity());
    }

    /**
     * This method is used to send the information needed to return to the admin main screen
     */
    private void openReturnActivity() {
        Intent intent = new Intent(this, AdminMainActivity.class);
        intent.putExtra("user", contextUser);
        intent.putExtra("authenticationToken", AUTHENTICATION_TOKEN);
        startActivity(intent);
    }
}