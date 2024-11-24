package com.example.trackingactivitesstudent.ui.health;

import static android.database.sqlite.SQLiteDatabase.openDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.trackingactivitesstudent.Database.DatabaseHelper;
import com.example.trackingactivitesstudent.R;
import com.example.trackingactivitesstudent.databinding.FragmentHealthBinding;
import com.example.trackingactivitesstudent.ui.calendar.CalendarViewModel;
import com.example.trackingactivitesstudent.ui.userinfo.UserInfoViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HealthFragment extends Fragment {
    SQLiteDatabase database;
    private HealthViewModel mViewModel;
    private TextView txtHello;
    private Spinner spnTinhTrang;
    private EditText edtGhiChu;
    private Button btnLuu;
    private ListView lv_SucKhoe;
    HealthViewModel healthViewModel = new HealthViewModel();
    HealthAdapter adapter = null;
    ArrayList<HealthViewModel> arrClass = new ArrayList<>();
    ArrayAdapter<String> adapterSpinner;

    public static HealthFragment newInstance() {
        return new HealthFragment();
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_health, container, false);
        txtHello = root.findViewById(R.id.txtHello);
        spnTinhTrang = root.findViewById(R.id.spnTinhTrang);
        edtGhiChu = root.findViewById(R.id.edtGhiChu);
        btnLuu = root.findViewById(R.id.btnLuu);
        lv_SucKhoe = root.findViewById(R.id.lv_SucKhoe);

        List<String> list = new ArrayList<>();
        list.add("Bình thường");
        list.add("Ốm");
        adapterSpinner = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, list);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTinhTrang.setAdapter(adapterSpinner);
        adapter = new HealthAdapter(getContext(), R.layout.health_item_list, arrClass);
        lv_SucKhoe.setAdapter(adapter);

        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tinhTrang = spnTinhTrang.getSelectedItem().toString();
                String ghiChu = edtGhiChu.getText().toString();
                updateFormData(tinhTrang, ghiChu);
                loadClassesFromDatabase();
                adapter.notifyDataSetChanged();
            }
        });

        database = openDatabase();

//        addCourses();
//        addInstructors();
//        addClasses();
//        addDease();
        loadClassesFromDatabase();
//        database.close();
        return root;
    }

    //Hàm mở db và lấy dữ liệu
    private SQLiteDatabase openDatabase() {
        // Lấy context của ứng dụng
        Context context = requireContext();
        // Mở hoặc tạo cơ sở dữ liệu nếu chưa có
        SQLiteDatabase db = context.openOrCreateDatabase("qlsv.db", Context.MODE_PRIVATE, null);
        return db;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(HealthViewModel.class);
    }

    //Hàm cập nhật dữ liệu
    private void loadClassesFromDatabase() {
        ArrayList<HealthViewModel> classList = new ArrayList<>();
        arrClass.clear();

        // Mở cơ sở dữ liệu
        SQLiteDatabase database = openDatabase();
        // Lấy ngày hiện tại
        String today = getDayOfWeek();

        // Truy vấn bảng classes với điều kiện LIKE
        Cursor cursor = database.query(
                "classes", // Tên bảng
                new String[]{"class_code", "time_in_day", "course_id", "instructor_id", "days_in_week"}, // Các cột muốn lấy
                "days_in_week LIKE ?", // Điều kiện WHERE
                new String[]{"%" + today + "%"}, // Truyền ngày hiện tại vào điều kiện LIKE
                null, // GROUP BY
                null, // HAVING
                null // ORDER BY
        );


        if (cursor != null) {
            while (cursor.moveToNext()) {
                String classCode = cursor.getString(cursor.getColumnIndexOrThrow("class_code"));
                String days = cursor.getString(cursor.getColumnIndexOrThrow("days_in_week"));
                String time = cursor.getString(cursor.getColumnIndexOrThrow("time_in_day"));
//                String created_at = cursor1.getString(cursor.getColumnIndexOrThrow("created_at"));
//                String disease = cursor1.getString(cursor.getColumnIndexOrThrow("disease"));
                int courseId = cursor.getInt(cursor.getColumnIndexOrThrow("course_id"));
                int instructorId = cursor.getInt(cursor.getColumnIndexOrThrow("instructor_id"));
                String tinhtrang = "Bình thường";
                String disease = "Bình thường";
                // Truy vấn bảng healths để lấy thông tin bệnh của sinh viên
                Cursor cursor1 = database.query("healths", new String[]{"created_at", "disease"}, null, null, null, null, null);
                if (cursor1 != null && cursor1.moveToFirst()) {
                    // Lấy dữ liệu bệnh từ bảng healths
                    tinhtrang = cursor1.getString(cursor1.getColumnIndexOrThrow("created_at"));
                     disease = cursor1.getString(cursor1.getColumnIndexOrThrow("disease"));
                    cursor1.close();
                }

                // Lấy tên khóa học từ bảng courses
                String courseName = getCourseName(courseId, database);
                // Lấy tên giáo viên từ bảng instructors
                String instructorName = getInstructorName(instructorId, database);

                // Thêm vào danh sách lớp học
                arrClass.add(new HealthViewModel(tinhtrang, disease, courseName, instructorName, time));
            }
            cursor.close();
        }
        adapter.notifyDataSetChanged();
    }

    //Hàm sửa thông tin
    private void updateFormData(String tinhTrang, String ghiChu){
        // Mở cơ sở dữ liệu
        SQLiteDatabase db = openDatabase();

        // Tạo đối tượng ContentValues chứa các thông tin cần cập nhật
        ContentValues values = new ContentValues();
        values.put("created_at", tinhTrang);
        values.put("disease", ghiChu);
        int rowsAffected = db.update("healths", values, "id = ?", new String[]{"1"});

        // Kiểm tra kết quả cập nhật
        if (rowsAffected > 0) {
            Toast.makeText(getContext(), "Cập nhật thông tin thành công!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Không thể cập nhật thông tin!", Toast.LENGTH_SHORT).show();
        }

        // Đóng cơ sở dữ liệu sau khi sử dụng
        db.close();
    }
