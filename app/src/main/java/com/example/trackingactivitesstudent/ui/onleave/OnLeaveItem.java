package com.example.trackingactivitesstudent.ui.onleave;

public class OnLeaveItem {
    private final String courseName;
    private final String timeInDay;
    private final String instructorName;
    private final String reason;
    private final String date;

    public OnLeaveItem(String courseName, String timeInDay, String instructorName, String reason, String date) {
        this.courseName = courseName;
        this.timeInDay = timeInDay;
        this.instructorName = instructorName;
        this.reason = reason;
        this.date = date;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getTimeInDay() {
        return timeInDay;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public String getReason() {
        return reason;
    }

    public String getDate() {
        return date;
    }
}

