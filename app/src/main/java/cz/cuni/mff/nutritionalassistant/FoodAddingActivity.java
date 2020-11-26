package cz.cuni.mff.nutritionalassistant;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.NumberPicker;
import android.widget.TextView;

import cz.cuni.mff.nutritionalassistant.databinding.ActivityFoodAddingBinding;

import java.util.List;


public class FoodAddingActivity extends AppCompatActivity {
    // Reference to singleton object
    private DataHolder data = DataHolder.getInstance();

    // View binding object
    private ActivityFoodAddingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFoodAddingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final NutritionDbHelper dbHelper = NutritionDbHelper.getInstance(this);

        List<String> foodNames = dbHelper.getFoodNamesQuery(dbHelper.getReadableDatabase(), false);

        String[] foodDb = new String[foodNames.size()];
        foodDb = foodNames.toArray(foodDb);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, foodDb);
        binding.searchFood.setAdapter(adapter);

        binding.searchFood.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Food foodToAdd = dbHelper.getFoodNutritionsQuery(dbHelper.getReadableDatabase(), ((TextView) view).getText().toString()); //Food.getNutritionValues(((TextView) view).getText().toString());

                //pozriet co to znamena toten kontext
                final NumberPicker nPicker = new NumberPicker(FoodAddingActivity.this);
                nPicker.setMinValue(1);
                nPicker.setMaxValue(1000);

                AlertDialog.Builder builder = new AlertDialog.Builder(FoodAddingActivity.this);
                builder.setTitle(foodToAdd.getName());
                builder.setMessage("On 100g:\n" +
                        "Calories: " + foodToAdd.getCals() + "\n" +
                        "Fats: " + foodToAdd.getFats() + "\n" +
                        "Carbohydrates: " + foodToAdd.getCarbs() + "\n" +
                        "Proteins: " + foodToAdd.getProts());
                builder.setView(nPicker);
                builder.setPositiveButton("Add",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                data.setCalsCurrent(Math.round(foodToAdd.getCals() / 100 * (float) nPicker.getValue()));
                                data.setFatsCurrent(Math.round(foodToAdd.getFats() / 100 * (float) nPicker.getValue()));
                                data.setCarbsCurrent(Math.round(foodToAdd.getCarbs() / 100 * (float) nPicker.getValue()));
                                data.setProtsCurrent(Math.round(foodToAdd.getProts() / 100 * (float) nPicker.getValue()));
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_food_adding, menu);

        //NEDA SA TO NEJAK OBIST S POMOCOU VIEW BINDING???????????????????????
        MenuItem foodCategorySpinner = menu.findItem(R.id.action_category);
        AppCompatSpinner spinner = (AppCompatSpinner) foodCategorySpinner.getActionView();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.food_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Not doing anything. Just a demonstration of an activity being used as dialog.
        // Need to be further coded.
        if (item.getItemId() == R.id.action_filter) {
            Intent intent = new Intent(this, FilterDialogActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        NutritionDbHelper dbHelper = NutritionDbHelper.getInstance(this);
        dbHelper.close();
        super.onDestroy();
    }


}
