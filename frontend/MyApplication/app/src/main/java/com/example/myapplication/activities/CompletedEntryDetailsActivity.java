package com.example.myapplication.activities;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.model.CompletedEntry;

/**
 * This is the main entity we will be using to display the CompletedEntry details in GUI
 */
public class CompletedEntryDetailsActivity extends AppCompatActivity {

    private CompletedEntry completedEntryDetails;

    private EditText name;
    private EditText category;
    private EditText dateFrom;
    private EditText dateTo;
    private EditText notes;
    private RatingBar ratingBar;
    private SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_completed_entry);
        initializeComponents();
    }

    /**
     * This method initializes all the components (buttons, listviews, services) needed
     */
    private void initializeComponents() {
        completedEntryDetails = (CompletedEntry) getIntent().getSerializableExtra("completedEntry");

        name = findViewById(R.id.editTextTextPersonName11);
        category = findViewById(R.id.editTextTextMultiLine5);
        dateFrom = findViewById(R.id.calendar_entry_dateFrom);
        dateTo = findViewById(R.id.editTextDate6);
        notes = findViewById(R.id.editTextTextMultiLine4);
        ratingBar = findViewById(R.id.ratingBar2);
        seekBar = findViewById(R.id.seekBar);

        name.setText(completedEntryDetails.getActivity().getName());
        category.setText(completedEntryDetails.getActivity().getCategory().getName());
        dateFrom.setText(completedEntryDetails.getDateFrom().split(" ")[1]);
        dateTo.setText(completedEntryDetails.getDateTo().split(" ")[1]);
        notes.setText(completedEntryDetails.getFinalNotes());
        ratingBar.setRating(Float.parseFloat(completedEntryDetails.getRating().toString()));
        seekBar.setProgress(Integer.parseInt(completedEntryDetails.getCompletion().toString()));
    }
}
