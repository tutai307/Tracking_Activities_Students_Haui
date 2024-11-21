package com.example.trackingactivitesstudent.ui.onleave;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.trackingactivitesstudent.R;

import java.util.List;

public class OnLeaveAdapter extends ArrayAdapter<OnLeaveModel> {
    private Context context;
    private int resource;
    private List<OnLeaveModel> leaveList;

    public OnLeaveAdapter(@NonNull Context context, int resource, @NonNull List<OnLeaveModel> leaveList) {
        super(context, resource, leaveList);
        this.context = context;
        this.resource = resource;
        this.leaveList = leaveList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);

        TextView txvCourseName = convertView.findViewById(R.id.txvCourseName);
        TextView txvCourseTime = convertView.findViewById(R.id.txvCourseTime);
        TextView txvInstructor = convertView.findViewById(R.id.txvInstructor);
        TextView txvReason = convertView.findViewById(R.id.txvReason);
        TextView txvDate = convertView.findViewById(R.id.txvDate);

        OnLeaveModel leave = leaveList.get(position);

        txvCourseName.setText(leave.getCourseName());
        txvCourseTime.setText(leave.getCourseTime());
        txvInstructor.setText(leave.getInstructor());
        txvReason.setText(leave.getReason());
        txvDate.setText(leave.getDate());

        return convertView;
    }
}
