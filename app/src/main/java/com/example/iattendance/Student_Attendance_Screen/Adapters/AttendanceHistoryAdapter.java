package com.example.iattendance.Student_Attendance_Screen.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iattendance.Dashboard_Fragments.Student.RecyclerView.Adapter.StudentAttendanceAdapter;
import com.example.iattendance.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttendanceHistoryAdapter extends RecyclerView.Adapter<AttendanceHistoryAdapter.ViewHolder> {

    Context context;
    HashMap<Integer, ArrayList<String>> historyAttendance;
    private int selectedPosition = RecyclerView.NO_POSITION;

    public AttendanceHistoryAdapter(Context context, HashMap<Integer, ArrayList<String>> historyAttendance) {
        this.context = context;
        this.historyAttendance = historyAttendance;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_attendance_history_view, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (historyAttendance.size() > 0) {
            if (historyAttendance.get(position).get(0).equals("Present")) {
                holder.attendanceDate.setText(String.valueOf(position));
                holder.attendanceDate.setBackgroundResource
                        (selectedPosition == position ?
                                R.drawable.selected_date_block_drawable_background : R.drawable.present_date_block_drawable_background);
                holder.attendanceDate.setTextColor(selectedPosition == position ? Color.WHITE : Color.parseColor("#02AB6E"));
                holder.AttendanceDate_tv.setText(historyAttendance.get(position).get(1));
                holder.AttendanceTime_tv.setText(historyAttendance.get(position).get(2));
            } else {
                holder.attendanceDate.setBackgroundResource(R.drawable.absent_date_block_drawable_background);
                holder.attendanceDate.setTextColor(Color.parseColor("#E93131"));
                holder.AttendanceDate_tv.setText(historyAttendance.get(position).get(1));
                holder.AttendanceTime_tv.setText(historyAttendance.get(position).get(2));
            }
            holder.attendanceDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int previousSelectedPos = selectedPosition;
                    selectedPosition = holder.getAdapterPosition();
                    notifyItemChanged(previousSelectedPos);
                    notifyItemChanged(selectedPosition);
//                    holder.attendanceDate.setBackgroundResource(R.drawable.selected_date_block_drawable_background);
//                    holder.attendanceDate.setTextColor(Color.WHITE);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return historyAttendance.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView AttendanceDate_tv, AttendanceTime_tv;
        TextView attendanceDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            AttendanceDate_tv = itemView.findViewById(R.id.AttendanceDate_tv);
            AttendanceTime_tv = itemView.findViewById(R.id.AttendanceTime_tv);
            attendanceDate = itemView.findViewById(R.id.attendanceDate);
        }
    }
}
