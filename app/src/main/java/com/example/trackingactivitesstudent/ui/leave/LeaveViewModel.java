package com.example.trackingactivitesstudent.ui.leave;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LeaveViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public LeaveViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Trang báo nghỉ");
    }

    public LiveData<String> getText() {
        return mText;
    }
}