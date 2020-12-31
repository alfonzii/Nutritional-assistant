package cz.cuni.mff.nutritionalassistant;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Pair;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.cuni.mff.nutritionalassistant.activity.BaseAbstractActivity;
import cz.cuni.mff.nutritionalassistant.activity.overview.ProductOverviewActivity;
import cz.cuni.mff.nutritionalassistant.activity.overview.RecipeOverviewActivity;
import cz.cuni.mff.nutritionalassistant.activity.overview.RestaurantfoodOverviewActivity;
import cz.cuni.mff.nutritionalassistant.databinding.ActivityMainBinding;
import cz.cuni.mff.nutritionalassistant.databinding.LayoutGeneratedFoodBinding;
import cz.cuni.mff.nutritionalassistant.foodtypes.Food;
import cz.cuni.mff.nutritionalassistant.guidancebot.Brain;
import cz.cuni.mff.nutritionalassistant.guidancebot.GeneratedFoodListCallback;
import cz.cuni.mff.nutritionalassistant.util.FormatUtil;
import cz.cuni.mff.nutritionalassistant.util.MyGson;
import cz.cuni.mff.nutritionalassistant.util.listener.AddedFoodTouchListener;
import cz.cuni.mff.nutritionalassistant.util.listener.GeneratedFoodClickListener;
import lombok.Setter;

import static cz.cuni.mff.nutritionalassistant.Constants.FOOD_REQUEST;
import static cz.cuni.mff.nutritionalassistant.Constants.PARAMETERS_REQUEST;
import static cz.cuni.mff.nutritionalassistant.Constants.RESULT_AUTOMATIC_FAILURE;
import static cz.cuni.mff.nutritionalassistant.Constants.VALUES_REQUEST;

public class MainActivity extends BaseAbstractActivity {
    //Reference to singleton object
    private DataHolder dataHolder;

    //View binding object
    private ActivityMainBinding binding;

    @Setter
    private Food clickedFood;

    public static final String ACTION_EXAMINE_DETAILS =
            "cz.cuni.mff.nutritionalassistant.action.EXAMINE_DETAILS";
    public static final String EXTRA_SERIALIZABLE_FOOD =
            "cz.cuni.mff.nutritionalassistant.EXTRA_SERIALIZABLE_FOOD";


    public void refreshValues() {
        binding.content.textCaloriesValue.setText(
                Math.round(dataHolder.getCaloriesCurrent()) + "/" + Math.round(dataHolder.getCaloriesGoal()));
        binding.content.textFatsValue.setText(
                Math.round(dataHolder.getFatsCurrent()) + "/" + Math.round(dataHolder.getFatsGoal()));
        binding.content.textCarbsValue.setText(
                Math.round(dataHolder.getCarbohydratesCurrent()) + "/" + Math.round(dataHolder.getCarbohydratesGoal()));
        binding.content.textProteinsValue.setText(
                Math.round(dataHolder.getProteinsCurrent()) + "/" + Math.round(dataHolder.getProteinsGoal()));
    }

    private void reset() {
        reset(true);
    }

