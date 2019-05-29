package com.myapplicationdev.android.pd4_alarm_notice_setter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "task.db";
    private static final int DATABASE_VER = 1;

    private static final String TABLE_TASK = "task";
    private static final String COLUMN_ID = "_id";
    private static final String TASK_NAME = "task_name";
    private static final String DESCRIPTION = "description";
    private static final String DATE = "date";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableSql = "CREATE TABLE " + TABLE_TASK + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TASK_NAME + " TEXT,"
                + DESCRIPTION + " TEXT, "
                + DATE + "TEXT )";
        db.execSQL(createTableSql);
        Log.i("info", "created tables");

        for (int i = 1; i< 4; i++) {
            ContentValues values = new ContentValues();
            values.put(TASK_NAME, "Task " + i);
            values.put(DESCRIPTION, "Description " + i);
            values.put(DATE, "Date " + i);
            db.insert(TABLE_TASK, null, values);
        }
        Log.i("info", "dummy records inserted");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK);
        onCreate(db);
    }

    // Insert Data
    public void insertTask(String task, String description){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TASK_NAME, task);
        values.put(DESCRIPTION, description);

        db.insert(TABLE_TASK, null, values);
        db.close();
    }


    public ArrayList<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<Task>();
        String selectQuery = "SELECT " + COLUMN_ID + ", "
                + TASK_NAME + ", "
                + DESCRIPTION
                + " FROM " + TABLE_TASK;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String description = cursor.getString(2);
                Task obj = new Task(id, name, description);
                tasks.add(obj);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return tasks;
    }

}
