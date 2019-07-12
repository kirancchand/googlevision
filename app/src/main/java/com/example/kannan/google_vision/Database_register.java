package com.example.kannan.google_vision;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class Database_register extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "VEHICLE_TRACKING";
    public static final String TABLE_NAME = "REGISTRATION";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "NAME";
    public static final String COL_3 = "PEN_NUMBER";
    public static final String COL_4 = "DOB";
    SQLiteDatabase db;


    public Database_register(Context context) {

        super(context, DATABASE_NAME, null, 1);
        db=this.getWritableDatabase();

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(" CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT NOT NULL,PEN_NUMBER TEXT NOT NULL,DOB TEXT NOT NULL)");
        Log.e("database", "database table"+TABLE_NAME+"created");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT NOT NULL,PEN_NUMBER TEXT NOT NULL,DOB TEXT NOT NULL)");


    }

    public boolean insertData(String name, String pen_number, String dob) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, pen_number);
        contentValues.put(COL_4, dob);

        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1)
            return false;

        else
            return true;
    }

}
