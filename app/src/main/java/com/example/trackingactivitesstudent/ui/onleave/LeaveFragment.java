package com.example.trackingactivitesstudent.ui.onleave;

import android.app.DatePickerDialog;
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
import androidx.navigation.Navigation;

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

//    private FragmentLeaveNoticeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        LeaveViewModel leaveViewModel = new ViewModelProvider(requireActivity()).get(LeaveViewModel.class);
//        binding = FragmentLeaveNoticeBinding.inflate(inflater, container, false);
//        View root = binding.getRoot();

        View view = inflater.inflate(R.layout.fragment_leave_notice, container, false);

        // Ánh xạ các phần tử
        txvDate = view.findViewById(R.id.txvDate);
        spnClass = view.findViewById(R.id.spnClass);
        edtReason = view.findViewById(R.id.edtReason);
        btnSave = view.findViewById(R.id.btnSave);
        imbCalendar = view.findViewById(R.id.imbCalendar);
        lsvOnLeave = view.findViewById(R.id.lsvOnLeave);

        // Gọi các phương thức khởi tạo
        setupSpinner();
        loadLeaveData();
        setupListeners();
//        return root;
        return view;
    }

//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        binding = null;
//    }

    private void setupListeners() {
        // Lắng nghe sự kiện click Calendar
        imbCalendar.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (datePicker, year, month, day) -> {
                        String selectedDate = day + "/" + (month + 1) + "/" + year;
                        txvDate.setText(selectedDate);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });

        // Lắng nghe sự kiện nhấn nút Lưu
        btnSave.setOnClickListener(view -> {
            String date = txvDate.getText().toString();
            String reason = edtReason.getText().toString();

            CourseModel selectedCourse = (CourseModel) spnClass.getSelectedItem();
            int classId = selectedCourse.getClassId();

            DatabaseHelper db = new DatabaseHelper(getContext());
            boolean isInserted = db.insertOnLeave(1, date, classId, reason); // student_id = 1

            if (isInserted) {
                Toast.makeText(getContext(), "Đã lưu thành công", Toast.LENGTH_SHORT).show();
                loadLeaveData(); // Cập nhật danh sách
            } else {
                Toast.makeText(getContext(), "Lưu thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupSpinner() {
        DatabaseHelper db = new DatabaseHelper(getContext());
        List<CourseModel> courses = db.getCourses();

        ArrayAdapter<CourseModel> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, courses);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnClass.setAdapter(adapter);
    }

    private void loadLeaveData() {
        DatabaseHelper db = new DatabaseHelper(getContext());
        List<OnLeaveModel> leaveList = db.getLeavesForStudent(1); // student_id = 1

        if (leaveList.isEmpty()) {
            lsvOnLeave.setVisibility(View.GONE);
        } else {
            lsvOnLeave.setVisibility(View.VISIBLE);
            OnLeaveAdapter adapter = new OnLeaveAdapter(getContext(), R.layout.list_item_onleave, leaveList);
            lsvOnLeave.setAdapter(adapter);
        }
    }
}