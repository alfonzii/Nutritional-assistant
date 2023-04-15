package cz.cuni.mff.nutritionalassistant;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
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
import cz.cuni.mff.nutritionalassistant.data.DataHolder;
import cz.cuni.mff.nutritionalassistant.data.PersistentStorage;
import cz.cuni.mff.nutritionalassistant.data.PersistentStorageBySharedPrefs;
import cz.cuni.mff.nutritionalassistant.databinding.ActivityMainBinding;
import cz.cuni.mff.nutritionalassistant.databinding.LayoutGeneratedFoodBinding;
import cz.cuni.mff.nutritionalassistant.foodtypes.Food;
import cz.cuni.mff.nutritionalassistant.guidancebot.Brain;
import cz.cuni.mff.nutritionalassistant.guidancebot.GeneratedFoodListCallback;
import cz.cuni.mff.nutritionalassistant.util.FormatUtil;
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
    private PersistentStorage storage;

    private MainViewModel mViewModel;

    //View binding object
    private ActivityMainBinding binding;

    @Setter
    private Food clickedFood;

    public static final String ACTION_EXAMINE_DETAILS =
            "cz.cuni.mff.nutritionalassistant.action.EXAMINE_DETAILS";
    public static final String EXTRA_SERIALIZABLE_FOOD =
            "cz.cuni.mff.nutritionalassistant.EXTRA_SERIALIZABLE_FOOD";

    @SuppressLint("SetTextI18n") //suppress setText warning
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataHolder = DataHolder.getInstance();
        storage = new PersistentStorageBySharedPrefs(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        binding.fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, FoodAddingActivity.class);
            startActivityForResult(intent, FOOD_REQUEST);
        });

        // Setup ViewModel
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        // Create observers
        mViewModel.getNutValuesTrigger().observe(this, aBoolean -> refreshValues());
        mViewModel.getGenFoodsTrigger().observe(this, aBoolean -> refreshGeneratedFoods());
        mViewModel.getUserAddTrigger().observe(this, aBoolean -> refreshUserAddedFoods());

        mViewModel.dateCheckInit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        storage.save();
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
                mViewModel.reset(true);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("SetTextI18n")
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

    @SuppressLint("SetTextI18n")
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

    private void refreshUserAddedFoods() {
        // clear user added foods frontend
        for (int meal = 0; meal < MealController.NUMBER_OF_MEALS; meal++) {
            int childCount = MealController.getLayoutFromMealID(binding, meal).getChildCount();
            MealController.getLayoutFromMealID(binding, meal).removeViews(2, childCount - 2);
        }

        for (int meal = 0; meal < MealController.NUMBER_OF_MEALS; meal++) {
            for (Food food : dataHolder.getUserAddedFoods().get(meal)) {
                View userAddedFoodView = createAddedFoodView(food);
                MealController.getLayoutFromMealID(binding, meal).addView(userAddedFoodView);
            }
        }
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

    @SuppressLint("SetTextI18n")
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

    public void regenerateButtonClick(View view) {
        List<Boolean> generatedFoodsChecked = new ArrayList<>();
        for (Pair<Food, Boolean> p : dataHolder.getGeneratedFoods()) {
            generatedFoodsChecked.add(p.second);
        }

        binding.content.progressBar.setVisibility(View.VISIBLE);
        binding.content.progressBar.setIndeterminate(true);
        disableCheckboxes();
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    Brain.getInstance().requestRegenerate(generatedFoodsChecked, getApplicationContext(), new GeneratedFoodListCallback() {
                        //private boolean alreadyFailed = false;

                        @Override
                        public void onSuccess(@NonNull List<Food> newGeneratedRecipes, List<Boolean> generatedFoodsFlags) {
                            //ListIterator<Pair<Food, Boolean>> it = dataHolder.getGeneratedFoods().listIterator();

                            for (int i = 0; i < MealController.NUMBER_OF_MEALS; i++) {
                                if (!generatedFoodsFlags.get(i)) {
                                    dataHolder.getGeneratedFoods().set(i, new Pair<>(newGeneratedRecipes.get(0), false));
                                    newGeneratedRecipes.remove(0);
                                }
                            }

                            refreshGeneratedFoods();
                            binding.content.progressBar.setIndeterminate(false);
                            binding.content.progressBar.setVisibility(View.GONE);
                            enableCheckboxes();
                        }

                        @Override
                        public void onFail(@NonNull Throwable throwable) {
                            MainActivity.this.runOnUiThread(() -> {
                                AlertDialog.Builder myAlertBuilder;
                                myAlertBuilder = new AlertDialog.Builder(MainActivity.this);
                                // Add the dialog buttons.
                                myAlertBuilder.setPositiveButton(getString(R.string.dismiss_en),
                                        (dialog, which) -> {}); // Click just dismisses and does nothing
                                myAlertBuilder.setTitle(getString(R.string.error_en));
                                myAlertBuilder.setMessage(
                                        getString(R.string.meal_plan_generation_exception_en) + throwable.getMessage());
                                // Create and show the AlertDialog.
                                myAlertBuilder.show();
                                binding.content.progressBar.setIndeterminate(false);
                                binding.content.progressBar.setVisibility(View.GONE);
                                enableCheckboxes();
                            });
                        }
                    });
                } catch (NullPointerException e) {
                    MainActivity.this.runOnUiThread(() -> {
                        AlertDialog.Builder myAlertBuilder;
                        myAlertBuilder = new AlertDialog.Builder(MainActivity.this);
                        // Add the dialog buttons.
                        myAlertBuilder.setPositiveButton(getString(R.string.dismiss_en),
                                (dialog, which) -> {}); // Dismiss button
                        myAlertBuilder.setTitle(getString(R.string.error_en));
                        myAlertBuilder.setMessage(getString(R.string.set_parameters_en));
                        // Create and show the AlertDialog.
                        myAlertBuilder.show();
                        binding.content.progressBar.setIndeterminate(false);
                        binding.content.progressBar.setVisibility(View.GONE);
                        enableCheckboxes();
                    });
                }
            }
        };
        t.start();

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



    // FRONT-END

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
                throw new IllegalStateException(getString(R.string.unexpected_value_en) + clickedFood.getFoodType());
        }
        intentFoodDetails.setAction(ACTION_EXAMINE_DETAILS);
        intentFoodDetails.putExtra(EXTRA_SERIALIZABLE_FOOD, clickedFood);
        startActivity(intentFoodDetails);
    }
    private void disableCheckboxes() {
        for (int i = 0; i < MealController.NUMBER_OF_MEALS; i++) {
            MealController.getGeneratedFoodBindingFromMealID(binding, i).checkBox.setEnabled(false);
        }
    }
    private void enableCheckboxes() {
        for (int i = 0; i < MealController.NUMBER_OF_MEALS; i++) {
            MealController.getGeneratedFoodBindingFromMealID(binding, i).checkBox.setEnabled(true);
        }
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



    //Not implemented - doing nothing
    /*public void exampleClick(View view) {
        Intent intent = new Intent(this, SwapActivity.class);
        startActivity(intent);
    }*/
}