package com.example.iattendance.Dashboard_Fragments.Admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.iattendance.R;
import com.example.iattendance.Utils.Admin.SessionManager;
import com.example.iattendance.Utils.Admin.Utils;
import com.example.iattendance.Welcome_screen;
import com.google.android.material.button.MaterialButton;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment_admin#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment_admin extends Fragment {

    // Creating session manager object
    SessionManager sessionManager;
    String adminId, adminPass, adminPhone;
    Utils utils;

    MaterialButton log_out_btn;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingsFragment_admin() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment_admin.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment_admin newInstance(String param1, String param2) {
        SettingsFragment_admin fragment = new SettingsFragment_admin();
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
        View view = inflater.inflate(R.layout.fragment_settings_admin, container, false);

        initializeView(view);

        log_out_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.logoutUser();
                Intent intent = new Intent(getActivity(), Welcome_screen.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

//        checkForIsAdminLoggedIn();

        return view;
    }

    private void initializeView(View view) {
        log_out_btn = view.findViewById(R.id.log_out_btn);

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