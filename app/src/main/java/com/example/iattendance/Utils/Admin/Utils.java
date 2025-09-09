package com.example.iattendance.Utils.Admin;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.iattendance.Login.Login_screen;
import com.example.iattendance.Sign_up_Screens.Admin_signup.ModalClass;
import com.example.iattendance.Utils.CheckLogin;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Utils {

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }


//    -------------------------------------------------------------------------------------------------------------


    // Validate name field
    public static boolean isValidName(String name) {
        // Add your validation logic here
        return name != null && !name.trim().isEmpty();
    }

    // Validate password field
    public static boolean isValidPassword(String password) {
        // Add your validation logic here
        return password != null && password.length() >= 6; // Example: Password must be at least 6 characters long
    }

    // Validate college code field
    public static boolean isValidCollegeCode(String collegeCode) {
        // Add your validation logic here
        return collegeCode != null && !collegeCode.trim().isEmpty();
    }

    // Validate college name field
    public static boolean isValidCollegeName(String collegeName) {
        // Add your validation logic here
        return collegeName != null && !collegeName.trim().isEmpty();
    }

    // Validate college address field
    public static boolean isValidCollegeAddress(String collegeAddress) {
        // Add your validation logic here
        return collegeAddress != null && !collegeAddress.trim().isEmpty();
    }


//    -------------------------------------------------------------------------------------------------------------


    private static final String ADMIN_COLLECTION = "Admin";
    private static final String COLLEGE_CODE = "College code";
    private static final String PHONE_NUMBERS = "Phone numbers";
    private static final String LOGIN_DETAILS = "Login details";
    private FirebaseFirestore db;
    private CollectionReference adminCollection;
    private CollectionReference codeCollection;
    private CollectionReference phoneCollection;
    private static CollectionReference loginCollection;

    public Utils() {
        db = FirebaseFirestore.getInstance();
        adminCollection = db.collection(ADMIN_COLLECTION);
        codeCollection = db.collection(COLLEGE_CODE);
        phoneCollection = db.collection(PHONE_NUMBERS);
        loginCollection = db.collection(LOGIN_DETAILS);
    }

    // Create a new Admin document
    public Task<Void> createAdmin(String adminId, String adminName, String phoneNumber, String password, String collegeCode, String collegeName, String collegeAddress) {
        DocumentReference adminRef = adminCollection.document(adminId);
        ModalClass admin = new ModalClass(adminName, phoneNumber, password, collegeCode, collegeName, collegeAddress);
        return adminRef.set(admin);
    }

    // Create a new Phone numbers document
    public Task<Void> addPhone(String adminId, String phoneNumber) {
        boolean exists = true;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference adminCollection = db.collection(PHONE_NUMBERS)
                .document(phoneNumber);
        Map<String, Object> adminData = new HashMap<>();
//        adminData.put("adminId", adminId);
        adminData.put("Exists", exists);

        return adminCollection.set(adminData);
    }

    // Adding college code to a document
    public Task<Void> addCollegeCode(String adminId) {
        DocumentReference codeRef = codeCollection.document(adminId);
        // Create a map to represent the admin data
        Map<String, Object> collCode = new HashMap<>();
        collCode.put("Code", adminId);

        return codeRef.set(collCode);
    }


    // Adding college code to a document
    public static Task<Void> storeLoginDetails(String phone, String pass, String collegeCode, String id, String role) {
        DocumentReference codeRef = loginCollection.document(phone);
        // Create a map to represent the admin data
        Map<String, Object> collCode = new HashMap<>();
        collCode.put("Phone number", phone);
        collCode.put("Password", pass);
        collCode.put("collegeCode", collegeCode);
        collCode.put("id", id);
        collCode.put("role", role);
        return codeRef.set(collCode);
    }

    public void getLoginDetails(CheckLogin checkLogin, Context context, String phoneNumber, String password) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Login details").document(phoneNumber)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            if (password.equals(documentSnapshot.get("Password"))) {

                                checkLogin.isValidUser(true, Objects.requireNonNull(documentSnapshot.get("role")).toString(),
                                        Objects.requireNonNull(documentSnapshot.get("id")).toString(), Objects.requireNonNull(documentSnapshot.get("collegeCode")).toString());
//                                Toast.makeText(context, "VALID USER!...", Toast.LENGTH_SHORT).show();
                            } else {
                                checkLogin.isValidUser(false, null, null, null);
                                Toast.makeText(context, "Invalid password!...", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            checkLogin.isValidUser(false, null, null, null);
                            Toast.makeText(context, "Phone number or password invalid!..", Toast.LENGTH_SHORT).show();
                        }


                    }
                });

    }

    // To get admin details
    public Task<ModalClass> getAdmin(String adminId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference adminRef = db.collection(ADMIN_COLLECTION).document(adminId);
        return adminRef.get().continueWith(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                return task.getResult().toObject(ModalClass.class);
            } else {
                return null;
            }
        });
    }

    // Method to check if phone number already exists
    public static Task<DocumentSnapshot> checkPhoneNumberExists(String phoneNumber) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection("Phone numbers").document(phoneNumber).get();
    }

    // Update an existing Admin document
    public Task<Void> updateAdmin(String adminId, String adminName, String phoneNumber, String password, String collegeCode, String collegeName, String collegeAddress) {
        DocumentReference adminRef = adminCollection.document(adminId);
        ModalClass admin = new ModalClass(adminName, phoneNumber, password, collegeCode, collegeName, collegeAddress);
        return adminRef.set(admin);
    }

    // Delete an Admin document
    public Task<Void> deleteAdmin(String adminId) {
        DocumentReference adminRef = adminCollection.document(adminId);
        return adminRef.delete();
    }

}
