package com.candy.focuscity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.candy.focuscity.Model.RecordModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String NAME = "recordsDatabase";
    private static final String RECORDS_TABLE = "records";
    private static final String ID = "id";
    private static final String DATE_TIME = "dateTime";
    private static final String BUILDING_IMAGE_ID = "buildingImageId";
    private static final String TOTAL_MINUTES = "totalMinutes";
    private static final String CREATE_RECORDS_TABLE = "CREATE TABLE " + RECORDS_TABLE +
            "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + BUILDING_IMAGE_ID + " INTEGER, " +
            DATE_TIME + " TEXT, " + TOTAL_MINUTES + " INTEGER)";

    private SQLiteDatabase db;

    public DatabaseHandler(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_RECORDS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the older tables
        db.execSQL("DROP TABLE IF EXISTS " + RECORDS_TABLE);
        // Create new tables
        onCreate(db);
    }

    public void openDatabase() {
        db = this.getWritableDatabase();
    }

    public void insertRecord(RecordModel record) {
        ContentValues cv = new ContentValues();
        cv.put(BUILDING_IMAGE_ID, record.getBuildingImageId());
        cv.put(DATE_TIME, record.getDateTimeFormatted());
        cv.put(TOTAL_MINUTES, record.getTotalMinutes());
        db.insert(RECORDS_TABLE, null, cv);
    }

    @SuppressLint("Range")
    public List<RecordModel> getAllRecords() {
        List<RecordModel> recordsList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try {
            cur = db.query(RECORDS_TABLE, null, null, null, null, null, null, null);
            if (cur != null) {
                if (cur.moveToFirst()) {
                    do {
                        RecordModel record = new RecordModel();
                        record.setId(cur.getInt(cur.getColumnIndex(ID)));
                        record.setBuildingImageId(cur.getInt(cur.getColumnIndex(BUILDING_IMAGE_ID)));
                        record.setDateTimeFormatted(cur.getString(cur.getColumnIndex(DATE_TIME)));
                        record.setTotalMinutes(cur.getInt(cur.getColumnIndex(TOTAL_MINUTES)));
                        recordsList.add(record);
                    } while (cur.moveToNext());
                }
            }
        } finally {
            db.endTransaction();
            cur.close();
        }
        return recordsList;
    }
}

