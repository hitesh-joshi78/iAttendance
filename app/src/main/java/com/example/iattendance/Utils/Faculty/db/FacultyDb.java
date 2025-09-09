package com.example.iattendance.Utils.Faculty.db;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.iattendance.Login.Login_screen;
import com.example.iattendance.Sign_up_Screens.Admin_signup.ModalClass;
import com.example.iattendance.Utils.Admin.Utils;
import com.example.iattendance.Utils.Faculty.FacultySessionManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class FacultyDb {
    Context context;
    HashMap<String, String> data = new HashMap<>();
    FirebaseFirestore db;
    FacultySessionManager facultySession;
    private Utils utils;


    public FacultyDb(Context context, HashMap<String, String> data) {
        this.context = context;
        this.data = data;
        db = FirebaseFirestore.getInstance();
        facultySession = new FacultySessionManager(context);
        utils = new Utils();
    }

    public void insertDb(InsertDbCallback callback) {

        String FcId = getFacultyId();
        db.collection("Faculty").document(data.get("collegeCode")).collection(FcId).add(data)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Create sessions for the faculty
                        facultySession.createSession(FcId, data.get("password"), data.get("contact"), data.get("collegeCode"),
                                data.get("facultyName"), "");
                        facultySession.createLoginSession(FcId, data.get("facultyName"));

                        // Add phone numbers and college code
                        Task<Void> addPhoneTask = utils.addPhone(FcId, data.get("contact"));
                        Task<Void> addLoginDetails = Utils.storeLoginDetails
                                (data.get("contact"), data.get("password"), data.get("collegeCode"), FcId, "faculty");
                        Tasks.whenAllComplete(addPhoneTask, addLoginDetails)
                                .addOnSuccessListener(new OnSuccessListener<List<Task<?>>>() {
                                    @Override
                                    public void onSuccess(List<Task<?>> tasks) {
                                        if (task.isSuccessful()) {
                                            callback.onInsertComplete(true);
                                        } else {
                                            callback.onInsertComplete(false);
                                        }
                                    }
                                });

                    } else {
                        callback.onInsertComplete(false);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onInsertComplete(false);
                    }
                });
    }

    public void getFaculty(InsertDbCallback callback, String facultyId, String collegeCode, String contact) {
        HashMap<String, String> response = new HashMap<>();

        db.collection("Faculty").document(collegeCode)
                .collection(facultyId).whereEqualTo("contact", contact)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (!value.isEmpty()) {
                            for (DocumentSnapshot snapshot : value.getDocuments()) {
                                response.put("collegeCode", snapshot.getString("collegeCode"));
                                response.put("facultyName", snapshot.getString("facultyName"));
                                response.put("password", snapshot.getString("password"));
                                response.put("contact", snapshot.getString("contact"));
                                response.put("course", snapshot.getString("course"));
                            }
                            callback.onDataRetrieval(response);
                        } else {
                            callback.onDataRetrieval(response);
                            Toast.makeText(context, "VALUE IS  EMPTY", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



    private String getFacultyId() {
        String id = "FC";
        Random random = new Random();
        int code = random.nextInt(9000) + 1000;
        id = id + code;
        return id;
    }


}