//private void loadClassesFromDatabase() {
//    ArrayList<HealthViewModel> classList = new ArrayList<>();
//    arrClass.clear();
//
//    // Mở cơ sở dữ liệu
//    SQLiteDatabase database = openDatabase();
//    // Lấy ngày hiện tại
//    String today = getDayOfWeek();
//
//    // Truy vấn bảng classes với điều kiện LIKE
//    Cursor cursor = database.query(
//            "classes", // Tên bảng
//            new String[]{"class_code", "time_in_day", "course_id", "instructor_id", "days_in_week", "student_id"}, // Các cột muốn lấy
//            "days_in_week LIKE ?", // Điều kiện WHERE
//            new String[]{"%" + today + "%"}, // Truyền ngày hiện tại vào điều kiện LIKE
//            null, // GROUP BY
//            null, // HAVING
//            null // ORDER BY
//    );
//
//    if (cursor != null) {
//        while (cursor.moveToNext()) {
//            int dayMo = cursor.getInt(cursor.getColumnIndexOrThrow("days_in_week"));
//            int timeMo = cursor.getInt(cursor.getColumnIndexOrThrow("time_in_day"));
//            int courseIdMo = cursor.getInt(cursor.getColumnIndexOrThrow("course_id"));
//            int instructorIdMo = cursor.getInt(cursor.getColumnIndexOrThrow("instructor_id"));
//
//            if(dayMo >= 0 && timeMo >= 0 && courseIdMo >= 0 && instructorIdMo >= 0){
//                String days = cursor.getString(cursor.getColumnIndexOrThrow("days_in_week"));
//                String time = cursor.getString(cursor.getColumnIndexOrThrow("time_in_day"));
//                int courseId = cursor.getInt(cursor.getColumnIndexOrThrow("course_id"));
//                int instructorId = cursor.getInt(cursor.getColumnIndexOrThrow("instructor_id"));
//
//                String disease = "Bình thường"; // Mặc định bệnh là bình thường nếu không tìm thấy
//                String created_at = "Bình thường"; // Mặc định bệnh là bình thường nếu không tìm thấy
//
////            if (cursor1 != null && cursor1.moveToFirst()) {
////                // Lấy dữ liệu bệnh từ bảng healths
////                created_at = cursor1.getString(cursor1.getColumnIndexOrThrow("created_at"));
////                disease = cursor1.getString(cursor1.getColumnIndexOrThrow("disease"));
////                cursor1.close();
////            }
//
//                // Lấy tên khóa học từ bảng courses
//                String courseName = getCourseName(courseId, database);
//                // Lấy tên giáo viên từ bảng instructors
//                String instructorName = getInstructorName(instructorId, database);
//
//                // Thêm vào danh sách lớp học
//                arrClass.add(new HealthViewModel(created_at, disease, courseName, instructorName, time));
//            }
////            // Truy vấn bảng healths để lấy thông tin bệnh của sinh viên
////            Cursor cursor1 = database.query(
////                    "healths", // Tên bảng healths
////                    new String[]{"created_at", "disease"}, // Các cột muốn lấy
////                    "student_id = ?", // Điều kiện WHERE
////                    new String[]{String.valueOf(studentId)}, // Truyền student_id vào điều kiện WHERE
////                    null, // GROUP BY
////                    null, // HAVING
////                    null // ORDER BY
////            );
//
//
//        }
//        cursor.close();
//    }
//    adapter.notifyDataSetChanged();
//}

    //Hàm thêm bệnh
    private void addDease(){
        ContentValues values = new ContentValues();
        values.put("disease", "Bệnh tim");
        values.put("created_at", "Ốm");
        values.put("updated_at", "2024-11-20");
        database.insert("healths", null, values);
        Toast.makeText(getContext(), "Thêm bệnh thành công", Toast.LENGTH_SHORT).show();
    }
    // Hàm để lấy ngày hiện tại
    private String getDayOfWeek() {
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1; // Lấy giá trị ngày trong tuần (1 = Chủ nhật, 2 = Thứ Hai, ...)
        String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        return days[dayOfWeek - 1]; // Trả về tên ngày tương ứng
    }

    // Hàm lấy tên môn học
    private String getCourseName(int courseId, SQLiteDatabase database) {
        Cursor cursor = database.query(
                "courses",
                new String[]{"course_name"},
                "id = ?", // Điều kiện WHERE
                new String[]{String.valueOf(courseId)}, // Giá trị điều kiện WHERE
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            String courseName = cursor.getString(cursor.getColumnIndexOrThrow("course_name"));
            cursor.close();
            return courseName;
        }

        return "Unknown Course";
    }
    //hàm lấy tên giảng viên
    private String getInstructorName(int instructorId, SQLiteDatabase database) {
        Cursor cursor = database.query(
                "instructors",
                new String[]{"fullname"},
                "id = ?", // Điều kiện WHERE
                new String[]{String.valueOf(instructorId)}, // Giá trị điều kiện WHERE
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            String instructorName = cursor.getString(cursor.getColumnIndexOrThrow("fullname"));
            cursor.close();
            return instructorName;
        }

        return "Unknown Instructor";
    }

    // Hàm thêm giáo viên vào bảng instructors
    private void addInstructors() {
        ContentValues values = new ContentValues();
        values.put("instructor_code", 1);
        values.put("fullname", "Giáo viên 1");
        values.put("created_at", "2024-11-20");
        values.put("updated_at", "2024-11-20");
        database.insert("instructors", null, values);

        values.put("instructor_code", 2);
        values.put("fullname", "Giáo viên 2");
        database.insert("instructors", null, values);

        Toast.makeText(getContext(), "Thêm giáo viên thành công", Toast.LENGTH_SHORT).show();
    }

    // Hàm thêm khóa học vào bảng courses
    private void addCourses() {
        ContentValues values = new ContentValues();
//        values.put("course_code", "CS101");
//        values.put("course_name", "Khoa học máy tính");
//        values.put("is_physical", 1);  // 1 = Physical class
//        values.put("credits", 3.0);
//        database.insert("courses", null, values);

        values.put("course_code", "ENG202");
        values.put("course_name", "Phát triển ứng dụng trên thiết bị di động");
        values.put("is_physical", 0);  // 0 = Online class
        values.put("credits", 2.0);
        database.insert("courses", null, values);

//        values.put("course_code", "ENG202");
//        values.put("course_name", "Tiếng Anh");
//        values.put("is_physical", 0);  // 0 = Online class
//        values.put("credits", 2.0);
//        database.insert("courses", null, values);

        Toast.makeText(getContext(), "Thêm khóa học thành công", Toast.LENGTH_SHORT).show();
    }

    // Hàm thêm lớp học vào bảng classes
    private void addClasses() {
        ContentValues values = new ContentValues();
        values.put("class_code", "CS301-01");
        values.put("days_in_week", "Monday, Wednesday, Saturday");
        values.put("time_in_day", "01:00 PM - 02:00 PM");
        values.put("started_at", "2024-11-21");
        values.put("finished_at", "2024-12-21");
        values.put("course_id", 3);  // Link to course ID
        values.put("instructor_id", 1);  // Link to instructor ID
        database.insert("classes", null, values);

        Toast.makeText(getContext(), "Thêm lớp học thành công", Toast.LENGTH_SHORT).show();
    }

}