package com.example.iattendance.Faculty_Attendance_Screen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.iattendance.R;

public class CustomSpinnerAdapter extends ArrayAdapter<String> {
    private Context context;
    private String[] items;

    public CustomSpinnerAdapter(@NonNull Context context, String[] items) {
        super(context, R.layout.spinner_item, items);
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createView(position, convertView, parent);
    }

    private View createView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.spinner_item, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.text);
//        View divider = convertView.findViewById(R.id.divider);
        textView.setText(items[position]);

        ImageView icon = convertView.findViewById(R.id.icon);
        if (position == 0) {  // Only show the icon for the first item (for example)
            icon.setVisibility(View.VISIBLE);
//            divider.setVisibility(View.VISIBLE);
        } else {
//            icon.setVisibility(View.INVISIBLE);
//            divider.setVisibility(View.VISIBLE);
//            textView.setPadding(8, 4, 8, 4);
        }

        return convertView;
    }
}
