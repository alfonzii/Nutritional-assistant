package cz.cuni.mff.nutritionalassistant;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import cz.cuni.mff.nutritionalassistant.activity.overview.ProductOverviewActivity;
import cz.cuni.mff.nutritionalassistant.databinding.ActivityMainBinding;
import cz.cuni.mff.nutritionalassistant.foodtypes.Food;
import cz.cuni.mff.nutritionalassistant.foodtypes.Product;

import static cz.cuni.mff.nutritionalassistant.Constants.FOOD_REQUEST;
import static cz.cuni.mff.nutritionalassistant.Constants.PARAMETERS_REQUEST;
import static cz.cuni.mff.nutritionalassistant.Constants.RESULT_AUTOMATIC_FAILURE;
import static cz.cuni.mff.nutritionalassistant.Constants.VALUES_REQUEST;

public class MainActivity extends AppCompatActivity {
    //Reference to singleton object
    private DataHolder dataHolder = DataHolder.getInstance();

    //View binding object
    private ActivityMainBinding binding;

    // Shared preferences object
    private SharedPreferences mPreferences;

    // Name of shared preferences file
    private final String sharedPrefFile =
            "cz.cuni.mff.nutritionalassistant";

    // Meal constants
    static final int BREAKFAST = 0;
    static final int LUNCH = 1;
    static final int DINNER = 2;
    static final int SNACK = 3;


    void refreshValues() {
        binding.content.textCaloriesValue.setText(
                Math.round(dataHolder.getCaloriesCurrent()) + "/" + Math.round(dataHolder.getCaloriesGoal()));
        binding.content.textFatsValue.setText(
                Math.round(dataHolder.getFatsCurrent()) + "/" + Math.round(dataHolder.getFatsGoal()));
        binding.content.textCarbsValue.setText(
                Math.round(dataHolder.getCarbohydratesCurrent()) + "/" + Math.round(dataHolder.getCarbohydratesGoal()));
        binding.content.textProteinsValue.setText(
                Math.round(dataHolder.getProteinsCurrent()) + "/" + Math.round(dataHolder.getProteinsGoal()));
    }

