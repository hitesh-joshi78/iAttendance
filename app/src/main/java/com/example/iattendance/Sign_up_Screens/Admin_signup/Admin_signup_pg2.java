package com.example.iattendance.Sign_up_Screens.Admin_signup;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iattendance.Bottom_navigation.Admin_bottom_nav;
import com.example.iattendance.R;
import com.example.iattendance.Utils.Admin.SessionManager;
import com.example.iattendance.Utils.Admin.Utils;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;


import java.util.Objects;
import java.util.Random;

public class Admin_signup_pg2 extends AppCompatActivity {
    Intent prevIntent;
    String admin_id, admin_name, admin_phone, admin_pass, college_code, college_name, college_addr, random_num;

    ImageButton backBtn;
    TextInputLayout admin_name_tb, admin_pass_tb, college_code_tb, college_name_tb, college_addr_tb;
    MaterialButton signup_btn;

    private Utils utils;
    // Creating session manager object
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_signup_pg2);


        initializeView();

        prevIntent = getIntent();
        admin_phone = prevIntent.getStringExtra("phoneNumber");

        utils = new Utils();

        random_num = generateRandom4DigitNumber();

        signup_btn.setOnClickListener(v -> {

            admin_name = Objects.requireNonNull(admin_name_tb.getEditText()).getText().toString();
            admin_pass = Objects.requireNonNull(admin_pass_tb.getEditText()).getText().toString();
            college_code = Objects.requireNonNull(college_code_tb.getEditText()).getText().toString();
            college_name = Objects.requireNonNull(college_name_tb.getEditText()).getText().toString();
            college_addr = Objects.requireNonNull(college_addr_tb.getEditText()).getText().toString();

            admin_id = college_code + "-" + random_num;

//            if (validateFields(admin_name, admin_pass, college_code, college_name, college_addr)) {
//                utils.createAdmin(admin_id, admin_name, admin_phone, admin_pass, admin_id, college_name, college_addr)
//                        .addOnSuccessListener(aVoid -> {
//
//                            // Storing session data
//                            sessionManager.createSession(admin_id, admin_pass, admin_phone);
//                            // Logging in
//                            sessionManager.createLoginSession(admin_id, admin_name);
//
//                            // To add phone nos. of admin in 'Phone numbers' table
//                            if (utils.addPhone(admin_id, admin_phone) && utils.addCollegeCode(college_name, admin_id)) {
//
//                                Intent intent = new Intent(Admin_signup_pg2.this, Admin_bottom_nav.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                startActivity(intent);
//
//                                Utils.showToast(Admin_signup_pg2.this, "Signup successful!");
//                            }
//
//
//                        })
//                        .addOnFailureListener(e -> {
//                            Utils.showToast(Admin_signup_pg2.this, "Signup failed!\nPlease try again.");
//                        });
//            }
//            if (validateFields(admin_name, admin_pass, college_code, college_name, college_addr)) {
//                utils.createAdmin(admin_id, admin_name, admin_phone, admin_pass, admin_id, college_name, college_addr)
//                        .addOnSuccessListener(aVoid -> {
//                            // Storing session data
//                            sessionManager.createSession(admin_id, admin_pass, admin_phone);
//                            // Logging in
//                            sessionManager.createLoginSession(admin_id, admin_name);
//
//                            // Add phone numbers and college code
//                            Task<Void> addPhoneTask = utils.addPhone(admin_id, admin_phone);
//                            Task<Void> addCollegeCodeTask = utils.addCollegeCode(college_name, admin_id);
//                            Task<Void> addLoginDetails = Utils.storeLoginDetails(admin_phone, admin_pass);
//
//                            // Handle tasks asynchronously
//                            Tasks.whenAllComplete(addPhoneTask, addCollegeCodeTask)
//                                    .addOnSuccessListener(results -> {
//                                        // Check if both tasks were successful
//                                        if (addPhoneTask.isSuccessful() && addCollegeCodeTask.isSuccessful() && addLoginDetails.isSuccessful()) {
//
//                                            Intent intent = new Intent(Admin_signup_pg2.this, Admin_bottom_nav.class);
//                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                            startActivity(intent);
//
//                                            Utils.showToast(Admin_signup_pg2.this, "Signup successful!");
//                                        } else {
//                                            // Handle failure of one or both tasks
//
//                                            Utils.showToast(Admin_signup_pg2.this, String.valueOf(addPhoneTask.isSuccessful()));
//                                            Utils.showToast(Admin_signup_pg2.this, String.valueOf(addCollegeCodeTask.isSuccessful()));
//                                            Utils.showToast(Admin_signup_pg2.this, String.valueOf(addLoginDetails.isSuccessful()));
//                                            Utils.showToast(Admin_signup_pg2.this, "Failed to add phone or college code.");
//                                        }
//                                    })
//                                    .addOnFailureListener(e -> {
//                                        // Handle failure of whenAllComplete task
//                                        Utils.showToast(Admin_signup_pg2.this, "Failed to complete tasks: " + e.getMessage());
//                                    });
//                        })
//                        .addOnFailureListener(e -> {
//                            Utils.showToast(Admin_signup_pg2.this, "Signup failed!\nPlease try again.");
//                        });
//            }
            if (validateFields(admin_name, admin_pass, college_code, college_name, college_addr)) {
                utils.createAdmin(admin_id, admin_name, admin_phone, admin_pass, admin_id, college_name, college_addr)
                        .addOnSuccessListener(aVoid -> {
                            // Storing session data
                            sessionManager.createSession(admin_id, admin_pass, admin_phone);
                            // Logging in
                            sessionManager.createLoginSession(admin_id, admin_name);

                            // Add phone numbers and college code
                            Task<Void> addPhoneTask = utils.addPhone(admin_id, admin_phone);
                            Task<Void> addCollegeCodeTask = utils.addCollegeCode(admin_id);
                            Task<Void> addLoginDetails = Utils.storeLoginDetails(admin_phone, admin_pass, admin_id, admin_id, "admin");

                            // Handle tasks asynchronously
                            Tasks.whenAllComplete(addPhoneTask, addCollegeCodeTask, addLoginDetails)
                                    .addOnSuccessListener(results -> {
                                        // Check if all tasks were successful
                                        boolean allTasksSuccessful = true;
                                        for (Task<?> task : results) {
                                            if (!task.isSuccessful()) {
                                                allTasksSuccessful = false;
                                                break;
                                            }
                                        }

                                        if (allTasksSuccessful) {
                                            // All tasks were successful
                                            Intent intent = new Intent(Admin_signup_pg2.this, Admin_bottom_nav.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);

                                            Utils.showToast(Admin_signup_pg2.this, "Signup successful!");
                                        } else {
                                            // Handle failure if any of the tasks failed
                                            Utils.showToast(Admin_signup_pg2.this, "Failed to add phone or college code.");
                                        }
                                    })
                                    .addOnFailureListener(e -> {
                                        // Handle failure of whenAllComplete task
                                        Utils.showToast(Admin_signup_pg2.this, "Failed to complete tasks: " + e.getMessage());
                                    });
                        })
                        .addOnFailureListener(e -> {
                            Utils.showToast(Admin_signup_pg2.this, "Signup failed!\nPlease try again.");
                        });
            }


        });
    }

    private void initializeView() {
        backBtn = findViewById(R.id.backBtn);
        admin_name_tb = findViewById(R.id.admin_name_tb);
        admin_pass_tb = findViewById(R.id.admin_pass_tb);
        college_code_tb = findViewById(R.id.college_code_tb);
        college_name_tb = findViewById(R.id.college_name_tb);
        college_addr_tb = findViewById(R.id.college_addr_tb);
        signup_btn = findViewById(R.id.signup_btn);

        sessionManager = new SessionManager(getApplicationContext());
    }

    private boolean validateFields(String name, String password, String collegeCode, String collegeName, String collegeAddress) {
        boolean isValid = true;

        if (!Utils.isValidName(name)) {
            isValid = false;
            admin_name_tb.setError("Enter name");
        }

        if (!Utils.isValidPassword(password)) {
            isValid = false;
            admin_pass_tb.setError("Empty password or length is less than 6");
        }

        if (!Utils.isValidCollegeCode(collegeCode)) {
            isValid = false;
            college_code_tb.setError("Enter college code");
        }

        if (!Utils.isValidCollegeName(collegeName)) {
            isValid = false;
            college_name_tb.setError("Enter college name");
        }

        if (!Utils.isValidCollegeAddress(collegeAddress)) {
            isValid = false;
            college_addr_tb.setError("Enter college address");
        }

        return isValid;
    }

    @SuppressLint("DefaultLocale")
    public static String generateRandom4DigitNumber() {
        Random random = new Random();
        int randomNumber = random.nextInt(9000) + 1000; // Generates a random number between 1000 and 9999 (inclusive)
        return String.format("%04d", randomNumber); // Ensures the number always has 4 digits
    }
}