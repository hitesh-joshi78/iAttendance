package com.example.iattendance.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.iattendance.Bottom_navigation.Admin_bottom_nav;
import com.example.iattendance.Bottom_navigation.Faculty_bottom_nav;
import com.example.iattendance.Bottom_navigation.Student_bottom_nav;
import com.example.iattendance.R;
import com.example.iattendance.Sign_up_Screens.Admin_signup.ModalClass;
import com.example.iattendance.Utils.Admin.SessionManager;
//import com.example.iattendance.Utils.Faculty.FacultySessionManager;
import com.example.iattendance.Utils.Admin.Utils;
import com.example.iattendance.Utils.CheckLogin;

import com.example.iattendance.Utils.Faculty.FacultySessionManager;
import com.example.iattendance.Utils.Faculty.db.FacultyDb;
import com.example.iattendance.Utils.Faculty.db.InsertDbCallback;
import com.example.iattendance.Utils.Student.StudentSessionManager;
import com.example.iattendance.Utils.Student.db.StudentDb;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;

public class Login_screen extends AppCompatActivity {

    MaterialButton loginBtn, signupBtn;
    TextInputEditText contactNo, password;
    ProgressBar progress_bar;
    FacultyDb facultyDb;
    StudentDb studentDb;

    FacultySessionManager facultySession;
    StudentSessionManager studentSession;
    SessionManager adminSession;

    Utils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

//        Hooks
        initialiseViews();
        setInProgress(false);

        loginBtn.setOnClickListener(v -> {
            setInProgress(true);
            String user_contact = contactNo.getText().toString().substring(0, 5)
                    + " " + contactNo.getText().toString().substring(5, 10);
            String user_password = password.getText().toString().trim();
            utils.getLoginDetails(new CheckLogin() {

                @Override
                public void isValidUser(boolean success, String role, String id, String collegeCode) {
                    if (success) {
                        if (role != null && id != null) {
                            if (role.equals("admin")) {
                                Intent intent = new Intent(Login_screen.this, Admin_bottom_nav.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                adminSession.createSession(id, password.getText().toString(), contactNo.getText().toString());
                                utils.getAdmin(id).addOnSuccessListener(new OnSuccessListener<ModalClass>() {
                                    @Override
                                    public void onSuccess(ModalClass modalClass) {
                                        adminSession.createLoginSession(id, modalClass.getAdminName());
                                        setInProgress(false);
                                        startActivity(intent);
                                    }
                                });

                            } else if (role.equals("faculty")) {
                                Intent intent = new Intent(Login_screen.this, Faculty_bottom_nav.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                facultyDb = new FacultyDb(getApplicationContext(), new HashMap<>());

                                facultyDb.getFaculty(new InsertDbCallback() {
                                    @Override
                                    public void onInsertComplete(boolean success) {

                                    }

                                    @Override
                                    public void onDataRetrieval(Map<String, String> map) {
                                        if (map.size() > 0) {
                                            facultySession.createSession(id, user_password, user_contact, collegeCode, map.get("facultyName"), map.get("course"));
                                            facultySession.createLoginSession(id, map.get("facultyName"));
                                            setInProgress(false);
                                            startActivity(intent);
                                        }
                                    }
                                }, id, collegeCode, user_contact);

                            } else {

                                Intent intent = new Intent(Login_screen.this, Student_bottom_nav.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                studentDb = new StudentDb(getApplicationContext(), new HashMap<>());
                                studentDb.getStudent(new InsertDbCallback() {
                                    @Override
                                    public void onInsertComplete(boolean success) {

                                    }

                                    @Override
                                    public void onDataRetrieval(Map<String, String> data) {
                                        if (data.size() > 0) {
//                                            id, user_password, user_contact, collegeCode, data.get("studName"), ""
                                            studentSession.createSession(id, user_password, user_contact, collegeCode, data.get("studName"), data.get("studCourse"), data.get("studDiv"), data.get("studRollNo"));
                                            studentSession.createLoginSession(id, data.get("studName"));
                                            setInProgress(false);
                                            startActivity(intent);
                                        }
                                    }
                                }, id, collegeCode, user_contact);

                            }
                        } else {
                            Toast.makeText(Login_screen.this, "ROLE IS NULL: ", Toast.LENGTH_SHORT).show();
                            setInProgress(false);
                        }
                    } else {
                        Toast.makeText(Login_screen.this, "SUCCESS IS FALSE: ", Toast.LENGTH_SHORT).show();
                        setInProgress(false);
                    }
                }

            }, getApplicationContext(), user_contact, user_password);

        });
    }

    private void initialiseViews() {

        loginBtn = findViewById(R.id.loginBtn);
        signupBtn = findViewById(R.id.signupBtn);
        password = findViewById(R.id.password);
        progress_bar = findViewById(R.id.progress_bar);
        contactNo = findViewById(R.id.contactNo);

        utils = new Utils();
        adminSession = new SessionManager(getApplicationContext());
        facultySession = new FacultySessionManager(getApplicationContext());
        studentSession = new StudentSessionManager(getApplicationContext());

    }


    void setInProgress(boolean inProgress) {
        if (inProgress) {
            //code for progress bar visibility
            //pg_bg.setBackgroundColor(getResources().getColor(R.color.progress_bar_bg));
            progress_bar.setVisibility(View.VISIBLE);
            contactNo.setEnabled(false);
            password.setEnabled(false);
            loginBtn.setEnabled(false);

        } else {
            progress_bar.setVisibility(View.GONE);
            contactNo.setEnabled(true);
            password.setEnabled(true);
            loginBtn.setEnabled(true);

        }
    }
}