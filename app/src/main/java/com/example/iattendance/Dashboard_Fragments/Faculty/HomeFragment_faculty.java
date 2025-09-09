package com.example.iattendance.Dashboard_Fragments.Faculty;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.iattendance.Dashboard_Fragments.Student.Student_SubjectModal;
import com.example.iattendance.R;
import com.example.iattendance.Utils.Faculty.FacultySessionManager;
import com.example.iattendance.Utils.Subjects.db.CourseDb;
import com.example.iattendance.Utils.Subjects.db.SubjectsModel;
import com.example.iattendance.Utils.Subjects.db.subjectInterface;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment_faculty#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment_faculty extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //    Views
    FloatingActionButton add_subject_fab;
    public TextView faculty_name, faculty_coll_code, first_letter, dpt_tv;
    Spinner spinner_semesters;
    RecyclerView rv_parent;
    ImageView empty_icon;

    String coll_code, faculty_id;

    //    Session
    FacultySessionManager facultySession;
    HashMap<String, String> facultyMember;
    FacultySubjectAdapter facultySubjectAdapter;
    FirebaseFirestore db;
    HashMap<String, String> data;
    CourseDb courseDb;


    public HomeFragment_faculty() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static HomeFragment_faculty newInstance(String param1, String param2) {
        HomeFragment_faculty fragment = new HomeFragment_faculty();
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
//        Firestore instance
        db = FirebaseFirestore.getInstance();

//        Faculty session
        facultySession = new FacultySessionManager(requireActivity());
        facultyMember = facultySession.getUserDetails();

        coll_code = facultyMember.get(FacultySessionManager.KEY_FC_COLLEGE);
        faculty_id = facultyMember.get(FacultySessionManager.KEY_FC_ID);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_faculty, container, false);

        initializeViews(view);

//        Add subject FAB onClickListener
        add_subject_fab.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), FacultyAddSubject.class));
        });

        return view;
    }

    @SuppressLint("SetTextI18n")
    private void initializeViews(View view) {
//        Hooks
        add_subject_fab = view.findViewById(R.id.add_subject_fab);
        faculty_name = view.findViewById(R.id.faculty_name);
        faculty_coll_code = view.findViewById(R.id.faculty_coll_code);
        first_letter = view.findViewById(R.id.first_letter);
        dpt_tv = view.findViewById(R.id.dpt_tv);
        rv_parent = view.findViewById(R.id.rv_parent);
        empty_icon = view.findViewById(R.id.empty_icon);
        rv_parent = view.findViewById(R.id.rv_parent);
        spinner_semesters = view.findViewById(R.id.spinner_semesters);

//        Setting text on TextViews
        faculty_name.setText(facultyMember.get(FacultySessionManager.KEY_FC_NAME));
        faculty_coll_code.setText(faculty_id);
        first_letter.setText(Objects.requireNonNull(facultyMember.get(FacultySessionManager.KEY_FC_NAME)).substring(0, 1));
        dpt_tv.setText("Master of Computer Applications");

//        Setting Linear Layout on RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_parent.setLayoutManager(layoutManager);

//        Setting adapter on the spinner
        ArrayList<String> semestersList = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_item, semestersList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_semesters.setAdapter(adapter);


        assert coll_code != null;
        assert faculty_id != null;
        data = new HashMap<>();
        data.put("collegeCode", coll_code);
        data.put("facultyId", faculty_id);
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


//        ItemSelectedListener on spinner
        spinner_semesters.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedSemester = semestersList.get(position);
                String[] parts = selectedSemester.split(", ");
                String selectedYear = parts[1].trim();
                String selectedSemesterNumber = parts[0].split(" ")[1].trim();


                fetchSubjects(selectedSemesterNumber, selectedYear, view);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

    }

    private void fetchSubjects(String semester, String year, View view) {
        String _courseName;
        if (semester == null || year == null || view == null) {
            Toast.makeText(getContext(), "Invalid input parameters", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(getContext(), "Course " + facultyMember.get(FacultySessionManager.KEY_FC_COURSE), Toast.LENGTH_SHORT).show();
        if (!facultyMember.get(FacultySessionManager.KEY_FC_COURSE).isEmpty()) {
            db.collection("Faculty Subjects")
                    .document(coll_code)
                    .collection("Course")
                    .document(facultyMember.get(FacultySessionManager.KEY_FC_COURSE))    //Must take from session manager
                    .collection("Year")
                    .document(year)
                    .collection("Semester")
                    .document("Semester " + semester)
                    .collection("Faculty code")
                    .document(faculty_id)
                    .collection("Details")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            ArrayList<SubjectsModel> subjectsList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String subName = document.getString("subject");
                                String batch = document.getString("batch");
                                int classCompleted = Objects.requireNonNull(document.getLong("class_completed")).intValue();
                                String div = document.getString("division");
                                String sem = document.getString("semester");
                                String subType = document.getString("subject_type");
                                String subCode = document.getString("subject_code");
                                String yr = document.getString("year");

                                // Create a new SubjectsModel object using the parameterized constructor
                                SubjectsModel subjectModel = new SubjectsModel(batch, div, sem, subCode, subName, subType, yr, classCompleted, "", facultyMember.get(FacultySessionManager.KEY_FC_NAME));
                                subjectsList.add(subjectModel);
                            }
                            updateRecyclerView(subjectsList);
//                        Toast.makeText(getContext(), "Fetched " + subjectsList.size() + " subjects", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Error getting subjects", Toast.LENGTH_SHORT).show();
                        }

                    });
        } else {
            empty_icon.setVisibility(View.VISIBLE);
            rv_parent.setVisibility(View.GONE);
            spinner_semesters.setVisibility(View.GONE);
        }
    }

    private void updateRecyclerView(ArrayList<SubjectsModel> subjectsList) {
        if (subjectsList.size() > 0) {
            facultySubjectAdapter = new FacultySubjectAdapter(subjectsList, getContext());
            rv_parent.setAdapter(facultySubjectAdapter);
            facultySubjectAdapter.notifyDataSetChanged();
            empty_icon.setVisibility(View.GONE);

        } else {

            empty_icon.setVisibility(View.VISIBLE);
            rv_parent.setVisibility(View.GONE);
            spinner_semesters.setVisibility(View.GONE);

        }
    }

}