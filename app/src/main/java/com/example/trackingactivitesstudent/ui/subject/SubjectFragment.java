package com.example.trackingactivitesstudent.ui.subject;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.trackingactivitesstudent.R;
import com.example.trackingactivitesstudent.databinding.FragmentSubjectBinding;
import com.example.trackingactivitesstudent.ui.leave.LeaveViewModel;

public class SubjectFragment extends Fragment {

    private FragmentSubjectBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        LeaveViewModel leaveViewModel =
                new ViewModelProvider(this).get(LeaveViewModel.class);

        binding = FragmentSubjectBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textSubject;
        leaveViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}