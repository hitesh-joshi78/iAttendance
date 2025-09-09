package com.example.iattendance.Dashboard_Fragments.Faculty;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iattendance.Faculty_Attendance_Screen.FacultyAttendancePg;
import com.example.iattendance.R;
import com.example.iattendance.Student_Attendance_Screen.StudentAttendance;
import com.example.iattendance.Utils.Subjects.db.SubjectsModel;

import java.util.ArrayList;

public class FacultySubjectAdapter extends RecyclerView.Adapter<FacultySubjectAdapter.ViewHolder> {
    private final ArrayList<SubjectsModel> subjectsList;
    Context context;

    public FacultySubjectAdapter(ArrayList<SubjectsModel> subjectsList, Context context) {
        this.subjectsList = subjectsList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parent_rv_layout, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SubjectsModel subject = subjectsList.get(position);

        holder.subjectFirst_letter.setText(subject.getSubject().substring(0, 1));
        holder.subj_type.setText(subject.getSubject_type());
        holder.subj_name.setText(subject.getSubject());
        holder.class_in_no.setText(String.valueOf(subject.getClass_completed()));
        holder.div_txt.setText("Division: " + subject.getDivision());

        if (subject.getSubject().equals("null")) {
            holder.batch_txt.setText("");
        } else {
            holder.batch_txt.setText("Batch: " + subject.getBatch());
        }

        holder.sem_txt.setText("Semester: " + subject.getSemester());
        holder.year_txt.setText(subject.getYear());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent attIntent = new Intent(context, FacultyAttendancePg.class);
                ArrayList<String> passingDataList = new ArrayList<>();
                passingDataList.add(subject.getSubject());
                passingDataList.add(subject.getDivision());
                passingDataList.add(subject.getFacultyName());
                passingDataList.add(subject.getSubject_code());
                passingDataList.add(subject.getSemester());
                passingDataList.add(subject.getSubject_type());
                attIntent.putStringArrayListExtra("Attendance Data", passingDataList);
                context.startActivity(attIntent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return subjectsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView subjectFirst_letter, subj_type, subj_name, class_in_no, div_txt, batch_txt, sem_txt, year_txt;

        public ViewHolder(View itemView) {
            super(itemView);
            subjectFirst_letter = itemView.findViewById(R.id.subjectFirst_letter);
            subj_type = itemView.findViewById(R.id.subj_type);
            subj_name = itemView.findViewById(R.id.subj_name);
            class_in_no = itemView.findViewById(R.id.class_in_no);
            div_txt = itemView.findViewById(R.id.div_txt);
            batch_txt = itemView.findViewById(R.id.batch_txt);
            sem_txt = itemView.findViewById(R.id.sem_txt);
            year_txt = itemView.findViewById(R.id.year_txt);
        }
    }
}

