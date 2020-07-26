package com.example.nutritionalassistant;

import android.provider.BaseColumns;

public final class NutritionDatabaseContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private NutritionDatabaseContract() {}

    /* Inner class that defines the table contents */
    public static class NutritionDbEntry implements BaseColumns {
        public static final String TABLE_NAME = "nutridatabase";
        public static final String COLUMN_NAME_FOOD = "EngFdNam";
        public static final String COLUMN_ENERGY_FOOD = "ENERC"; //kcal
        public static final String COLUMN_FATS_FOOD = "FAT";
        public static final String COLUMN_CARBOHYDRATES_FOOD = "CHOT";
        public static final String COLUMN_PROTEINS_FOOD = "PROT";
    }

    static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + NutritionDbEntry.TABLE_NAME + " (" +
                    NutritionDbEntry._ID + " INTEGER PRIMARY KEY," +
                    NutritionDbEntry.COLUMN_NAME_FOOD + " TEXT," +
                    NutritionDbEntry.COLUMN_ENERGY_FOOD + " INTEGER," +
                    NutritionDbEntry.COLUMN_FATS_FOOD + " FLOAT," +
                    NutritionDbEntry.COLUMN_CARBOHYDRATES_FOOD + " FLOAT," +
                    NutritionDbEntry.COLUMN_PROTEINS_FOOD + " FLOAT)";

    static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + NutritionDbEntry.TABLE_NAME;
}
