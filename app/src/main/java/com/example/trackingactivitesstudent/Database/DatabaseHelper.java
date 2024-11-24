package com.example.trackingactivitesstudent.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.trackingactivitesstudent.ui.onleave.ClassItem;
import com.example.trackingactivitesstudent.ui.onleave.OnLeaveItem;

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

        // Nếu cơ sở dữ liệu đã tồn tại, xóa nó trước khi sao chép mới
        if (dbFile.exists()) {
            dbFile.delete();  // Xóa cơ sở dữ liệu cũ
        }

        try {
            copyDatabase();  // Sao chép cơ sở dữ liệu mới
        } catch (IOException e) {
            e.printStackTrace();
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

}
