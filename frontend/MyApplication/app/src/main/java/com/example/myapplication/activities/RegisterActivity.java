package com.example.myapplication.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.config.ErrorDialog;
import com.example.myapplication.config.RestClient;
import com.example.myapplication.model.Person;
import com.example.myapplication.model.Role;
import com.example.myapplication.model.User;
import com.example.myapplication.service.UserService;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This is the main entity we will be using to display the CreateNewAccount screen
 */
public class RegisterActivity extends AppCompatActivity {

    private EditText firstNameBox;
    private EditText lastNameBox;
    private EditText birthDateBox;
    private EditText professionBox;
    private EditText genderBox;
    private EditText emailBox;
    private EditText usernameBox;
    private EditText passwordBox;
    private UserService userService;
    private Button saveButton;
    private final Calendar myCalendar = Calendar.getInstance();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_creation);
        initializeComponents();
    }

    /**
     * This method initializes all the components (buttons, listviews, services) needed
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initializeComponents() {
        userService = RestClient.createService(UserService.class);

        firstNameBox = findViewById(R.id.create_user_first_name);
        lastNameBox = findViewById(R.id.create_user_last_name);
        birthDateBox = findViewById(R.id.create_user_date);
        professionBox = findViewById(R.id.edit_user_prof);
        genderBox = findViewById(R.id.edit_user_gender);
        emailBox = findViewById(R.id.edit_user_email);
        usernameBox = findViewById(R.id.edit_user_username);
        passwordBox = findViewById(R.id.edit_user_password);
        saveButton = findViewById(R.id.edit_user_save);

        initializeBirthdayListener();
        initializeRegisterListener();
    }

    /**
     * BIRTHDATE EditText listener
     */
    private void initializeBirthdayListener() {
        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };

        birthDateBox.setOnClickListener(v -> {
            new DatePickerDialog(RegisterActivity.this, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    /**
     * This method sets the user's birthdate
     */
    private void updateLabel() {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        birthDateBox.setText(sdf.format(myCalendar.getTime()));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initializeRegisterListener() {
        saveButton.setOnClickListener(v -> {
            final User user = new User();
            user.setUsername(usernameBox.getText().toString());
            user.setPassword(passwordBox.getText().toString());
            final Role role = new Role();
            role.setId(2L);
            user.setRole(role);
            user.setEmail(emailBox.getText().toString());
            final Person person = new Person();
            person.setBirthDate(birthDateBox.getText().toString() + " 00:00:00");
            person.setFirstName(firstNameBox.getText().toString());
            person.setGender(genderBox.getText().toString());
            person.setLastName(lastNameBox.getText().toString());
            person.setProfession(professionBox.getText().toString());
            user.setPerson(person);
            if(checkForm(user)) {
                registerUser(user);
            }
        });
    }

    /**
     * This methods checks if the information added by the user is correct
     * @param user
     * @return boolean
     */
    private boolean checkForm(final User user) {
        return true;
    }

    /**
     * This method gets the user and sends it to the user's main screen
     */
    private void registerUser(final User user) {
        final Call<Void> userCall = userService.createUser(user);
        userCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    ErrorDialog.showDialog(RegisterActivity.this, "Error during call");
                }
                else {
                    openMainActivity();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                ErrorDialog.showDialog(RegisterActivity.this, t.getMessage());
            }
        });
    }

    /**
     * This method sends the information needed to display the user's main screen
     */
    private void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
