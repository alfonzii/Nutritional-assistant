package com.example.nutritionalassistant;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static android.provider.BaseColumns._ID;
import static com.example.nutritionalassistant.NutritionDatabaseContract.NutritionDbEntry.COLUMN_CARBOHYDRATES_FOOD;
import static com.example.nutritionalassistant.NutritionDatabaseContract.NutritionDbEntry.COLUMN_ENERGY_FOOD;
import static com.example.nutritionalassistant.NutritionDatabaseContract.NutritionDbEntry.COLUMN_FATS_FOOD;
import static com.example.nutritionalassistant.NutritionDatabaseContract.NutritionDbEntry.COLUMN_NAME_FOOD;
import static com.example.nutritionalassistant.NutritionDatabaseContract.NutritionDbEntry.COLUMN_PROTEINS_FOOD;
import static com.example.nutritionalassistant.NutritionDatabaseContract.NutritionDbEntry.TABLE_NAME;
import static com.example.nutritionalassistant.NutritionDatabaseContract.SQL_CREATE_ENTRIES;
import static com.example.nutritionalassistant.NutritionDatabaseContract.SQL_DELETE_ENTRIES;


public class NutritionDbHelper extends SQLiteOpenHelper {
    private static NutritionDbHelper INSTANCE;
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "NutriDatabase.db";

    private static boolean createdDB = true;

    public static synchronized NutritionDbHelper getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new NutritionDbHelper(context.getApplicationContext());
            INSTANCE.fillDatabase(INSTANCE.getWritableDatabase(), context);
            INSTANCE.close();
        }
        return INSTANCE;
    }

    private NutritionDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
        createdDB = false;
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    private void fillDatabase(SQLiteDatabase db, Context context) {
        if (!createdDB) {
            String mCSVfile = "NutriDatabaze-v7.16-data-export.csv";
            AssetManager manager = context.getAssets();
            InputStream inStream = null;
            try {
                inStream = manager.open(mCSVfile);
            } catch (IOException e) {
                e.printStackTrace();
            }

            BufferedReader buffer = new BufferedReader(new InputStreamReader(inStream));

            String line = "";
            db.beginTransaction();
            try {
                while ((line = buffer.readLine()) != null) {
                    String[] colums = line.split(";");
                    if (colums.length != 23) {
                        Log.d("CSVParser", "Skipping Bad CSV Row");
                        continue;
                    }
                    ContentValues cv = new ContentValues();
                    cv.put(_ID, colums[0].trim());
                    cv.put(COLUMN_NAME_FOOD, colums[2].trim());
                    cv.put(COLUMN_ENERGY_FOOD, colums[8].trim());
                    cv.put(COLUMN_FATS_FOOD, colums[9].trim());
                    cv.put(COLUMN_CARBOHYDRATES_FOOD, colums[14].trim());
                    cv.put(COLUMN_PROTEINS_FOOD, colums[18].trim());
                    db.insert(TABLE_NAME, null, cv);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        }
    }
}
