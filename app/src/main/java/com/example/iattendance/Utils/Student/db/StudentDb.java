package com.example.iattendance.Utils.Student.db;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.iattendance.Utils.Admin.Utils;
import com.example.iattendance.Utils.Faculty.FacultySessionManager;
import com.example.iattendance.Utils.Faculty.db.InsertDbCallback;
import com.example.iattendance.Utils.Student.StudentSessionManager;
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

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class StudentDb {

    Context context;
    HashMap<String, String> data;
    FirebaseFirestore db;
    StudentSessionManager studentSession;
    HashMap<String, String> subjectMap;
    private Utils utils;

    public StudentDb(Context context, HashMap<String, String> mapData) {
        this.context = context;
        this.data = mapData;
        db = FirebaseFirestore.getInstance();
        studentSession = new StudentSessionManager(context);
        utils = new Utils();
    }


    public void insertDb(InsertDbCallback callback) {
        String StudId = getStudentId();
//        String SubjectId = getSubjectId();
        db.collection("Student").document(data.get("collegeCode")).collection(StudId).add(data)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            studentSession.createSession(StudId, data.get("studPassword"), data.get("studContact"), data.get("collegeCode"), data
                                    .get("studName"), data.get("studCourse"), data.get("studDiv"), data.get("studRollNo"));
                            studentSession.createLoginSession(StudId, data.get("studName"));
                            Task<Void> addPhoneTask = utils.addPhone(StudId, data.get("studContact"));
                            Task<Void> addLoginDetails = Utils.storeLoginDetails
                                    (data.get("studContact"), data.get("studPassword"), data.get("collegeCode"), StudId, "student");
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
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onInsertComplete(false);
                        Toast.makeText(context, "Not able to create try again!...", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public void getStudent(InsertDbCallback callback, String studentId, String collegeCode, String contact) {
        HashMap<String, String> response = new HashMap<>();
        Toast.makeText(context, "Student id :" + studentId, Toast.LENGTH_SHORT).show();
        db.collection("Student").document(collegeCode)
                .collection(studentId).whereEqualTo("studContact", contact)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (!value.isEmpty()) {
                            for (DocumentSnapshot snapshot : value.getDocuments()) {
                                response.put("collegeCode", snapshot.getString("collegeCode"));
                                response.put("studName", snapshot.getString("studName"));
                                response.put("studPassword", snapshot.getString("studPassword"));
                                response.put("studContact", snapshot.getString("studContact"));
                                response.put("studCourse", snapshot.getString("studCourse"));
                                response.put("studDiv", snapshot.getString("studDiv"));
                                response.put("studRollNo", snapshot.getString("studRoll"));
                            }
                            callback.onDataRetrieval(response);
                        } else {
                            callback.onDataRetrieval(response);
                            Toast.makeText(context, "VALUE IS  EMPTY", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private String getSubjectId() {
        String id = "SUB";
        Random random = new Random();
        int code = random.nextInt(9000) + 1000;
        id = id + code;
        return id;
    }


    private String getStudentId() {
        String id = "STU";
        Random random = new Random();
        int code = random.nextInt(9000) + 1000;
        id = id + code;
        return id;
    }
}
