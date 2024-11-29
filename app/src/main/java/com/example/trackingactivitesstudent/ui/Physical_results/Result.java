package com.example.trackingactivitesstudent.ui.Physical_results;

public class Result {
    private float test1;
    private float midtermScore;
    private float examScore;
    private String classCode;
    private String courseName;
    private String courseCode;

    // Constructor mặc định
    public Result() {
    }

    // Getter và Setter cho test1
    public float getTest1() {
        return test1;
    }

    public void setTest1(float test1) {
        this.test1 = test1;
    }

    // Getter và Setter cho midtermScore
    public float getMidtermScore() {
        return midtermScore;
    }

    public void setMidtermScore(float midtermScore) {
        this.midtermScore = midtermScore;
    }

    // Getter và Setter cho examScore
    public float getExamScore() {
        return examScore;
    }

    public void setExamScore(float examScore) {
        this.examScore = examScore;
    }

    // Getter và Setter cho classCode
    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    // Getter và Setter cho courseName
    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    // Getter và Setter cho courseCode
    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }
}

