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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import cz.cuni.mff.nutritionalassistant.foodtypes.Food;
import cz.cuni.mff.nutritionalassistant.foodtypes.FoodAdapterType;
import cz.cuni.mff.nutritionalassistant.foodtypes.Product;
import cz.cuni.mff.nutritionalassistant.foodtypes.ProductAdapterType;
import cz.cuni.mff.nutritionalassistant.foodtypes.Recipe;
import cz.cuni.mff.nutritionalassistant.foodtypes.RecipeAdapterType;
import cz.cuni.mff.nutritionalassistant.foodtypes.RestaurantFood;
import cz.cuni.mff.nutritionalassistant.foodtypes.RestaurantFoodAdapterType;


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

    public List<FoodAdapterType>
    getFoodLightweightListByNameQuery(@NotNull SQLiteDatabase db, String foodName) {

        String selection = NutritionDatabaseContract.NutritionDbEntry.COLUMN_NAME_FOOD + " LIKE ?";
        String[] selectionArgs = {foodName + "%"};

        Cursor cursor = db.query(
                NutritionDatabaseContract.NutritionDbEntry.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        List<FoodAdapterType> suitableFoods = new ArrayList<>();

        while (cursor.moveToNext()) {
            FoodAdapterType food = new RecipeAdapterType();
            food.setFoodName(cursor.getString(
                    cursor.getColumnIndexOrThrow(NutritionDatabaseContract.NutritionDbEntry.COLUMN_NAME_FOOD)));
            food.setCalories(
                    cursor.getInt(cursor.getColumnIndexOrThrow(NutritionDatabaseContract.NutritionDbEntry.COLUMN_ENERGY_FOOD)));
            food.setThumbnailURL("https://d2eawub7utcl6.cloudfront.net/images/nix-apple-grey.png");
            food.setFoodType(Food.FoodType.RECIPE);
            food.setServingUnit("portion");
            //((RestaurantFoodAdapterType) food).setBrandName("McDonald's");

            suitableFoods.add(food);
        }
        cursor.close();

        return suitableFoods;
    }

    public Food getFoodDetailedInfo(@NotNull SQLiteDatabase db, String foodName) {
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

        Food food = new Recipe();
        food.setFoodName(foodName);
        food.setCalories(cursor.getInt(cursor.getColumnIndexOrThrow(NutritionDatabaseContract.NutritionDbEntry.COLUMN_ENERGY_FOOD)));
        food.setFats(cursor.getFloat(cursor.getColumnIndexOrThrow(NutritionDatabaseContract.NutritionDbEntry.COLUMN_FATS_FOOD)));
        food.setCarbohydrates(cursor.getFloat(cursor.getColumnIndexOrThrow(NutritionDatabaseContract.NutritionDbEntry.COLUMN_CARBOHYDRATES_FOOD)));
        food.setProteins(cursor.getFloat(cursor.getColumnIndexOrThrow(NutritionDatabaseContract.NutritionDbEntry.COLUMN_PROTEINS_FOOD)));

        food.setThumbnailURL("https://d2eawub7utcl6.cloudfront.net/images/nix-apple-grey.png");
        food.setFoodType(Food.FoodType.RECIPE);

        // immutable servingUnit List
        food.setServingUnit(Collections.unmodifiableList(Arrays.asList("portion"/*, "ounce"*/)));

        // immutable servingWeight List
        //food.setServingWeight(Collections.unmodifiableList(Arrays.asList(100f, 28.35f)));

        // immutable servingQuantity List
        food.setServingQuantity(Collections.unmodifiableList(Arrays.asList(/*100f, */1f)));

        //food.getServingUnit().add("g");
        //food.getServingWeight().add(100f);
        //food.getServingQuantity().add(100f);
        //food.getServingUnit().add("ounce");
        //food.getServingWeight().add(28f);
        //food.getServingQuantity().add(1f);
        //((RestaurantFood) food).setBrandName("McDonald's");
        Recipe.Ingredient i1 = new Recipe.Ingredient("egg", 2f, "ounce", 3f, "grams");
        Recipe.Ingredient i2 = new Recipe.Ingredient("milk", 5.4f, "oz", 9.23f, "mililiters");
        Recipe.Ingredient i3 = new Recipe.Ingredient("butter", 1f, "pounds", 2.2f, "kg");
        String instructions = "1. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec tincidunt magna quis dolor auctor, eu accumsan nisl porttitor. Fusce non leo in urna maximus ornare ut quis risus. Suspendisse potenti. Nulla bibendum euismod tortor, et faucibus elit pharetra sed. In diam turpis, semper nec diam quis, placerat egestas sem. Nullam mattis ante ac scelerisque sodales. Sed venenatis nisi ac rutrum tincidunt. Nullam ut lorem diam. Donec at mollis lectus.\n\n2. Praesent pulvinar suscipit neque sed tristique. Morbi vel diam laoreet, consectetur enim non, sollicitudin metus. Aenean elit arcu, iaculis ut arcu ut, blandit laoreet quam. Integer sed pretium tellus. Nam eu sapien est. Praesent id orci interdum, tristique nibh ut, tempor massa. Nam hendrerit nisl ligula, hendrerit porta sapien dignissim pellentesque. Aenean ut porta magna.\n\n4. Quisque ullamcorper mi vel sapien mattis finibus vitae sit amet libero. Vestibulum nisl diam, rhoncus hendrerit nisl non, condimentum malesuada ligula. Maecenas a placerat nisi. Mauris id neque sollicitudin, ultrices enim a, maximus mauris. Quisque metus neque, luctus eget posuere vel, ullamcorper et mi. Morbi venenatis pharetra nisi. Cras suscipit pulvinar lectus nec accumsan. Maecenas velit risus, facilisis quis viverra vel, sodales eget velit. Suspendisse potenti. Donec maximus, lorem vel ullamcorper sodales, velit lacus placerat justo, vel fermentum enim ante vitae nibh. Phasellus congue pellentesque libero quis faucibus. Quisque facilisis nunc id nunc ornare, eu accumsan massa vestibulum.";

        ((Recipe) food).setIngredients(Collections.unmodifiableList(Arrays.asList(i1, i2, i3)));
        ((Recipe) food).setInstructions(instructions);

        cursor.close();

        return food;
    }
}
