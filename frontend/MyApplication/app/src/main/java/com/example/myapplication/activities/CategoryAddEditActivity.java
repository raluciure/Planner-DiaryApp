package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.config.ErrorDialog;
import com.example.myapplication.config.RestClient;
import com.example.myapplication.model.Category;
import com.example.myapplication.model.User;
import com.example.myapplication.service.CategoryService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This is the main entity we will be using to display the Add/EditCategory GUI
 */
public class CategoryAddEditActivity extends AppCompatActivity {
    private EditText nameBox;
    private Button saveButton;
    private CategoryService categoryService;

    private User contextUser;
    private String AUTHENTICATION_TOKEN;
    private Category categoryDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_creation);
        initializeComponents();
    }

    /**
     * This method initializes all the components (buttons, listviews, services) needed
     */
    private void initializeComponents() {
        contextUser = (User) getIntent().getSerializableExtra("user");
        AUTHENTICATION_TOKEN = (String) getIntent().getSerializableExtra("authenticationToken");
        categoryService = RestClient.createService(CategoryService.class);
        categoryDetails = (Category) getIntent().getSerializableExtra("categoryDetails");

        nameBox = findViewById(R.id.edit_category_name);
        saveButton = findViewById(R.id.edit_category_saveBtn);

        if(categoryDetails != null) {
            nameBox.setText(categoryDetails.getName());
        }

        initializeSaveListener();
    }

    /**
     * SAVE button listener
     */
    private void initializeSaveListener() {
        saveButton.setOnClickListener(v -> {
            final Category newCategory = new Category();
            if (categoryDetails != null) {
                newCategory.setId(categoryDetails.getId());
            }
            newCategory.setName(nameBox.getText().toString());
            editCategory(newCategory);
        });
    }

    /**
     * This method gets the category and sends it to the edit page
     */
    private void editCategory(final Category category){
        final Call<Void> categoryCall = categoryService.saveOrUpdate(category, AUTHENTICATION_TOKEN);
        categoryCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    ErrorDialog.showDialog(CategoryAddEditActivity.this, "Error during call");
                }
                else {
                    openCategoryListActivity();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                ErrorDialog.showDialog(CategoryAddEditActivity.this, t.getMessage());
            }
        });
    }

    /**
     * This method is used to send the information needed to see page of all the categories
     */
    private void openCategoryListActivity() {
        Intent intent = new Intent(this, CategoriesActivity.class);
        intent.putExtra("user", contextUser);
        intent.putExtra("authenticationToken", AUTHENTICATION_TOKEN);
        startActivity(intent);
    }
}
