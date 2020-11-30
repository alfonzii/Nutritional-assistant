package cz.cuni.mff.nutritionalassistant.localdatabase;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import cz.cuni.mff.nutritionalassistant.Food;

import static android.provider.BaseColumns._ID;


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

    @Override
    public void onCreate(@NotNull SQLiteDatabase db) {
        db.execSQL(NutritionDatabaseContract.SQL_CREATE_ENTRIES);
        createdDB = false;
    }

    @Override
    public void onUpgrade(@NotNull SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(NutritionDatabaseContract.SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(@NotNull SQLiteDatabase db, int oldVersion, int newVersion) {
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

            String line = "";
            db.beginTransaction();

            assert inStream != null;
            try (BufferedReader buffer = new BufferedReader(new InputStreamReader(inStream))) {
                while ((line = buffer.readLine()) != null) {
                    String[] colums = line.split(";");
                    if (colums.length != 23) {
                        Log.d("CSVParser", "Skipping Bad CSV Row");
                        continue;
                    }
                    ContentValues cv = new ContentValues();
                    cv.put(_ID, colums[0].trim());
                    cv.put(NutritionDatabaseContract.NutritionDbEntry.COLUMN_NAME_FOOD, colums[2].trim());
                    cv.put(NutritionDatabaseContract.NutritionDbEntry.COLUMN_ENERGY_FOOD, colums[8].trim());
                    cv.put(NutritionDatabaseContract.NutritionDbEntry.COLUMN_FATS_FOOD, colums[9].trim());
                    cv.put(NutritionDatabaseContract.NutritionDbEntry.COLUMN_CARBOHYDRATES_FOOD, colums[14].trim());
                    cv.put(NutritionDatabaseContract.NutritionDbEntry.COLUMN_PROTEINS_FOOD, colums[18].trim());
                    db.insert(NutritionDatabaseContract.NutritionDbEntry.TABLE_NAME, null, cv);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        }
    }

    public List<String> getFoodNamesQuery(@NotNull SQLiteDatabase db, boolean sortByAlphabet) {
        String[] projection = {
                NutritionDatabaseContract.NutritionDbEntry.COLUMN_NAME_FOOD
        };
        String sortOrder;
        if (sortByAlphabet)
            sortOrder = NutritionDatabaseContract.NutritionDbEntry.COLUMN_NAME_FOOD + " ASC";
        else
            sortOrder = null;

        Cursor cursor = db.query(
                NutritionDatabaseContract.NutritionDbEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,          // The columns for the WHERE clause
                null,       // The values for the WHERE clause
                null,           // don't group the rows
                null,            // don't filter by row groups
                sortOrder               // The sort order
        );

        List<String> foodNames = new ArrayList<>();
        while (cursor.moveToNext()) {
            String itemName = cursor.getString(
                    cursor.getColumnIndexOrThrow(NutritionDatabaseContract.NutritionDbEntry.COLUMN_NAME_FOOD));
            foodNames.add(itemName);
        }
        cursor.close();

        return foodNames;
    }

    public Food getFoodNutritionsQuery(@NotNull SQLiteDatabase db, String foodName) {

        String selection = NutritionDatabaseContract.NutritionDbEntry.COLUMN_NAME_FOOD + " = ?";
        String[] selectionArgs = {foodName};

        Cursor cursor = db.query(
                NutritionDatabaseContract.NutritionDbEntry.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        cursor.moveToFirst();

        Food food = new Food();
        food.setName(foodName);
        food.setCals(cursor.getInt(cursor.getColumnIndexOrThrow(NutritionDatabaseContract.NutritionDbEntry.COLUMN_ENERGY_FOOD)));
        food.setCarbs(cursor.getFloat(cursor.getColumnIndexOrThrow(NutritionDatabaseContract.NutritionDbEntry.COLUMN_CARBOHYDRATES_FOOD)));
        food.setFats(cursor.getFloat(cursor.getColumnIndexOrThrow(NutritionDatabaseContract.NutritionDbEntry.COLUMN_FATS_FOOD)));
        food.setProts(cursor.getFloat(cursor.getColumnIndexOrThrow(NutritionDatabaseContract.NutritionDbEntry.COLUMN_PROTEINS_FOOD)));
        cursor.close();

        return food;
    }
}
