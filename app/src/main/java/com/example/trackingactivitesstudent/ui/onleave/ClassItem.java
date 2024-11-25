package com.example.trackingactivitesstudent.ui.onleave;

import androidx.annotation.NonNull;

public class ClassItem {
    private final int id;
    private final String courseName;

    public ClassItem(int id, String courseName) {
        this.id = id;
        this.courseName = courseName;
    }

    public int getId() {
        return id;
    }

    public String getCourseName() {
        return courseName;
    }

    @NonNull
    @Override
    public String toString() {
        return courseName;
    }
}

