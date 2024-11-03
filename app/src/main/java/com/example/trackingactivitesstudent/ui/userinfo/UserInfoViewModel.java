package com.example.trackingactivitesstudent.ui.userinfo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UserInfoViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private final MutableLiveData<String> name = new MutableLiveData<>();
    private final MutableLiveData<String> email = new MutableLiveData<>();
    private final MutableLiveData<String> phone = new MutableLiveData<>();

    public UserInfoViewModel() {
        // Khởi tạo các giá trị mặc định, có thể lấy từ cơ sở dữ liệu hoặc API
//        name = new MutableLiveData<>();
//        email = new MutableLiveData<>();
//        phone = new MutableLiveData<>();

        name.setValue("Nguyen Van A"); // Dữ liệu mẫu
        email.setValue("nguyenvana@example.com");
        phone.setValue("0123456789");
    }

    public LiveData<String> getName() {
        return name;
    }

    public LiveData<String> getEmail() {
        return email;
    }

    public LiveData<String> getPhone() {
        return phone;
    }

    // Phương thức để cập nhật thông tin cá nhân, có thể được gọi từ Fragment
    public void setPersonalInfo(String name, String email, String phone) {
        this.name.setValue(name);
        this.email.setValue(email);
        this.phone.setValue(phone);
    }
}