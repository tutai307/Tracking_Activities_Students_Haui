package com.example.trackingactivitesstudent.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.trackingactivitesstudent.ui.onleave.CourseModel;
import com.example.trackingactivitesstudent.ui.onleave.OnLeaveModel;

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

    public List<CourseModel> getCourses() {
        List<CourseModel> courses = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT DISTINCT c.class_id, c.course_name " +
                "FROM classes c " +
                "ORDER BY c.course_name ASC";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            int classIdIndex = cursor.getColumnIndex("class_id");
            int courseNameIndex = cursor.getColumnIndex("course_name");

            do {
                // Lấy dữ liệu từ Cursor
                int classId = cursor.getInt(classIdIndex);
                String courseName = cursor.getString(courseNameIndex);

                // Tạo một đối tượng CourseModel từ dữ liệu
                courses.add(new CourseModel(classId, courseName));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return courses;
    }

    public List<OnLeaveModel> getLeavesForStudent(int studentId) {
        List<OnLeaveModel> leaveList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT c.course_name, o.time_in_day, o.instructors, o.reason, o.date " +
                "FROM onleave o " +
                "JOIN courses c ON o.class_id = c.class_id " +
                "WHERE o.student_id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(studentId)});

        if (cursor.moveToFirst()) {
            do {
                leaveList.add(new OnLeaveModel(
                        cursor.getString(0), // course_name
                        cursor.getString(1), // time_in_day
                        cursor.getString(2), // instructors
                        cursor.getString(3), // reason
                        cursor.getString(4)  // date
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return leaveList;
    }

    public boolean insertOnLeave(int studentId, String date, int classId, String reason) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("student_id", studentId);
        values.put("date", date);
        values.put("class_id", classId);
        values.put("reason", reason);
        values.put("status", "Pending");
        values.put("created_at", System.currentTimeMillis());
        values.put("updated_at", System.currentTimeMillis());

        long result = db.insert("onleave", null, values);
        return result != -1;
    }

}
