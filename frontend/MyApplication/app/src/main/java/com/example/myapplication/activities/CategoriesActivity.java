package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.adapter.CategoryListAdapter;
import com.example.myapplication.config.ErrorDialog;
import com.example.myapplication.config.RestClient;
import com.example.myapplication.model.Category;
import com.example.myapplication.model.User;
import com.example.myapplication.service.CategoryService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This is the main entity we will be using to display the list of categories in GUI
 */
public class CategoriesActivity extends AppCompatActivity {
    private User contextUser;
    private String AUTHENTICATION_TOKEN;

    private ListView lvCategory;
    private CategoryListAdapter categoryListAdapter;
    private List<Category> categoryList;

    private FloatingActionButton moreButton;
    private FloatingActionButton addButton;
    private FloatingActionButton returnButton;

    private TextView returnActionText;
    private TextView addActionText;

    Boolean isAllFabsVisible;

    private CategoryService categoryService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categories_list);
        initializeComponents();
    }

    /**
     * This method initializes all the components (buttons, listviews, services) needed
     */
    private void initializeComponents() {
        contextUser = (User) getIntent().getSerializableExtra("user");
        AUTHENTICATION_TOKEN = (String) getIntent().getSerializableExtra("authenticationToken");
        categoryService = RestClient.createService(CategoryService.class);

        lvCategory = (ListView) findViewById(R.id.listview_category);
        moreButton = findViewById(R.id.more_fab);
        addButton = findViewById(R.id.add_category_fab);
        returnButton = findViewById(R.id.return_fab);

        returnActionText = findViewById(R.id.return_action_text);
        addActionText = findViewById(R.id.add_category_action_text);

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
     * This method gets the categories from the data base and displays them in GUI
     */
    private void initializeActivityList() {
        final Call<List<Category>> getAllCall = categoryService.getAll(AUTHENTICATION_TOKEN);
        getAllCall.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (!response.isSuccessful()) {
                    ErrorDialog.showDialog(CategoriesActivity.this, "Error");
                }
                else {
                    final List<Category> categories = response.body();
                    if(response.body() == null) {
                        ErrorDialog.showDialog(CategoriesActivity.this, "Error");
                    }
                    else {
                        categoryList = categories;
                        categoryListAdapter = new CategoryListAdapter(getApplicationContext(), categoryList);
                        lvCategory.setAdapter(categoryListAdapter);
                        lvCategory.setOnItemClickListener((parent, view, position, id) -> {
                            final Category category = categoryList.get(position);
                            openCategoryAddEditActivity(category);
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                ErrorDialog.showDialog( CategoriesActivity.this, t.getMessage());
            }
        });
    }

    /**
     * This method is used to send the information needed to display the AddEditCategory page
     * for a category from the list
     * @param category
     */
    private void openCategoryAddEditActivity(final Category category) {
        Intent intent = new Intent(this, CategoryAddEditActivity.class);
        intent.putExtra("user", contextUser);
        intent.putExtra("categoryDetails", category);
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
     * This method is used to send the information needed to add a new category
     */
    private void openAddActivity() {
        Intent intent = new Intent(this, CategoryAddEditActivity.class);
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
     * This method is used to send the information needed to return to the previous page
     */
    private void openReturnActivity() {
        Intent intent = new Intent(this, AdminMainActivity.class);
        intent.putExtra("user", contextUser);
        intent.putExtra("authenticationToken", AUTHENTICATION_TOKEN);
        startActivity(intent);
    }
}
