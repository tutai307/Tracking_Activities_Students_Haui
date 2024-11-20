package com.example.trackingactivitesstudent.ui.exercise;

import android.app.DatePickerDialog;
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

import com.example.trackingactivitesstudent.Database.DatabaseHelper;
import com.example.trackingactivitesstudent.R;
import com.example.trackingactivitesstudent.databinding.FragmentExerciseBinding;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class ExerciseFragment extends Fragment {

    CheckBox chkDrinkWater, chkDoExercise;
    EditText edtTimeExercise;
    Button btnSaveTracking;
    TextView txtStartDate, txtEndDate;
    FragmentExerciseBinding binding;
    ImageButton imbStart;
    ImageButton imbEnd;
    DatabaseHelper dbHelper;
    Button btnViewHistory;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentExerciseBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        dbHelper = new DatabaseHelper(getContext());

        // Lấy các tham chiếu đến các views
        chkDrinkWater = root.findViewById(R.id.chkDrinkWater);
        chkDoExercise = root.findViewById(R.id.chkDoExercise);
        edtTimeExercise = root.findViewById(R.id.edtTimeExercise);
        btnSaveTracking = root.findViewById(R.id.btnSaveTracking);
        txtStartDate = root.findViewById(R.id.txtStartDate);
        txtEndDate = root.findViewById(R.id.txtEndDate);
        imbStart = root.findViewById(R.id.imbStart);
        imbEnd = root.findViewById(R.id.imbEnd);

        imbStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(txtStartDate);
            }
        });

        // Chọn ngày kết thúc
        imbEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(txtEndDate);
            }
        });

        btnSaveTracking.setOnClickListener(view -> {
            try {
                // Lấy giá trị từ các input của người dùng
                int timeExercise = Integer.parseInt(edtTimeExercise.getText().toString());
                int drankWater = chkDrinkWater.isChecked() ? 1 : 0;
                int didExercise = chkDoExercise.isChecked() ? 1 : 0;
                float liter = 2; // Giả sử là 2 lít nước (hoặc có thể lấy từ người dùng)

                // Lấy ngày hiện tại
                String createdAt = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    createdAt = LocalDate.now().toString();
                }
                String updatedAt = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    updatedAt = LocalDate.now().toString();
                }

                int studentId = 10;

                // Gọi phương thức để chèn dữ liệu vào cơ sở dữ liệu
                boolean success = this.dbHelper.insertTrackingData(drankWater, liter, didExercise, timeExercise, createdAt, updatedAt, studentId);

                if (success) {
                    Toast.makeText(getActivity(), "Lưu thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Lưu thất bại!", Toast.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Dữ liệu không hợp lệ!", Toast.LENGTH_SHORT).show();
            }
        });

        btnViewHistory = root.findViewById(R.id.btnViewHistory);

        return root;
    }

    // Hàm hiển thị DatePicker
    private void showDatePicker(final TextView textView) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        if (getActivity() != null) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    getActivity(),
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                            textView.setText(selectedDate);
                        }
                    },
                    year, month, dayOfMonth
            );
            datePickerDialog.show();
        }
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




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}