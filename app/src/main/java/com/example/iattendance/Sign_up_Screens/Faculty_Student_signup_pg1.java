package com.example.iattendance.Sign_up_Screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iattendance.R;
import com.example.iattendance.Utils.Faculty.Utils;
import com.example.iattendance.Utils.Faculty.Validation.facultyValidation;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class Faculty_Student_signup_pg1 extends AppCompatActivity {
    ProgressBar progress_bar;
    ImageButton back_btn;
    TextInputLayout college_code_tb;
    MaterialButton nxt_btn, login_btn;
    facultyValidation validation;
    String coll_code;
    String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faculty_student_signup_pg1);

        initializeViews();

        back_btn.setOnClickListener(v -> finish());

        nxt_btn.setOnClickListener(v -> {
            coll_code = Objects.requireNonNull(college_code_tb.getEditText()).getText().toString();
            // Validate email and password fields
            if (!validation.isValidCode(coll_code)) {
                college_code_tb.setError("Please enter college code");
            } else {
                setInProgress(true);

                validation.checkCollegeCodeExists(coll_code)
                        .addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {

                                // College code exists
                                if (role.equals("Faculty")) {
                                    Intent intent = new Intent(Faculty_Student_signup_pg1.this, Signup_OTP_pg.class);
                                    intent.putExtra("role", "Faculty");
                                    intent.putExtra("collegeCode", coll_code);
                                    startActivity(intent);
                                    setInProgress(false);
                                } else {
                                    Intent intent = new Intent(Faculty_Student_signup_pg1.this, Signup_OTP_pg.class);
                                    intent.putExtra("role", "Student");
                                    intent.putExtra("collegeCode", coll_code);
                                    startActivity(intent);
                                    setInProgress(false);
                                }


                            } else {
                                // College code does not exist
                                setInProgress(false);

                                Utils.showToast(Faculty_Student_signup_pg1.this, "College code does not exist");
                            }
                        })
                        .addOnFailureListener(e -> {
                            // Handle failure
                            setInProgress(false);

                            Utils.showToast(Faculty_Student_signup_pg1.this, "Failed to check college code existence: " + e.getMessage());
                        });
            }
        });
    }

    private void initializeViews() {
        progress_bar = findViewById(R.id.progress_bar);
        back_btn = findViewById(R.id.back_btn);
        college_code_tb = findViewById(R.id.college_code_tb);
        nxt_btn = findViewById(R.id.nxt_btn);
        login_btn = findViewById(R.id.login_btn);
        validation = new facultyValidation(getApplicationContext());
        Intent i = getIntent();
        role = i.getStringExtra("role");
    }

    void setInProgress(boolean inProgress) {
        if (inProgress) {
            progress_bar.setVisibility(View.VISIBLE);
            back_btn.setEnabled(false);
            college_code_tb.setEnabled(false);
            nxt_btn.setEnabled(false);
            login_btn.setEnabled(false);
        } else {
            progress_bar.setVisibility(View.GONE);
            back_btn.setEnabled(true);
            college_code_tb.setEnabled(true);
            nxt_btn.setEnabled(true);
            login_btn.setEnabled(true);
        }
    }
}