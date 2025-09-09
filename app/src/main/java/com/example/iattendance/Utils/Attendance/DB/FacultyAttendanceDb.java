package com.example.iattendance.Utils.Attendance.DB;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.iattendance.Utils.Attendance.attendanceInterface;
import com.example.iattendance.Utils.Attendance.attendanceSSID;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class FacultyAttendanceDb {

    Context context;
    FirebaseFirestore db;
    HashMap<String, String> map;

    public FacultyAttendanceDb(Context context, HashMap<String, String> attData) {
        this.context = context;
        this.db = FirebaseFirestore.getInstance();
        this.map = attData;
    }

    public void startAttendance(String collegeCode, String SubjectName, String course, String subCode) {
        db.collection("Attendance Status")
                .document(collegeCode)
                .collection("Course")
                .document(course)
                .collection("Subjects")
                .document(subCode)
                .set(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            if (Boolean.valueOf(map.get("status"))) {
                                Toast.makeText(context, "Attendance Session started!...", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Attendance Session closed!...", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(context, "Failed to Start Session!...", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Failed to Start Sessiopn!...", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void getAttendanceStatus(String collegeCode, String SubjectCode, String course,
                                    attendanceSSID setSSID) {
        db.collection("Attendance Status")
                .document(collegeCode)
                .collection("Course")
                .document(course)
                .collection("Subjects")
                .document(SubjectCode)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Boolean status = Boolean.valueOf(documentSnapshot.getString("status"));
                            setSSID.getSSID(status, documentSnapshot.getString("Wifi SSID"));
                        } else {
                            Toast.makeText(context, "document doesn't exist  ", Toast.LENGTH_SHORT).show();
                            setSSID.getSSID(false, "");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Getting error in fetching", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
