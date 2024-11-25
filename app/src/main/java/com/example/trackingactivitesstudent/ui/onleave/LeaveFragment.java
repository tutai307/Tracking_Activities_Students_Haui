package com.example.trackingactivitesstudent.ui.onleave;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.trackingactivitesstudent.Database.DatabaseHelper;
import com.example.trackingactivitesstudent.R;
import com.example.trackingactivitesstudent.databinding.FragmentLeaveNoticeBinding;

import java.util.Calendar;
import java.util.List;

public class LeaveFragment extends Fragment {

    private TextView txvDate;
    private Spinner spnClass;
    private EditText edtReason;
    private Button btnSave;
    private ImageButton imbCalendar;
    private ListView lsvOnLeave;

    private FragmentLeaveNoticeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LeaveViewModel leaveViewModel = new ViewModelProvider(requireActivity()).get(LeaveViewModel.class);
        binding = FragmentLeaveNoticeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        int studentId = sharedPreferences.getInt("studentId", -1);

        // Ánh xạ các phần tử
        txvDate = view.findViewById(R.id.txvDate);
        spnClass = view.findViewById(R.id.spnClass);
        edtReason = view.findViewById(R.id.edtReason);
        btnSave = view.findViewById(R.id.btnSave);
        imbCalendar = view.findViewById(R.id.imbCalendar);
        lsvOnLeave = view.findViewById(R.id.lsvOnLeave);

        // Khởi tạo DatabaseHelper
        DatabaseHelper db = new DatabaseHelper(getContext());

        // Hiển thị danh sách class trong Spinner
        List<ClassItem> classItems = db.getClasses();
        ArrayAdapter<ClassItem> classAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, classItems);
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnClass.setAdapter(classAdapter);

        // Hiển thị danh sách onLeave trong ListView
        updateOnLeaveList(db);

        // Xử lý chọn ngày
        imbCalendar.setOnClickListener(v -> showDatePicker());

        // Xử lý nút "Lưu"
        btnSave.setOnClickListener(v -> {
            String date = txvDate.getText().toString();
            ClassItem selectedClass = (ClassItem) spnClass.getSelectedItem();
            String reason = edtReason.getText().toString();

            if (selectedClass != null && !date.isEmpty() && !reason.isEmpty()) {
                db.addOnLeave(selectedClass.getId(), date, reason, 1, 0); // student_id = 1, status = 0
                Toast.makeText(getContext(), "Lưu thành công", Toast.LENGTH_SHORT).show();
                updateOnLeaveList(db);
            } else {
                Toast.makeText(getContext(), "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    // Hiển thị DatePickerDialog
    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                (view, year1, month1, dayOfMonth) -> txvDate.setText(dayOfMonth + "/" + (month1 + 1) + "/" + year1),
                year, month, day);
        datePickerDialog.show();
    }

    // Cập nhật danh sách onLeave
    private void updateOnLeaveList(DatabaseHelper db) {
        List<OnLeaveItem> onLeaveItems = db.getOnLeaves(1); // student_id = 1
        if (onLeaveItems.isEmpty()) {
            lsvOnLeave.setVisibility(View.GONE);
        } else {
            lsvOnLeave.setVisibility(View.VISIBLE);
            OnLeaveAdapter adapter = new OnLeaveAdapter(getContext(), onLeaveItems);
            lsvOnLeave.setAdapter(adapter);
        }
    }
}