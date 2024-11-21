package com.example.trackingactivitesstudent.ui.onleave;

public class OnLeaveModel {
    private String courseName;
    private String courseTime;
    private String instructor;
    private String reason;
    private String date;

    // Constructor
    public OnLeaveModel(String courseName, String courseTime, String instructor, String reason, String date) {
        this.courseName = courseName;
        this.courseTime = courseTime;
        this.instructor = instructor;
        this.reason = reason;
        this.date = date;
    }

    // Getters and Setters
    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseTime() {
        return courseTime;
    }

    public void setCourseTime(String courseTime) {
        this.courseTime = courseTime;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

