package com.example.nutritionalassistant;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class FoodAddingActivity extends AppCompatActivity {
    DataHolder data = DataHolder.getInstance();
    AutoCompleteTextView searchbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_adding);

        final NutritionDbHelper dbHelper = NutritionDbHelper.getInstance(this);

        List foodNames = dbHelper.getFoodNamesQuery(this, dbHelper.getReadableDatabase(), false);

        searchbar = findViewById(R.id.foodSearchbar);
        String[] foodDb = new String[foodNames.size()];
        foodDb = (String[])foodNames.toArray(foodDb);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, foodDb);
        searchbar.setAdapter(adapter);

        searchbar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Food foodToAdd = getFoodNutritionsQuery(FoodAddingActivity.this, dbHelper.getReadableDatabase(), ((TextView) view).getText().toString()); //Food.getNutritionValues(((TextView) view).getText().toString());

                //pozriet co to znamena toten kontext
                final NumberPicker nPicker = new NumberPicker(FoodAddingActivity.this);
                nPicker.setMinValue(1);
                nPicker.setMaxValue(1000);

                AlertDialog.Builder builder = new AlertDialog.Builder(FoodAddingActivity.this);
                builder.setTitle(foodToAdd.getName());
                builder.setMessage( "On 100g:\n" +
                                    "Calories: " + foodToAdd.getCals() + "\n" +
                                    "Fats: " + foodToAdd.getFats() + "\n" +
                                    "Carbohydrates: " + foodToAdd.getCarbs() + "\n" +
                                    "Proteins: " + foodToAdd.getProts());
                builder.setView(nPicker);
                builder.setPositiveButton("Add",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                data.setCalsCurrent(Math.round(foodToAdd.getCals()/100 * (float)nPicker.getValue()));
                                data.setFatsCurrent(Math.round(foodToAdd.getFats()/100 * (float)nPicker.getValue()));
                                data.setCarbsCurrent(Math.round(foodToAdd.getCarbs()/100 * (float)nPicker.getValue()));
                                data.setProtsCurrent(Math.round(foodToAdd.getProts()/100 * (float)nPicker.getValue()));
                                setResult(RESULT_OK);
                                finish();
                            }
                        });
                builder.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // what to do when user cliks Cancel
                            }
                        });
                builder.show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        NutritionDbHelper dbHelper = NutritionDbHelper.getInstance(this);
        dbHelper.close();
        super.onDestroy();
    }

    /*private List getFoodNamesQuery(Context context, SQLiteDatabase db){
        //SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                NutritionDatabaseContract.NutritionDbEntry.COLUMN_NAME_FOOD
        };

        Cursor cursor = db.query(
                NutritionDatabaseContract.NutritionDbEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        List foodNames = new ArrayList<>();
        while(cursor.moveToNext()) {
            String itemName = cursor.getString(
                    cursor.getColumnIndexOrThrow(NutritionDatabaseContract.NutritionDbEntry.COLUMN_NAME_FOOD));
            foodNames.add(itemName);
        }
        cursor.close();

        return foodNames;
    }*/

    private Food getFoodNutritionsQuery(Context context, SQLiteDatabase db, String foodName){

        String selection = NutritionDatabaseContract.NutritionDbEntry.COLUMN_NAME_FOOD + " = ?";
        String[] selectionArgs = { foodName };

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
