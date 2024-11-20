package com.example.trackingactivitesstudent.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
}
