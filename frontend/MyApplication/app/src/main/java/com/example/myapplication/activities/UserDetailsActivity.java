package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.config.ErrorDialog;
import com.example.myapplication.config.RestClient;
import com.example.myapplication.model.User;
import com.example.myapplication.service.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This is the main entity we will be using to display the User details in GUI
 */
public class UserDetailsActivity extends AppCompatActivity {

    private User contextUser;
    private String AUTHENTICATION_TOKEN;
    private Long userId;
    private User user;

    private EditText firstName;
    private EditText lastName;
    private EditText birthDate;
    private EditText profession;
    private EditText gender;
    private EditText email;
    private EditText username;
    private Button button;
    private Button returnButton;

    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_user);
        initializeComponents();
    }

    /**
     * This method initializes all the components (buttons, listviews, services) needed
     */
    private void initializeComponents() {
        contextUser = (User) getIntent().getSerializableExtra("user");
        AUTHENTICATION_TOKEN = (String) getIntent().getSerializableExtra("authenticationToken");
        userId = (Long) getIntent().getSerializableExtra("userId");
        userService = RestClient.createService(UserService.class);

        firstName = findViewById(R.id.create_user_last_name);
        lastName = findViewById(R.id.view_user_lname);
        birthDate = findViewById(R.id.view_user_birth);
        profession = findViewById(R.id.edit_user_prof);
        gender = findViewById(R.id.view_user_gender);
        email = findViewById(R.id.edit_user_email);
        username = findViewById(R.id.edit_user_username);
        button = findViewById(R.id.edit_user_save);
        returnButton = findViewById(R.id.view_user_returnBtn);

        initializeReturnListener();
        initializeEditButton();
        getUserById(userId);
    }

    /**
     * This method initializes the existent data for the user
     */
    private void initializeData() {
        firstName.setText(user.getPerson().getFirstName());
        lastName.setText(user.getPerson().getLastName());
        birthDate.setText(user.getPerson().getBirthDate().split(" ")[0]);
        profession.setText(user.getPerson().getProfession());
        gender.setText(user.getPerson().getGender());
        email.setText(user.getEmail());
        username.setText(user.getUsername());
    }

    /**
     * This method gets the user that has the id = userId and sends it to the userDetails page
     * @param userId
     */
    private void getUserById(final Long userId) {
        final Call<User> userCall = userService.getById(userId, AUTHENTICATION_TOKEN);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    ErrorDialog.showDialog(UserDetailsActivity.this, "Error");
                }
                else {
                    user = response.body();
                    initializeData();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                ErrorDialog.showDialog(UserDetailsActivity.this, t.getMessage());
            }
        });
    }

    /**
     * EDIT button listener
     */
    private void initializeEditButton() {
        button.setOnClickListener(v -> openEditUserActivity());
    }

    /**
     * This method sends the information needed to display EditUserPage
     */
    private void openEditUserActivity() {
        Intent intent = new Intent(this, EditUserActivity.class);
        intent.putExtra("user", contextUser);
        intent.putExtra("userDetails", user);
        intent.putExtra("authenticationToken", AUTHENTICATION_TOKEN);
        startActivity(intent);
    }

    /**
     * RETURN button listener
     */
    private void initializeReturnListener() {
        returnButton.setOnClickListener( v -> openUsersActivity());
    }

    /**
     * This method sends the information needed to return to the Users page
     */
    private void openUsersActivity() {
        Intent intent = new Intent(this, UsersActivity.class);
        intent.putExtra("user", contextUser);
        intent.putExtra("authenticationToken", AUTHENTICATION_TOKEN);
        startActivity(intent);
    }

}
