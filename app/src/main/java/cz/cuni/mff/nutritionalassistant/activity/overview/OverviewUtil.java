package cz.cuni.mff.nutritionalassistant.activity.overview;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import cz.cuni.mff.nutritionalassistant.DataHolder;
import cz.cuni.mff.nutritionalassistant.MainActivity;
import cz.cuni.mff.nutritionalassistant.foodtypes.Food;
import cz.cuni.mff.nutritionalassistant.foodtypes.Product;
import cz.cuni.mff.nutritionalassistant.foodtypes.Recipe;

/*
* We have an assumption, that there can be only these 2 scenarios with Product:
* 1. Null servingWeight implies no alt_measures.
* 2. Non-null servingWeight imples alt_measures must also have non-null serving weight.
*
* Provided this assumption is incorrect, then code can do unexpected things.
*/
class OverviewUtil implements AdapterView.OnItemSelectedListener {

    private TextView txtName, txtBrand;
    private EditText numberQuantity;
    private DynamicWidthSpinner spinnerServingUnit;
    private TextView txtWeight;
    private Spinner spinnerMeal;
    private TextView txtCaloriesValue, txtFatsValue, txtCarbohydratesValue, txtProteinsValue;

    private Context context;
    private DataHolder dataHolder = DataHolder.getInstance();
    private Food food;
    private Food.FoodType type;
    private String intentAction;

    private FoodSpecificOverviewUtil foodUtil;

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
            calculation();
            refreshValues();
        }
    };

    // Constructor for Product / RestaurantFood
    OverviewUtil(Context context, TextView txtName, TextView txtBrand, EditText numberQuantity,
                 DynamicWidthSpinner spinnerServingUnit, TextView txtWeight, Spinner spinnerMeal,
                 TextView txtCaloriesValue, TextView txtFatsValue, TextView txtCarbohydratesValue, TextView txtProteinsValue,
                 Food food, String intentAction) {
        this.context = context;
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
        type = food.getFoodType();
        quantity = ((Product) food).getServingQuantity().get(0);
        this.intentAction = intentAction;

        foodUtil = new ProductUtil(food);
    }

    // Constructor for Recipe
    OverviewUtil(TextView txtName, EditText numberQuantity, Spinner spinnerMeal,
                 TextView txtCaloriesValue, TextView txtFatsValue, TextView txtCarbohydratesValue, TextView txtProteinsValue,
                 Food food, String intentAction) {
        this.txtName = txtName;
        this.numberQuantity = numberQuantity;
        this.spinnerMeal = spinnerMeal;
        this.txtCaloriesValue = txtCaloriesValue;
        this.txtFatsValue = txtFatsValue;
        this.txtCarbohydratesValue = txtCarbohydratesValue;
        this.txtProteinsValue = txtProteinsValue;
        this.food = food;
        type = Food.FoodType.RECIPE;
        quantity = 1;
        this.intentAction = intentAction;

        foodUtil = new RecipeUtil(food);
    }

    // initial setup same for all overview activities
    private void initialSetupGeneral() {
        if (intentAction.equals(MainActivity.ACTION_EXAMINE_DETAILS)) {
            foodUtil.examineDetailsSetup();
        } else { // equals(FoodViewHolder.ACTION_ADD_FOOD)
            foodUtil.addFoodSetup();
            numberQuantity.addTextChangedListener(textWatcher);
        }
        txtName.setText(food.getFoodName());
        //binding.thumbnail SET IMAGE BUT ALWAYS DOWNLOADING IS NASTY. PROBABLY SAVE SOMEWHERE FROM
        // FOOD LIGHTWEIGHT WHERE IT IS ALREADY DOWNLOADED.

        numberQuantity.setText(correctStringFormat(quantity));
        txtCaloriesValue.setText(correctStringFormat(food.getCalories()));
        txtFatsValue.setText(correctStringFormat(food.getFats()));
        txtCarbohydratesValue.setText(correctStringFormat(food.getCarbohydrates()));
        txtProteinsValue.setText(correctStringFormat(food.getProteins()));

        foodUtil.initialSetupSpecific();
    }

    private void calculation() {
        float multiplicator = foodUtil.calculateMultiplicator();
        newCalories = food.getCalories() * multiplicator;
        newFats = food.getFats() * multiplicator;
        newCarbohydrates = food.getCarbohydrates() * multiplicator;
        newProteins = food.getProteins() * multiplicator;
    }

    private void refreshValues() {
        txtCaloriesValue.setText(correctStringFormat(newCalories));
        txtFatsValue.setText(correctStringFormat(newFats));
        txtCarbohydratesValue.setText(correctStringFormat(newCarbohydrates));
        txtProteinsValue.setText(correctStringFormat(newProteins));
        if(food.getFoodType() != Food.FoodType.RECIPE && ((Product)food).getServingWeight() != null) {
            txtWeight.setText("(" + correctStringFormat(baseWeight * quantity) + " g)");
        }
    }

    private interface FoodSpecificOverviewUtil {
        void initialSetupSpecific();
        void addFoodSetup();
        void examineDetailsSetup();
        float calculateMultiplicator();
    }

    private class ProductUtil implements FoodSpecificOverviewUtil, AdapterView.OnItemSelectedListener {
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
        public void addFoodSetup() {
            ArrayAdapter<String> adapterServingUnit = new ArrayAdapter<>(
                    context, android.R.layout.simple_spinner_item, (product.getServingUnit()));

            adapterServingUnit.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerServingUnit.setAdapter(adapterServingUnit);
            spinnerServingUnit.setOnItemSelectedListener(this);
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (product.getServingWeight() == null) {
                baseWeight = product.getServingQuantity().get(0);
            } else {
                baseWeight = product.getServingWeight().get(position) / product.getServingQuantity().get(position);
            }
            calculation();
            refreshValues();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }

        @Override
        public float calculateMultiplicator() {
            if (product.getServingWeight() == null) {
                return quantity / product.getServingQuantity().get(0);
            } else {
                return (quantity * baseWeight) / product.getServingWeight().get(0);
            }
        }
    }

    private class RecipeUtil implements FoodSpecificOverviewUtil {

        private Recipe recipe;

        RecipeUtil(Food food) {
            recipe = (Recipe) food;
        }

        @Override
        // Here can be initialization of ingredients and instructions for example
        public void initialSetupSpecific() {

        }

        @Override
        public void addFoodSetup() {
            baseWeight = recipe.getServingQuantity();
        }

        @Override
        public void examineDetailsSetup() {

        }

        @Override
        public float calculateMultiplicator() {
            return quantity / recipe.getServingQuantity();
        }
    }

    private float twoDecimalsRound(float num) {
        return Math.round(num * 100) / 100.0f;
    }

    // returns two digit decimal float number if num is decimal and returns integer if non-decimal
    private String correctStringFormat(float num) {
        final float EPSILON = 0.001f;

        if (Math.abs(Math.round(num) - num) < EPSILON) {
            return String.valueOf(Math.round(num));
        } else { // number is decimal
            return String.valueOf(twoDecimalsRound(num));
        }
    }
}
