package com.example.trackingactivitesstudent.ui.tracking;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trackingactivitesstudent.Database.DatabaseHelper;
import com.example.trackingactivitesstudent.R;
import com.example.trackingactivitesstudent.databinding.FragmentExerciseBinding;
import com.example.trackingactivitesstudent.databinding.FragmentTrackingBinding;

import java.util.List;

public class TrackingFragment extends Fragment {

    private TrackingViewModel mViewModel;
    FragmentTrackingBinding binding;
    private static final String TAG = "TrackingFragment";
    DatabaseHelper dbHelper;
    public static TrackingFragment newInstance() {
        return new TrackingFragment();
    }
    ListView lvhistory;
    TextView tvDateRange;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentTrackingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        dbHelper = new DatabaseHelper(getContext());

        tvDateRange = root.findViewById(R.id.tvDateRange);
        lvhistory = root.findViewById(R.id.lvHistory);
        SharedPreferences sharedDate = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);

        int studentId = sharedDate.getInt("studentId", -1);
        String startDate = sharedDate.getString("startDate", "N/A");
        String endDate = sharedDate.getString("endDate", "N/A");

        // Hiển thị thông tin trong logcat
        Log.d(TAG, "Student ID: " + studentId);
        Log.d(TAG, "Start Date: " + startDate);
        Log.d(TAG, "End Date: " + endDate);

        tvDateRange.setText("Từ " + startDate + " đến " + endDate);

        List<Tracker> trackerHistory = dbHelper.getTrackersByDateRangeAndStudent(startDate, endDate, studentId);

        // Kiểm tra dữ liệu
        if (trackerHistory.isEmpty()) {
            Log.d(TAG, "Không có bản ghi nào trong khoảng ngày đã chọn.");
            Toast.makeText(getContext(),"Không tìm thấy khoảng ngày tập luyện", Toast.LENGTH_SHORT).show();

        } else {
            Log.d(TAG, "Dữ liệu tracker: " + trackerHistory.toString());
            TrackerAdapter adapter = new TrackerAdapter(requireContext(), trackerHistory);
            lvhistory.setAdapter(adapter);
        }


        return root;
    }

}