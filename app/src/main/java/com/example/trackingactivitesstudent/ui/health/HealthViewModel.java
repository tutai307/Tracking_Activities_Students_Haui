package com.example.trackingactivitesstudent.ui.health;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HealthViewModel extends ViewModel {


    private String tinhTrang;
    private String disease;
    private String courseName;
    private String instructorName;
    private String time;


    public HealthViewModel() {
    }
    public HealthViewModel(String tinhTrang, String disease, String courseName, String instructorName, String time) {
        this.tinhTrang = tinhTrang;
        this.disease = disease;
        this.courseName = courseName;
        this.instructorName = instructorName;
        this.time = time;
    }
    public String getTinhTrang() {
        return tinhTrang;
    }
    public void setTinhTrang(String tinhTrang) {
        this.tinhTrang = tinhTrang;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}