package cz.cuni.mff.nutritionalassistant.activity.overview;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Collections;

import cz.cuni.mff.nutritionalassistant.DataHolder;
import cz.cuni.mff.nutritionalassistant.foodtypes.Food;
import cz.cuni.mff.nutritionalassistant.utils.FormatUtil;

import static cz.cuni.mff.nutritionalassistant.utils.FormatUtil.correctStringFormat;
import static cz.cuni.mff.nutritionalassistant.utils.FormatUtil.roundedStringFormat;

/*
 * This util packs methods which are common for all overview activities.
 *
 *
 * We have an assumption, that there can be only these 2 scenarios with Food:
 * 1. Null servingWeight implies no alt_measures.
 * 2. Non-null servingWeight imples alt_measures must also have non-null serving weight.
 *
 * Provided this assumption is incorrect, then code can do unexpected things.
 */
class GeneralOverviewUtil {

    private TextView txtName, txtBrand;
    private EditText numberQuantity;
    private DynamicWidthSpinner spinnerServingUnit;
    private TextView txtWeight;
    private Spinner spinnerMeal;
    private TextView txtCaloriesValue, txtFatsValue, txtCarbohydratesValue, txtProteinsValue;
    private Button buttonAdd;

    private DataHolder dataHolder = DataHolder.getInstance();
    private Food food;

