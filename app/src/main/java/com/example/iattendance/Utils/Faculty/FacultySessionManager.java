package com.example.iattendance.Utils.Faculty;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.util.HashMap;

public class FacultySessionManager {
    public static final String PREF_NAME = "FacultySession";
    public static final String KEY_FC_ID = "facultyId";
    public static final String KEY_FC_PASS = "facultyPass";
    public static final String KEY_FC_PHONE = "facultyPhone";
    public static final String KEY_FC_COLLEGE = "facultyCollegeCode";
    public static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    public static final String KEY_FC_NAME = "facultyName";
    public static final String KEY_FC_COURSE = "facultyCourse";
    public SharedPreferences pref;
    public SharedPreferences.Editor editor;
    public Context context;

    public FacultySessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void createSession(String facultyId, String facultyPass, String facultyPhone, String collegeCode, String facultyName, String course) {
//        Toast.makeText(context, "NAME : " + facultyName, Toast.LENGTH_SHORT).show();
        editor.putString(KEY_FC_ID, facultyId);
        editor.putString(KEY_FC_PASS, facultyPass);
        editor.putString(KEY_FC_PHONE, facultyPhone);
        editor.putString(KEY_FC_NAME, facultyName);
        editor.putString(KEY_FC_COLLEGE, collegeCode);
        editor.putString(KEY_FC_COURSE, course);
        editor.commit();
    }

    public void updateSession(String courseName) {
        editor.putString(KEY_FC_COURSE, courseName);
        Toast.makeText(context, "Successfully updated session!...", Toast.LENGTH_SHORT).show();
        editor.commit();
    }

    public void createLoginSession(String userId, String username) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putString("userId", userId);
        editor.putString("username", username);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<>();
        user.put(KEY_FC_ID, pref.getString(KEY_FC_ID, null));
        user.put(KEY_FC_PASS, pref.getString(KEY_FC_PASS, null));
        user.put(KEY_FC_PHONE, pref.getString(KEY_FC_PHONE, null));
        user.put(KEY_FC_COLLEGE, pref.getString(KEY_FC_COLLEGE, null));
        user.put(KEY_FC_NAME, pref.getString(KEY_FC_NAME, null));
        user.put(KEY_FC_COURSE, pref.getString(KEY_FC_COURSE, null));
        return user;
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();
    }
}
