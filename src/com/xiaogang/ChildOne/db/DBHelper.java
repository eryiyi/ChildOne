package com.xiaogang.ChildOne.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by apple on 14-9-15.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "liangxun.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS lx_message (id INTEGER PRIMARY KEY AUTOINCREMENT, title VARCHAR, content TEXT, custom TEXT, time TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
