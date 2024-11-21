package com.example.trackingactivitesstudent.ui.onleave;

public class CourseModel {
    private int classId;
    private String courseName;

    public CourseModel(int classId, String courseName) {
        this.classId = classId;
        this.courseName = courseName;
    }

    public int getClassId() {
        return classId;
    }

    public String getCourseName() {
        return courseName;
    }

    @Override
    public String toString() {
        return courseName; // Hiển thị course_name trong Spinner
    }
}
