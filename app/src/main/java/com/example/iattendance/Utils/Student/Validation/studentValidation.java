package com.example.iattendance.Utils.Student.Validation;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class studentValidation {

    String passRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$";
    String divRegex = "^[a-zA-Z]*$";
    Pattern pattern = Pattern.compile(passRegex);
    Context c;

    public studentValidation(Context context) {
        this.c = context;
    }

    public boolean emptyValidation(String data) {
        return (!TextUtils.isEmpty(data));
    }

    public boolean isValidPassword(String password) {
        if (!password.isEmpty()) {
            if (password.length() > 8) {
                Matcher matcher = pattern.matcher(password);
                if (matcher.matches()) {
                    return true;
                } else {
                    Toast.makeText(this.c, "Password invalid!...", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {
                Toast.makeText(this.c, "Password length greater than 8!..", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        Toast.makeText(this.c, "Empty password!...", Toast.LENGTH_SHORT).show();
        return false;

    }

    public boolean isValidDivision(String div) {
        return Pattern.matches(passRegex, div);
    }
}
