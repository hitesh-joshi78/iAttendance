package com.example.iattendance;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iattendance.Bottom_navigation.Admin_bottom_nav;
import com.example.iattendance.Bottom_navigation.Faculty_bottom_nav;
import com.example.iattendance.Bottom_navigation.Student_bottom_nav;
import com.example.iattendance.Utils.Admin.SessionManager;
import com.example.iattendance.Utils.Faculty.FacultySessionManager;
import com.example.iattendance.Utils.Student.StudentSessionManager;

public class MainActivity extends AppCompatActivity {
    SessionManager admin_session_manager;
    FacultySessionManager faculty_session_manager;
    StudentSessionManager studentSessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        admin_session_manager = new SessionManager(MainActivity.this);
        faculty_session_manager = new FacultySessionManager(MainActivity.this);
        studentSessionManager = new StudentSessionManager(MainActivity.this);

        new Handler().postDelayed(() -> {
            if (admin_session_manager.isLoggedIn()) {
                Intent intent = new Intent(MainActivity.this, Admin_bottom_nav.class);
                startActivity(intent);
                finish();
            } else if (faculty_session_manager.isLoggedIn()) {
                Intent intent = new Intent(MainActivity.this, Faculty_bottom_nav.class);
                startActivity(intent);
                finish();
            } else if (studentSessionManager.isLoggedIn()) {
                Intent intent = new Intent(MainActivity.this, Student_bottom_nav.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(MainActivity.this, Welcome_screen.class);
                startActivity(intent);
                finish();
            }
        }, 3000);

        //Message from @Rupesh to add for commit
        //Message from @Hitesh to push new content to branch
    }
}