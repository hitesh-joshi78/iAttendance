package com.example.iattendance.Utils.Attendance.DB;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.iattendance.Utils.Attendance.AttendanceHistoryInterface;
import com.example.iattendance.Utils.Attendance.Modals.StudAttendanceModal;
import com.example.iattendance.Utils.Attendance.Modals.WifiAttendanceModal;
import com.example.iattendance.Utils.Attendance.attendanceInterface;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentAttendanceDb {

    private final FirebaseFirestore db;
    private final Context context;
    ArrayList<StudAttendanceModal> attendanceArrayList;
    List<HashMap<String, String>> attList;


//    private boolean rollNumberFound = false;

    public StudentAttendanceDb(Context context) {
        this.context = context;
        this.db = FirebaseFirestore.getInstance();
        this.attendanceArrayList = new ArrayList<>();
    }


    public void addWifiAttendanceData(attendanceInterface attInterface, String collegeCode, String semester, String courseName,
                                      String division, String batchRange, String attDate, String subCode, String subType, HashMap<String, String> mapData) {

        HashMap<String, String> attData = new HashMap<>();
        attData.put("rollNo", mapData.get("Roll no"));
        attData.put("status", "Present");
        attData.put("studImg", mapData.get("image"));
        attData.put("studName", mapData.get("studentName"));
        attData.put("studAttTime", getCurrentTime());
        attData.put("studAttDate", attDate);

        DocumentReference docRef = db.collection("Wifi Attendance").document(collegeCode)
                .collection(courseName).document(semester)
                .collection("Subject code")
                .document(subCode + "_" + subType).collection(attDate)
                .document("Attendance");
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    boolean flag = false;
                    attList = (ArrayList<HashMap<String, String>>) documentSnapshot.get(division);
                    for (HashMap<String, String> map : attList) {
                        if (map.get("rollNo").equals(mapData.get("Roll no"))) {
                            flag = true;
                            Toast.makeText(context, "Already added attendance!...", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                    if (flag == false) {
                        if (attList == null) {
                            attList = new ArrayList<>();
                        }

                        attList.add(attData);
                        Map<String, Object> updateDate = new HashMap<>();
                        updateDate.put(division, attList);

                        docRef.set(updateDate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            attInterface.isCheckingAttendance(true);
                                        } else {
                                            attInterface.isCheckingAttendance(false);
                                        }
                                    }
                                });
                    } else {

                    }
                } else {
                    attList = new ArrayList<>();
                    attList.add(attData);
                    Map<String, Object> newData = new HashMap<>();
                    newData.put(division, attList);

                    docRef.set(newData)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        attInterface.isCheckingAttendance(true);
                                    } else {
                                        attInterface.isCheckingAttendance(false);
                                    }
                                }
                            });
                }
            }
        });

    }

    private String getCurrentTime() {
        LocalDateTime now = null;
        String time = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            now = LocalDateTime.now();
        }

        // Define the date-time formatter
        DateTimeFormatter formatter = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("hh:mm a");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            time = now.format(formatter);
        }
        return time;
    }


    public void fetchWifiAttendanceData(attendanceInterface attInterface, String
            collegeCode, String semester, String courseName,
                                        String division, String attDate, String subCode, String subType) {
        DocumentReference docRef = db.collection("Wifi Attendance").document(collegeCode)
                .collection(courseName).document(semester)
                .collection("Subject code")
                .document(subCode + "_" + subType).collection(attDate)
                .document("Attendance");
        docRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Map<String, Object> attendanceData = documentSnapshot.getData();
                            try {
                                attInterface.getWifiData(attendanceData);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            try {
                                attInterface.getWifiData(new HashMap<>());
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            Toast.makeText(context, "Empty snapshot", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Failed to get details!...", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void fetchAttendanceData(final String collegeCode, final String semesterYear,
                                    final String division, final int userRollNo, attendanceInterface attendanceData) {

        // Collection path
        String basePath = "Students Attendance/" + collegeCode + "/Semester, Year/" + semesterYear
                + "/Division/" + division + "/Batch range/";

        CollectionReference batchRangeCollection = db.collection(basePath);

        batchRangeCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().isEmpty()) {
                    Toast.makeText(context, "No batch ranges found", Toast.LENGTH_SHORT).show();
                    return;
                }
                boolean rollNumberFound = false;
                for (QueryDocumentSnapshot batchRangeDoc : task.getResult()) {
                    String batchRange = batchRangeDoc.getId();

                    int startRoll = Integer.parseInt(batchRange.split("-")[0]);
                    int endRoll = Integer.parseInt(batchRange.split("-")[1]);

                    // Checking if the user's roll number falls within the batch range
                    if (userRollNo >= startRoll && userRollNo <= endRoll) {
                        Toast.makeText(context, "User roll number " + userRollNo + " found in range " + batchRange, Toast.LENGTH_SHORT).show();
                        rollNumberFound = true;

                        // Getting the collection reference for the user's roll number
                        CollectionReference detailsCollection = batchRangeCollection.document(batchRange)
                                .collection("Students")
                                .document(String.valueOf(userRollNo))
                                .collection("Details");

                        detailsCollection.get().addOnCompleteListener(detailsTask -> {
                            if (detailsTask.isSuccessful()) {

                                for (QueryDocumentSnapshot document : detailsTask.getResult()) {
                                    if (document.exists()) {
                                        // Retrieving the data fields from the document
                                        String attendance = document.getString("Attendance");
                                        String batch = document.getString("batch");
                                        String facultyName = document.getString("Faculty name");
                                        String subjectName = document.getString("Subject name");
                                        String subjectType = document.getString("Subject type");
                                        String rollNo = document.getString("Roll no");
                                        String subCode = document.getString("subjectCode");

                                        StudAttendanceModal attendanceModal = new StudAttendanceModal(attendance, facultyName, rollNo, subjectName, subjectType, batch, subCode);
                                        attendanceArrayList.add(attendanceModal);
                                        // Handle the retrieved data
                                        Toast.makeText(context, "Subject: " + subjectName + ", Attendance: " + attendance, Toast.LENGTH_SHORT).show();
                                    }
                                }
                                attendanceData.getStudentAttendance(attendanceArrayList);

                            } else {
                                Toast.makeText(context, "Error getting details documents: " + detailsTask.getException(), Toast.LENGTH_SHORT).show();
                                attendanceData.getStudentAttendance(new ArrayList<>());
                            }
                        });

                        // Exiting loop since the roll number was found in a batch range
                    }
                }

                if (!rollNumberFound) {
                    Toast.makeText(context, "User's roll number is not within any batch range.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Error getting batch ranges: " + task.getException(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void updateAttendanceData(attendanceInterface updateStatus, String collegeCode, String semesterYear, String division, String userRollNo, String subjectCode) {
        ArrayList<String> batchRange = new ArrayList<>();
        CollectionReference collectionRef = FirebaseFirestore.getInstance().collection("Students Attendance")
                .document(collegeCode).collection("Semester, Year")
                .document(semesterYear).collection("Division").document(division)
                .collection("Batch range");

        collectionRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (DocumentSnapshot docSnapshot : queryDocumentSnapshots.getDocuments()) {
                                batchRange.add(docSnapshot.getId());
                            }

                            for (String batch : batchRange) {
                                String idx[] = batch.split("-");
                                if (Integer.parseInt(idx[0]) < Integer.parseInt(userRollNo) &&
                                        Integer.parseInt(idx[1]) > Integer.parseInt(userRollNo)) {

                                    CollectionReference collectionReference2 = collectionRef.document(batch).collection("Students")
                                            .document(userRollNo).collection("Details");
                                    collectionReference2
                                            .get()
                                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                @Override
                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                    if (!queryDocumentSnapshots.isEmpty()) {
                                                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                                                            if (documentSnapshot.getString("subjectCode").toString().equals(subjectCode)) {
                                                                String Attendance[] = documentSnapshot.getString("Attendance").split(" ");
                                                                int newAttendance = Integer.parseInt(Attendance[0]) + 1;
                                                                String _Attendance = String.valueOf(newAttendance) + " out of " + Attendance[Attendance.length - 1];

                                                                String _FacultyName = documentSnapshot.getString("Faculty name");
                                                                String _RollNo = documentSnapshot.getString("Roll no");
                                                                String _SubjectName = documentSnapshot.getString("Subject name");
                                                                String _SubjectCode = documentSnapshot.getString("subjectCode");
                                                                String _SubjectType = documentSnapshot.getString("Subject type");
                                                                String _batch = documentSnapshot.getString("batch");

                                                                HashMap<String, String> updatedData = new HashMap<>();
                                                                updatedData.put("Attendance", _Attendance);
                                                                updatedData.put("Faculty name", _FacultyName);
                                                                updatedData.put("Roll no", _RollNo);
                                                                updatedData.put("Subject name", _SubjectName);
                                                                updatedData.put("subjectCode", _SubjectCode);
                                                                updatedData.put("Subject type", _SubjectType);
                                                                updatedData.put("batch", _batch);

                                                                collectionReference2.document(documentSnapshot.getId())
                                                                        .set(updatedData)
                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                if (task.isSuccessful()) {
                                                                                    updateStatus.isCheckingAttendance(true);
                                                                                    Toast.makeText(context, "Successfully updated Attendance!...", Toast.LENGTH_SHORT).show();
                                                                                } else {
                                                                                    updateStatus.isCheckingAttendance(false);
                                                                                }
                                                                            }
                                                                        })
                                                                        .addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception e) {
                                                                                updateStatus.isCheckingAttendance(false);
                                                                                Toast.makeText(context, "Failed to update attendance!...", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        });

                                                            }
                                                        }
                                                    }
                                                }
                                            });
                                }
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Failed to get batchRange!...", Toast.LENGTH_SHORT).show();
                    }
                });
    }


//    public void fetchAttendanceDates(AttendanceHistoryInterface attInterface, String
//            collegeCode, String semester, String courseName,
//                                     String division, String subCode, String subType) {
//        String documentPath = "Wifi Attendance/" + collegeCode + "/" + courseName + "/" + semester + "/Subject code/" + subCode + "_" + subType;
//
//        CollectionReference collectionRef = db.collection(documentPath);
//
//        // Get collections under the specified document path
//        collectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    List<DocumentSnapshot> documents = task.getResult().getDocuments();
//                    for (DocumentSnapshot documentSnapshot : documents) {
//                        Log.d("FirestoreCollection", "Document ID: " + documentSnapshot.getId());
//                    }
//                } else {
//                    Log.e("FirestoreError", "Error getting collections: ", task.getException());
//                }
//            }
//        });
//    }

}