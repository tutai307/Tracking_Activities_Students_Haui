package com.example.trackingactivitesstudent.ui.exercise;

import static android.content.Context.MODE_PRIVATE;
import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.trackingactivitesstudent.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TrackingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrackingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TrackingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TrackingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TrackingFragment newInstance(String param1, String param2) {
        TrackingFragment fragment = new TrackingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int studentId = sharedPreferences.getInt("studentId", -1);
        if (getArguments() != null) {
            String startDate = getArguments().getString("startDate");
            String endDate = getArguments().getString("endDate");

            // Lọc dữ liệu
//            filterHistory(studentId, startDate, endDate);
            Log.d("LoginActivity", "Student ID saved: " + studentId);
            Log.d("LoginActivity", "Student ID saved: " + startDate);
            Log.d("LoginActivity", "Student ID saved: " + endDate);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tracking, container, false);
    }

//    private void filterHistory(int studentId, String startDate, String endDate) {
//        List<Tracker> trackerList = new ArrayList<>();
//
//        // Kết nối cơ sở dữ liệu
//        SQLiteDatabase database = openOrCreateDatabase("qlsv.db", MODE_PRIVATE, null);
//
//        // Truy vấn bảng trackers với điều kiện student_id và khoảng thời gian
//        String query = "SELECT * FROM trackers WHERE student_id = ? AND created_at BETWEEN ? AND ?";
//        Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(studentId), startDate, endDate});
//
//        if (cursor.moveToFirst()) {
//            do {
//                Tracker tracker = new Tracker();
//                tracker.setId(cursor.getInt(cursor.getColumnIndex("id")));
//                tracker.setStudentId(cursor.getInt(cursor.getColumnIndex("student_id")));
//                tracker.setIsEnoughWater(cursor.getInt(cursor.getColumnIndex("is_enought_water")) == 1); // convert to boolean
//                tracker.setLiter(cursor.getFloat(cursor.getColumnIndex("liter")));
//                tracker.setIsDoingExercise(cursor.getInt(cursor.getColumnIndex("is_doing_exercise")) == 1); // convert to boolean
//                tracker.setExerciseSpan(cursor.getFloat(cursor.getColumnIndex("exercise_span")));
//                tracker.setCreatedAt(cursor.getString(cursor.getColumnIndex("created_at")));
//                tracker.setUpdatedAt(cursor.getString(cursor.getColumnIndex("updated_at")));
//                trackerList.add(tracker);
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//
//        // Hiển thị danh sách
//        displayTrackerHistory(trackerList);
//    }

    private void displayTrackerHistory(List<Tracker> trackerList) {
//        ListView listView = getView().findViewById(R.id.listViewTrackerHistory);
//
//        // Dùng ArrayAdapter hoặc CustomAdapter để hiển thị dữ liệu
//        TrackerAdapter adapter = new TrackerAdapter(getContext(), trackerList);
//        listView.setAdapter(adapter);
    }



}