package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.config.BasicToken;
import com.example.myapplication.config.ErrorDialog;
import com.example.myapplication.config.ExceptionResponse;
import com.example.myapplication.config.RestClient;
import com.example.myapplication.model.Person;
import com.example.myapplication.model.Role;
import com.example.myapplication.model.User;
import com.example.myapplication.service.UserService;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This is the entity we will be using to display the MainScreen in GUI
 */
public class MainActivity extends AppCompatActivity {

    private EditText userBox;
    private EditText passwordBox;
    private Button loginButton;
    private TextView registerText;

    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeComponents();
    }

    /**
     * This method initializes all the components (buttons, listviews, services) needed
     */
    private void initializeComponents() {
        userService = RestClient.createService(UserService.class);

        userBox = findViewById(R.id.editTextTextPersonName);
        passwordBox = findViewById(R.id.editTextTextPersonName2);
        loginButton = findViewById(R.id.button3);
        registerText = findViewById(R.id.textView);

        initializeLoginListener();
        initializeRegisterListener();
    }

    /**
     * SignUp TextBox listener
     */
    private void initializeRegisterListener() {
        registerText.setOnClickListener(v -> {
            openRegisterActivity();
        });
    }

    /**
     * LOGIN button listener
     */
    private void initializeLoginListener() {
        loginButton.setOnClickListener(v -> {
            final User user = new User();
            user.setUsername(userBox.getText().toString());
            user.setPassword(passwordBox.getText().toString());
            if(checkCredentials(user)) {
                validateCredentials(user);
            }
        });
    }

    /**
     * This method is used to check if the username and password are added by the user
     * @param user
     * @return boolean
     */
    private boolean checkCredentials(final User user) {
        if (user.getUsername().isEmpty()) {
            userBox.setError("Username cannot be empty!");
            return false;
        } else if (user.getPassword().isEmpty()) {
            passwordBox.setError("Password cannot be empty!");
            return false;
        }
        return true;
    }

    /**
     * This method is used to check if the combination user password is correct
     * @param user
     */
    private void validateCredentials(final User user) {

        final Call<BasicToken> userCall = userService.login(user);
        userCall.enqueue(new Callback<BasicToken>() {
            @Override
            public void onResponse(Call<BasicToken> call, Response<BasicToken> response) {
                if (!response.isSuccessful()) {
                    ErrorDialog.showDialog(MainActivity.this, "Invalid username and password combination");
                }
                else {
                    final BasicToken basicToken = response.body();
                    if(basicToken != null) {
                        user.setId(basicToken.getUserId());
                        final Role role = new Role();
                        role.setType(basicToken.getUserRole());
                        user.setRole(role);
                        final Person person = new Person();
                        person.setId(basicToken.getPersonId());
                        user.setPerson(person);
                        if (role.getType().equals("ROLE_USER")) {
                            openUserMainActivity(user, basicToken.getAuthenticationToken());
                        }
                        else if (role.getType().equals("ROLE_ADMIN")) {
                            openAdminMainActivity(user, basicToken.getAuthenticationToken());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<BasicToken> call, Throwable t) {
                ErrorDialog.showDialog(MainActivity.this, t.getMessage());
            }
        });

    }

    /**
     * This method sends the information needed to display admin main screen
     * @param user
     * @param authenticationToken
     */
    private void openAdminMainActivity(User user, String authenticationToken) {
        Intent intent = new Intent(this, AdminMainActivity.class);
        intent.putExtra("user", user);
        intent.putExtra("authenticationToken", authenticationToken);
        startActivity(intent);
    }

    /**
     * This method sends the information needed to display user main screen
     * @param user
     * @param authenticationToken
     */
    private void openUserMainActivity(User user, String authenticationToken) {
        Intent intent = new Intent(this, UserMainActivity.class);
        intent.putExtra("user", user);
        intent.putExtra("authenticationToken", authenticationToken);
        startActivity(intent);
    }

    /**
     * This method is used to send the information needed to display CreateNewUser page
     */
    private void openRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

}