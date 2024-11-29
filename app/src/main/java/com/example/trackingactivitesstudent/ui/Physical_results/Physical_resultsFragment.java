package com.example.trackingactivitesstudent.ui.Physical_results;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.trackingactivitesstudent.Database.DatabaseHelper;
import com.example.trackingactivitesstudent.R;

import java.util.List;

public class Physical_resultsFragment extends Fragment {

    private PhysicalResultsViewModel mViewModel;
    private ListView lsvResult;
    private ResultAdapter adapter;

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
        lsvResult = getView().findViewById(R.id.lsvResult);

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        int studentId = sharedPreferences.getInt("studentId", -1);
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        List<Result> resultList = dbHelper.getResultsByStudentId(studentId);

        if (resultList.size() > 0) {
            adapter = new ResultAdapter(getContext(), resultList);
            lsvResult.setAdapter(adapter);

            // Cập nhật txvSoTinHoanThanh và txvSoTinThieu
            TextView txvSoTinHoanThanh = getView().findViewById(R.id.txvSoTinHoanThanh);
            TextView txvSoTinThieu = getView().findViewById(R.id.txvSoTinThieu);

            // Số lượng item trong ListView = số tín đã hoàn thành
            int completedCredits = adapter.getCount();
            txvSoTinHoanThanh.setText(String.valueOf(completedCredits));

            // Số tín tối thiểu = 4 (Giả sử)
            int minimumCredits = 4;
            int remainingCredits = minimumCredits - completedCredits;
            txvSoTinThieu.setText(String.valueOf(remainingCredits));
        } else {
            // Nếu không có dữ liệu
            lsvResult.setVisibility(View.GONE);
        }
    }
}

