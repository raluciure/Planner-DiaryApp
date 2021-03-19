package com.example.myapplication.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
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
 * This is the main entity we will be using to display the EditUser GUI
 */
public class EditUserActivity extends AppCompatActivity {

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
    private CheckBox adminCheckBox;
    private CheckBox userCheckBox;
    private final Calendar myCalendar = Calendar.getInstance();

    private User contextUser;
    private String AUTHENTICATION_TOKEN;
    private User userDetails;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_user);
        initializeComponents();
    }

    /**
     * This method initializes all the components (buttons, listviews, services) needed
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initializeComponents() {
        contextUser = (User) getIntent().getSerializableExtra("user");
        AUTHENTICATION_TOKEN = (String) getIntent().getSerializableExtra("authenticationToken");
        userDetails = (User) getIntent().getSerializableExtra("userDetails");
        userService = RestClient.createService(UserService.class);

        firstNameBox = findViewById(R.id.edit_user_first_name);
        lastNameBox = findViewById(R.id.edit_user_last_name);
        birthDateBox = findViewById(R.id.create_user_date);
        professionBox = findViewById(R.id.edit_user_prof);
        genderBox = findViewById(R.id.edit_user_gender);
        emailBox = findViewById(R.id.edit_user_email);
        usernameBox = findViewById(R.id.edit_user_username);
        passwordBox = findViewById(R.id.edit_user_password);
        saveButton = findViewById(R.id.edit_user_save);
        adminCheckBox = findViewById(R.id.admin_checkBox);
        userCheckBox = findViewById(R.id.user_checkBox);

        adminCheckBox.setOnClickListener( v -> {
            userCheckBox.setChecked(false);
            adminCheckBox.setChecked(true);
        } );

        userCheckBox.setOnClickListener( v -> {
            userCheckBox.setChecked(true);
            adminCheckBox.setChecked(false);
        });

        if(userDetails != null) {
            initializeData();
        }

        initializeBirthdayListener();
        initializeRegisterListener();
    }

    /**
     * This method initializes the existent data for the user
     */
    private void initializeData() {
        firstNameBox.setText(userDetails.getPerson().getFirstName());
        lastNameBox.setText(userDetails.getPerson().getLastName());
        birthDateBox.setText(userDetails.getPerson().getBirthDate().split(" ")[0]);
        professionBox.setText(userDetails.getPerson().getProfession());
        genderBox.setText(userDetails.getPerson().getGender());
        emailBox.setText(userDetails.getEmail());
        usernameBox.setText(userDetails.getUsername());
        adminCheckBox.setChecked(userDetails.getRole().getType().equals("ROLE_ADMIN"));
        userCheckBox.setChecked(userDetails.getRole().getType().equals("ROLE_USER"));

    }

    /**
     * BIRTHDAY EditText listener
     */
    private void initializeBirthdayListener() {
        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };

        birthDateBox.setOnClickListener(v -> {
            new DatePickerDialog(EditUserActivity.this, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    /**
     * This method sets the user's birthday
     */
    private void updateLabel() {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        birthDateBox.setText(sdf.format(myCalendar.getTime()));
    }

    /**
     * Register button listener
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initializeRegisterListener() {
        saveButton.setOnClickListener(v -> {
            final User user = new User();
            if(userDetails != null) {
                user.setId(userDetails.getId());
            }
            user.setUsername(usernameBox.getText().toString());
            user.setPassword(passwordBox.getText().toString());
            final Role role = new Role();
            if(adminCheckBox.isChecked()) {
                role.setId(1L);
            }
            else if(userCheckBox.isChecked()) {
                role.setId(2L);
            }
            user.setRole(role);
            user.setEmail(emailBox.getText().toString());
            final Person person = new Person();
            if(userDetails != null) {
                person.setId(userDetails.getPerson().getId());
            }
            person.setBirthDate(birthDateBox.getText().toString() + " 00:00:00");
            person.setFirstName(firstNameBox.getText().toString());
            person.setGender(genderBox.getText().toString());
            person.setLastName(lastNameBox.getText().toString());
            person.setProfession(professionBox.getText().toString());
            user.setPerson(person);
            if(checkForm(user)) {
                editUser(user);
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
     * This method gets the user and sends it to the users page or to the editUser page
     */
    private void editUser(final User user) {
        final Call<Void> userCall = userService.saveOrUpdate(user, AUTHENTICATION_TOKEN);
        userCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    ErrorDialog.showDialog(EditUserActivity.this, "Error during call");
                }
                else {
                    if(userDetails != null) {
                        openUserDetailsActivity();
                    }
                    else {
                        openUsersActivity();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                ErrorDialog.showDialog(EditUserActivity.this, t.getMessage());
            }
        });
    }

    /**
     * This method is used to send the information needed to see the user's details page
     */
    private void openUserDetailsActivity() {
        Intent intent = new Intent(this, UserDetailsActivity.class);
        intent.putExtra("user", contextUser);
        intent.putExtra("userId", userDetails.getId());
        intent.putExtra("authenticationToken", AUTHENTICATION_TOKEN);
        startActivity(intent);
    }

    /**
     * This method is used to send the information needed to see the page of all users
     */
    private void openUsersActivity(){
        Intent intent = new Intent(this, UsersActivity.class);
        intent.putExtra("user", contextUser);
        intent.putExtra("authenticationToken", AUTHENTICATION_TOKEN);
        startActivity(intent);
    }

}