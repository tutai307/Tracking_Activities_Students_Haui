package com.example.trackingactivitesstudent.ui.calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.trackingactivitesstudent.R;

import java.util.ArrayList;

public class CalendarAdapter extends ArrayAdapter<CalendarViewModel> {
    Context context;
    int layoutID;
    ArrayList<CalendarViewModel> list;

    // Constructor
    public CalendarAdapter(@NonNull Context context, int resource, @NonNull ArrayList<CalendarViewModel> objects) {
        super(context, resource, objects);
        this.context = context;
        this.layoutID = resource;
        this.list = objects;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);  // Sử dụng LayoutInflater từ context
        if (convertView == null) {
            convertView = inflater.inflate(layoutID, parent, false);  // Inflate view chỉ khi convertView null
        }

        if (position >= 0 && position < list.size()) {
            final TextView time = convertView.findViewById(R.id.time);
            final TextView name_course = convertView.findViewById(R.id.name_course);
            final TextView name_instructor = convertView.findViewById(R.id.name_instructor);

            CalendarViewModel calendarViewModel = list.get(position);

            // Thiết lập dữ liệu vào các TextView
            time.setText(calendarViewModel.getTimeInDay());
            name_course.setText(calendarViewModel.getCourseName());
            name_instructor.setText(calendarViewModel.getInstructorName());
        }

        return convertView;
    }
}
