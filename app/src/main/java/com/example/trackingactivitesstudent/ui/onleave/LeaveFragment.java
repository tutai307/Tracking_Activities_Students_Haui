package com.example.trackingactivitesstudent.ui.onleave;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.trackingactivitesstudent.R;
import com.example.trackingactivitesstudent.databinding.FragmentLeaveNoticeBinding;

public class LeaveFragment extends Fragment {

    private FragmentLeaveNoticeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        LeaveViewModel leaveViewModel = new ViewModelProvider(requireActivity()).get(LeaveViewModel.class);

        binding = FragmentLeaveNoticeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}