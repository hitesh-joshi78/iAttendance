package com.example.iattendance;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iattendance.Login.Login_screen;
import com.google.android.material.button.MaterialButton;

public class Welcome_screen extends AppCompatActivity {
    Button loginBtn;
    MaterialButton signupBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen);

//        Hooks
        loginBtn = findViewById(R.id.loginBtn);
        signupBtn = findViewById(R.id.signupBtn);

        loginBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Welcome_screen.this, Login_screen.class);
            startActivity(intent);
        });

        signupBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Welcome_screen.this, SelectRole.class);
            startActivity(intent);
        });
    }
}