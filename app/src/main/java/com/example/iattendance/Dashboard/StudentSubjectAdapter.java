package com.example.iattendance.Dashboard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iattendance.R;
import com.example.iattendance.Student_Attendance_Screen.StudentAttendance;
import com.example.iattendance.Utils.Attendance.Modals.StudAttendanceModal;

import java.util.ArrayList;

public class StudentSubjectAdapter extends RecyclerView.Adapter<StudentSubjectAdapter.ViewHolder> {
    private final Context context;
    ArrayList<StudAttendanceModal> subjectItemLists;
    String semYr;

    public StudentSubjectAdapter(Context context, ArrayList<StudAttendanceModal> subjectItemLists, String semYear) {
        this.context = context;
        this.subjectItemLists = subjectItemLists;
        this.semYr = semYear;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.subjects_recview, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StudAttendanceModal parentItem = subjectItemLists.get(position);
        String[] abbr = parentItem.getSubjectName().split(" ");
        StringBuilder subAbbr = new StringBuilder();
        for (String word : abbr) {
            if (!word.equals("and")
                    && !word.equals("of")
                    && !word.equals("the")) {
                subAbbr.append(word.substring(0, 1).toUpperCase());
            }
        }
        abbr = parentItem.getAttendance().split(" ");
        int currentAtt = Integer.parseInt(abbr[0]);
        int totalAtt = Integer.parseInt(abbr[3]);
        int perAtt = totalAtt == 0 ? 0 : (currentAtt * 100) / totalAtt;

        holder.subj_abbr.setText(subAbbr.toString());
        holder.prof_name.setText(parentItem.getFacultyName());
        holder.subj_name.setText(parentItem.getSubjectName());
        holder.first_letter.setText(parentItem.getSubjectName().substring(0, 1));
        holder.present_count.setText(String.valueOf(currentAtt));
        holder.total_count.setText(String.valueOf(totalAtt));
        holder.present_percent.setText(perAtt + "%");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent attIntent = new Intent(context, StudentAttendance.class);
                attIntent.putExtra("faculty_name", parentItem.getFacultyName());
                attIntent.putExtra("sub_name", parentItem.getSubjectName());
                attIntent.putExtra("current_att", String.valueOf(currentAtt));
                attIntent.putExtra("total_att", String.valueOf(totalAtt));
                attIntent.putExtra("subCode", parentItem.getSubjectCode());
                attIntent.putExtra("batch", parentItem.getBatch());
                attIntent.putExtra("semYear", semYr);
                attIntent.putExtra("subType", parentItem.getSubjectType());
                context.startActivity(attIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return subjectItemLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView subj_abbr, subj_name, prof_name, first_letter, present_count, total_count, present_percent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            subj_abbr = itemView.findViewById(R.id.subj_abbr);
            prof_name = itemView.findViewById(R.id.prof_name);
            subj_name = itemView.findViewById(R.id.subj_name);
            first_letter = itemView.findViewById(R.id.first_letter);
            present_count = itemView.findViewById(R.id.present_count);
            total_count = itemView.findViewById(R.id.total_count);
            present_percent = itemView.findViewById(R.id.present_percent);
        }
    }

}
