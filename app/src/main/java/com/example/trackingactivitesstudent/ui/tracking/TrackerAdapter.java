package com.example.trackingactivitesstudent.ui.tracking;

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

public class TrackerAdapter extends ArrayAdapter<Tracker> {

    private final Context context;
    private final List<Tracker> trackers;

    public TrackerAdapter(Context context, List<Tracker> trackers) {
        super(context, 0, trackers);
        this.context = context;
        this.trackers = trackers;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_tracker, parent, false);
        }

        Tracker tracker = trackers.get(position);

        // Gắn các view
        TextView tvDate = convertView.findViewById(R.id.tvDate);
        TextView tvWaterStatus = convertView.findViewById(R.id.tvWaterStatus);
        TextView tvExercise = convertView.findViewById(R.id.tvExercise);

        // Định dạng dữ liệu và gắn vào các view
        tvDate.setText("Ngày: " + tracker.getCreatedAt());
        tvWaterStatus.setText(tracker.isEnoughWater() ? "Uống đủ nước" : "Chưa uống đủ nước");
        tvExercise.setText("Tập luyện: " + tracker.getExerciseSpan() + " phút");

        return convertView;
    }
}

