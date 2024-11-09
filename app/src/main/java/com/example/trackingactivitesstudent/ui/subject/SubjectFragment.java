package com.example.trackingactivitesstudent.ui.subject;

import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.trackingactivitesstudent.R;
import com.example.trackingactivitesstudent.databinding.FragmentSubjectBinding;

public class SubjectFragment extends Fragment {

    private FragmentSubjectBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Khởi tạo ViewModel
        SubjectViewModel subjectViewModel =
                new ViewModelProvider(this).get(SubjectViewModel.class);

        // Inflate layout với ViewBinding
        binding = FragmentSubjectBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Xử lý sự kiện Button
        Button btnCacHPTC = binding.btnCacHPTC;
        btnCacHPTC.setOnClickListener(v -> {
            // Xử lý khi nhấn nút "Các học phần thể chất"
            // Bạn có thể thêm hành động chuyển sang một Fragment hoặc Activity khác
        });

        Button btnKQTheChat = binding.btnKQTheChat;
        btnKQTheChat.setOnClickListener(v -> {
            // Xử lý khi nhấn nút "Kết quả thể chất"
            // Bạn có thể thêm hành động chuyển sang một Fragment hoặc Activity khác
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