    private void reset(boolean isButtonClicked) {
        // clear user added foods frontend
        for (int meal = 0; meal < MealController.NUMBER_OF_MEALS; meal++) {
            int childCount = MealController.getLayoutFromMealID(binding, meal).getChildCount();
            MealController.getLayoutFromMealID(binding, meal).removeViews(2, childCount - 2);
        }
        // clear user added foods backend
        for (List<Food> mealList : dataHolder.getUserAddedFoods()) {
            mealList.clear();
        }
        // clear generatedFoods checkboxes frontend and backend flags
        for (int i = 0; i < MealController.NUMBER_OF_MEALS; i++) {
            LayoutGeneratedFoodBinding generatedFoodBinding = MealController.getGeneratedFoodBindingFromMealID(binding, i);
            generatedFoodBinding.checkBox.setChecked(false);
            Food genFood = dataHolder.getGeneratedFoods().get(i).first;
            dataHolder.getGeneratedFoods().set(i, new Pair<>(genFood, false));
        }

        // reset is called from MainActivity initialization
        if (!isButtonClicked) {
            if (dataHolder.getAdHocFlag() == DataHolder.AdHocFlag.NEXTDAY) {
                if (dataHolder.getCaloriesCurrent() - dataHolder.getCaloriesGoal() > 0) {
                    dataHolder.setCaloriesExcess(dataHolder.getCaloriesCurrent() - dataHolder.getCaloriesGoal());
                } else {
                    dataHolder.setCaloriesExcess(0);
                }
            } else {
                dataHolder.setCaloriesExcess(0);
            }
        }

        // set adhoc flag on UNSET
        dataHolder.setAdHocFlag(DataHolder.AdHocFlag.UNSET);

        // annulate NH values
        dataHolder.setCaloriesCurrent(0);
        dataHolder.setFatsCurrent(0);
        dataHolder.setCarbohydratesCurrent(0);
        dataHolder.setProteinsCurrent(0);
    }

    @SuppressLint("SetTextI18n") //suppress setText warning
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataHolder = DataHolder.getInstance();

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

        if (!dataHolder.getLastRunDate().equals(DateFormat.getDateInstance().format(new Date()))) {
            reset(false);
            Brain.getInstance().requestNHConstraintsCalculation(dataHolder.getCaloriesExcess());
        }

        dataHolder.setLastRunDate(DateFormat.getDateInstance().format(new Date()));

        refreshGeneratedFoods();
        refreshUserAddedFoods();
        refreshValues();
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences mPreferences = getSharedPreferences(SHARED_PREFERENCES_FILE, MODE_PRIVATE);

        SharedPreferences.Editor preferencesEditor = mPreferences.edit();

        String json = MyGson.PolymorphicGson.getInstance().toJson(dataHolder);
        preferencesEditor.putString(DataHolder.class.getName(), json);
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
                reset();
                refreshValues();
                break;

            case R.id.action_foodDatabase:
                Intent databaseIntent = new Intent(this, DatabaseActivity.class);
                startActivity(databaseIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint({"SetTextI18n"}) //suppress setText warning
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
                    Food food = dataHolder.getLastEatenFood();
                    View newAddedFood = createAddedFoodView(food);
                    MealController.getLayoutFromMealID(
                            binding, dataHolder.getLastAddedMeal()).addView(newAddedFood);

                    refreshValues();
                }
                break;

