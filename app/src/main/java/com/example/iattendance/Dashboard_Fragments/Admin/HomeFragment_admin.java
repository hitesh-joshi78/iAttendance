package com.example.iattendance.Dashboard_Fragments.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.iattendance.Dashboard_Fragments.Faculty.FacultyAddSubject;
import com.example.iattendance.R;
import com.example.iattendance.Sign_up_Screens.Admin_signup.ModalClass;
import com.example.iattendance.Utils.Admin.SessionManager;
import com.example.iattendance.Utils.Admin.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment_admin#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment_admin extends Fragment {

    // Creating session manager object
    SessionManager sessionManager;
    String adminId, adminPass, adminPhone;
    Utils utils;
    public String firebase_ad_name, firebase_coll_code;
    char initial;

    TextView admin_name, admin_coll_code, first_letter;
    FloatingActionButton add_subject_fab;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public HomeFragment_admin() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment_admin.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment_admin newInstance(String param1, String param2) {
        HomeFragment_admin fragment = new HomeFragment_admin();
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
        View view = inflater.inflate(R.layout.fragment_home_admin, container, false);

        initializeView(view);

//        checkForIsAdminLoggedIn();

        getAdminDetails();

//        add_subject_fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), FacultyAddSubject.class);
//                startActivity(intent);
//            }
//        });

        return view;
    }

    private void setAllViewsWithValues(char initial, String firebase_ad_name, String firebase_coll_code) {
        admin_name.setText(firebase_ad_name);
        admin_coll_code.setText(firebase_coll_code);
        first_letter.setText(String.valueOf(initial));
    }

    private void getAdminDetails() {
        // Call the method to get admin details from Firebase
        utils.getAdmin(adminId).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Admin details retrieved successfully
                ModalClass admin = task.getResult();
                if (admin != null) {
                    firebase_ad_name = admin.getAdminName();
                    firebase_coll_code = admin.getCollegeCode();
                    initial = firebase_ad_name.charAt(0);

                    setAllViewsWithValues(initial, firebase_ad_name, firebase_coll_code);
                } else {
                    // Admin not found or error occurred
                    Utils.showToast(getActivity(), "Please try again!");
                }
            } else {
                // Error occurred while retrieving admin details
                Exception exception = task.getException();
                if (exception != null) {
                    // Handle the exception
                }
            }
        });
    }

    private void initializeView(View view) {
        admin_name = view.findViewById(R.id.admin_name);
        admin_coll_code = view.findViewById(R.id.admin_coll_code);
        first_letter = view.findViewById(R.id.first_letter);
//        add_subject_fab = view.findViewById(R.id.add_subject_fab);
        sessionManager = new SessionManager(requireActivity());

        // Getting stored user details
        HashMap<String, String> user = sessionManager.getUserDetails();
        adminId = user.get(SessionManager.KEY_AD_ID);
        adminPass = user.get(SessionManager.KEY_AD_PASS);
        adminPhone = user.get(SessionManager.KEY_AD_PHONE);

        // Instantiate utils object here
        utils = new Utils();
    }

}