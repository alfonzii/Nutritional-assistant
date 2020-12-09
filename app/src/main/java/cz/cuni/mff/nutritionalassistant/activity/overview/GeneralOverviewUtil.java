package cz.cuni.mff.nutritionalassistant.activity.overview;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Collections;

import cz.cuni.mff.nutritionalassistant.DataHolder;
import cz.cuni.mff.nutritionalassistant.MainActivity;
import cz.cuni.mff.nutritionalassistant.foodtypes.Food;
import cz.cuni.mff.nutritionalassistant.foodtypes.Product;
import cz.cuni.mff.nutritionalassistant.foodtypes.Recipe;

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
    }

    // initial setup same for all overview activities
    void initialSetupGeneral() {
        txtName.setText(food.getFoodName());
        //binding.thumbnail SET IMAGE BUT ALWAYS DOWNLOADING IS NASTY. PROBABLY SAVE SOMEWHERE FROM
        // FOOD LIGHTWEIGHT WHERE IT IS ALREADY DOWNLOADED.

        numberQuantity.setText(correctStringFormat(quantity));
        txtCaloriesValue.setText(correctStringFormat(food.getCalories()));
        txtFatsValue.setText(correctStringFormat(food.getFats()));
        txtCarbohydratesValue.setText(correctStringFormat(food.getCarbohydrates()));
        txtProteinsValue.setText(correctStringFormat(food.getProteins()));
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
        txtCaloriesValue.setText(correctStringFormat(newCalories));
        txtFatsValue.setText(correctStringFormat(newFats));
        txtCarbohydratesValue.setText(correctStringFormat(newCarbohydrates));
        txtProteinsValue.setText(correctStringFormat(newProteins));
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
        dataHolder.getEatenFood().get(meal).add(food);
        dataHolder.setLastAddedMeal(meal);

        dataHolder.setCaloriesCurrent(dataHolder.getCaloriesCurrent() + newCalories);
        dataHolder.setFatsCurrent(dataHolder.getFatsCurrent() + newFats);
        dataHolder.setCarbohydratesCurrent(dataHolder.getCarbohydratesCurrent() + newCarbohydrates);
        dataHolder.setProteinsCurrent(dataHolder.getProteinsCurrent() + newProteins);
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

    /*private class ProductUtil implements FoodSpecificOverviewUtil, AdapterView.OnItemSelectedListener {
        private Product product;

        ProductUtil(Food food) {
            product = (Product) food;
        }

        @Override
        public void initialSetupSpecific() {
            if (product.getBrandName() != null) {
                txtBrand.setText(((Product) food).getBrandName());
            }
            if (product.getServingWeight() != null) {
                txtWeight.setText("(" + correctStringFormat(product.getServingWeight().get(0)) + " g)");
            }
        }

        @Override
        public void addFoodSetupSpecific() {
            ArrayAdapter<String> adapterServingUnit = new ArrayAdapter<>(
                    context, android.R.layout.simple_spinner_item, (product.getServingUnit()));

            adapterServingUnit.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerServingUnit.setAdapter(adapterServingUnit);
            spinnerServingUnit.setOnItemSelectedListener(this);
        }

        @Override
        public void examineDetailsSetupSpecific() {

        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            calculateBaseWeight(position);
            calculateNH();
            refreshValues();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }

        @Override
        public float calculateMultiplicatorSpecific() {
            if (product.getServingWeight() == null) {
                return quantity / product.getServingQuantity().get(0);
            } else {
                return (quantity * baseWeight) / product.getServingWeight().get(0);
            }
        }

        @Override
        public void addFoodToManagerSpecific() {
            product.setServingQuantity(Collections.singletonList(Float.parseFloat(numberQuantity.getText().toString())));
            product.setServingUnit(Collections.singletonList(spinnerServingUnit.getSelectedItem().toString()));
            if (product.getServingWeight() != null) {
                product.setServingWeight(Collections.singletonList(baseWeight * quantity));
            }
        }
    }*/

    private float twoDecimalsRound(float num) {
        return Math.round(num * 100) / 100.0f;
    }

    // returns two digit decimal float number if num is decimal and returns integer if non-decimal
    String correctStringFormat(float num) {
        final float EPSILON = 0.001f;

        if (Math.abs(Math.round(num) - num) < EPSILON) {
            return String.valueOf(Math.round(num));
        } else { // number is decimal
            return String.valueOf(twoDecimalsRound(num));
        }
    }
}
