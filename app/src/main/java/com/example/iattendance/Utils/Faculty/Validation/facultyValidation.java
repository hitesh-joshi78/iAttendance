package com.example.iattendance.Utils.Faculty.Validation;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class facultyValidation {
    String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$";
    Pattern pattern = Pattern.compile(regex);
    Context c;

    public facultyValidation(Context context) {
        this.c = context;
    }

    public boolean isValidCode(String coll_code) {
        return (!TextUtils.isEmpty(coll_code));
    }

    public boolean isValidName(String name) {
        return (!TextUtils.isEmpty(name));
    }

    public boolean isValidPassword(String password) {
        if (!password.isEmpty()) {
            if (password.length() > 8) {
                Matcher matcher = pattern.matcher(password);
                if (matcher.matches()) {
                    return true;
                } else {
                    Toast.makeText(c, "Password invalid!...", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {
                Toast.makeText(c, "Password length greater than 8!..", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        Toast.makeText(c, "Empty password!...", Toast.LENGTH_SHORT).show();
        return false;

    }

    public static Task<DocumentSnapshot> checkCollegeCodeExists(String collegeCode) {
        return FirebaseFirestore.getInstance()
                .collection("College code")
                .document(collegeCode)
                .get();
    }
}
