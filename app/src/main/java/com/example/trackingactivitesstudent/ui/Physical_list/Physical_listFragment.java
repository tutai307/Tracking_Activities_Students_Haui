package com.example.trackingactivitesstudent.ui.Physical_list;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.trackingactivitesstudent.R;

public class Physical_listFragment extends Fragment {

    private PhysicalListViewModel mViewModel;

    public static Physical_listFragment newInstance() {
        return new Physical_listFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_physical_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PhysicalListViewModel.class);
        // TODO: Use the ViewModel
    }

}