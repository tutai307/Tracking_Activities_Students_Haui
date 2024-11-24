package com.example.trackingactivitesstudent.ui.exercise;

public class Tracker {
    private int id;
    private int studentId;
    private boolean isEnoughWater;
    private float liter;
    private boolean isDoingExercise;
    private float exerciseSpan;
    private String createdAt;
    private String updatedAt;

    // Getter and Setter methods
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public boolean isEnoughWater() { return isEnoughWater; }
    public void setIsEnoughWater(boolean isEnoughWater) { this.isEnoughWater = isEnoughWater; }

    public float getLiter() { return liter; }
    public void setLiter(float liter) { this.liter = liter; }

    public boolean isDoingExercise() { return isDoingExercise; }
    public void setIsDoingExercise(boolean isDoingExercise) { this.isDoingExercise = isDoingExercise; }

    public float getExerciseSpan() { return exerciseSpan; }
    public void setExerciseSpan(float exerciseSpan) { this.exerciseSpan = exerciseSpan; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
}

