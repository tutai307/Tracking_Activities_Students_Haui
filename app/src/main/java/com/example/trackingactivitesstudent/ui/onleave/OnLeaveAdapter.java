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

public class OnLeaveAdapter extends ArrayAdapter<OnLeaveItem> {
    private final Context context;
    private final List<OnLeaveItem> items;

    public OnLeaveAdapter(@NonNull Context context, @NonNull List<OnLeaveItem> objects) {
        super(context, R.layout.list_item_onleave, objects);
        this.context = context;
        this.items = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.list_item_onleave, parent, false);
        }

        OnLeaveItem item = items.get(position);

        TextView txvCourseName = convertView.findViewById(R.id.txvCourseName);
        TextView txvCourseTime = convertView.findViewById(R.id.txvCourseTime);
        TextView txvInstructor = convertView.findViewById(R.id.txvInstructor);
        TextView txvReason = convertView.findViewById(R.id.txvReason);
        TextView txvDate = convertView.findViewById(R.id.txvDate);

        txvCourseName.setText(item.getCourseName());
        txvCourseTime.setText(item.getTimeInDay());
        txvInstructor.setText(item.getInstructorName());
        txvReason.setText(item.getReason());
        txvDate.setText(item.getDate());

        return convertView;
    }
}

