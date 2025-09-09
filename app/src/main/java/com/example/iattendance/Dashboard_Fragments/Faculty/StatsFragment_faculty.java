package com.example.iattendance.Dashboard_Fragments.Faculty;

import static android.content.Context.STORAGE_SERVICE;


import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.iattendance.R;
import com.example.iattendance.Utils.Attendance.DB.StudentAttendanceDb;
import com.example.iattendance.Utils.Attendance.Modals.StudAttendanceModal;
import com.example.iattendance.Utils.Attendance.attendanceInterface;
import com.example.iattendance.Utils.Faculty.FacultySessionManager;
import com.example.iattendance.Utils.FacultySubjectInterface;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.checkerframework.checker.units.qual.A;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatsFragment_faculty#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatsFragment_faculty extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Spinner spinnerSubject;
    FirebaseFirestore db;
    FacultySessionManager facultySession;
    HashMap<String, String> facultyMember;
    String coll_code, faculty_id, faculty_courseName;
    ArrayList<String> subjectsList;
    ArrayList<String> divisionList;
    MaterialButton downloadBtn;
    String selectedSubject;
    StudentAttendanceDb studentAttendanceDb;
    private static final int REQUEST_CODE_PERMISSIONS = 123;


    public StatsFragment_faculty() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StatsFragment_faculty.
     */
    // TODO: Rename and change types and number of parameters
    public static StatsFragment_faculty newInstance(String param1, String param2) {
        StatsFragment_faculty fragment = new StatsFragment_faculty();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stats_faculty, container, false);
        initializeViews(view);
        return view;
    }

    public void initializeViews(View view) {
        spinnerSubject = view.findViewById(R.id.spinnerSubject);
        facultySession = new FacultySessionManager(requireActivity());
        facultyMember = facultySession.getUserDetails();
        downloadBtn = view.findViewById(R.id.downloadBtn);

        coll_code = facultyMember.get(FacultySessionManager.KEY_FC_COLLEGE);
        faculty_id = facultyMember.get(FacultySessionManager.KEY_FC_ID);
        faculty_courseName = facultyMember.get(FacultySessionManager.KEY_FC_COURSE);
        db = FirebaseFirestore.getInstance();
        studentAttendanceDb = new StudentAttendanceDb(getContext());
        subjectsList = new ArrayList<>();

        getSubjectsDetails(new FacultySubjectInterface() {
            @Override
            public void getSubjectCodes(ArrayList<String> subjects, ArrayList<String> subjectDivison) {
                if (subjects.size() > 0) {
                    subjectsList = subjects;
                    divisionList = subjectDivison;
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_item, subjectsList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerSubject.setAdapter(adapter);
                }
            }
        }, coll_code, faculty_id, faculty_courseName);

        spinnerSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSubject = subjectsList.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchAndCreateExcelFile();
            }
        });
    }

    private void fetchAndCreateExcelFile() {
        Toast.makeText(getContext(), "Division " + divisionList.get(0), Toast.LENGTH_SHORT).show();
        studentAttendanceDb.fetchWifiAttendanceData(new attendanceInterface() {
            @Override
            public void getStudentAttendance(ArrayList<StudAttendanceModal> list) {
                // Handle student attendance data
            }

            @Override
            public void isCheckingAttendance(boolean status) {
                // Handle attendance checking status
            }

            @Override
            public void getWifiData(Map<String, Object> attendanceData) throws IOException {
                if (attendanceData.size() > 0) {
                    createExcelFile(attendanceData);
                } else {
                    Toast.makeText(getContext(), "Empty attendanceData", Toast.LENGTH_SHORT).show();
                }
            }
        }, coll_code, "Semester 1, 2024", faculty_courseName, divisionList.get(0), getCurrentDate(), selectedSubject, "Practical");
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

    private void createExcelFile(Map<String, Object> attendanceData) throws IOException {
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        HSSFSheet hssfSheet = hssfWorkbook.createSheet("Attendance");

        HSSFRow hssfRow = hssfSheet.createRow(0);

        HSSFCell hssfCell = hssfRow.createCell(0);
        hssfCell.setCellValue("Roll number");

        hssfCell = hssfRow.createCell(1);
        hssfCell.setCellValue("studName");

        hssfCell = hssfRow.createCell(2);
        hssfCell.setCellValue("Status");

        hssfCell = hssfRow.createCell(3);
        hssfCell.setCellValue("studAttDate");

        hssfCell = hssfRow.createCell(4);
        hssfCell.setCellValue("studAttDate");

        hssfCell = hssfRow.createCell(5);
        hssfCell.setCellValue("studAttTime");

        hssfCell = hssfRow.createCell(6);
        hssfCell.setCellValue("studImage");

        hssfCell = hssfRow.createCell(5);
        hssfCell.setCellValue("studAttDate");

        if (attendanceData.containsKey("A")) {
            List<Map<String, Object>> innerList = (List<Map<String, Object>>) attendanceData.get("A");

            for (int i = 0; i < innerList.size(); i++) {
                hssfRow = hssfSheet.createRow(i + 1);
                int j = 0;
                while (j < innerList.get(i).size()) {
                    hssfCell = hssfRow.createCell(j);
                    hssfCell.setCellValue(innerList.get(i).get("rollNo").toString());
                    j++;
                    hssfCell = hssfRow.createCell(j);
                    hssfCell.setCellValue(innerList.get(i).get("studName").toString());
                    j++;
                    hssfCell = hssfRow.createCell(j);
                    hssfCell.setCellValue(innerList.get(i).get("status").toString());
                    j++;
                    hssfCell = hssfRow.createCell(j);
                    hssfCell.setCellValue(innerList.get(i).get("studAttDate").toString());
                    j++;
                    hssfCell = hssfRow.createCell(j);
                    hssfCell.setCellValue(innerList.get(i).get("studAttTime").toString());
                    j++;
                    hssfCell = hssfRow.createCell(j);
                    hssfCell.setCellValue(innerList.get(i).get("studImg").toString());
                    j++;
                }
            }
            saveWorkBook(hssfWorkbook);
        } else {
            Toast.makeText(getContext(), "Contains key failed", Toast.LENGTH_SHORT).show();
        }

    }

    private void saveWorkBook(HSSFWorkbook hssfWorkbook) throws IOException {
        StorageManager storageManager = (StorageManager) getContext().getSystemService(STORAGE_SERVICE);

        StorageVolume storageVolume = storageManager.getStorageVolumes().get(0);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            File fileOutput = new File(storageVolume.getDirectory().getPath() + "/Download/iAttendance/Attendance.xls");
            FileOutputStream fileOutputStream = new FileOutputStream(fileOutput);
            hssfWorkbook.write(fileOutputStream);
            fileOutputStream.close();
            hssfWorkbook.close();
            Toast.makeText(getContext(), "File Created successfully!....", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "VERSION FAILED!..", Toast.LENGTH_SHORT).show();
        }
    }


    private void getSubjectsDetails(FacultySubjectInterface SubjectInterface, String
            coll_code, String faculty_id, String faculty_courseName) {

        db.collection("Faculty Subjects").document(coll_code).collection("Course")
                .document(faculty_courseName).collection("Year").document("2024")
                .collection("Semester").document("Semester 1").collection("Faculty code")
                .document(faculty_id).collection("Details")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            ArrayList<String> subjectCodes = new ArrayList<>();
                            ArrayList<String> subjectDivisions = new ArrayList<>();
                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                                subjectCodes.add(documentSnapshot.getString("subject_code"));
                                if (!subjectDivisions.contains(documentSnapshot.getString("division"))) {
                                    subjectDivisions.add(documentSnapshot.getString("division"));
                                }
                            }
                            SubjectInterface.getSubjectCodes(subjectCodes, subjectDivisions);
                        } else {
                            SubjectInterface.getSubjectCodes(new ArrayList<>(), new ArrayList<>());
                            Toast.makeText(getContext(), "Empty query snapshot", Toast.LENGTH_SHORT).show();
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