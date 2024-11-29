package com.example.trackingactivitesstudent.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.trackingactivitesstudent.ui.Physical_results.Result;
import com.example.trackingactivitesstudent.ui.onleave.ClassItem;
import com.example.trackingactivitesstudent.ui.onleave.OnLeaveItem;
import com.example.trackingactivitesstudent.ui.tracking.Tracker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "qlsv.db";
    private static final int DATABASE_VERSION = 1;
    private static String DATABASE_PATH = "";
    private final Context mContext;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
        DATABASE_PATH = context.getApplicationInfo().dataDir + "/databases/";
        copyDatabaseIfNeeded();
    }

    private void copyDatabaseIfNeeded() {
        File dbFile = new File(DATABASE_PATH + DATABASE_NAME);
        if (!dbFile.exists()) {
            try {
                copyDatabase();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void copyDatabase() throws IOException {
        InputStream input = mContext.getAssets().open(DATABASE_NAME);
        File folder = new File(DATABASE_PATH);
        if (!folder.exists()) {
            folder.mkdir();
        }
        String outFileName = DATABASE_PATH + DATABASE_NAME;
            OutputStream output = new FileOutputStream(outFileName);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = input.read(buffer)) > 0) {
            output.write(buffer, 0, length);
        }

        output.flush();
        output.close();
        input.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Không cần tạo bảng vì database được sao chép từ assets
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Thực hiện cập nhật nếu cần
    }

//    public Cursor getStudentByCode(int studentCode) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        String query = "SELECT * FROM students WHERE student_code = ?";
//        return db.rawQuery(query, new String[]{String.valueOf(studentCode)});
//    }

    // Lấy danh sách class_id và course_name cho Spinner
    public List<ClassItem> getClasses() {
        List<ClassItem> classList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT classes.id, courses.course_name FROM classes " +
                "JOIN courses ON classes.course_id = courses.id";
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String courseName = cursor.getString(1);
            classList.add(new ClassItem(id, courseName));
        }
        cursor.close();
        return classList;
    }

    // Truy vấn dữ liệu để hiển thị trong ListView
    public List<OnLeaveItem> getOnLeaves(int studentId) {
        List<OnLeaveItem> leaveList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT courses.course_name, classes.time_in_day, instructors.fullname, " +
                "onleaves.reason, onleaves.date " +
                "FROM onleaves " +
                "JOIN classes ON onleaves.class_id = classes.id " +
                "JOIN courses ON classes.course_id = courses.id " +
                "JOIN instructors ON classes.instructor_id = instructors.id " +
                "WHERE onleaves.student_id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(studentId)});
        while (cursor.moveToNext()) {
            String courseName = cursor.getString(0);
            String timeInDay = cursor.getString(1);
            String instructorName = cursor.getString(2);
            String reason = cursor.getString(3);
            String date = cursor.getString(4);
            leaveList.add(new OnLeaveItem(courseName, timeInDay, instructorName, reason, date));
        }
        cursor.close();
        return leaveList;
    }

    // Thêm bản ghi vào bảng onleaves
    public void addOnLeave(int classId, String date, String reason, int studentId, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("class_id", classId);
        values.put("date", date);
        values.put("reason", reason);
        values.put("student_id", studentId);
        values.put("status", status);
        values.put("created_at", System.currentTimeMillis());
        values.put("updated_at", System.currentTimeMillis());
        db.insert("onleaves", null, values);
    }

    public boolean insertTrackingData(int isEnoughWater, float liter,
                                      int isDoingExercise, int exerciseSpan,
                                      String createdAt, String updatedAt,int studentId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        // Thêm các giá trị vào ContentValues
        contentValues.put("is_enought_water", exerciseSpan);  // 1 cho true, 0 cho false
        contentValues.put("liter", liter);
        contentValues.put("is_doing_exercise", exerciseSpan);  // 1 cho true, 0 cho false
        contentValues.put("exercise_span", exerciseSpan);
        contentValues.put("created_at", createdAt);
        contentValues.put("updated_at", updatedAt);
        contentValues.put("student_id", studentId);

        // Chèn dữ liệu vào bảng 'trackers'
        long result = db.insert("trackers", null, contentValues);
        Log.d("TrackingData", "Thời gian tập: " + exerciseSpan + " phút");
        Log.d("TrackingData", "Đã uống đủ nước: " + (exerciseSpan == 1 ? "Có" : "Không"));
        Log.d("TrackingData", "Tập thể dục: " + (exerciseSpan == 1 ? "Có" : "Không"));
        Log.d("TrackingData", "Số lít nước đã uống: " + liter);
        Log.d("TrackingData", "Ngày tạo: " + createdAt);
        Log.d("TrackingData", "ID sinh viên: " + studentId);
        Log.d("TrackingData", "Insert result: " + result);

        if (result == -1) {
            Log.e("DatabaseError", "Insert failed, no row inserted.");
            db.close();
            return false; // Trả về false nếu thất bại
        }

        db.close();
        return true; // Trả về true nếu thành công
    }

    public boolean isTrackingDataExist(String date, int studentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM trackers WHERE created_at = ? AND student_id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{date, String.valueOf(studentId)});
        boolean exists = false;
        if (cursor.moveToFirst()) {
            exists = cursor.getInt(0) > 0;
        }
        cursor.close();
        return exists;
    }
    public boolean updateTrackingData(int drankWater, float liter, int didExercise, int timeExercise, String updatedAt, int studentId, String createdAt) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("is_enought_water", drankWater);
        values.put("liter", liter);
        values.put("is_doing_exercise", didExercise);
        values.put("exercise_span", timeExercise);
        values.put("updated_at", updatedAt);

        int rowsAffected = db.update("trackers", values, "created_at = ? AND student_id = ?", new String[]{createdAt, String.valueOf(studentId)});
        return rowsAffected > 0;
    }

    public List<Tracker> getTrackersByDateRangeAndStudent(String startDate, String endDate, int studentId) {
        List<Tracker> trackerList = new ArrayList<>();
        SQLiteDatabase database = null;
        Cursor cursor = null;

        try {
            database = this.getReadableDatabase();
            String query = "SELECT * FROM trackers WHERE student_id = ? AND created_at BETWEEN ? AND ?";
            cursor = database.rawQuery(query, new String[]{String.valueOf(studentId), startDate, endDate});

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    // Lấy dữ liệu từ các cột
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                    boolean isEnoughWater = cursor.getInt(cursor.getColumnIndexOrThrow("is_enought_water")) == 1;
                    double liter = cursor.getDouble(cursor.getColumnIndexOrThrow("liter"));
                    boolean isDoingExercise = cursor.getInt(cursor.getColumnIndexOrThrow("is_doing_exercise")) == 1;
                    int exerciseSpan = cursor.getInt(cursor.getColumnIndexOrThrow("exercise_span"));
                    String createdAt = cursor.getString(cursor.getColumnIndexOrThrow("created_at"));
                    String updatedAt = cursor.getString(cursor.getColumnIndexOrThrow("updated_at"));

                    // Tạo đối tượng Tracker
                    Tracker tracker = new Tracker(id, isEnoughWater, liter, isDoingExercise, exerciseSpan, createdAt, updatedAt, studentId);

                    // Thêm vào danh sách
                    trackerList.add(tracker);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("Database Error", "Error fetching trackers: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (database != null) {
                database.close();
            }
        }
        return trackerList;
    }



    public Cursor getStudentInfo(int studentCode) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT height, weight FROM students WHERE id = ?", new String[]{String.valueOf(studentCode)});
    }

    public int getStudentId(int studentCode, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT id FROM students WHERE student_code = ? AND password = ?",
                new String[]{String.valueOf(studentCode), password});

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            cursor.close();
            return id;
        }

        return -1;
    }

    public List<Result> getResultsByStudentId(int studentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Result> resultList = new ArrayList<>();

        // Truy vấn dữ liệu từ bảng 'results' và kết nối với các bảng 'classes' và 'courses'
        String query = "SELECT r.test1, r.midterm_score, r.exam_score, c.class_code, cs.course_name, cs.course_code " +
                "FROM results r " +
                "JOIN classes c ON r.class_id = c.id " +
                "JOIN courses cs ON c.course_id = cs.id " +
                "WHERE r.student_id = ?";
        Cursor cursor = db.rawQuery(query, new String[] {String.valueOf(studentId)});

        if (cursor.moveToFirst()) {
            do {
                Result result = new Result();
                result.setTest1(cursor.getFloat(cursor.getColumnIndex("test1")));
                result.setMidtermScore(cursor.getFloat(cursor.getColumnIndex("midterm_score")));
                result.setExamScore(cursor.getFloat(cursor.getColumnIndex("exam_score")));
                result.setClassCode(cursor.getString(cursor.getColumnIndex("class_code")));
                result.setCourseName(cursor.getString(cursor.getColumnIndex("course_name")));
                result.setCourseCode(cursor.getString(cursor.getColumnIndex("course_code")));

                resultList.add(result);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return resultList;
    }

}
