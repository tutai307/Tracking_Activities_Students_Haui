package com.example.trackingactivitesstudent.ui.leave;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.trackingactivitesstudent.databinding.FragmentLeaveNoticeBinding;

public class LeaveFragment extends Fragment {

    private FragmentLeaveNoticeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        LeaveViewModel leaveViewModel = new ViewModelProvider(requireActivity()).get(LeaveViewModel.class);

        binding = FragmentLeaveNoticeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textLeave;
        leaveViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}