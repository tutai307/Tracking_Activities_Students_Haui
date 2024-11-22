package com.example.trackingactivitesstudent.ui.subject;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

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
            Navigation.findNavController(v).navigate(R.id.navigation_subject_to_physical_listFragment);
        });

        Button btnKQTheChat = binding.btnKQTheChat;
        btnKQTheChat.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.navigation_subject_to_physical_resultsFragment);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
