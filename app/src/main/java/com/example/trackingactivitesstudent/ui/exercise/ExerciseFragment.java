package com.example.trackingactivitesstudent.ui.exercise;

import static android.content.Context.MODE_PRIVATE;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.trackingactivitesstudent.Database.DatabaseHelper;
import com.example.trackingactivitesstudent.R;
import com.example.trackingactivitesstudent.databinding.FragmentExerciseBinding;
import com.example.trackingactivitesstudent.ui.userinfo.UserInfoFragment;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ExerciseFragment extends Fragment {
    CheckBox chkDrinkWater, chkDoExercise;
    EditText edtTimeExercise, edtLiter;
    Button btnSaveTracking;
    TextView txtStartDate, txtEndDate;
    FragmentExerciseBinding binding;
    ImageButton imbStart;
    ImageButton imbEnd;
    DatabaseHelper dbHelper;
    Button btnViewHistory;
    int timeExercise = 0;
    float liter = 0;
    TextView txtHeight, txtWeight, txtBMI, txtStatus;
    Calendar startDate = Calendar.getInstance();
    Calendar endDate = Calendar.getInstance();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentExerciseBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        dbHelper = new DatabaseHelper(getContext());

        // Lấy các tham chiếu đến các views
        chkDrinkWater = root.findViewById(R.id.chkDrinkWater);
        chkDoExercise = root.findViewById(R.id.chkDoExercise);
        edtTimeExercise = root.findViewById(R.id.edtTimeExercise);
        edtLiter = root.findViewById(R.id.edtLiter);
        btnSaveTracking = root.findViewById(R.id.btnSaveTracking);
        txtStartDate = root.findViewById(R.id.txtStartDate);
        txtEndDate = root.findViewById(R.id.txtEndDate);
        imbStart = root.findViewById(R.id.imbStart);
        imbEnd = root.findViewById(R.id.imbEnd);
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int studentId = sharedPreferences.getInt("studentId", -1);
        txtHeight = root.findViewById(R.id.txtHeight);
        txtWeight = root.findViewById(R.id.txtWeight);
        txtBMI = root.findViewById(R.id.txtBMI);
        txtStatus = root.findViewById(R.id.txtStatus);
        btnViewHistory = root.findViewById(R.id.btnViewHistory);

        if (studentId > 0) { // Kiểm tra studentId có hợp lệ không
            Cursor cursor = dbHelper.getStudentInfo(studentId); // Lấy thông tin từ DB
            if (cursor != null && cursor.moveToFirst()) { // Kiểm tra dữ liệu trả về
                // Lấy chiều cao và cân nặng từ Cursor
                float height = cursor.getFloat(cursor.getColumnIndex("height"));
                float weight = cursor.getFloat(cursor.getColumnIndex("weight"));

                // Hiển thị chiều cao và cân nặng
                txtHeight.setText(String.format("%.2f cm", height));
                txtWeight.setText(String.format("%.2f kg", weight));

                // Tính toán BMI
                float bmi = calculateBMI(height, weight);
                txtBMI.setText(String.format("%.2f", bmi));

                // Xác định tình trạng sức khoẻ từ BMI
                String status = getStatusFromBMI(bmi);
                txtStatus.setText(status);

                cursor.close(); // Đóng Cursor
            } else {
                // Không tìm thấy dữ liệu
                Toast.makeText(getContext(), "Không tìm thấy thông tin sinh viên", Toast.LENGTH_SHORT).show();
            }
        } else {
            // studentId không hợp lệ
            Toast.makeText(getContext(), "ID sinh viên không hợp lệ", Toast.LENGTH_SHORT).show();
        }

        imbStart.setOnClickListener(v -> showDatePickerDialog(startDate, txtStartDate, "startDate"));
        imbEnd.setOnClickListener(v -> showDatePickerDialog(endDate, txtEndDate, "endDate"));


        chkDrinkWater.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                edtLiter.setEnabled(true);  // Cho phép chỉnh sửa EditText khi checkbox được chọn
            } else {
                edtLiter.setEnabled(false); // Vô hiệu hóa EditText khi checkbox không được chọn
                edtLiter.setText("0"); // Đặt lại giá trị là 0 khi không thể chỉnh sửa
            }
        });

        chkDoExercise.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                edtTimeExercise.setEnabled(true);  // Cho phép chỉnh sửa EditText khi checkbox được chọn
            } else {
                edtTimeExercise.setEnabled(false); // Vô hiệu hóa EditText khi checkbox không được chọn
                edtTimeExercise.setText("0"); // Đặt lại giá trị là 0 khi không thể chỉnh sửa
            }
        });

        btnSaveTracking.setOnClickListener(view -> {
            try {
                if (edtTimeExercise.isEnabled()) {
                    try {
                        timeExercise = Integer.parseInt(edtTimeExercise.getText().toString());
                    } catch (NumberFormatException e) {
                        timeExercise = 0;  // Nếu có lỗi khi lấy giá trị, gán mặc định là 0
                    }
                } else {
                    timeExercise = 0;  // Nếu EditText bị vô hiệu hóa, gán giá trị mặc định là 0
                }

                if (edtLiter.isEnabled()) {
                    try {
                        liter = Float.parseFloat(edtLiter.getText().toString());
                    } catch (NumberFormatException e) {
                        liter = 0;  // Nếu có lỗi khi lấy giá trị, gán mặc định là 0
                    }
                } else {
                    liter = 0;  // Nếu EditText bị vô hiệu hóa, gán giá trị mặc định là 0
                }
                int drankWater = chkDrinkWater.isChecked() ? 1 : 0;
                int didExercise = chkDoExercise.isChecked() ? 1 : 0;

                String createdAt = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    createdAt = LocalDate.now().toString(); // Lấy ngày hiện tại
                }
                String updatedAt = createdAt;

                // Kiểm tra xem dữ liệu của ngày hôm nay đã tồn tại chưa
                boolean isExist = dbHelper.isTrackingDataExist(createdAt, studentId);

                if (isExist) {
                    // Nếu đã tồn tại, thực hiện cập nhật
                    boolean success = dbHelper.updateTrackingData(drankWater, liter, didExercise, timeExercise, updatedAt, studentId, createdAt);
                    if (success) {
                        Toast.makeText(getActivity(), "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Nếu chưa tồn tại, thêm mới
                    boolean success = dbHelper.insertTrackingData(drankWater, liter, didExercise, timeExercise, createdAt, updatedAt, studentId);
                    if (success) {
                        Toast.makeText(getActivity(), "Lưu thành công!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Lưu thất bại!", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Dữ liệu không hợp lệ!", Toast.LENGTH_SHORT).show();
            }
        });

        Log.d("LoginActivity", "Student ID saved: " + studentId);

        btnViewHistory.setOnClickListener(view -> {
            String startDate = txtStartDate.getText().toString();
            String endDate = txtEndDate.getText().toString();

            if (startDate.isEmpty() || endDate.isEmpty()) {
                Toast.makeText(getActivity(), "Vui lòng chọn ngày bắt đầu và ngày kết thúc.", Toast.LENGTH_SHORT).show();
                return; // Dừng lại nếu ngày chưa được chọn
            }

            if (isEndDateBeforeStartDate()) {
                Toast.makeText(getActivity(), "Chọn ngày kết thúc lớn hơn hoặc bằng ngày bắt đầu.", Toast.LENGTH_SHORT).show();
            } else {
                NavController navController = NavHostFragment.findNavController(ExerciseFragment.this);
                navController.navigate(R.id.navigation_tracking);
            }
        });

        return root;
    }

    private void showDatePickerDialog(Calendar date, TextView txtDate, String key) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), (view, year, month, dayOfMonth) -> {
            // Cập nhật ngày được chọn vào Calendar
            date.set(Calendar.YEAR, year);
            date.set(Calendar.MONTH, month);
            date.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            // Hiển thị ngày trên TextView
            String selectedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date.getTime());
            txtDate.setText(selectedDate);

            // Cập nhật ngày vào SharedPreferences
            SharedPreferences sharedDate = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedDate.edit();
            editor.putString(key, selectedDate); // Lưu ngày theo định dạng yyyy-MM-dd
            editor.apply();

        }, date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }



    // Hàm kiểm tra ngày kết thúc có nhỏ hơn ngày bắt đầu không
    private boolean isEndDateBeforeStartDate() {
        try {
            String startDateString = txtStartDate.getText().toString();
            String endDateString = txtEndDate.getText().toString();

            if (!startDateString.isEmpty() && !endDateString.isEmpty()) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date startDate = dateFormat.parse(startDateString);
                Date endDate = dateFormat.parse(endDateString);

                // So sánh ngày kết thúc và ngày bắt đầu
                return endDate.before(startDate);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private float calculateBMI(float height, float weight) {
        // Chiều cao được lưu bằng cm, cần chuyển sang mét
        if (height > 0) {
            return weight / ((height / 100) * (height / 100));
        }
        return 0;
    }

    // Hàm lấy tình trạng từ BMI
    private String getStatusFromBMI(float bmi) {
        if (bmi < 18.5) {
            return "Gầy";
        } else if (bmi < 24.9) {
            return "Bình thường";
        } else if (bmi < 29.9) {
            return "Thừa cân";
        } else {
            return "Béo phì";
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}