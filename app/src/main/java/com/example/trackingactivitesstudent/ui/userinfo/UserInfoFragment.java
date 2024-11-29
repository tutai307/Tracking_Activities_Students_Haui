package com.example.trackingactivitesstudent.ui.userinfo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModel;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trackingactivitesstudent.LoginActivity;
import com.example.trackingactivitesstudent.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Calendar;

public class UserInfoFragment extends Fragment {
    SQLiteDatabase database;
    private UserInfoViewModel mViewModel;
    private TextView nameTextView, studentCodeTextView, text_dob, text_gender, text_height, text_weight;
    private Button editButton, logoutButton;
    UserInfoViewModel userInfo = new UserInfoViewModel();
    String studentCode = "1";
    public static UserInfoFragment newInstance() {
        return new UserInfoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_user_info, container, false);

        // Tìm nút "Sửa" và thiết lập sự kiện nhấn
        editButton = root.findViewById(R.id.button_edit);
        editButton.setOnClickListener(v -> showBottomSheetDialog(userInfo));
        logoutButton = root.findViewById(R.id.button_logout);
        logoutButton.setOnClickListener(v -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(requireContext());
            dialog.setTitle("Đăng xuất");
            dialog.setMessage("Bạn có chắc chắn muốn đăng xuất?");
            dialog.setPositiveButton("Có", (dialogInterface, i) -> {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            });
            dialog.setNegativeButton("Không", (dialogInterface, i) -> {

            });
            dialog.show();
        });

        // Liên kết các thành phần UI
        nameTextView = root.findViewById(R.id.text_name);
        studentCodeTextView = root.findViewById(R.id.text_student_code);
        text_dob = root.findViewById(R.id.text_dob);
        text_gender = root.findViewById(R.id.text_gender);
        text_height = root.findViewById(R.id.text_height);
        text_weight = root.findViewById(R.id.text_weight);


        TextView viewMore = root.findViewById(R.id.viewMoreText);
        viewMore.setOnClickListener(view -> {
            NavController navController = NavHostFragment.findNavController(UserInfoFragment.this);
            navController.navigate(R.id.navigation_calendar);
        });

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        int studentId = sharedPreferences.getInt("studentId", -1);
        if(studentId != -1){
               studentCode = studentId + "";
        }
        database = openDatabase();
//        addStudent("2022000001", "Nguyễn Văn A", "password123", "avatar.jpg",
//                1.75f, 65f, "Không", 0, "01/01/2000");

        if (database != null) {
            // Tiến hành truy vấn
            // Lấy dữ liệu sinh viên và hiển thị
            fetchStudentDataByCode(studentCode);
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

    private void updateStudentInfo(String studentCode, String fullname, String dob, double height, double weight,
                                   String underlyingDisease, int gender) {
        // Mở cơ sở dữ liệu
        SQLiteDatabase db = openDatabase();

        // Tạo đối tượng ContentValues chứa các thông tin cần cập nhật
        ContentValues values = new ContentValues();
        values.put("fullname", fullname);
        values.put("dob", dob);
        values.put("height", height);
        values.put("weight", weight);
        values.put("underlying_disease", underlyingDisease);
        values.put("gender", gender);
        values.put("updated_at", System.currentTimeMillis()); // Cập nhật thời gian sửa

        // Cập nhật thông tin trong bảng "students"
        int rowsAffected = db.update("students", values, "student_code = ?", new String[]{studentCode});

        // Kiểm tra kết quả cập nhật
        if (rowsAffected > 0) {
            Toast.makeText(getContext(), "Cập nhật thông tin thành công!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Không thể cập nhật thông tin!", Toast.LENGTH_SHORT).show();
        }

        // Đóng cơ sở dữ liệu sau khi sử dụng
        db.close();
    }


    private void fetchStudentDataByCode(String studentCode) {
        // Truy vấn bảng students để lấy thông tin của sinh viên có student_code cụ thể
//        Toast.makeText(getContext(), "StudentId = " + studentCode, Toast.LENGTH_SHORT).show();
        Cursor cursor = database.query(
                "students",              // Tên bảng
                null,                     // Lấy tất cả các cột
                "id = ?",      // Điều kiện lọc theo student_code
                new String[]{studentCode},// Giá trị của student_code sẽ thay vào dấu chấm hỏi
                null,                     // Không cần nhóm dữ liệu
                null,                     // Không cần sắp xếp
                null                      // Không cần sắp xếp theo thứ tự
        );

        // Kiểm tra xem có sinh viên nào thỏa mãn điều kiện hay không
        if (cursor != null && cursor.moveToFirst()) {
            userInfo = new UserInfoViewModel(
                    cursor.getInt(cursor.getColumnIndexOrThrow("student_code")),
                    cursor.getString(cursor.getColumnIndexOrThrow("password")),
                    cursor.getString(cursor.getColumnIndexOrThrow("fullname")),
                    cursor.getString(cursor.getColumnIndexOrThrow("avatar")),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("height")),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("weight")),
                    cursor.getString(cursor.getColumnIndexOrThrow("underlying_disease")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("gender")),
                    cursor.getString(cursor.getColumnIndexOrThrow("dob"))
            );
            String gender = "Khác";
            if(userInfo.getGender() == 0){
                gender = "Nam";
            }else if(userInfo.getGender() == 1){
                gender = "Nữ";
            }
            nameTextView.setText(userInfo.getFullname() + "");
            studentCodeTextView.setText(userInfo.getStudentCode() + "");
            text_dob.setText(userInfo.getDob() + "");
            text_gender.setText(gender + "");
            text_height.setText(userInfo.getHeight() + "");
            text_weight.setText(userInfo.getWeight() + "");
        } else {
            // Nếu không tìm thấy sinh viên, hiển thị thông báo
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
    }

