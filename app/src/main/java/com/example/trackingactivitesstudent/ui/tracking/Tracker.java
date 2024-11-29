package com.example.trackingactivitesstudent.ui.tracking;

public class Tracker {
    private int id;
    private boolean isEnoughWater; // Trạng thái uống đủ nước
    private double liter;          // Số lít nước đã uống
    private boolean isDoingExercise; // Trạng thái có tập luyện không
    private int exerciseSpan;      // Thời gian tập luyện (phút)
    private String createdAt;      // Ngày tạo bản ghi
    private String updatedAt;      // Ngày cập nhật bản ghi
    private int studentId;         // ID của sinh viên

    // Constructor mặc định
    public Tracker() {
    }

    // Constructor đầy đủ
    public Tracker(int id, boolean isEnoughWater, double liter, boolean isDoingExercise, int exerciseSpan,
                   String createdAt, String updatedAt, int studentId) {
        this.id = id;
        this.isEnoughWater = isEnoughWater;
        this.liter = liter;
        this.isDoingExercise = isDoingExercise;
        this.exerciseSpan = exerciseSpan;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.studentId = studentId;
    }

    // Getter và Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isEnoughWater() {
        return isEnoughWater;
    }

    public void setEnoughWater(boolean enoughWater) {
        isEnoughWater = enoughWater;
    }

    public double getLiter() {
        return liter;
    }

    public void setLiter(double liter) {
        this.liter = liter;
    }

    public boolean isDoingExercise() {
        return isDoingExercise;
    }

    public void setDoingExercise(boolean doingExercise) {
        isDoingExercise = doingExercise;
    }

    public int getExerciseSpan() {
        return exerciseSpan;
    }

    public void setExerciseSpan(int exerciseSpan) {
        this.exerciseSpan = exerciseSpan;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    @Override
    public String toString() {
        return "Tracker{" +
                "id=" + id +
                ", isEnoughWater=" + isEnoughWater +
                ", liter=" + liter +
                ", isDoingExercise=" + isDoingExercise +
                ", exerciseSpan=" + exerciseSpan +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", studentId=" + studentId +
                '}';
    }
}

