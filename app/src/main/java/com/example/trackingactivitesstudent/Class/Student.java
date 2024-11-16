package com.example.trackingactivitesstudent.Class;

public class Student {
    private String fullname;
    private String studentCode;
    private String dob;
    private String gender;

    public Student(String fullname, String studentCode, String dob, String gender) {
        this.fullname = fullname;
        this.studentCode = studentCode;
        this.dob = dob;
        this.gender = gender;
    }

    // Getter và Setter
    public String getFullname() {
        return fullname;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public String getDob() {
        return dob;
    }

    public String getGender() {
        return gender;
    }
}

