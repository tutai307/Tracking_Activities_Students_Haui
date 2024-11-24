package com.example.trackingactivitesstudent.ui.Physical_results;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.trackingactivitesstudent.R;

public class Physical_resultsFragment extends Fragment {

    private PhysicalResultsViewModel mViewModel;

    public static Physical_resultsFragment newInstance() {
        return new Physical_resultsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_physical_results, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PhysicalResultsViewModel.class);
        // TODO: Use the ViewModel
    }

}