package com.example.iattendance.Utils.Subjects.db;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.iattendance.Dashboard_Fragments.Student.Student_SubjectModal;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class CourseDb {
    Context context;
    HashMap<String, String> data;
    FirebaseFirestore db;
    String year;


    public CourseDb(Context context, HashMap<String, String> data) {
        this.context = context;
        this.data = data;
        db = FirebaseFirestore.getInstance();
        this.year = getCurrentYr();
    }

    private String getCurrentYr() {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        return String.valueOf(currentYear);
    }

    public void getSemesters(subjectInterface subInterface) {
        ArrayList<String> semestersList = new ArrayList<>();
        db.collection("Semesters")
                .document(Objects.requireNonNull(data.get("collegeCode")))
                .collection(Objects.requireNonNull(data.get("facultyId")))
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            for (QueryDocumentSnapshot document : querySnapshot) {

                                // Checking if the fields exist`
                                if (document.contains("Semester") && document.contains("Year")) {
                                    String semester = "Semester " + document.getString("Semester") + ", " + document.getString("Year");
                                    semestersList.add(semester);
                                    subInterface.getSemesterList(semestersList);
                                } else {
                                    Toast.makeText(context, "Please add a subject!" + document.getId(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(context, "Failed to get semesters!...", Toast.LENGTH_SHORT).show());
    }

    public void getStudentSubjects(subjectInterface subjectInterface) {

        if (data.size() > 0) {

            db.collection("Student Card")
                    .document(Objects.requireNonNull(data.get("collegeCode")))
                    .collection("Semester, Year")
                    .document("Semester " + data.get("semester") + ", " + data.get("year"))
                    .collection("Subject Type")
                    .document(Objects.requireNonNull(data.get("subjectType")))
                    .collection("Details")
                    .whereEqualTo("Division", data.get("division"))
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            ArrayList<Student_SubjectModal> studCardList = new ArrayList<>();
                            if (!queryDocumentSnapshots.isEmpty()) {
                                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {

                                    String _collegeCode = documentSnapshot.getString("collegeCode");
                                    String _courseName = documentSnapshot.getString("courseName");
                                    String _year = documentSnapshot.getString("year");
                                    String _semester = documentSnapshot.getString("semester");
                                    String _facultyName = documentSnapshot.getString("facultyName");
                                    String _subjectType = documentSnapshot.getString("subjectType");
                                    String _Division = documentSnapshot.getString("Division");
                                    String _subjectName = documentSnapshot.getString("subjectName");
                                    String _batch = documentSnapshot.getString("batch");
                                    String _batchRange = documentSnapshot.getString("batchRange");
                                    String range[] = _batchRange.split("-");
                                    if (!_subjectType.equals("Theory")) {
                                        if (Integer.parseInt(data.get("studRollNo")) >= Integer.parseInt(range[0])
                                                && Integer.parseInt(data.get("studRollNo")) <= Integer.parseInt(range[1])) {
                                            Student_SubjectModal modal = new
                                                    Student_SubjectModal(_collegeCode, _semester, _year, _subjectType, _Division, _courseName, _facultyName, _subjectName, _batch, _batchRange);
                                            studCardList.add(modal);
                                        }
                                    } else {
                                        Student_SubjectModal modal = new
                                                Student_SubjectModal(_collegeCode, _semester, _year, _subjectType, _Division, _courseName, _facultyName, _subjectName, _batch, _batchRange);
                                        studCardList.add(modal);
                                    }

                                }
                                subjectInterface.getStudentSemesterList(studCardList);

                            } else {
                                Toast.makeText(context, "Empty QuerySnapshot", Toast.LENGTH_SHORT).show();
                                subjectInterface.getStudentSemesterList(studCardList);
                            }

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Getting error in fetching", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(context, "Data size is empty!...", Toast.LENGTH_SHORT).show();
        }
    }

    public void addStudentSubCard(subjectInterface insertConfirm) {
        if (data.size() > 0) {
            db.collection("Student Card")
                    .document(Objects.requireNonNull(data.get("collegeCode")))
                    .collection("Semester, Year")
                    .document("Semester " + data.get("semester") + ", " + year)
                    .collection("Subject Type")
                    .document(Objects.requireNonNull(data.get("subjectType")))
                    .collection("Details")
                    .add(data)
                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()) {
                                insertConfirm.isConfirmed(true);
                            } else {
                                insertConfirm.isConfirmed(false);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            insertConfirm.isConfirmed(false);
                        }
                    });
        } else {
            Toast.makeText(context, "Empty data size", Toast.LENGTH_SHORT).show();
            insertConfirm.isConfirmed(false);
        }

    }


    public void alreadyExistSubject(String collegeCode, String courseId, String year, String semester, String facultyCode,
                                    String batch, String subjectName, subjectInterface exist) {
        db.collection("Faculty Subjects")
                .document(collegeCode)
                .collection("Course")
                .document(courseId)
                .collection("Year")
                .document(year)
                .collection("Semester")
                .document("Semester " + semester)
                .collection("Faculty code")
                .document(facultyCode)
                .collection("Details")
                .whereEqualTo("subject", subjectName)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String _batch = "";
                        boolean status = false;
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                                _batch = document.getString("batch");
                                if (_batch.equals(batch)) {
                                    status = true;
                                    exist.isConfirmed(status);
                                }
                            }
                            exist.isConfirmed(status);
                        } else {
                            exist.isConfirmed(status);
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }

}