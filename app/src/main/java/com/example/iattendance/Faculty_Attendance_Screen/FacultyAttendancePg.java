package com.example.iattendance.Faculty_Attendance_Screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.iattendance.Dashboard_Fragments.Faculty.FacultySubjectAdapter;
import com.example.iattendance.Dashboard_Fragments.Student.RecyclerView.Adapter.StudentAttendanceAdapter;
import com.example.iattendance.R;
import com.example.iattendance.Utils.Attendance.DB.FacultyAttendanceDb;
import com.example.iattendance.Utils.Attendance.DB.StudentAttendanceDb;
import com.example.iattendance.Utils.Attendance.Modals.StudAttendanceModal;
import com.example.iattendance.Utils.Attendance.attendanceInterface;
import com.example.iattendance.Utils.Faculty.FacultySessionManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FacultyAttendancePg extends AppCompatActivity {
    Toolbar toolbar;
    RelativeLayout alert_layout;
    Spinner session_mode_spinner;
    RecyclerView stud_list_rv;
    TextView alert_tv, first_letter_tv, subj_name_tv, prof_name_tv, div_tv, date_tv, present_count_tv, total_count_tv, percent_tv;
    MaterialButton startSessionBtn, endSessionBtn;
    String alert_txt, subj_name_txt, prof_name_txt, div_txt, date_txt, present_count_txt, total_count_txt, percent_txt;
    String facultyName, courseName, collCode;
    CustomSpinnerAdapter adapter;
    HashMap<String, String> mapData;
    String selectedMode;
    ArrayList<String> listData;
    FacultyAttendanceDb attendanceDb;
    FacultySessionManager facultySession;
    String[] spinner_items;
    StudentAttendanceAdapter studentAttendanceAdapter;
    StudentAttendanceDb StudentattendanceDb;
    ImageView empty_icon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faculty_attendance_pg);

        Intent intent = getIntent();
        listData = intent.getStringArrayListExtra("Attendance Data");
        initializeViews();

        session_mode_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                if (position != 0) {  // Ignore the hint item
                    selectedMode = selectedItem;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        startSessionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String SSID = "";
                ActivityCompat.requestPermissions(FacultyAttendancePg.this,
                        new String[]{Manifest.permission.ACCESS_WIFI_STATE,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION},
                        PackageManager.PERMISSION_GRANTED);

                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
                WifiInfo wifiInfo;
                if (selectedMode.equals("Manual")) {
                    wifiInfo = wifiManager.getConnectionInfo();
                    if (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {
                        SSID = wifiInfo.getSSID();
                    }
                    startSessionBtn.setAlpha(0.5f);
                    HashMap<String, String> statusMap = new HashMap<String, String>();

                    statusMap.put("status", "true");
                    statusMap.put("Wifi SSID", SSID);

                    attendanceDb = new FacultyAttendanceDb(getApplicationContext(), statusMap);
                    addAttendance(collCode, "Semester " + listData.get(4) + ", " + Calendar.getInstance().get(Calendar.YEAR), listData.get(1), listData.get(3));
                    attendanceDb.startAttendance(collCode, listData.get(0), courseName, listData.get(3));
                } else if (selectedMode.equals("Automatic")) {
                    wifiInfo = wifiManager.getConnectionInfo();
                    if (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {
                        SSID = wifiInfo.getSSID();
                    }
                    startSessionBtn.setAlpha(0.5f);
                    HashMap<String, String> statusMap = new HashMap<String, String>();
                    statusMap.put("status", "true");
                    statusMap.put("Wifi SSID", SSID);
                    attendanceDb = new FacultyAttendanceDb(getApplicationContext(), statusMap);
                    addAttendance(collCode, "Semester " + listData.get(4) + ", " + Calendar.getInstance().get(Calendar.YEAR), listData.get(1), listData.get(3));
                    attendanceDb.startAttendance(collCode, listData.get(0), courseName, listData.get(3));
                }
            }
        });

        endSessionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSessionBtn.setAlpha(1);
                HashMap<String, String> statusMap = new HashMap<String, String>();
                statusMap.put("status", "false");
                statusMap.put("Wifi SSID", "");
                attendanceDb = new FacultyAttendanceDb(getApplicationContext(), statusMap);
                attendanceDb.startAttendance(collCode, listData.get(0), courseName, listData.get(3));
            }
        });


    }

    private void initializeViews() {

//        Hooks
        toolbar = findViewById(R.id.toolbar);
        alert_layout = findViewById(R.id.alert_layout);
        session_mode_spinner = findViewById(R.id.session_mode_spinner);
        stud_list_rv = findViewById(R.id.stud_list_rv);

        alert_tv = findViewById(R.id.alert_tv);
        first_letter_tv = findViewById(R.id.first_letter_tv);
        subj_name_tv = findViewById(R.id.subj_name_tv);
        prof_name_tv = findViewById(R.id.prof_name_tv);
        div_tv = findViewById(R.id.div_tv);
        date_tv = findViewById(R.id.date_tv);
        present_count_tv = findViewById(R.id.present_count_tv);
        total_count_tv = findViewById(R.id.total_count_tv);
        percent_tv = findViewById(R.id.percent_tv);
        startSessionBtn = findViewById(R.id.startSessionBtn);
        endSessionBtn = findViewById(R.id.endSessionBtn);
        empty_icon = findViewById(R.id.empty_icon);
        StudentattendanceDb = new StudentAttendanceDb(getApplicationContext());

//        Setting Toolbar view
        {
            // Base text
            SpannableString attendanceText = new SpannableString("Attendance");

            // Extra text
            SpannableString extraText = new SpannableString("  time");
            extraText.setSpan(new ForegroundColorSpan(Color.GRAY), 0, extraText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            extraText.setSpan(new RelativeSizeSpan(0.9f), 0, extraText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            // Combine texts
            SpannableStringBuilder title = new SpannableStringBuilder();
            title.append(attendanceText);
            title.append(extraText);

            // Set the toolbar title
            toolbar.setTitle(title);
            setSupportActionBar(toolbar);

            // Additional toolbar setup if needed
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(v -> onBackPressed());
        }

        subj_name_tv.setText(listData.get(0));
        first_letter_tv.setText(listData.get(0).substring(0, 1));
        div_tv.setText("Div - " + listData.get(1));
        prof_name_tv.setText(listData.get(2));

        date_tv.setText(getCurrentDate());

        spinner_items = new String[]{
                "Select session mode",
                "Automatic (3 min)",
                "Manual"
        };

        facultySession = new FacultySessionManager(getApplicationContext());
        mapData = facultySession.getUserDetails();
        collCode = mapData.get(FacultySessionManager.KEY_FC_COLLEGE);
        courseName = mapData.get(FacultySessionManager.KEY_FC_COURSE);
        facultyName = mapData.get(FacultySessionManager.KEY_FC_NAME);

        adapter = new CustomSpinnerAdapter(this, spinner_items);
        session_mode_spinner.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        stud_list_rv.setLayoutManager(layoutManager);


        StudentattendanceDb.fetchWifiAttendanceData(new attendanceInterface() {
            @Override
            public void getStudentAttendance(ArrayList<StudAttendanceModal> list) {

            }

            @Override
            public void isCheckingAttendance(boolean status) {

            }

            @Override
            public void getWifiData(Map<String, Object> attendanceData) {
                if (attendanceData.size() > 0) {
                    empty_icon.setVisibility(View.GONE);
                    studentAttendanceAdapter = new StudentAttendanceAdapter(attendanceData, getApplicationContext(), listData.get(1));
                    stud_list_rv.setAdapter(studentAttendanceAdapter);
                    studentAttendanceAdapter.notifyDataSetChanged();
                } else {
                    empty_icon.setVisibility(View.VISIBLE);
                    Toast.makeText(FacultyAttendancePg.this, "Empty attendanceData", Toast.LENGTH_SHORT).show();
                }
            }
        }, collCode, "Semester " + listData.get(4) + ", " + Calendar.getInstance().get(Calendar.YEAR), courseName, listData.get(1), getCurrentDate(), listData.get(3), listData.get(5));

    }

    private String getCurrentDate() {
        LocalDate currentDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            currentDate = LocalDate.now();
        }

        // Define the date format
        DateTimeFormatter formatter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("MMM dd, E");
        }

        // Format the current date
        String formattedDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formattedDate = currentDate.format(formatter);
        }

        // Print the formatted date
        return formattedDate;
    }


    private void addAttendance(String collegeCode, String semester, String division, String subCode) {
        ArrayList<String> batchRange = new ArrayList<>();
        CollectionReference collectionRef = FirebaseFirestore.getInstance().collection("Students Attendance")
                .document(collegeCode).collection("Semester, Year")
                .document(semester).collection("Division").document(division)
                .collection("Batch range");

        collectionRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        int i;
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (DocumentSnapshot docSnapshot : queryDocumentSnapshots.getDocuments()) {
                                batchRange.add(docSnapshot.getId());
                            }
                            for (String batch : batchRange) {
                                String idx[] = batch.split("-");
                                for (i = Integer.parseInt(idx[0]); i <= Integer.parseInt(idx[1]); i++) {
                                    int finalI = i;
                                    FirebaseFirestore.getInstance().collection("Students Attendance")
                                            .document(collegeCode).collection("Semester, Year")
                                            .document(semester).collection("Division").document(division)
                                            .collection("Batch range")
                                            .document(batch)
                                            .collection("Students")
                                            .document(String.valueOf(i))
                                            .collection("Details")
                                            .get()
                                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                @Override
                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                    if (!queryDocumentSnapshots.isEmpty()) {
                                                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                                                            if (documentSnapshot.getString("subjectCode").equals(subCode)) {
                                                                System.out.println(documentSnapshot.getData());

                                                                HashMap<String, String> attendanceData = new HashMap<>();
                                                                String attVal[] = documentSnapshot.getString("Attendance").split(" ");
                                                                String att = String.valueOf(Integer.valueOf(attVal[attVal.length - 1]) + 1);
                                                                String finalAttendance = attVal[0] + " out of " + att;
                                                                attendanceData.put("Attendance", finalAttendance);
                                                                attendanceData.put("Faculty name", documentSnapshot.getString("Faculty name"));
                                                                attendanceData.put("Roll no.", String.valueOf(finalI));
                                                                attendanceData.put("Subject name", documentSnapshot.getString("Subject name"));
                                                                attendanceData.put("Subject type", documentSnapshot.getString("Subject type"));
                                                                attendanceData.put("batch", documentSnapshot.getString("batch"));
                                                                attendanceData.put("subjectCode", documentSnapshot.getString("subjectCode"));


                                                                FirebaseFirestore.getInstance().collection("Students Attendance")
                                                                        .document(collegeCode).collection("Semester, Year")
                                                                        .document(semester).collection("Division").document(division)
                                                                        .collection("Batch range")
                                                                        .document(batch)
                                                                        .collection("Students")
                                                                        .document(String.valueOf(finalI))
                                                                        .collection("Details")
                                                                        .document(documentSnapshot.getId())
                                                                        .set(attendanceData)
                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                if (task.isSuccessful()) {
                                                                                    Toast.makeText(FacultyAttendancePg.this, "Successfully added new attendance!...", Toast.LENGTH_SHORT).show();
                                                                                } else {
                                                                                    Toast.makeText(FacultyAttendancePg.this, "Not able tp  added new attendance!...", Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            }
                                                                        });
                                                            }
                                                        }
                                                    } else {
                                                        Toast.makeText(FacultyAttendancePg.this, "Empty SubjectSnapshots", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }
                            }
                        } else {
                            Toast.makeText(FacultyAttendancePg.this, "Empty querySnapshot", Toast.LENGTH_SHORT).show();
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