package com.example.trackingactivitesstudent.ui.health;

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

public class HealthAdapter extends ArrayAdapter<HealthViewModel> {
    Context context;
    int layoutID;
    ArrayList<HealthViewModel> list;
    public HealthAdapter(Context context, int resource, ArrayList<HealthViewModel> objects) {
        super(context, resource, objects);
        this.context = context;
        this.layoutID = resource;
        this.list = objects;
    }
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        LayoutInflater inflater = LayoutInflater.from(context);
        if (convertView == null) {
            convertView = inflater.inflate(layoutID, parent, false);
        }
        if (position >= 0 && position < list.size()) {
            final TextView txtNameCourse = convertView.findViewById(R.id.txtNameCourse);
            final TextView txtTime = convertView.findViewById(R.id.txtTime);
            final TextView txtInstructorName = convertView.findViewById(R.id.txtInstructorName);
            final TextView txtDisease = convertView.findViewById(R.id.txtDisease);
            final TextView txtTinhTrang = convertView.findViewById(R.id.txtTinhTrang);
            // Lấy đối tượng HealthViewModel tại vị trí hiện tại
            HealthViewModel healthViewModel = list.get(position);
            // Thiết lập dữ liệu vào các TextView
            txtNameCourse.setText(healthViewModel.getCourseName());
            txtTime.setText(healthViewModel.getTime());
            txtInstructorName.setText(healthViewModel.getInstructorName());
            txtDisease.setText(healthViewModel.getDisease());
            txtTinhTrang.setText(healthViewModel.getTinhTrang());
        }
        return convertView;
    }
}
