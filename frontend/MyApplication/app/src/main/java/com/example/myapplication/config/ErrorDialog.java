package com.example.myapplication.config;

import android.app.Dialog;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

/**
 * This class creates a pop-up that will appear when an error is encountered
 */
public class ErrorDialog {

    public static void showDialog(AppCompatActivity activity, String errorMessage) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.error_dialog);
        final Button dismissButton = dialog.findViewById(R.id.dismissButton);
        final TextView errorText = dialog.findViewById(R.id.errorText);
        errorText.setText(errorMessage);
        dismissButton.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

}
