package com.example.myapplication.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.config.ErrorDialog;
import com.example.myapplication.config.RestClient;
import com.example.myapplication.model.Activity;
import com.example.myapplication.model.Category;
import com.example.myapplication.model.User;
import com.example.myapplication.service.ActivityService;
import com.example.myapplication.service.CategoryService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This is the main entity we will be using to display the Add/EditActivity GUI
 */
public class ActivityAddEditActivity extends AppCompatActivity {
    private EditText nameBox;
    private EditText descriptionBox;
    private Spinner categoriesSpinner;
    private Button saveButton;

    private ActivityService activityService;
    private CategoryService categoryService;
    private User contextUser;
    private String AUTHENTICATION_TOKEN;
    private Activity activityDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation);
        initializeComponents();
    }

    /**
     * This method initializes all the components (buttons, listviews, services) needed
     */
    private void initializeComponents() {
        contextUser = (User) getIntent().getSerializableExtra("user");
        AUTHENTICATION_TOKEN = (String) getIntent().getSerializableExtra("authenticationToken");
        activityService = RestClient.createService(ActivityService.class);
        categoryService = RestClient.createService(CategoryService.class);
        activityDetails = (Activity) getIntent().getSerializableExtra("activityEdit");

        nameBox = findViewById(R.id.edit_activity_name);
        descriptionBox = findViewById(R.id.edit_activity_description);
        categoriesSpinner = findViewById(R.id.edit_activity_categorySpinner);
        saveButton = findViewById(R.id.edit_activity_saveBtn);

        initializeData();
        initializeSaveListener();
    }

    /**
     * This method initializes the existent data for the activity
     */
    private void initializeData(){
        if(activityDetails != null) {
            nameBox.setText(activityDetails.getName());
            descriptionBox.setText(activityDetails.getDescription());
        }
        initializeSpinner();
    }

    /**
     * This method initializes the spinner needed to choose a category for the activity
     */
    private void initializeSpinner() {
        final Call<List<Category>> getAllCall = categoryService.getAll(AUTHENTICATION_TOKEN);
        getAllCall.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (!response.isSuccessful()) {
                    ErrorDialog.showDialog(ActivityAddEditActivity.this, "Error");
                }
                else {
                    final List<Category> categories = response.body();
                    if(response.body() == null) {
                        ErrorDialog.showDialog(ActivityAddEditActivity.this, "Error");
                    }
                    else {
                        categoriesSpinner = findViewById(R.id.edit_activity_categorySpinner);
                        final ArrayList<String> arrayList = new ArrayList<>();
                        arrayList.add("Categories");
                        final List<String> categoryList = categories.stream().map(Category::getName).collect(Collectors.toList());
                        arrayList.addAll(categoryList);
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ActivityAddEditActivity.this, android.R.layout.simple_spinner_item, arrayList);
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        categoriesSpinner.setAdapter(arrayAdapter);
                        categoriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                categoriesSpinner.setSelection(position);
                            }

                            @Override
                            public void onNothingSelected(AdapterView <?> parent) {
                            }
                        });
                        if(activityDetails != null) {
                            arrayList.forEach(name -> {
                                if (name.equals(activityDetails.getCategory().getName())) {
                                    int position = arrayList.indexOf(name);
                                    categoriesSpinner.setSelection(position);
                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                ErrorDialog.showDialog( ActivityAddEditActivity.this, t.getMessage());
            }
        });
    }

    /**
     * Listener for SAVE button
     */
    private void initializeSaveListener(){
        saveButton.setOnClickListener(v -> {
            final Activity newActivity = new Activity();
            if(activityDetails != null) {
                newActivity.setId(activityDetails.getId());
            }
            newActivity.setName(nameBox.getText().toString());
            newActivity.setDescription(descriptionBox.getText().toString());
            final Category category = new Category();
            category.setName(categoriesSpinner.getSelectedItem().toString());
            if(category.getName().equals("Categories")) {
                TextView errorText = (TextView)categoriesSpinner.getSelectedView();
                errorText.setError("Error");
                errorText.setTextColor(Color.RED);
                errorText.setText("Category not selected!");
            }
            else {
                newActivity.setCategory(category);
                editActivity(newActivity);
            }
        });
    }

    /**
     * This method gets the activity and sends it to the edit page
     */
    private void editActivity(final Activity activity){
        final Call<Void> activityCall = activityService.saveOrUpdate(activity, AUTHENTICATION_TOKEN);
        activityCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    ErrorDialog.showDialog(ActivityAddEditActivity.this, "Error during call");
                }
                else {
                    openActivitiesListActivity();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                ErrorDialog.showDialog(ActivityAddEditActivity.this, t.getMessage());
            }
        });
    }

    /**
     * This method is used to send the information needed to see page of all the activities
     */
    private void openActivitiesListActivity() {
        Intent intent = new Intent(this, ActivitiesActivity.class);
        intent.putExtra("user", contextUser);
        intent.putExtra("authenticationToken", AUTHENTICATION_TOKEN);
        startActivity(intent);
    }

}
