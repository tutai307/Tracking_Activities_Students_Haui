package com.example.trackingactivitesstudent.ui.Physical_list;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.trackingactivitesstudent.Database.DatabaseHelper;
import com.example.trackingactivitesstudent.R;

import java.util.ArrayList;
import java.util.List;

public class Physical_listFragment extends Fragment {

    private ListView lvDShocphanTC;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_physical_list, container, false);

        lvDShocphanTC = rootView.findViewById(R.id.lvDShocphanTC);

        // Hiển thị danh sách các môn học tc
        loadPhysicalCourses();

        return rootView;
    }

    private void loadPhysicalCourses() {
        DatabaseHelper dbHelper = new DatabaseHelper(requireContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Truy vấn các môn học có is_physical = 1
        String query = "SELECT course_name, course_code FROM courses WHERE is_physical = 1";
        Cursor cursor = db.rawQuery(query, null);

        // Chuẩn bị danh sách dữ liệu cho adapter
        ArrayList<String[]> courseList = new ArrayList<>();
        while (cursor.moveToNext()) {
            String courseName = cursor.getString(cursor.getColumnIndex("course_name"));
            String courseCode = cursor.getString(cursor.getColumnIndex("course_code"));
            courseList.add(new String[]{courseName, courseCode, "1.0"}); // Thêm dữ liệu vào danh sách
        }
        cursor.close();
        db.close();

        // Tạo custom adapter
        CourseAdapter adapter = new CourseAdapter(requireContext(), R.layout.item_list_physical, courseList);
        lvDShocphanTC.setAdapter(adapter);
    }

    // Custom Adapter cho ListView
    public class CourseAdapter extends ArrayAdapter<String[]> {

        private Context context;
        private int resource;
        private List<String[]> courses;

        public CourseAdapter(@NonNull Context context, int resource, @NonNull List<String[]> courses) {
            super(context, resource, courses);
            this.context = context;
            this.resource = resource;
            this.courses = courses;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(resource, parent, false);
            }

            String[] course = courses.get(position);

            TextView courseNameTextView = convertView.findViewById(R.id.course_name);
            TextView courseCodeTextView = convertView.findViewById(R.id.course_code);
            TextView sotin = convertView.findViewById(R.id.sotin);

            // Gán giá trị vào các TextView
            courseNameTextView.setText(course[0]); // Tên môn học
            courseCodeTextView.setText(course[1]); // Mã môn học
            sotin.setText("Số tín: " + course[2]); // Số tín

            return convertView;
        }
    }
}
