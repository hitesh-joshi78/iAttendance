package com.example.iattendance.Utils.Faculty;

import android.content.Context;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Utils {

    public static void showToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }


//    ############################################################################################################


    public static boolean isValidCode(String coll_code) {
        return (!TextUtils.isEmpty(coll_code));
    }


//    ############################################################################################################


    public static Task<DocumentSnapshot> checkCollegeCodeExists(String collegeCode) {
        return FirebaseFirestore.getInstance()
                .collection("College code")
                .document(collegeCode)
                .get();
    }
}
