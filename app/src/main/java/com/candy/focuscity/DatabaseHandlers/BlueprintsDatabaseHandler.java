package com.candy.focuscity.DatabaseHandlers;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.candy.focuscity.Model.BlueprintModel;

import java.util.ArrayList;
import java.util.List;

public class BlueprintsDatabaseHandler extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String NAME = "blueprintsDatabase";
    private static final String BLUEPRINTS_TABLE = "blueprints";
    private static final String ID = "id";
    private static final String BUILDING_IMAGE_ID = "buildingImageId";
    private static final String BUILDING_NAME = "buildingName";
    private static final String TOTAL_MINUTES = "totalMinutes";
    private static final String CREATE_BLUEPRINTS_TABLE = "CREATE TABLE " + BLUEPRINTS_TABLE +
            "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + BUILDING_IMAGE_ID + " INTEGER, " +
            BUILDING_NAME + " TEXT, " + TOTAL_MINUTES + " INTEGER)";

    private SQLiteDatabase db;

    public BlueprintsDatabaseHandler(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BLUEPRINTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the older tables
        db.execSQL("DROP TABLE IF EXISTS " + BLUEPRINTS_TABLE);
        // Create new tables
        onCreate(db);
    }

    public void openDatabase() {
        db = this.getWritableDatabase();
    }

    public void insertBlueprint(BlueprintModel blueprint) {
        ContentValues cv = new ContentValues();
        cv.put(BUILDING_IMAGE_ID, blueprint.getBuildingImageId());
        cv.put(BUILDING_NAME, blueprint.getBuildingName());
        cv.put(TOTAL_MINUTES, blueprint.getTotalMinutes());
        db.insert(BLUEPRINTS_TABLE, null, cv);
    }

    public void deleteBlueprint (int id) {
        db.delete(BLUEPRINTS_TABLE, ID + "=?", new String[] {String.valueOf(id)} );
    }

    @SuppressLint("Range")
    public List<BlueprintModel> getAllBlueprints() {
        List<BlueprintModel> blueprintsList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try {
            cur = db.query(BLUEPRINTS_TABLE, null, null, null, null, null, null, null);
            if (cur != null) {
                if (cur.moveToFirst()) {
                    do {
                        BlueprintModel blueprint = new BlueprintModel();
                        blueprint.setId(cur.getInt(cur.getColumnIndex(ID)));
                        blueprint.setBuildingImageId(cur.getInt(cur.getColumnIndex(BUILDING_IMAGE_ID)));
                        blueprint.setBuildingName(cur.getString(cur.getColumnIndex(BUILDING_NAME)));
                        blueprint.setTotalMinutes(cur.getInt(cur.getColumnIndex(TOTAL_MINUTES)));
                        blueprintsList.add(blueprint);
                    } while (cur.moveToNext());
                }
            }
        } finally {
            db.endTransaction();
            cur.close();
        }
        return blueprintsList;
    }
}


