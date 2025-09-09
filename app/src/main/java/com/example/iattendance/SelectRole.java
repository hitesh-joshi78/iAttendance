package com.example.iattendance;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iattendance.Sign_up_Screens.Signup_OTP_pg;
import com.example.iattendance.Sign_up_Screens.Faculty_Student_signup_pg1;
import com.example.iattendance.Utils.Admin.Utils;
import com.google.android.material.button.MaterialButton;

public class SelectRole extends AppCompatActivity {
    MaterialButton nxtBtn;
    RadioGroup roleRadio;
    RadioButton admin_rb, faculty_rb, student_rb;
    String role_str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_role_pg);

        initalizeViews();

        roleRadio.setOnCheckedChangeListener((group, checkedId) -> {
            if (R.id.admin_rb == checkedId) {
                role_str = admin_rb.getText().toString();
            } else if (R.id.faculty_rb == checkedId) {
                role_str = faculty_rb.getText().toString();
            } else if (R.id.student_rb == checkedId) {
                role_str = student_rb.getText().toString();
            }
        });

        nxtBtn.setOnClickListener(v -> {
            if (role_str.isEmpty()) {
                Utils.showToast(SelectRole.this, "Please select an option.");
            } else if (role_str.equals(admin_rb.getText().toString())) {
                Intent intent = new Intent(SelectRole.this, Signup_OTP_pg.class);
                intent.putExtra("role", "Admin");
                startActivity(intent);
            } else if (role_str.equals(faculty_rb.getText().toString())) {
                Intent intent = new Intent(SelectRole.this, Faculty_Student_signup_pg1.class);
                intent.putExtra("role", "Faculty");
                startActivity(intent);
            } else if (role_str.equals(student_rb.getText().toString())) {
                Intent intent = new Intent(SelectRole.this, Faculty_Student_signup_pg1.class);
                intent.putExtra("role", "Student");
                startActivity(intent);
            }
        });
    }

    private void initalizeViews() {
        nxtBtn = findViewById(R.id.nxtBtn);
        roleRadio = findViewById(R.id.roleRadio);
        admin_rb = findViewById(R.id.admin_rb);
        faculty_rb = findViewById(R.id.faculty_rb);
        student_rb = findViewById(R.id.student_rb);
    }
}