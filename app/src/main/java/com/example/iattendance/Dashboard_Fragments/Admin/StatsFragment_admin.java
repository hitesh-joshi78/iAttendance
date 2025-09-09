package com.example.iattendance.Dashboard_Fragments.Admin;

import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.iattendance.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatsFragment_admin#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatsFragment_admin extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ProgressBar progress_circular;
    TextView progress_text;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StatsFragment_admin() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StatsFragment_admin.
     */
    // TODO: Rename and change types and number of parameters
    public static StatsFragment_admin newInstance(String param1, String param2) {
        StatsFragment_admin fragment = new StatsFragment_admin();
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
        View view = inflater.inflate(R.layout.fragment_stats_admin, container, false);

        initializeView(view);

        return view;
    }

    private void initializeView(View view) {
        progress_circular = view.findViewById(R.id.progress_circular);
        progress_text = view.findViewById(R.id.progress_text);

        // Example of setting progress
        int progress = 60;
        progress_circular.setProgress(progress);
        progress_text.setText(progress + "%");

        if (progress >= 75) {
            progress_circular.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
        } else if (progress >= 60) {
            progress_circular.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.orange)));
        } else {
            progress_circular.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
        }

    }
}