private void showBottomSheetDialog(UserInfoViewModel userInfo) {
    // Tạo Bottom Sheet Dialog
    BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
    View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_layout, null);
    bottomSheetDialog.setContentView(bottomSheetView);

    // Gán dữ liệu vào các View trong Bottom Sheet
    EditText studentCodeEditText = bottomSheetView.findViewById(R.id.studentCodeEditText);
    EditText fullnameEditText = bottomSheetView.findViewById(R.id.fullnameEditText);
    EditText birthDateEditText = bottomSheetView.findViewById(R.id.birthDateEditText);
    EditText heightEditText = bottomSheetView.findViewById(R.id.heightEditText);
    EditText weightEditText = bottomSheetView.findViewById(R.id.weightEditText);
    EditText underlyingDiseaseEditText = bottomSheetView.findViewById(R.id.underlyingDiseaseEditText);
    RadioGroup genderRadioGroup = bottomSheetView.findViewById(R.id.genderRadioGroup);

    // Gán dữ liệu từ UserInfoViewModel
    studentCodeEditText.setText(String.valueOf(userInfo.getStudentCode()));
    fullnameEditText.setText(userInfo.getFullname());
    birthDateEditText.setText(userInfo.getDob());
    heightEditText.setText(String.valueOf(userInfo.getHeight()));
    weightEditText.setText(String.valueOf(userInfo.getWeight()));
    underlyingDiseaseEditText.setText(userInfo.getUnderlyingDisease());

    // Đặt radio button cho giới tính
    if (userInfo.getGender() == 0) {
        ((RadioButton) genderRadioGroup.findViewById(R.id.radioButton)).setChecked(true);
    } else {
        ((RadioButton) genderRadioGroup.findViewById(R.id.radioButton1)).setChecked(true);
    }

    // Sự kiện nút đóng
    Button closeButton = bottomSheetView.findViewById(R.id.closeButton);
    closeButton.setOnClickListener(v -> bottomSheetDialog.dismiss());

    // Sự kiện nút đóng ở dưới cùng
    Button btnDong = bottomSheetView.findViewById(R.id.btnDong);
    btnDong.setOnClickListener(v -> bottomSheetDialog.dismiss());

    // Hiển thị DatePicker khi nhấn vào EditText ngày sinh
    birthDateEditText.setOnClickListener(v -> showDatePicker(birthDateEditText));

    Button btnEdit = bottomSheetView.findViewById(R.id.btnEdit);
    btnEdit.setOnClickListener(v -> {
        // Lấy dữ liệu từ các EditText
        String studentCode1 = studentCodeEditText.getText().toString().trim();
        String fullname = fullnameEditText.getText().toString().trim();
        String dob = birthDateEditText.getText().toString().trim();
        double height = 0;
        double weight = 0;
        String underlyingDisease = underlyingDiseaseEditText.getText().toString().trim();

        // Lấy giá trị giới tính từ radio group
        int gender = genderRadioGroup.getCheckedRadioButtonId() == R.id.radioButton ? 0 : 1;

        // Kiểm tra dữ liệu hợp lệ
        if (fullname.isEmpty() || dob.isEmpty()) {
            Toast.makeText(getContext(), "Họ tên và ngày sinh không được để trống!", Toast.LENGTH_SHORT).show();
            return;
        }
        try{
            height = Double.parseDouble(heightEditText.getText().toString().trim());
            weight = Double.parseDouble(weightEditText.getText().toString().trim());
        }catch (Exception e){
            Toast.makeText(getContext(), "Vui lòng nhập chiều cao và cân nặng đúng định dạng!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Cập nhật dữ liệu vào cơ sở dữ liệu
        updateStudentInfo(studentCode1, fullname, dob, height, weight, underlyingDisease, gender);

        fetchStudentDataByCode(studentCode);

        // Đóng BottomSheetDialog
        bottomSheetDialog.dismiss();
    });

    // Hiển thị Bottom Sheet
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

