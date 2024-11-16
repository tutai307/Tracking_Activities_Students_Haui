package com.example.trackingactivitesstudent.ui.userinfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModel;

import android.app.DatePickerDialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trackingactivitesstudent.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Calendar;

public class UserInfoFragment extends Fragment {
    SQLiteDatabase database;
    private UserInfoViewModel mViewModel;
    private TextView nameTextView, studentCodeTextView, departmentTextView;
    private Button editButton;

    public static UserInfoFragment newInstance() {
        return new UserInfoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_user_info, container, false);

        // Tìm nút "Sửa" và thiết lập sự kiện nhấn
        editButton = root.findViewById(R.id.button_edit);
        editButton.setOnClickListener(v -> showBottomSheetDialog());

        // Liên kết các thành phần UI
        nameTextView = root.findViewById(R.id.text_name);
        studentCodeTextView = root.findViewById(R.id.text_student_code);
        departmentTextView = root.findViewById(R.id.text_department);


        TextView viewMore = root.findViewById(R.id.viewMoreText);
        viewMore.setOnClickListener(view -> {
            NavController navController = NavHostFragment.findNavController(UserInfoFragment.this);
            navController.navigate(R.id.navigation_calendar);
        });
        database = openDatabase();
//        addStudent("2022000001", "Nguyễn Văn A", "password123", "avatar.jpg",
//                1.75f, 65f, "Không", 0, "01/01/2000");

        if (database != null) {
            // Tiến hành truy vấn
            // Lấy dữ liệu sinh viên và hiển thị
            fetchStudentDataByCode("2022000001");
        } else {
            Toast.makeText(getContext(), "Không thể kết nối đến cơ sở dữ liệu.", Toast.LENGTH_SHORT).show();
        }


        return root;
    }

    private SQLiteDatabase openDatabase() {
        // Lấy context của ứng dụng
        Context context = requireContext();

        // Mở hoặc tạo cơ sở dữ liệu nếu chưa có
        SQLiteDatabase db = context.openOrCreateDatabase("qlsv.db", Context.MODE_PRIVATE, null);

        return db;
    }

    private void addStudent(String studentCode, String fullName, String password, String avatar,
                            float height, float weight, String underlyingDisease, int gender,
                            String dob) {
        // Mở cơ sở dữ liệu
        SQLiteDatabase database = openDatabase();

        // Kiểm tra xem mã sinh viên đã tồn tại trong cơ sở dữ liệu chưa
        Cursor cursor = database.query("students", new String[]{"student_code"},
                "student_code = ?", new String[]{studentCode},
                null, null, null);

        // Nếu có dữ liệu trả về từ truy vấn, tức là sinh viên với mã này đã tồn tại
        if (cursor != null && cursor.getCount() > 0) {
            Toast.makeText(getContext(), "Mã sinh viên này đã tồn tại!", Toast.LENGTH_SHORT).show();
            cursor.close();  // Đóng cursor sau khi sử dụng
            database.close();  // Đóng cơ sở dữ liệu
            return;  // Dừng lại và không thêm sinh viên
        }

        // Nếu mã sinh viên chưa tồn tại, tiến hành thêm sinh viên mới
        ContentValues values = new ContentValues();
        values.put("student_code", studentCode);
        values.put("fullname", fullName);
        values.put("password", password);
        values.put("avatar", avatar);
        values.put("height", height);
        values.put("weight", weight);
        values.put("underlying_disease", underlyingDisease);
        values.put("gender", gender);
        values.put("dob", dob);
        values.put("created_at", System.currentTimeMillis());
        values.put("updated_at", System.currentTimeMillis());

        // Chèn dữ liệu vào bảng
        long result = database.insert("students", null, values);

        // Kiểm tra kết quả chèn dữ liệu
        if (result != -1) {
            Toast.makeText(getContext(), "Thêm sinh viên thành công!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Có lỗi xảy ra khi thêm sinh viên.", Toast.LENGTH_SHORT).show();
        }

        // Đóng cursor và cơ sở dữ liệu sau khi sử dụng
        cursor.close();
        database.close();
    }



    private void fetchStudentDataByCode(String studentCode) {
        // Truy vấn bảng students để lấy thông tin của sinh viên có student_code cụ thể
        Cursor cursor = database.query(
                "students",              // Tên bảng
                null,                     // Lấy tất cả các cột
                "student_code = ?",      // Điều kiện lọc theo student_code
                new String[]{studentCode},// Giá trị của student_code sẽ thay vào dấu chấm hỏi
                null,                     // Không cần nhóm dữ liệu
                null,                     // Không cần sắp xếp
                null                      // Không cần sắp xếp theo thứ tự
        );

        // Kiểm tra xem có sinh viên nào thỏa mãn điều kiện hay không
        if (cursor != null && cursor.moveToFirst()) {
            // Lấy chỉ số cột từ Cursor
            int studentCodeIndex = cursor.getColumnIndex("student_code");
            int fullNameIndex = cursor.getColumnIndex("fullname");
            int genderIndex = cursor.getColumnIndex("gender");
            int dobIndex = cursor.getColumnIndex("dob");
            int heightIndex = cursor.getColumnIndex("height");
            int weightIndex = cursor.getColumnIndex("weight");

            // Lấy dữ liệu từ cursor
            if (studentCodeIndex != -1 && fullNameIndex != -1 && genderIndex != -1 && dobIndex != -1) {
                nameTextView.setText(cursor.getString(fullNameIndex));
                studentCodeTextView.setText(cursor.getString(studentCodeIndex));
                Toast.makeText(getContext(), "Tìm thấy và có chạy!", Toast.LENGTH_SHORT).show();
//
//                // Đặt dữ liệu vào TextView để hiển thị
//                studentDataTextView.setText(data.toString());
            } else {
//                studentDataTextView.setText("Dữ liệu không hợp lệ.");
                Toast.makeText(getContext(), "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Nếu không tìm thấy sinh viên, hiển thị thông báo
//            studentDataTextView.setText("Không tìm thấy sinh viên với mã này.");
            Toast.makeText(getContext(), "Không tìm thấy sinh viên!", Toast.LENGTH_SHORT).show();
        }

        // Đóng cursor sau khi sử dụng
        if (cursor != null) {
            cursor.close();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(UserInfoViewModel.class);
        // Giả sử bạn có thể lấy dữ liệu từ ViewModel và gán cho các TextView

//        nameTextView.setText("Chạy");
//        studentCodeTextView.setText("2021604405");
//        departmentTextView.setText("Khoa: Khoa Công nghệ thông tin");
    }

    private void showBottomSheetDialog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_layout, null);
        bottomSheetDialog.setContentView(bottomSheetView);

        // Tìm ImageView của nút đóng và thiết lập sự kiện OnClick
        Button closeButton = bottomSheetView.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(v -> bottomSheetDialog.dismiss());
        Button btnDong = bottomSheetView.findViewById(R.id.btnDong);
        btnDong.setOnClickListener(v -> bottomSheetDialog.dismiss());

        // Khởi tạo EditText cho ngày sinh
        EditText birthDateEditText = bottomSheetView.findViewById(R.id.birthDateEditText);
        birthDateEditText.setOnClickListener(v -> showDatePicker(birthDateEditText));

        bottomSheetDialog.show();
    }

    private void showDatePicker(EditText editText) {
        // Lấy ngày hiện tại
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Tạo DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Định dạng ngày thành chuỗi
                    String formattedDate = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear);
                    editText.setText(formattedDate); // Gán ngày chọn vào EditText
                }, year, month, day);

        datePickerDialog.show(); // Hiển thị DatePicker
    }
}