    @SuppressLint("SetTextI18n") //suppress setText warning
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FoodAddingActivity.class);
                startActivityForResult(intent, FOOD_REQUEST);
            }
        });

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

        dataHolder.setCaloriesGoal(mPreferences.getFloat("CALORIESGOAL", dataHolder.getCaloriesGoal()));
        dataHolder.setFatsGoal(mPreferences.getFloat("FATSGOAL", dataHolder.getFatsGoal()));
        dataHolder.setCarbohydratesGoal(mPreferences.getFloat("CARBOHYDRATESGOAL", dataHolder.getCarbohydratesGoal()));
        dataHolder.setProteinsGoal(mPreferences.getFloat("PROTEINSGOAL", dataHolder.getProteinsGoal()));
        dataHolder.setCaloriesCurrent(mPreferences.getFloat("CALORIESCURRENT", dataHolder.getCaloriesCurrent()));
        dataHolder.setFatsCurrent(mPreferences.getFloat("FATSCURRENT", dataHolder.getFatsCurrent()));
        dataHolder.setCarbohydratesCurrent(mPreferences.getFloat("CARBOHYDRATESCURRENT", dataHolder.getCaloriesCurrent()));
        dataHolder.setProteinsCurrent(mPreferences.getFloat("PROTEINSCURRENT", dataHolder.getProteinsCurrent()));

        dataHolder.setAge(mPreferences.getInt("AGE", dataHolder.getAge()));
        dataHolder.setWeight(mPreferences.getInt("WEIGHT", dataHolder.getWeight()));
        dataHolder.setHeight(mPreferences.getInt("HEIGHT", dataHolder.getHeight()));
        dataHolder.setSex(dataHolder.convertSex(mPreferences.getInt("SEX", dataHolder.convertSex(dataHolder.getSex()))));

        refreshValues();
    }

    /**
     * Callback for activity pause.  Shared preferences are saved here.
     */
    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putFloat("CALORIESGOAL", dataHolder.getCaloriesGoal());
        preferencesEditor.putFloat("FATSGOAL", dataHolder.getFatsGoal());
        preferencesEditor.putFloat("CARBOHYDRATESGOAL", dataHolder.getCarbohydratesGoal());
        preferencesEditor.putFloat("PROTEINSGOAL", dataHolder.getProteinsGoal());
        preferencesEditor.putFloat("CALORIESCURRENT", dataHolder.getCaloriesCurrent());
        preferencesEditor.putFloat("FATSCURRENT", dataHolder.getFatsCurrent());
        preferencesEditor.putFloat("CARBOHYDRATESCURRENT", dataHolder.getCarbohydratesCurrent());
        preferencesEditor.putFloat("PROTEINSCURRENT", dataHolder.getProteinsCurrent());

        preferencesEditor.putInt("AGE", dataHolder.getAge());
        preferencesEditor.putInt("WEIGHT", dataHolder.getWeight());
        preferencesEditor.putInt("HEIGHT", dataHolder.getHeight());
        preferencesEditor.putInt("SEX", dataHolder.convertSex(dataHolder.getSex()));

        preferencesEditor.apply();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_nutSettings:
                Intent intent = new Intent(this, NHSetActivity.class);
                startActivityForResult(intent, VALUES_REQUEST);
                break;

            case R.id.action_userParameters:
                Intent uParIntent = new Intent(this, UserParametersActivity.class);
                startActivityForResult(uParIntent, PARAMETERS_REQUEST);
                break;

            case R.id.action_resetCurrent:
                dataHolder.setCaloriesCurrent(0);
                dataHolder.setFatsCurrent(0);
                dataHolder.setCarbohydratesCurrent(0);
                dataHolder.setProteinsCurrent(0);
                refreshValues();
                break;

            case R.id.action_foodDatabase:
                Intent databaseIntent = new Intent(this, DatabaseActivity.class);
                startActivity(databaseIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("SetTextI18n") //suppress setText warning
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case VALUES_REQUEST:
                if (resultCode == RESULT_OK) {
                    //tu bolo pridavanie do values
                    refreshValues();
                } else if (resultCode == RESULT_AUTOMATIC_FAILURE) {
                    Intent uParIntent = new Intent(this, UserParametersActivity.class);
                    startActivityForResult(uParIntent, PARAMETERS_REQUEST);
                }
                break;

            case FOOD_REQUEST:
                if (resultCode == RESULT_OK) {
                    View newAddedFood = getLayoutInflater().inflate(R.layout.layout_added_food, null, false);
                    TextView txtNameAddedFood, txtWeightAddedFood, txtCaloriesAddedFood;
                    LinearLayout layout;
                    txtNameAddedFood = newAddedFood.findViewById(R.id.text_name_added_food);
                    txtWeightAddedFood = newAddedFood.findViewById(R.id.text_weight_added_food);
                    txtCaloriesAddedFood = newAddedFood.findViewById(R.id.text_calories_added_food);
                    layout = newAddedFood.findViewById(R.id.layout_added_food);
                    Food food = dataHolder.getLastEatenFood();
                    txtNameAddedFood.setText(food.getFoodName());
                    if (food.getFoodType() == Food.FoodType.PRODUCT) {
                        txtWeightAddedFood.setText(
                                ((Product) food).getServingQuantity().get(0) + " x " +
                                ((Product) food).getServingUnit().get(0) + " (" +
                                ((Product) food).getServingWeight().get(0) + " g)"
                        );
                    }
                    txtCaloriesAddedFood.setText(Math.round(food.getCalories()) + " cal");

                    layout.setOnTouchListener(new LinearLayoutTouchListener(
                            this, newAddedFood, food));

                    switch (dataHolder.getLastAddedMeal()) {
                        case BREAKFAST:
                            binding.content.LinearLayoutBreakfast.addView(newAddedFood);
                            break;
                        case LUNCH:
                            binding.content.LinearLayoutLunch.addView(newAddedFood);
                            break;
                        case DINNER:
                            binding.content.LinearLayoutDinner.addView(newAddedFood);
                            break;
                        case SNACK:
                            binding.content.LinearLayoutSnack.addView(newAddedFood);
                            break;
                    }

                    refreshValues();
                }
                break;

            case PARAMETERS_REQUEST:
                if (resultCode == RESULT_OK) {
                    /*int[] userParams = data.getFloatArrayExtra(UserParametersActivity.EXTRA_PARAMETERS);
                    sex = userParams[0];
                    age = userParams[1];
                    weight = userParams[2];
                    height = userParams[3];*/
                }
                break;
        }
    }

    public void exampleClick(View view) {
        Intent intent = new Intent(this, SwapActivity.class);
        startActivity(intent);
    }

    public void openProductOverviewActivity(View view) {
        //Intent intent = new Intent(this, ProductOverviewActivity.class);
        //startActivity(intent);
    }
}


