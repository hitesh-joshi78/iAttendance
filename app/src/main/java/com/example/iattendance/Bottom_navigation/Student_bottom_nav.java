package com.example.iattendance.Bottom_navigation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.iattendance.Dashboard_Fragments.Student.HomeFragment_student;
import com.example.iattendance.Dashboard_Fragments.Student.SettingsFragment_student;
import com.example.iattendance.Dashboard_Fragments.Student.StatsFragment_student;
import com.example.iattendance.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Student_bottom_nav extends AppCompatActivity {
    FrameLayout frame_layout;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_bottom_nav);

//        Hooks
        frame_layout = findViewById(R.id.frame_layout);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        getSupportFragmentManager().beginTransaction().add(R.id.frame_layout, new HomeFragment_student()).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.home) {
                item.setChecked(true);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new HomeFragment_student()).commit();
            }
            else if (itemId == R.id.stats) {
                item.setChecked(true);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new StatsFragment_student()).commit();
            }
            else if (itemId == R.id.settings) {
                item.setChecked(true);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new SettingsFragment_student()).commit();
            }
            return true;
        });

    }
}