            case PARAMETERS_REQUEST:
                if (resultCode == RESULT_OK) {
                    refreshValues();
                }
                break;
        }
    }

    public void exampleClick(View view) {
        Intent intent = new Intent(this, SwapActivity.class);
        startActivity(intent);
    }

    private View createAddedFoodView(Food food) {
        View newAddedFood = getLayoutInflater().inflate(R.layout.layout_added_food, null, false);
        TextView txtNameAddedFood, txtWeightAddedFood, txtCaloriesAddedFood;
        LinearLayout layout;
        txtNameAddedFood = newAddedFood.findViewById(R.id.text_name_added_food);
        txtWeightAddedFood = newAddedFood.findViewById(R.id.text_weight_added_food);
        txtCaloriesAddedFood = newAddedFood.findViewById(R.id.text_calories_added_food);
        layout = newAddedFood.findViewById(R.id.layout_added_food);

        txtNameAddedFood.setText(food.getFoodName());
        if (food.getServingWeight() != null) {
            txtWeightAddedFood.setText(
                    FormatUtil.correctStringFormat(food.getServingQuantity().get(0)) + " x " +
                            food.getServingUnit().get(0) + " (" +
                            FormatUtil.correctStringFormat(food.getServingWeight().get(0)) + " g)"
            );
        } else { // null Product servingWeight
            txtWeightAddedFood.setText(
                    FormatUtil.correctStringFormat(food.getServingQuantity().get(0)) + " x " +
                            food.getServingUnit().get(0)
            );
        }
        txtCaloriesAddedFood.setText(Math.round(food.getCalories()) + " cal");

        layout.setOnTouchListener(new AddedFoodTouchListener(
                this, newAddedFood, food));

        return newAddedFood;
    }

    private void refreshUserAddedFoods() {
        for (int meal = 0; meal < MealController.NUMBER_OF_MEALS; meal++) {
            for (Food food : dataHolder.getUserAddedFoods().get(meal)) {
                View userAddedFoodView = createAddedFoodView(food);
                MealController.getLayoutFromMealID(binding, meal).addView(userAddedFoodView);
            }
        }
    }

    // LayoutAddedFood.onClick
    public void examineAddedFoodDetails(View view) {
        Intent intentFoodDetails;
        switch (clickedFood.getFoodType()) {
            case PRODUCT:
                intentFoodDetails = new Intent(this, ProductOverviewActivity.class);
                break;
            case RECIPE:
                intentFoodDetails = new Intent(this, RecipeOverviewActivity.class);
                break;
            case RESTAURANTFOOD:
                intentFoodDetails = new Intent(this, RestaurantfoodOverviewActivity.class);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + clickedFood.getFoodType());
        }
        intentFoodDetails.setAction(ACTION_EXAMINE_DETAILS);
        intentFoodDetails.putExtra(EXTRA_SERIALIZABLE_FOOD, clickedFood);
        startActivity(intentFoodDetails);
    }

    public void regenerateButtonClick(View view) {
        List<Boolean> generatedFoodsChecked = new ArrayList<>();
        for (Pair<Food, Boolean> p : dataHolder.getGeneratedFoods()) {
            generatedFoodsChecked.add(p.second);
        }

        binding.content.progressBar.setVisibility(View.VISIBLE);
        binding.content.progressBar.setIndeterminate(true);
        Thread t = new Thread() {
            @Override
            public void run() {
                Brain.getInstance().requestRegenerate(generatedFoodsChecked, getApplicationContext(), new GeneratedFoodListCallback() {
                    @Override
                    public void onSuccess(@NonNull List<Food> newGeneratedRecipes, List<Boolean> generatedFoodsFlags) {
                        //ListIterator<Pair<Food, Boolean>> it = dataHolder.getGeneratedFoods().listIterator();

                        for (int i = 0; i < MealController.NUMBER_OF_MEALS; i++) {
                            if (!generatedFoodsFlags.get(i)) {
                                dataHolder.getGeneratedFoods().set(i, new Pair<>(newGeneratedRecipes.get(0), false));
                                newGeneratedRecipes.remove(0);
                            }
                        }

                /*while (it.hasNext()) {
                    if (!it.next().second) {
                        it.set(new Pair<>(newGeneratedRecipes.get(0), false));
                        newGeneratedRecipes.remove(0);
                    }
                }*/
                        refreshGeneratedFoods();
                        binding.content.progressBar.setIndeterminate(false);
                        binding.content.progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFail(@NonNull Throwable throwable) {
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder myAlertBuilder;
                                myAlertBuilder = new AlertDialog.Builder(MainActivity.this);
                                // Add the dialog buttons.
                                myAlertBuilder.setPositiveButton("Dismiss",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        });
                                myAlertBuilder.setTitle("Error");
                                myAlertBuilder.setMessage(
                                        "Application wasn't able to generate meal plan for you because of following reason:\n" + throwable.getMessage());
                                // Create and show the AlertDialog.
                                myAlertBuilder.show();
                                binding.content.progressBar.setIndeterminate(false);
                                binding.content.progressBar.setVisibility(View.GONE);
                            }
                        });
                    }
                });
            }
        };
        t.start();

    }

    private void refreshGeneratedFoods() {
        for (int i = 0; i < MealController.NUMBER_OF_MEALS; i++) {
            Food genFood = dataHolder.getGeneratedFoods().get(i).first;
            boolean isChecked = dataHolder.getGeneratedFoods().get(i).second;
            LayoutGeneratedFoodBinding generatedFoodBinding = MealController.getGeneratedFoodBindingFromMealID(binding, i);
            generatedFoodBinding.textNameGeneratedFood.setText(genFood.getFoodName());
            generatedFoodBinding.textCaloriesGeneratedFood.setText(
                    FormatUtil.roundedStringFormat(genFood.getCalories()) + " cals");
            generatedFoodBinding.checkBox.setChecked(isChecked);
            //if (!dataHolder.getGeneratedFoods().get(i).second) {
            generatedFoodBinding.textNameGeneratedFood.setOnClickListener(
                    new GeneratedFoodClickListener(this, genFood));
            //}
        }
    }

    public void onCheckboxClick(View view) {
        int checkboxMealID = -1;
        checkboxMealID = MealController.getMealIDfromCheckbox(binding, view);

        Pair<Food, Boolean> genFood = dataHolder.getGeneratedFoods().get(checkboxMealID);

        if (((CheckBox) view).isChecked()) {
            dataHolder.addFoodToCurrentNH(genFood.first);
        } else {
            dataHolder.subtractFoodFromCurrentNH(genFood.first);
        }
        dataHolder.getGeneratedFoods().set(checkboxMealID, new Pair<>(genFood.first, !genFood.second));
        refreshValues();
    }


    // Controller class responsible for correct processing when interaction with meal layouts
    // is needed. This class exists because of modularity reasons. Should we ever need to increase
    // number of meals, only thing we need to change is content of methods here. Rest of code
    // will act accordingly to it and no changes should be required to make.
    public static class MealController {

        // Meal constants (Meal IDs)
        public static final int NUMBER_OF_MEALS = 4;
        public static final int BREAKFAST = 0;
        public static final int LUNCH = 1;
        public static final int DINNER = 2;
        public static final int SNACK = 3;

        public static int getMealIDfromCheckbox(ActivityMainBinding binding, View view) {
            if (view == binding.content.generatedFoodBreakfast.checkBox) {
                return BREAKFAST;
            } else if (view == binding.content.generatedFoodLunch.checkBox) {
                return LUNCH;
            } else if (view == binding.content.generatedFoodDinner.checkBox) {
                return DINNER;
            } else if (view == binding.content.generatedFoodSnack.checkBox) {
                return SNACK;
            } else { // ERROR
                throw new IllegalStateException("Unexpected value: " + view.getId());
            }
        }

        public static int getMealIDfromLayout(ViewGroup parent) {
            switch (parent.getId()) {
                case R.id.LinearLayout_breakfast:
                    return BREAKFAST;
                case R.id.LinearLayout_lunch:
                    return LUNCH;
                case R.id.LinearLayout_dinner:
                    return DINNER;
                case R.id.LinearLayout_snack:
                    return SNACK;
                default:
                    throw new IllegalStateException("Unexpected value: " + parent.getId());
            }
        }

        public static ViewGroup getLayoutFromMealID(ActivityMainBinding binding, int mealID) {
            LinearLayout layout = null;
            switch (mealID) {
                case BREAKFAST:
                    layout = binding.content.LinearLayoutBreakfast;
                    break;
                case LUNCH:
                    layout = binding.content.LinearLayoutLunch;
                    break;
                case DINNER:
                    layout = binding.content.LinearLayoutDinner;
                    break;
                case SNACK:
                    layout = binding.content.LinearLayoutSnack;
                    break;
            }
            return layout;
        }

        public static LayoutGeneratedFoodBinding getGeneratedFoodBindingFromMealID(ActivityMainBinding binding, int mealID) {
            LayoutGeneratedFoodBinding genFoodBinding = null;
            switch (mealID) {
                case BREAKFAST:
                    genFoodBinding = binding.content.generatedFoodBreakfast;
                    break;
                case LUNCH:
                    genFoodBinding = binding.content.generatedFoodLunch;
                    break;
                case DINNER:
                    genFoodBinding = binding.content.generatedFoodDinner;
                    break;
                case SNACK:
                    genFoodBinding = binding.content.generatedFoodSnack;
                    break;
            }
            return genFoodBinding;
        }
    }


}