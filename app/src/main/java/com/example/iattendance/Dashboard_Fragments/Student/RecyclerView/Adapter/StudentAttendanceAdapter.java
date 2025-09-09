package com.example.iattendance.Dashboard_Fragments.Student.RecyclerView.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.iattendance.Dashboard_Fragments.Faculty.FacultySubjectAdapter;
import com.example.iattendance.R;
import com.example.iattendance.Utils.Attendance.Modals.StudentStatusModal;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentAttendanceAdapter extends RecyclerView.Adapter<StudentAttendanceAdapter.ViewHolder> {

    Map<String, Object> attendanceData;
    Context context;
    String currentDivision;

    public StudentAttendanceAdapter(Map<String, Object> attendanceData, Context context, String currentDivision) {
        this.attendanceData = attendanceData;
        this.context = context;
        this.currentDivision = currentDivision;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_attendance_status_faculty_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (attendanceData.size() > 0) {
            if (attendanceData.containsKey(currentDivision)) {
                List<Map<String, Object>> studentList = (List<Map<String, Object>>) attendanceData.get(currentDivision);
                Glide.with(context)
                        .load(studentList.get(position).get("studImg"))
                        .into(holder.studentImageAbbr);

                holder.studentName.setText(studentList.get(position).get("studName").toString());
                holder.studentRoll.setText(studentList.get(position).get("rollNo").toString());
            }
        }
    }

    @Override
    public int getItemCount() {
        List<Map<String, Object>> studentList = (List<Map<String, Object>>) attendanceData.get(currentDivision);
        return studentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView studentImageAbbr;
        TextView studentName, studentRoll;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            studentImageAbbr = itemView.findViewById(R.id.studImageAbbr);
            studentName = itemView.findViewById(R.id.studentName);
            studentRoll = itemView.findViewById(R.id.studentRoll);
        }
    }
}
