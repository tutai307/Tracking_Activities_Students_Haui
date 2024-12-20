package com.example.trackingactivitesstudent;

import static androidx.core.content.ContentProviderCompat.requireContext;
import static java.security.AccessController.getContext;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trackingactivitesstudent.Database.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {

    private EditText edtStudentCode, edtPassword;
    private Button btnLogin;
    private TextView txtError;
    private SQLiteDatabase database;
    private boolean isPasswordVisible = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        // Ánh xạ view
        edtStudentCode = findViewById(R.id.edtStudentCode);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        txtError = findViewById(R.id.txtError);

        TextView txtForgotPassword = findViewById(R.id.txtForgotPassword);
        txtForgotPassword.setOnClickListener(v -> {
            String url = "https://sv.haui.edu.vn/help?repass=1";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });


        Button btnEnroll = findViewById(R.id.btnEnroll);
        btnEnroll.setOnClickListener(v -> {
            String url = "https://nhaphoc.haui.edu.vn/enroll/enrolled/chao-mung-sinh-vien-nhap-hoc.htm";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });



        edtPassword.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                int drawableStart = edtPassword.getCompoundDrawables()[2] != null ? edtPassword.getCompoundDrawables()[2].getBounds().width() : 0;
                float x = event.getX();

                // Kiểm tra xem người dùng có chạm vào vị trí của icon mắt hay không
                if (x > edtPassword.getWidth() - edtPassword.getPaddingRight() - drawableStart) {
                    togglePasswordVisibility();
                    return true;
                }
            }
            return false;
        });

        // Khởi tạo hoặc mở cơ sở dữ liệu
        database = openOrCreateDatabase("qlsv.db", MODE_PRIVATE, null);

        // Tạo bảng nếu chưa tồn tại
        createTableIfNotExists();

        // Thêm dữ liệu mẫu nếu cần
        //insertSampleData();

        // Xử lý nút đăng nhập
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String studentCodeStr = edtStudentCode.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();

                if (studentCodeStr.isEmpty() || password.isEmpty()) {
                    txtError.setText("Tên đăng nhập và mật khẩu không được để trống.");
                    txtError.setVisibility(View.VISIBLE);
                    return;
                }

                int studentCode;
                try {
                    studentCode = Integer.parseInt(studentCodeStr);
                } catch (NumberFormatException e) {
                    txtError.setText("Tên đăng nhập phải là số.");
                    txtError.setVisibility(View.VISIBLE);
                    return;
                }

                // Lấy studentId từ hàm getStudentId
                int studentId = getStudentId(studentCode, password);

                if (studentId != -1) { // Nếu id tồn tại (đăng nhập thành công)
                    txtError.setVisibility(View.GONE);

                    // Lưu id vào SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("studentId", studentId);
                    editor.apply();

                    // Chuyển sang MainActivity
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    txtError.setText("Tên đăng nhập hoặc mật khẩu sai!");
                    txtError.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    // Tạo bảng nếu chưa tồn tại
    private void createTableIfNotExists() {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS students (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "student_code INTEGER, " +
                "password TEXT, " +
                "fullname TEXT, " +
                "avatar TEXT, " +
                "height REAL, " +
                "weight REAL, " +
                "underlying_disease TEXT, " +
                "gender INTEGER, " +
                "dob TEXT, " +
                "created_at TEXT, " +
                "updated_at TEXT)";
        database.execSQL(createTableQuery);
    }

    // Thêm dữ liệu mẫu
    private void insertSampleData() {
        String insertDataQuery = "INSERT OR IGNORE INTO students (student_code, password, fullname, created_at, updated_at) " +
                "VALUES " +
                "(20230001, 'password123', 'Nguyễn Văn A', '2024-11-22', '2024-11-22'), " +
                "(20230002, 'mypassword', 'Trần Thị B', '2024-11-22', '2024-11-22')";
        database.execSQL(insertDataQuery);
    }

    // Kiểm tra thông tin đăng nhập
    private int getStudentId(int studentCode, String password) {
        String query = "SELECT id FROM students WHERE student_code = ? AND password = ?";
        Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(studentCode), password});

        int studentId = -1; // -1 nghĩa là không tìm thấy
        if (cursor.moveToFirst()) {
            studentId = cursor.getInt(cursor.getColumnIndex("id"));
        }
        cursor.close();
        return studentId;
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            // Ẩn mật khẩu
            edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            edtPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_lock_24, 0, R.drawable.ic_eye_off, 0); // Giữ icon khóa và icon ẩn mật khẩu
        } else {
            // Hiển thị mật khẩu
            edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            edtPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_lock_24, 0, R.drawable.baseline_remove_red_eye_24, 0); // Giữ icon khóa và icon hiển thị mật khẩu
        }
        edtPassword.setSelection(edtPassword.length()); // Đặt con trỏ ở cuối
        isPasswordVisible = !isPasswordVisible; // Đảo trạng thái
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (database != null) {
            database.close();
        }
    }
}
