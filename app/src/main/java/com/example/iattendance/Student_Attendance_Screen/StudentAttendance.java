package com.example.iattendance.Student_Attendance_Screen;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.iattendance.Dashboard.StudentSubjectAdapter;
import com.example.iattendance.Faculty_Attendance_Screen.FacultyAttendancePg;
import com.example.iattendance.R;
import com.example.iattendance.Student_Attendance_Screen.Adapters.AttendanceHistoryAdapter;
import com.example.iattendance.Utils.Attendance.AttendanceHistoryInterface;
import com.example.iattendance.Utils.Attendance.DB.FacultyAttendanceDb;
import com.example.iattendance.Utils.Attendance.DB.StudentAttendanceDb;
import com.example.iattendance.Utils.Attendance.ImgStoreInterface;
import com.example.iattendance.Utils.Attendance.Modals.AttendanceHistoryModal;
import com.example.iattendance.Utils.Attendance.Modals.StudAttendanceModal;
import com.example.iattendance.Utils.Attendance.Modals.WifiAttendanceModal;
import com.example.iattendance.Utils.Attendance.attendanceInterface;
import com.example.iattendance.Utils.Attendance.attendanceSSID;
import com.example.iattendance.Utils.Student.StudentSessionManager;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class StudentAttendance extends AppCompatActivity {
    StudentSessionManager studentSessionManager;
    HashMap<String, String> studentMembers;
    Intent prevIntent;
    RelativeLayout alert_layout;
    TextView alert_tv, first_letter_tv, subj_name_tv, prof_name_tv, div_tv, date_tv, present_count_tv, total_count_tv, percent_tv;
    String alert_txt, subj_name_txt, prof_name_txt, div_txt, date_txt, present_count_txt, total_count_txt, percent_txt;
    MaterialToolbar toolbar;
    MaterialButton mark_att_btn;
    FacultyAttendanceDb checkingSession;
    Bitmap photoImage = null;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    StudentAttendanceDb studAttDb;
    RecyclerView attendanceHistory_rv;
    AttendanceHistoryAdapter attendanceHistoryAdapter;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_attendance);

        initializeViews();

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


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inside your Activity
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.popBackStack();
                // Inside your Activity
                finish();
            }
        });

        mark_att_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent open_camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(open_camera, 100);

            }
        });
    }

    private void storeImage(ImgStoreInterface imageStore) {
        String path = "/" + studentMembers.get(StudentSessionManager.KEY_ST_COLLEGE) + "/" + studentMembers.get(StudentSessionManager.KEY_ST_COURSE)
                + "/" + prevIntent.getStringExtra("semYear") +
                "/" + prevIntent.getStringExtra("subCode") + "_" + prevIntent.getStringExtra("subType") + "/" + studentMembers.get(StudentSessionManager.KEY_ST_DIV)
                + "/" + prevIntent.getStringExtra("batch") + "/" + getCurrentDateFormatted() + "/" + studentMembers.get(StudentSessionManager.KEY_ST_ROLL);

        byte[] imageData = bitmapToByteArray(photoImage);
        // Upload the Byte Array
        UploadTask uploadTask = storageReference.child(path).putBytes(imageData);
        uploadTask.addOnFailureListener(exception -> {
            // Handle unsuccessful uploads
            exception.printStackTrace();
            Toast.makeText(this, "Getting error : " + exception.getMessage(), Toast.LENGTH_SHORT).show();

        }).addOnSuccessListener(taskSnapshot -> {
            // Handle successful uploads
            // You can get the download URL here if needed
            storageReference.child(path).getDownloadUrl().addOnSuccessListener((Uri uri) -> {
                // Do something with the download URL

                imageStore.getImageUri(uri.toString());
            });
        });
    }


    @SuppressLint("SetTextI18n")
    private void initializeViews() {
//        StudentSessionManager
        studentSessionManager = new StudentSessionManager(getApplicationContext());
        studentMembers = studentSessionManager.getUserDetails();

//        Previous intent data
        prevIntent = getIntent();
        prof_name_txt = prevIntent.getStringExtra("faculty_name");
        subj_name_txt = prevIntent.getStringExtra("sub_name");
        present_count_txt = prevIntent.getStringExtra("current_att");
        total_count_txt = prevIntent.getStringExtra("total_att");

//        Hooks
        toolbar = findViewById(R.id.toolbar);
        alert_layout = findViewById(R.id.alert_layout);
        alert_tv = findViewById(R.id.alert_tv);
        first_letter_tv = findViewById(R.id.first_letter_tv);
        subj_name_tv = findViewById(R.id.subj_name_tv);
        prof_name_tv = findViewById(R.id.prof_name_tv);
        div_tv = findViewById(R.id.div_tv);
        date_tv = findViewById(R.id.date_tv);
        present_count_tv = findViewById(R.id.present_count_tv);
        total_count_tv = findViewById(R.id.total_count_tv);
        percent_tv = findViewById(R.id.percent_tv);
        mark_att_btn = findViewById(R.id.mark_att_btn);
        attendanceHistory_rv = findViewById(R.id.attendanceHistory_rv);

        if (Integer.parseInt(present_count_txt) != 0) {
            percent_txt = String.valueOf((Integer.parseInt(present_count_txt) * 100) / Integer.parseInt(total_count_txt));
        } else {
            percent_txt = "0";
        }
        div_txt = studentMembers.get(StudentSessionManager.KEY_ST_DIV);
        date_txt = getCurrentDateFormatted();

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        studAttDb = new StudentAttendanceDb(getApplicationContext());

        checkingSession = new FacultyAttendanceDb(getApplicationContext(), new HashMap<>());
        checkingSession.getAttendanceStatus(studentMembers.get(StudentSessionManager.KEY_ST_COLLEGE), prevIntent.getStringExtra("subCode"),
                studentMembers.get(StudentSessionManager.KEY_ST_COURSE), new attendanceSSID() {
                    @Override
                    public void getSSID(Boolean status, String storedSSID) {
                        if (status) {
                            String SSID = "";
                            ActivityCompat.requestPermissions(StudentAttendance.this,
                                    new String[]{Manifest.permission.ACCESS_WIFI_STATE,
                                            Manifest.permission.ACCESS_COARSE_LOCATION,
                                            Manifest.permission.ACCESS_FINE_LOCATION},
                                    PackageManager.PERMISSION_GRANTED);
                            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
                            WifiInfo wifiInfo;
                            wifiInfo = wifiManager.getConnectionInfo();
                            if (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {
                                SSID = wifiInfo.getSSID();
                            }

                            if (SSID.equals(storedSSID)) {
                                mark_att_btn.setAlpha(1);
                                mark_att_btn.setClickable(true);
                            } else {
                                Toast.makeText(StudentAttendance.this, "Please connect to same Wifi!..", Toast.LENGTH_SHORT).show();
                                mark_att_btn.setAlpha(0.5f);
                                mark_att_btn.setClickable(false);
                            }

                        } else {
                            Toast.makeText(StudentAttendance.this, "Sorry not started yet!...", Toast.LENGTH_SHORT).show();
                            mark_att_btn.setAlpha(0.5f);
                            mark_att_btn.setClickable(false);
                        }
                    }
                });

//        Setting views with values
        first_letter_tv.setText(subj_name_txt.substring(0, 1));
        prof_name_tv.setText(prof_name_txt);
        subj_name_tv.setText(subj_name_txt);
        present_count_tv.setText(present_count_txt);
        total_count_tv.setText("/ " + total_count_txt);
        percent_tv.setText(percent_txt + "%");
        div_tv.append(" " + div_txt);
        date_tv.setText(date_txt);

        HashMap<Integer, ArrayList<String>> attendanceHistory = new HashMap<>();
        attendanceHistory.put(0, new ArrayList<>(Arrays.asList("Present", "20 Jun, Mon", "02:14 Pm")));
        attendanceHistory.put(1, new ArrayList<>(Arrays.asList("Present", "21 Jun, Mon", "03:14 Pm")));
        attendanceHistory.put(2, new ArrayList<>(Arrays.asList("Absent", "19 Jun, Mon", "01:14 Pm")));
        attendanceHistory.put(3, new ArrayList<>(Arrays.asList("Present", "13 Jun, Mon", "12:14 Pm")));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        attendanceHistory_rv.setLayoutManager(layoutManager);
        attendanceHistoryAdapter = new AttendanceHistoryAdapter(getApplicationContext(), attendanceHistory);
        attendanceHistory_rv.setAdapter(attendanceHistoryAdapter);

        attendanceHistoryAdapter.notifyDataSetChanged();


//        studAttDb.fetchAttendanceDates(new AttendanceHistoryInterface() {
//                                           @Override
//                                           public void getAttendanceDHistory(HashMap<String, AttendanceHistoryModal> passedData) {
//
//                                           }
//
//                                           @Override
//                                           public void isCheckingStatus(boolean status) {
//                                               if (status) {
//
//                                               }
//                                           }
//                                       }, studentMembers.get(StudentSessionManager.KEY_ST_COLLEGE),
//                prevIntent.getStringExtra("semYear"), studentMembers.get(StudentSessionManager.KEY_ST_COURSE),
//                studentMembers.get(StudentSessionManager.KEY_ST_DIV), prevIntent.getStringExtra("subCode"),
//                prevIntent.getStringExtra("subType"));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        photoImage = (Bitmap) data.getExtras().get("data");
        if (photoImage != null && photoImage.getWidth() != 0 && photoImage.getHeight() != 0) {
            storeImage(new ImgStoreInterface() {
                @Override
                public void getImageUri(String imgUri) {
                    if (!imgUri.isEmpty()) {
                        HashMap<String, String> attendanceData = new HashMap<>();
                        attendanceData.put("status", "Present");
                        attendanceData.put("Roll no", studentMembers.get(StudentSessionManager.KEY_ST_ROLL));
                        attendanceData.put("subjectCode", prevIntent.getStringExtra("subCode"));
                        attendanceData.put("facultyName", prof_name_txt);
                        attendanceData.put("image", imgUri);
                        attendanceData.put("Date", getCurrentDateFormatted());
                        attendanceData.put("studentName", studentMembers.get(StudentSessionManager.KEY_ST_NAME));
                        studAttDb.addWifiAttendanceData(new attendanceInterface() {
                                                            @Override
                                                            public void getStudentAttendance(ArrayList<StudAttendanceModal> list) {

                                                            }

                                                            @Override
                                                            public void isCheckingAttendance(boolean status) {
                                                                if (status) {
                                                                    studAttDb.updateAttendanceData(new attendanceInterface() {
                                                                                                       @Override
                                                                                                       public void getStudentAttendance(ArrayList<StudAttendanceModal> list) {

                                                                                                       }

                                                                                                       @Override
                                                                                                       public void isCheckingAttendance(boolean status) {
                                                                                                           if (status) {
                                                                                                               Toast.makeText(StudentAttendance.this, "Successfully inserted wifi Attendance", Toast.LENGTH_SHORT).show();
                                                                                                           } else {
                                                                                                               Toast.makeText(StudentAttendance.this, "Failed to update Attendance", Toast.LENGTH_SHORT).show();
                                                                                                           }
                                                                                                       }

                                                                                                       @Override
                                                                                                       public void getWifiData(Map<String, Object> attendanceData) {

                                                                                                       }
                                                                                                   }, studentMembers.get(StudentSessionManager.KEY_ST_COLLEGE),
                                                                            prevIntent.getStringExtra("semYear"), studentMembers.get(StudentSessionManager.KEY_ST_DIV),
                                                                            studentMembers.get(StudentSessionManager.KEY_ST_ROLL), prevIntent.getStringExtra("subCode"));

                                                                } else {
                                                                    Toast.makeText(StudentAttendance.this, "Failed to insert wifi Attendance", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }

                                                            @Override
                                                            public void getWifiData(Map<String, Object> attendanceData) {

                                                            }
                                                        }, studentMembers.get(StudentSessionManager.KEY_ST_COLLEGE),
                                prevIntent.getStringExtra("semYear"), studentMembers.get(StudentSessionManager.KEY_ST_COURSE),
                                studentMembers.get(StudentSessionManager.KEY_ST_DIV), "1-22", getCurrentDateFormatted(), prevIntent.getStringExtra("subCode"),
                                prevIntent.getStringExtra("subType"), attendanceData);

                    } else {
                        Toast.makeText(StudentAttendance.this, "Image not uploaded!...", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(StudentAttendance.this, "Photo is empty!...", Toast.LENGTH_SHORT).show();
        }
    }


    private String getCurrentDateFormatted() {
        Date currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, EEE", Locale.getDefault());
        return dateFormat.format(currentDate);
    }

    public byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream baos = null;
        if (bitmap != null && bitmap.getHeight() > 0 && bitmap.getWidth() > 0) {
            baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        }
        return baos.toByteArray();
    }

}