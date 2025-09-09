package com.example.iattendance.Utils.Admin;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {
    public static final String PREF_NAME = "AdminSession";
    public static final String KEY_AD_ID = "adminId";
    public static final String KEY_AD_PASS = "adminPass";
    public static final String KEY_AD_PHONE = "adminPhone";
    public static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    public SharedPreferences pref;
    public SharedPreferences.Editor editor;
    public Context context;

    // Constructor
    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    // Method to store session data
    public void createSession(String adminId, String adminPass, String adminPhone) {
        editor.putString(KEY_AD_ID, adminId);
        editor.putString(KEY_AD_PASS, adminPass);
        editor.putString(KEY_AD_PHONE, adminPhone);
        editor.commit();
    }

    // Method to store session data when user logs in
    public void createLoginSession(String userId, String username) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putString("userId", userId);
        editor.putString("username", username);
        editor.apply();
    }

    // Method to check if user is logged in
    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    // Method to get stored user details
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<>();
        user.put(KEY_AD_ID, pref.getString(KEY_AD_ID, null));
        user.put(KEY_AD_PASS, pref.getString(KEY_AD_PASS, null));
        user.put(KEY_AD_PHONE, pref.getString(KEY_AD_PHONE, null));
        return user;
    }

    // Method to log out user
    public void logoutUser() {
        editor.clear();
        editor.commit();
    }
}

