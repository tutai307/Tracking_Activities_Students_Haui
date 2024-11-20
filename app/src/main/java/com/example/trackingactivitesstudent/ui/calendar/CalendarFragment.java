package com.example.trackingactivitesstudent.ui.calendar;

import androidx.lifecycle.ViewModelProvider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.trackingactivitesstudent.R;
import com.example.trackingactivitesstudent.ui.userinfo.UserInfoFragment;

import java.util.ArrayList;
import java.util.Calendar;

public class CalendarFragment extends Fragment {
    private SQLiteDatabase database;
    CalendarAdapter adapter = null;
    ArrayList<CalendarViewModel> arrClass = new ArrayList<>();
    ListView listView;
    LinearLayout t2Layout, t3Layout, t4Layout, t5Layout, t6Layout, t7Layout, cnLayout;
    private CalendarViewModel mViewModel;
    // Lấy ngày hôm nay
    Calendar calendar = Calendar.getInstance();
    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
    private String selectedDay = "";

    public static CalendarFragment newInstance() {
        return new CalendarFragment();
    }


    private SQLiteDatabase openDatabase() {
        // Lấy context của ứng dụng
        Context context = requireContext();

        // Mở hoặc tạo cơ sở dữ liệu nếu chưa có
        SQLiteDatabase db = context.openOrCreateDatabase("qlsv.db", Context.MODE_PRIVATE, null);

        return db;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate layout cho Fragment
        View root = inflater.inflate(R.layout.fragment_calendar, container, false);
        // Tìm các LinearLayout của ngày trong activity
        t2Layout = root.findViewById(R.id.t2);
        t3Layout = root.findViewById(R.id.t3);
        t4Layout = root.findViewById(R.id.t4);
        t5Layout = root.findViewById(R.id.t5);
        t6Layout = root.findViewById(R.id.t6);
        t7Layout = root.findViewById(R.id.t7);
        cnLayout = root.findViewById(R.id.cn);
        listView = root.findViewById(R.id.listView);
        adapter = new CalendarAdapter(getContext(), R.layout.calendar_item_list ,arrClass);
        listView.setAdapter(adapter);

        // Tạo mảng LinearLayout để dễ dàng lặp qua
        LinearLayout[] allLayouts = {t2Layout, t3Layout, t4Layout, t5Layout, t6Layout, t7Layout, cnLayout};

        // Thiết lập sự kiện click cho các ngày
        t2Layout.setOnClickListener(v -> selectDay(t2Layout, allLayouts));
        t3Layout.setOnClickListener(v -> selectDay(t3Layout, allLayouts));
        t4Layout.setOnClickListener(v -> selectDay(t4Layout, allLayouts));
        t5Layout.setOnClickListener(v -> selectDay(t5Layout, allLayouts));
        t6Layout.setOnClickListener(v -> selectDay(t6Layout, allLayouts));
        t7Layout.setOnClickListener(v -> selectDay(t7Layout, allLayouts));
        cnLayout.setOnClickListener(v -> selectDay(cnLayout, allLayouts));

        selectToday(allLayouts, dayOfWeek);

        database = openDatabase();
//        addInstructors();
//        addCourses();
//        addClasses();
        database.close();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

//    private void loadClassesFromDatabase() {
//        ArrayList<CalendarViewModel> classList = new ArrayList<>();
//
//        // Mở cơ sở dữ liệu
//        SQLiteDatabase database = openDatabase();
//
//        // Truy vấn các lớp học
//        Cursor cursor = database.query("classes", new String[]{"id", "class_code", "days_in_week", "time_in_day", "course_id", "instructor_id"},
//                null, null, null, null, null);
//
//        if (cursor != null && cursor.moveToFirst()) {
//            do {
//                // Lấy chỉ số của các cột
//                int classIdIndex = cursor.getColumnIndex("id");
//                int classCodeIndex = cursor.getColumnIndex("class_code");
//                int daysInWeekIndex = cursor.getColumnIndex("days_in_week");
//                int timeInDayIndex = cursor.getColumnIndex("time_in_day");
//                int courseIdIndex = cursor.getColumnIndex("course_id");
//                int instructorIdIndex = cursor.getColumnIndex("instructor_id");
//
//                // Kiểm tra các chỉ số cột hợp lệ
//                if (classIdIndex >= 0 && classCodeIndex >= 0 && daysInWeekIndex >= 0 && timeInDayIndex >= 0 && courseIdIndex >= 0 && instructorIdIndex >= 0) {
//                    int classId = cursor.getInt(classIdIndex);
//                    String classCode = cursor.getString(classCodeIndex);
//                    String daysInWeek = cursor.getString(daysInWeekIndex);
//                    String timeInDay = cursor.getString(timeInDayIndex);
//                    int courseId = cursor.getInt(courseIdIndex);
//                    int instructorId = cursor.getInt(instructorIdIndex);
//
//                    // Lấy thông tin khóa học
//                    Cursor courseCursor = database.query("courses", new String[]{"course_name"},
//                            "id = ?", new String[]{String.valueOf(courseId)}, null, null, null);
//
//                    String courseName = "";
//                    if (courseCursor != null && courseCursor.moveToFirst()) {
//                        int courseNameIndex = courseCursor.getColumnIndex("course_name");
//                        if (courseNameIndex >= 0) {
//                            courseName = courseCursor.getString(courseNameIndex);
//                        }
//                    }
//                    if (courseCursor != null) {
//                        courseCursor.close();
//                    }
//
//                    // Lấy thông tin giảng viên
//                    Cursor instructorCursor = database.query("instructors", new String[]{"fullname"},
//                            "id = ?", new String[]{String.valueOf(instructorId)}, null, null, null);
//
//                    String instructorName = "";
//                    if (instructorCursor != null && instructorCursor.moveToFirst()) {
//                        int instructorNameIndex = instructorCursor.getColumnIndex("fullname");
//                        if (instructorNameIndex >= 0) {
//                            instructorName = instructorCursor.getString(instructorNameIndex);
//                        }
//                    }
//                    if (instructorCursor != null) {
//                        instructorCursor.close();
//                    }
//
//                    // Tạo đối tượng CalendarViewModel và thêm vào danh sách
//                    CalendarViewModel calendarViewModel = new CalendarViewModel(classCode, daysInWeek, timeInDay, courseName, instructorName);
//                    classList.add(calendarViewModel);
//                }
//            } while (cursor.moveToNext());
//        }
//        if (cursor != null) {
//            cursor.close();
//        }
//
//        // Cập nhật adapter với danh sách lớp học đã lấy được
//        arrClass.clear();
//        arrClass.addAll(classList);
//        adapter.notifyDataSetChanged();
//
//        // Đóng cơ sở dữ liệu sau khi hoàn tất
//        database.close();
//    }

    private void loadClassesFromDatabase() {
        ArrayList<CalendarViewModel> classList = new ArrayList<>();

        // Mở cơ sở dữ liệu
        SQLiteDatabase database = openDatabase();

        // Truy vấn các lớp học
        Cursor cursor = database.query("classes", new String[]{"id", "class_code", "days_in_week", "time_in_day", "course_id", "instructor_id"},
                null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Lấy chỉ số của các cột
                int classIdIndex = cursor.getColumnIndex("id");
                int classCodeIndex = cursor.getColumnIndex("class_code");
                int daysInWeekIndex = cursor.getColumnIndex("days_in_week");
                int timeInDayIndex = cursor.getColumnIndex("time_in_day");
                int courseIdIndex = cursor.getColumnIndex("course_id");
                int instructorIdIndex = cursor.getColumnIndex("instructor_id");

                // Kiểm tra các chỉ số cột hợp lệ
                if (classIdIndex >= 0 && classCodeIndex >= 0 && daysInWeekIndex >= 0 && timeInDayIndex >= 0 && courseIdIndex >= 0 && instructorIdIndex >= 0) {
                    int classId = cursor.getInt(classIdIndex);
                    String classCode = cursor.getString(classCodeIndex);
                    String daysInWeek = cursor.getString(daysInWeekIndex);
                    String timeInDay = cursor.getString(timeInDayIndex);
                    int courseId = cursor.getInt(courseIdIndex);
                    int instructorId = cursor.getInt(instructorIdIndex);

                    // Kiểm tra xem lớp học có chứa ngày được chọn không
                    if (daysInWeek.contains(selectedDay)) {
                        // Lấy thông tin khóa học
                        Cursor courseCursor = database.query("courses", new String[]{"course_name"},
                                "id = ?", new String[]{String.valueOf(courseId)}, null, null, null);

                        String courseName = "";
                        if (courseCursor != null && courseCursor.moveToFirst()) {
                            int courseNameIndex = courseCursor.getColumnIndex("course_name");
                            if (courseNameIndex >= 0) {
                                courseName = courseCursor.getString(courseNameIndex);
                            }
                        }
                        if (courseCursor != null) {
                            courseCursor.close();
                        }

                        // Lấy thông tin giảng viên
                        Cursor instructorCursor = database.query("instructors", new String[]{"fullname"},
                                "id = ?", new String[]{String.valueOf(instructorId)}, null, null, null);

                        String instructorName = "";
                        if (instructorCursor != null && instructorCursor.moveToFirst()) {
                            int instructorNameIndex = instructorCursor.getColumnIndex("fullname");
                            if (instructorNameIndex >= 0) {
                                instructorName = instructorCursor.getString(instructorNameIndex);
                            }
                        }
                        if (instructorCursor != null) {
                            instructorCursor.close();
                        }

                        // Tạo đối tượng CalendarViewModel và thêm vào danh sách
                        CalendarViewModel calendarViewModel = new CalendarViewModel(classCode, daysInWeek, timeInDay, courseName, instructorName);
                        classList.add(calendarViewModel);
                    }
                }
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }

        // Cập nhật adapter với danh sách lớp học đã lấy được
        arrClass.clear();
        arrClass.addAll(classList);
        adapter.notifyDataSetChanged();

        // Đóng cơ sở dữ liệu sau khi hoàn tất
        database.close();
    }



    // Hàm để thay đổi background khi chọn ngày
//    private void selectDay(LinearLayout selectedLayout, LinearLayout[] allLayouts) {
//        // Đặt background cho ngày được chọn
//        selectedLayout.setBackgroundResource(R.drawable.rounded_green_background);
//
//        // Reset background cho các ngày còn lại
//        for (LinearLayout layout : allLayouts) {
//            if (layout != selectedLayout) {
//                layout.setBackgroundResource(android.R.color.white); // Hoặc màu trắng, hoặc trạng thái mặc định
//            }
//        }
//    }
    private void selectDay(LinearLayout selectedLayout, LinearLayout[] allLayouts) {
        // Đặt background cho ngày được chọn
        selectedLayout.setBackgroundResource(R.drawable.rounded_green_background);

        // Reset background cho các ngày còn lại
        for (LinearLayout layout : allLayouts) {
            if (layout != selectedLayout) {
                layout.setBackgroundResource(android.R.color.white); // Hoặc màu trắng, hoặc trạng thái mặc định
            }
        }

        // Lưu trữ ngày được chọn bằng if-else
        if (selectedLayout.getId() == R.id.t2) {
            selectedDay = "Monday";
        } else if (selectedLayout.getId() == R.id.t3) {
            selectedDay = "Tuesday";
        } else if (selectedLayout.getId() == R.id.t4) {
            selectedDay = "Wednesday";
        } else if (selectedLayout.getId() == R.id.t5) {
            selectedDay = "Thursday";
        } else if (selectedLayout.getId() == R.id.t6) {
            selectedDay = "Friday";
        } else if (selectedLayout.getId() == R.id.t7) {
            selectedDay = "Saturday";
        } else if (selectedLayout.getId() == R.id.cn) {
            selectedDay = "Sunday";
        }

        // Gọi lại hàm loadClassesFromDatabase để lọc các lớp học theo ngày
        loadClassesFromDatabase();
    }


    // Hàm để chọn ngày hôm nay
    private void selectToday(LinearLayout[] allLayouts, int dayOfWeek) {


        // Sắp xếp các ngày theo ngày trong tuần: 1 = Chủ nhật, 2 = Thứ hai, ... 7 = Thứ bảy
        // Lưu ý: Calendar sử dụng Sunday = 1, Monday = 2, ..., Saturday = 7
        LinearLayout selectedLayout = null;
        switch (dayOfWeek) {
            case Calendar.MONDAY:
                selectedLayout = t2Layout;
                break;
            case Calendar.TUESDAY:
                selectedLayout = t3Layout;
                break;
            case Calendar.WEDNESDAY:
                selectedLayout = t4Layout;
                break;
            case Calendar.THURSDAY:
                selectedLayout = t5Layout;
                break;
            case Calendar.FRIDAY:
                selectedLayout = t6Layout;
                break;
            case Calendar.SATURDAY:
                selectedLayout = t7Layout;
                break;
            case Calendar.SUNDAY:
                selectedLayout = cnLayout;
                break;
        }
        Toast.makeText(getContext(), "Có." + dayOfWeek, Toast.LENGTH_SHORT).show();
        // Nếu có ngày tương ứng, gọi hàm selectDay để đánh dấu ngày hôm nay
        if (selectedLayout != null) {
            selectDay(selectedLayout, allLayouts);
        }
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
//        values.put("class_code", "CS101-01");
//        values.put("days_in_week", "Monday, Wednesday");
//        values.put("time_in_day", "10:00 AM - 12:00 PM");
//        values.put("started_at", "2024-11-21");
//        values.put("finished_at", "2024-12-21");
//        values.put("course_id", 1);  // Link to course ID
//        values.put("instructor_id", 1);  // Link to instructor ID
//        database.insert("classes", null, values);

//        values.put("class_code", "ENG202-01");
//        values.put("days_in_week", "Tuesday, Thursday");
//        values.put("time_in_day", "02:00 PM - 04:00 PM");
//        values.put("started_at", "2024-11-21");
//        values.put("finished_at", "2024-12-21");
//        values.put("course_id", 2);  // Link to course ID
//        values.put("instructor_id", 2);  // Link to instructor ID
//        database.insert("classes", null, values);

        Toast.makeText(getContext(), "Thêm lớp học thành công", Toast.LENGTH_SHORT).show();
    }
}