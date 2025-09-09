package com.example.iattendance.Utils.Student;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class StudentSessionManager {
    public static final String PREF_NAME = "StudentSession";
    public static final String KEY_ST_ID = "studentId";
    public static final String KEY_ST_PASS = "studentPass";
    public static final String KEY_ST_PHONE = "studentPhone";
    public static final String KEY_ST_COLLEGE = "studentCollegeCode";
    public static final String KEY_ST_LOGGED_IN = "isLoggedIn";
    public static final String KEY_ST_NAME = "studentName";
    public static final String KEY_ST_COURSE = "studCourse";
    public static final String KEY_ST_DIV = "studDivision";
    public static final String KEY_ST_ROLL = "studRollNo";
    public SharedPreferences pref;
    public SharedPreferences.Editor editor;
    public Context context;

    public StudentSessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void createSession(String studentId, String studentPass, String studentPhone, String collegeCode, String studentName, String studCourse, String studDivision, String studRollNo) {

        editor.putString(KEY_ST_ID, studentId);
        editor.putString(KEY_ST_PASS, studentPass);
        editor.putString(KEY_ST_PHONE, studentPhone);
        editor.putString(KEY_ST_NAME, studentName);
        editor.putString(KEY_ST_COLLEGE, collegeCode);
        editor.putString(KEY_ST_COURSE, studCourse);
        editor.putString(KEY_ST_DIV, studDivision);
        editor.putString(KEY_ST_ROLL, studRollNo);
        editor.commit();
    }

    public void createLoginSession(String userId, String username) {
        editor.putBoolean(KEY_ST_LOGGED_IN, true);
        editor.putString("userId", userId);
        editor.putString("username", username);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_ST_LOGGED_IN, false);
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<>();
        user.put(KEY_ST_ID, pref.getString(KEY_ST_ID, null));
        user.put(KEY_ST_PASS, pref.getString(KEY_ST_PASS, null));
        user.put(KEY_ST_PHONE, pref.getString(KEY_ST_PHONE, null));
        user.put(KEY_ST_COLLEGE, pref.getString(KEY_ST_COLLEGE, null));
        user.put(KEY_ST_NAME, pref.getString(KEY_ST_NAME, null));
        user.put(KEY_ST_COURSE, pref.getString(KEY_ST_COURSE, null));
        user.put(KEY_ST_DIV, pref.getString(KEY_ST_DIV, null));
        user.put(KEY_ST_ROLL, pref.getString(KEY_ST_ROLL, null));
        return user;
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();
    }
}
