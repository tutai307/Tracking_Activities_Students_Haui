package com.example.trackingactivitesstudent.ui.userinfo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModel;

import android.app.DatePickerDialog;
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

import java.util.Calendar;

public class UserInfoFragment extends Fragment {

    private UserInfoViewModel mViewModel;
    private TextView nameTextView, emailTextView, phoneTextView;

    public static UserInfoFragment newInstance() {
        return new UserInfoFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_user_info, container, false);

        // Tìm nút "Sửa" và thiết lập sự kiện nhấn
        Button editButton = root.findViewById(R.id.button_edit);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetDialog();
            }
        });
//        // Liên kết các thành phần UI
//        nameTextView = root.findViewById(R.id.text_name);
//        emailTextView = root.findViewById(R.id.text_email);
//        phoneTextView = root.findViewById(R.id.text_phone);
        TextView viewMore = root.findViewById(R.id.viewMoreText);
        viewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = NavHostFragment.findNavController(UserInfoFragment.this);
                navController.navigate(R.id.navigation_calendar);
            }
        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(UserInfoViewModel.class);

//        mViewModel.getName().observe(getViewLifecycleOwner(), nameTextView::setText);
//        mViewModel.getEmail().observe(getViewLifecycleOwner(), emailTextView::setText);
//        mViewModel.getPhone().observe(getViewLifecycleOwner(), phoneTextView::setText);
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
