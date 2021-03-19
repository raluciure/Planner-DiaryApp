package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.config.ErrorDialog;
import com.example.myapplication.config.RestClient;
import com.example.myapplication.model.DiaryEntry;
import com.example.myapplication.model.Person;
import com.example.myapplication.model.User;
import com.example.myapplication.service.DiaryEntryService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This is the main entity we will be using to display the diary entry in GUI
 */
public class DiaryEntryActivity extends AppCompatActivity {

    private User contextUser;
    private String AUTHENTICATION_TOKEN;
    private String date;

    private EditText details;
    private Button save;

    private DiaryEntry diaryData;

    private DiaryEntryService diaryEntryService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_diary_entry);
        initializeComponents();
    }

    /**
     * This method initializes all the components (buttons, listviews, services) needed
     */
    private void initializeComponents() {
        diaryEntryService = RestClient.createService(DiaryEntryService.class);

        contextUser = (User) getIntent().getSerializableExtra("user");
        AUTHENTICATION_TOKEN = (String) getIntent().getSerializableExtra("authenticationToken");
        date = (String) getIntent().getSerializableExtra("date");

        details = findViewById(R.id.diary_entry_details);
        save = findViewById(R.id.diary_entry_save_btn);

        initializeData();
        initializeSaveListener();
    }

    /**
     * This method initializes the existent data for the diary entry
     */
    private void initializeData() {
        final DiaryEntry diaryEntry = new DiaryEntry();
        diaryEntry.setDate(date);
        final Person person = new Person();
        person.setId(contextUser.getPerson().getId());
        diaryEntry.setPerson(person);
        final Call<DiaryEntry> diaryEntryCall = diaryEntryService.getDiaryEntryByPersonAndDay(diaryEntry, AUTHENTICATION_TOKEN);
        diaryEntryCall.enqueue(new Callback<DiaryEntry>() {
            @Override
            public void onResponse(Call<DiaryEntry> call, Response<DiaryEntry> response) {
                if (!response.isSuccessful()) {
                    ErrorDialog.showDialog(DiaryEntryActivity.this, "Error");
                }
                else {
                    DiaryEntry diaryEntryResp = response.body();
                    if (diaryEntryResp.getId() != null) {
                        diaryData = diaryEntryResp;
                        details.setText(diaryEntryResp.getNotes());
                    }
                    else {
                        diaryData = diaryEntry;
                    }
                }
            }

            @Override
            public void onFailure(Call<DiaryEntry> call, Throwable t) {
                ErrorDialog.showDialog(DiaryEntryActivity.this, t.getMessage());
            }
        });
    }

    /**
     * SAVE button listener
     */
    private void initializeSaveListener() {
        save.setOnClickListener(v -> {
            diaryData.setNotes(details.getText().toString());
            final Call<Void> diaryEntryCall = diaryEntryService.saveOrUpdate(diaryData, AUTHENTICATION_TOKEN);
            diaryEntryCall.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (!response.isSuccessful()) {
                        ErrorDialog.showDialog(DiaryEntryActivity.this, "Error");
                    }
                    else {
                        openUserMainActivity();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    ErrorDialog.showDialog(DiaryEntryActivity.this, t.getMessage());
                }
            });
        });
    }

    /**
     * This method is used to send the information needed to return to the main screen
     */
    private void openUserMainActivity() {
        Intent intent = new Intent(this, UserMainActivity.class);
        intent.putExtra("user", contextUser);
        intent.putExtra("authenticationToken", AUTHENTICATION_TOKEN);
        startActivity(intent);
    }
}
