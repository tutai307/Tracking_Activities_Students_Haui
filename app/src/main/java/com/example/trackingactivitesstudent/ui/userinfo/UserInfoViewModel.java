package com.example.trackingactivitesstudent.ui.userinfo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UserInfoViewModel extends ViewModel {
    private int studentCode;             // Mã sinh viên
    private String password;             // Mật khẩu
    private String fullname;             // Họ và tên
    private String avatar;               // Ảnh đại diện
    private double height;               // Chiều cao
    private double weight;               // Cân nặng
    private String underlyingDisease;    // Bệnh lý nền
    private int gender;                  // Giới tính (0: Nam, 1: Nữ, hoặc mã khác)
    private String dob;                  // Ngày sinh (dạng chuỗi)

    public UserInfoViewModel() {
    }

    public UserInfoViewModel(int studentCode, String password, String fullname, String avatar, double height, double weight, String underlyingDisease, int gender, String dob) {
        this.studentCode = studentCode;
        this.password = password;
        this.fullname = fullname;
        this.avatar = avatar;
        this.height = height;
        this.weight = weight;
        this.underlyingDisease = underlyingDisease;
        this.gender = gender;
        this.dob = dob;
    }

    public int getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(int studentCode) {
        this.studentCode = studentCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getUnderlyingDisease() {
        return underlyingDisease;
    }

    public void setUnderlyingDisease(String underlyingDisease) {
        this.underlyingDisease = underlyingDisease;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }
}
