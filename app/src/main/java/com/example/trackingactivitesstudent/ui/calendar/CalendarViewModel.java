package com.example.trackingactivitesstudent.ui.calendar;

import androidx.lifecycle.ViewModel;

public class CalendarViewModel extends ViewModel {
    private String classCode;
    private String daysInWeek;
    private String timeInDay;
    private String courseName;
    private String instructorName;

    public CalendarViewModel() {
    }

    public CalendarViewModel(String classCode, String daysInWeek, String timeInDay, String courseName, String instructorName) {
        this.classCode = classCode;
        this.daysInWeek = daysInWeek;
        this.timeInDay = timeInDay;
        this.courseName = courseName;
        this.instructorName = instructorName;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getDaysInWeek() {
        return daysInWeek;
    }

    public void setDaysInWeek(String daysInWeek) {
        this.daysInWeek = daysInWeek;
    }

    public String getTimeInDay() {
        return timeInDay;
    }

    public void setTimeInDay(String timeInDay) {
        this.timeInDay = timeInDay;
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
}