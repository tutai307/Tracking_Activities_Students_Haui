package com.example.trackingactivitesstudent.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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




    @Override
    public void onCreate(SQLiteDatabase db) {
        // Không cần tạo bảng vì database được sao chép từ assets
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Thực hiện cập nhật nếu cần
    }
}
