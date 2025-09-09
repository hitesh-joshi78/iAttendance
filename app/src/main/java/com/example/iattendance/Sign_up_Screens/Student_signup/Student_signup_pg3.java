package com.example.iattendance.Sign_up_Screens.Student_signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.iattendance.Bottom_navigation.Student_bottom_nav;
import com.example.iattendance.R;
import com.example.iattendance.Utils.Faculty.db.InsertDbCallback;
import com.example.iattendance.Utils.Student.Validation.studentValidation;
import com.example.iattendance.Utils.Student.db.StudentDb;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Student_signup_pg3 extends AppCompatActivity {

    TextInputEditText studName, studPassword, studCourse, studDiv, studRoll;
    String collegeCode, phoneNumber;
    Button signUpBtn;
    studentValidation validation;
    HashMap<String, String> map;
    StudentDb studentDb;
    ProgressBar progress_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_signup_pg3);


        initialiseViews();


        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setInProgress(true);
                String _name = Objects.requireNonNull(studName.getText()).toString();
                String _password = Objects.requireNonNull(studPassword.getText()).toString();
                String _course = Objects.requireNonNull(studCourse.getText()).toString().trim();
                String _div = Objects.requireNonNull(studDiv.getText()).toString().trim();
                String _rollNo = Objects.requireNonNull(studRoll.getText()).toString().trim();

                if (validation.emptyValidation(_name) && validation.emptyValidation(_password)
                        && validation.emptyValidation(_course) && validation.emptyValidation(_div)
                        && validation.emptyValidation(_rollNo)) {
                    if (validation.isValidPassword(_password)) {
                        map.put("studName", _name);
                        map.put("studPassword", _password);
                        map.put("studContact", phoneNumber);
                        map.put("collegeCode", collegeCode);
                        map.put("studCourse", _course);
                        map.put("studDiv", _div);
                        map.put("studRoll", _rollNo);

                        studentDb = new StudentDb(getApplicationContext(), map);
                        studentDb.insertDb(new InsertDbCallback() {
                            @Override
                            public void onInsertComplete(boolean success) {
                                if (success) {
                                    Intent intent = new Intent(Student_signup_pg3.this, Student_bottom_nav.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    setInProgress(false);
                                    startActivity(intent);
                                } else {
                                    setInProgress(false);
                                }
                            }

                            @Override
                            public void onDataRetrieval(Map<String, String> data) {

                            }
                        });
                    }
                }
            }
        });
    }

    private void initialiseViews() {
        studName = findViewById(R.id.studName);
        studPassword = findViewById(R.id.studPassword);
        studCourse = findViewById(R.id.studCourse);
        studDiv = findViewById(R.id.studDivision);
        studRoll = findViewById(R.id.studRollCall);
        signUpBtn = findViewById(R.id.signUpBtn);
        progress_bar = findViewById(R.id.progress_bar);
        setInProgress(false);

        Intent i = getIntent();
        phoneNumber = i.getStringExtra("phoneNumber");
        collegeCode = i.getStringExtra("collegeCode");

        map = new HashMap<>();
        validation = new studentValidation(getApplicationContext());


    }

    private void setInProgress(boolean inProgress) {
        if (inProgress) {
            //code for progress bar visibility
            //pg_bg.setBackgroundColor(getResources().getColor(R.color.progress_bar_bg));
            progress_bar.setVisibility(View.VISIBLE);
            studName.setEnabled(false);
            studPassword.setEnabled(false);
            studCourse.setEnabled(false);
            studDiv.setEnabled(false);
            signUpBtn.setEnabled(false);
            studRoll.setEnabled(false);


        } else {
            progress_bar.setVisibility(View.GONE);
            studName.setEnabled(true);
            studPassword.setEnabled(true);
            studCourse.setEnabled(true);
            studDiv.setEnabled(true);
            signUpBtn.setEnabled(true);
            studRoll.setEnabled(true);
        }
    }
}