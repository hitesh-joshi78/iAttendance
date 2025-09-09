package com.example.iattendance.Dashboard_Fragments.Student;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iattendance.Dashboard.StudentSubjectAdapter;
import com.example.iattendance.R;
import com.example.iattendance.Utils.Attendance.Modals.StudAttendanceModal;
import com.example.iattendance.Utils.Attendance.Modals.WifiAttendanceModal;
import com.example.iattendance.Utils.Attendance.attendanceInterface;
import com.example.iattendance.Utils.Attendance.DB.*;
import com.example.iattendance.Utils.Student.StudentSessionManager;
import com.example.iattendance.Utils.Subjects.db.CourseDb;
import com.example.iattendance.Utils.Subjects.db.subjectInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HomeFragment_student extends Fragment {
    StudentSubjectAdapter categoryAdapter;

    TextView id, studentName, first_letter;
    ArrayList<StudAttendanceModal> subjectModalArrayList;
    RecyclerView studSubjectCardView;
    RelativeLayout animatedComponent;
    Spinner spinner_semesters;
    String semesterYr;
    StudentSessionManager studentSession;
    HashMap<String, String> studentMember;
    ImageView empty_icon;
    int rollNumber;
    private TextView activeText;
    StudentAttendanceDb studAttDb;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private CourseDb courseDb;

    public HomeFragment_student() {
        // Required empty public constructor
    }

    public static HomeFragment_student newInstance(String param1, String param2) {
        HomeFragment_student fragment = new HomeFragment_student();
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

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_student, container, false);
        initializeViews(view);

        studentSession = new StudentSessionManager(requireContext());
        studentMember = studentSession.getUserDetails();

        id.setText(studentMember.get(StudentSessionManager.KEY_ST_ID));
        studentName.setText(studentMember.get("studentName"));
        first_letter.setText(Objects.requireNonNull(studentMember.get("studentName")).substring(0, 1));
//        Hooks

        ArrayList<String> semestersList = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_item, semestersList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_semesters.setAdapter(adapter);

        HashMap<String, String> data = new HashMap<>();
        data.put("collegeCode", studentMember.get(StudentSessionManager.KEY_ST_COLLEGE));
        data.put("facultyId", "FC8601");
        data.put("courseName", studentMember.get(StudentSessionManager.KEY_ST_COURSE));
        data.put("division", studentMember.get(StudentSessionManager.KEY_ST_DIV));
        rollNumber = Integer.parseInt(Objects.requireNonNull(studentMember.get(StudentSessionManager.KEY_ST_ROLL)));


        courseDb = new CourseDb(getContext(), data);
        courseDb.getSemesters(new subjectInterface() {
            @Override
            public void getSemesterList(ArrayList<String> semesterList) {
                if (semesterList.size() > 0) {
                    for (String str : semesterList) {
                        if (!semestersList.contains(str)) {
                            semestersList.add(str);
                        }
                    }
                    adapter.notifyDataSetChanged();
                    // Setting default selection to the latest semester and year
                    spinner_semesters.setSelection(semestersList.size() - 1);
                } else {
                    Toast.makeText(getContext(), "Empty Semester lists received", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void getStudentSemesterList(ArrayList<Student_SubjectModal> semesterList) {

            }

            @Override
            public void getFacultyCodes(ArrayList<String> facultyCodeList) {

            }

            @Override
            public void isConfirmed(boolean val) {

            }
        });

        spinner_semesters.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedSemester = semestersList.get(position);
                String[] parts = selectedSemester.split(", ");
                String selectedYear = parts[1].trim();
                String selectedSemesterNumber = parts[0].split(" ")[1].trim();
                semesterYr = "Semester " + selectedSemesterNumber + ", " + selectedYear;
                data.put("year", selectedYear);
                data.put("semester", selectedSemesterNumber);
                data.put("subjectType", "Theory");
                courseDb = new CourseDb(getContext(), data);

                // Fetch roll number from session

                studAttDb = new StudentAttendanceDb(getContext());
                studAttDb.fetchAttendanceData(studentMember.get(StudentSessionManager.KEY_ST_COLLEGE), selectedSemester,
                        studentMember.get(StudentSessionManager.KEY_ST_DIV), rollNumber, new attendanceInterface() {
                            @Override
                            public void getStudentAttendance(ArrayList<StudAttendanceModal> list) {
                                if (list.size() > 0) {
                                    updateRecyclerView(list);
                                } else {
                                    updateRecyclerView(list);
                                }
                            }

                            @Override
                            public void isCheckingAttendance(boolean status) {

                            }

                            @Override
                            public void getWifiData(Map<String, Object> attendanceData) {

                            }


                        });     //Roll no, must be fetched from student session.

                categoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        subjectModalArrayList = new ArrayList<>();

        studSubjectCardView.setLayoutManager(new LinearLayoutManager(getActivity()));
        categoryAdapter = new StudentSubjectAdapter(getActivity(), subjectModalArrayList, semesterYr);
        studSubjectCardView.setAdapter(categoryAdapter);

        categoryAdapter.notifyDataSetChanged();
        // Fetch data for each category and update the adapter

        return view;
    }

    private void initializeViews(View view) {
        id = view.findViewById(R.id.id);
        studentName = view.findViewById(R.id.studentName);
        first_letter = view.findViewById(R.id.first_letter);
        spinner_semesters = view.findViewById(R.id.spinner_semesters);
        studSubjectCardView = view.findViewById(R.id.category_recView);
        activeText = view.findViewById(R.id.activeText);
        empty_icon = view.findViewById(R.id.empty_icon);

    }

    private void updateRecyclerView(ArrayList<StudAttendanceModal> subjectsList) {
        if (subjectsList.size() > 0) {

            categoryAdapter = new StudentSubjectAdapter(getContext(), subjectsList, semesterYr);
            studSubjectCardView.setAdapter(categoryAdapter);
            empty_icon.setVisibility(View.GONE);
            categoryAdapter.notifyDataSetChanged();
        } else {
            empty_icon.setVisibility(View.VISIBLE);
            studSubjectCardView.setVisibility(View.GONE);
            spinner_semesters.setVisibility(View.VISIBLE);
        }
    }

}