package com.example.iattendance.Sign_up_Screens.Faculty_signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.iattendance.Bottom_navigation.Faculty_bottom_nav;
import com.example.iattendance.R;
import com.example.iattendance.Utils.Faculty.FacultySessionManager;
import com.example.iattendance.Utils.Faculty.Validation.facultyValidation;
import com.example.iattendance.Utils.Faculty.db.FacultyDb;
import com.example.iattendance.Utils.Faculty.db.InsertDbCallback;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;

public class Faculty_signup_pg2 extends AppCompatActivity {
    String coll_code, phNo;
    FacultyDb facultyDb;
    HashMap<String, String> map;
    facultyValidation validation;
    TextInputEditText facultyName, facultyPassword;
    FacultySessionManager facultySession;
    Button signup_btn;
    ProgressBar progress_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faculty_signup_pg2);

        initializeView();
        setInProgress(false);

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setInProgress(true);
                if (validation.isValidName(facultyName.getText().toString())) {
                    map.put("facultyName", facultyName.getText().toString().trim());
                } else {
                    facultyName.setError("Empty field");
                }
                if (validation.isValidPassword(facultyPassword.getText().toString())) {
                    map.put("password", facultyPassword.getText().toString().trim());
                } else {
                    facultyPassword.setError("Invalid password");
                }
                map.put("collegeCode", coll_code);
                map.put("contact", phNo);
                map.put("course", "");

                facultyDb = new FacultyDb(getApplicationContext(), map);
                facultyDb.insertDb(new InsertDbCallback() {
                    @Override
                    public void onInsertComplete(boolean success) {
                        if (success) {
                            Intent intent = new Intent(Faculty_signup_pg2.this, Faculty_bottom_nav.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            setInProgress(false);
                            startActivity(intent);
                        } else {
                            setInProgress(false);
                            Toast.makeText(Faculty_signup_pg2.this, "Not able to insert!...", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onDataRetrieval(Map<String, String> data) {

                    }
                });
            }
        });

    }

    private void initializeView() {
        Intent i = getIntent();
        coll_code = i.getStringExtra("collegeCode");
        phNo = i.getStringExtra("phoneNumber");
        map = new HashMap<>();
        facultyName = findViewById(R.id.facultyName);
        facultyPassword = findViewById(R.id.facultyPassword);
        signup_btn = findViewById(R.id.signup_btn);
        progress_bar = findViewById(R.id.progress_bar);
        validation = new facultyValidation(getApplicationContext());

    }

    private void setInProgress(boolean inProgress) {
        if (inProgress) {
            //code for progress bar visibility
            //pg_bg.setBackgroundColor(getResources().getColor(R.color.progress_bar_bg));
            progress_bar.setVisibility(View.VISIBLE);
            facultyName.setEnabled(false);
            facultyPassword.setEnabled(false);
            signup_btn.setEnabled(false);

        } else {
            progress_bar.setVisibility(View.GONE);
            facultyName.setEnabled(true);
            facultyPassword.setEnabled(true);
            signup_btn.setEnabled(true);
        }
    }
}