    private float baseWeight;
    private float quantity;
    private float newCalories;
    private float newFats;
    private float newCarbohydrates;
    private float newProteins;

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            // Float.parseFloat() should not produce exception when parsing string like "X."
            quantity = s.toString().equals("") ? 0 : Float.parseFloat(s.toString());
            calculateNH();
            refreshValues();
        }
    };

    // Constructor for Product / RestaurantFood
    GeneralOverviewUtil(TextView txtName, TextView txtBrand, EditText numberQuantity,
                        DynamicWidthSpinner spinnerServingUnit, TextView txtWeight, Spinner spinnerMeal,
                        TextView txtCaloriesValue, TextView txtFatsValue, TextView txtCarbohydratesValue, TextView txtProteinsValue,
                        Food food) {
        this.txtName = txtName;
        this.txtBrand = txtBrand;
        this.numberQuantity = numberQuantity;
        this.spinnerServingUnit = spinnerServingUnit;
        this.txtWeight = txtWeight;
        this.spinnerMeal = spinnerMeal;
        this.txtCaloriesValue = txtCaloriesValue;
        this.txtFatsValue = txtFatsValue;
        this.txtCarbohydratesValue = txtCarbohydratesValue;
        this.txtProteinsValue = txtProteinsValue;
        this.food = food;
        quantity = food.getServingQuantity().get(0);
    }

    // Constructor for Recipe
    GeneralOverviewUtil(TextView txtName, EditText numberQuantity, Spinner spinnerMeal,
                        TextView txtCaloriesValue, TextView txtFatsValue, TextView txtCarbohydratesValue, TextView txtProteinsValue,
                        Food food) {
        this.txtName = txtName;
        this.numberQuantity = numberQuantity;
        this.spinnerMeal = spinnerMeal;
        this.txtCaloriesValue = txtCaloriesValue;
        this.txtFatsValue = txtFatsValue;
        this.txtCarbohydratesValue = txtCarbohydratesValue;
        this.txtProteinsValue = txtProteinsValue;
        this.food = food;
        quantity = 1;
        newCalories = food.getCalories();
        newFats = food.getFats();
        newCarbohydrates = food.getCarbohydrates();
        newProteins = food.getProteins();
    }

    // initial setup same for all overview activities
    void initialSetupGeneral() {
        txtName.setText(food.getFoodName());
        //binding.thumbnail SET IMAGE BUT ALWAYS DOWNLOADING IS NASTY. PROBABLY SAVE SOMEWHERE FROM
        // FOOD LIGHTWEIGHT WHERE IT IS ALREADY DOWNLOADED.

        numberQuantity.setText(roundedStringFormat(quantity));
        txtCaloriesValue.setText(roundedStringFormat(food.getCalories()));
        txtFatsValue.setText(roundedStringFormat(food.getFats()));
        txtCarbohydratesValue.setText(roundedStringFormat(food.getCarbohydrates()));
        txtProteinsValue.setText(roundedStringFormat(food.getProteins()));
    }

    void addFoodSetupGeneral() {
        calculateBaseWeight(0);
        numberQuantity.addTextChangedListener(textWatcher);
    }

    private void calculateNH() {
        float multiplicator;
        if (food.getServingWeight() == null) {
            multiplicator = quantity / food.getServingQuantity().get(0);
        } else {
            multiplicator = (quantity * baseWeight) / food.getServingWeight().get(0);
        }
        newCalories = food.getCalories() * multiplicator;
        newFats = food.getFats() * multiplicator;
        newCarbohydrates = food.getCarbohydrates() * multiplicator;
        newProteins = food.getProteins() * multiplicator;
    }

    private void calculateBaseWeight(int position) {
        if (food.getServingWeight() == null) {
            baseWeight = food.getServingQuantity().get(0);
        } else {
            baseWeight = food.getServingWeight().get(position) / food.getServingQuantity().get(position);
        }
    }

    void onServingUnitSelected(int position) {
        calculateBaseWeight(position);
        calculateNH();
        refreshValues();
    }

    private void refreshValues() {
        txtCaloriesValue.setText(roundedStringFormat(newCalories));
        txtFatsValue.setText(roundedStringFormat(newFats));
        txtCarbohydratesValue.setText(roundedStringFormat(newCarbohydrates));
        txtProteinsValue.setText(roundedStringFormat(newProteins));
        if (food.getServingWeight() != null) {
            txtWeight.setText("(" + correctStringFormat(baseWeight * quantity) + " g)");
        }
    }

    void onAddButtonClickGeneral() {
        food.setServingQuantity(Collections.singletonList(Float.parseFloat(numberQuantity.getText().toString())));
        // Not general, but too simple to divide code
        if (food.getFoodType() != Food.FoodType.RECIPE) {
            food.setServingUnit(Collections.singletonList(spinnerServingUnit.getSelectedItem().toString()));
        }
        if (food.getServingWeight() != null) {
            food.setServingWeight(Collections.singletonList(baseWeight * quantity));
        }
        food.setCalories(newCalories);
        food.setFats(newFats);
        food.setCarbohydrates(newCarbohydrates);
        food.setProteins(newProteins);

        int meal = spinnerMeal.getSelectedItemPosition();
        dataHolder.getUserAddedFoods().get(meal).add(food);
        dataHolder.setLastAddedMeal(meal);

        dataHolder.addFoodToCurrentNH(food);
    }

    void examineDetailsSetupGeneral(Context context, ConstraintLayout layoutHeader, TextView txtQuantity, TextView txtMeal, Button buttonAdd) {
        buttonAdd.setEnabled(false);
        buttonAdd.setVisibility(View.INVISIBLE);
        spinnerMeal.setEnabled(false);
        spinnerMeal.setVisibility(View.INVISIBLE);
        if (food.getFoodType() != Food.FoodType.RECIPE) {
            spinnerServingUnit.setEnabled(false);
            spinnerServingUnit.setVisibility(View.INVISIBLE);
        }
        txtMeal.setVisibility(View.INVISIBLE);
        numberQuantity.setEnabled(false);
        numberQuantity.setVisibility(View.INVISIBLE);

        // Setup TextView instead of EditText Quantity number
        ConstraintLayout.LayoutParams params =
                (ConstraintLayout.LayoutParams) numberQuantity.getLayoutParams();

        TextView txtQuantityEditReplacement = new TextView(context);
        txtQuantityEditReplacement.setId(1);

        ConstraintLayout.LayoutParams newParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT);

        newParams.setMarginStart(params.getMarginStart());
        newParams.startToEnd = params.startToEnd;
        newParams.bottomToBottom = txtQuantity.getId();

        txtQuantityEditReplacement.setText(food.getServingQuantity().get(0).toString());

        layoutHeader.addView(txtQuantityEditReplacement, -1, newParams);


        // this should not be here, as it's not generalized
        if (food.getFoodType() != Food.FoodType.RECIPE) {
            // Setup TextView instead of serving unit spinner too (we don't need to change serving units)
            params = (ConstraintLayout.LayoutParams) spinnerServingUnit.getLayoutParams();

            TextView txtServingUnit = new TextView(context);
            txtServingUnit.setId(2);

            newParams = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT);

            newParams.setMarginStart(8);
            newParams.bottomToBottom = params.bottomToBottom;
            newParams.startToEnd = txtQuantityEditReplacement.getId();
            newParams.topToTop = params.topToTop;

            txtServingUnit.setText(food.getServingUnit().get(0));

            layoutHeader.addView(txtServingUnit, -1, newParams);

            if (food.getServingWeight() != null) {
                // Rebind start constraint of weight text to serving unit TextView
                newParams = (ConstraintLayout.LayoutParams) txtWeight.getLayoutParams();

                newParams.startToEnd = txtServingUnit.getId();
                newParams.setMarginStart(16);

                txtWeight.setLayoutParams(newParams);
            }
        }
    }
}
