package com.example.trackingactivitesstudent.ui.subject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SubjectViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public SubjectViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Trang cài đặt");
    }

    public LiveData<String> getText() {
        return mText;
    }
}