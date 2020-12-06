package cz.cuni.mff.nutritionalassistant.activity.overview;

import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import cz.cuni.mff.nutritionalassistant.DataHolder;
import cz.cuni.mff.nutritionalassistant.FoodAddingActivity;
import cz.cuni.mff.nutritionalassistant.MainActivity;
import cz.cuni.mff.nutritionalassistant.R;
import cz.cuni.mff.nutritionalassistant.foodtypes.Product;
import cz.cuni.mff.nutritionalassistant.databinding.ActivityProductOverviewBinding;

public class ProductOverviewActivity extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener {

    private ActivityProductOverviewBinding binding;
    private Product product;
    private DataHolder dataHolder = DataHolder.getInstance();

    private float baseWeight;
    private float quantity = 100;
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
            quantity = s.toString().equals("") ? 0 : Float.parseFloat(s.toString());
            calculation();
            refreshValues();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_overview);
        binding = ActivityProductOverviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initialSetup();
    }


    private void initialSetup() {
        product = (Product) getIntent().getSerializableExtra(MainActivity.EXTRA_SERIALIZABLE_FOOD);

        if (getIntent().getAction().equals(MainActivity.ACTION_EXAMINE_DETAILS)) {
            examineDetailsSetup();
        } else { // equals(FoodViewHolder.ACTION_ADD_FOOD)
            addFoodSetup();
        }
        binding.textProductName.setText(product.getFoodName());
        //binding.thumbnail SET IMAGE BUT ALWAYS DOWNLOADING IS NASTY. PROBABLY SAVE SOMEWHERE FROM
        // FOOD LIGHTWEIGHT WHERE IT IS ALREADY DOWNLOADED.
        binding.numberQuantity.setText(product.getServingQuantity().get(0).toString());
        binding.textWeightUnit.setText("(" + product.getServingWeight().get(0) + " g)");
        binding.textCaloriesValue.setText(String.valueOf(Math.round(product.getCalories())));
        binding.textFatsValue.setText(String.valueOf(Math.round(product.getFats())));
        binding.textCarbsValue.setText(String.valueOf(Math.round(product.getCarbohydrates())));
        binding.textProteinsValue.setText(String.valueOf(Math.round(product.getProteins())));
    }

    private void examineDetailsSetup() {
        binding.buttonAdd.setEnabled(false);
        binding.buttonAdd.setVisibility(View.INVISIBLE);
        binding.spinnerMeal.setEnabled(false);
        binding.spinnerMeal.setVisibility(View.INVISIBLE);
        binding.spinnerServingUnit.setEnabled(false);
        binding.spinnerServingUnit.setVisibility(View.INVISIBLE);
        binding.textMeal.setVisibility(View.INVISIBLE);
        binding.numberQuantity.setEnabled(false);
        binding.numberQuantity.setVisibility(View.INVISIBLE);

        // Setup TextView instead of EditText Quantity number
        ConstraintLayout.LayoutParams params =
                (ConstraintLayout.LayoutParams) binding.numberQuantity.getLayoutParams();

        TextView txtQuantityEditReplacement = new TextView(this);
        txtQuantityEditReplacement.setId(1);

        ConstraintLayout.LayoutParams newParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT);

        newParams.setMarginStart(params.getMarginStart());
        newParams.startToEnd = params.startToEnd;
        newParams.bottomToBottom = binding.textQuantity.getId();

        txtQuantityEditReplacement.setText(product.getServingQuantity().get(0).toString());

        binding.ConstraintLayoutHeader.addView(txtQuantityEditReplacement, -1, newParams);

        // Setup TextView instead of serving unit spinner too (we don't need to change serving units)
        params = (ConstraintLayout.LayoutParams) binding.spinnerServingUnit.getLayoutParams();

        TextView txtServingUnit = new TextView(this);
        txtServingUnit.setId(2);

        newParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT);

        newParams.setMarginStart(8);
        newParams.bottomToBottom = params.bottomToBottom;
        newParams.startToEnd = txtQuantityEditReplacement.getId();
        newParams.topToTop = params.topToTop;

        txtServingUnit.setText(product.getServingUnit().get(0));

        binding.ConstraintLayoutHeader.addView(txtServingUnit, -1, newParams);

        // Rebind start constraint of weight text to serving unit TextView
        newParams = (ConstraintLayout.LayoutParams) binding.textWeightUnit.getLayoutParams();

        newParams.startToEnd = txtServingUnit.getId();
        newParams.setMarginStart(16);

        binding.textWeightUnit.setLayoutParams(newParams);
    }


    private void addFoodSetup() {
        ArrayAdapter<String> adapterServingUnit = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, product.getServingUnit());

        adapterServingUnit.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerServingUnit.setAdapter(adapterServingUnit);
        binding.spinnerServingUnit.setOnItemSelectedListener(this);
        binding.numberQuantity.addTextChangedListener(textWatcher);
    }

    private void calculation() {
        float multiplicator = (quantity * baseWeight) / (float) product.getServingWeight().get(0);
        newCalories = product.getCalories() * multiplicator;
        newFats = product.getFats() * multiplicator;
        newCarbohydrates = product.getCarbohydrates() * multiplicator;
        newProteins = product.getProteins() * multiplicator;
    }

    private void refreshValues() {
        binding.textCaloriesValue.setText(String.valueOf(Math.round(newCalories)));
        binding.textFatsValue.setText(String.valueOf(Math.round(newFats)));
        binding.textCarbsValue.setText(String.valueOf(Math.round(newCarbohydrates)));
        binding.textProteinsValue.setText(String.valueOf(Math.round(newProteins)));
        binding.textWeightUnit.setText("(" + Math.round(baseWeight * quantity) + " g)");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        baseWeight = (float) product.getServingWeight().get(position) / (float) product.getServingQuantity().get(position);
        calculation();
        refreshValues();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void addFoodToManager(View view) {
        // We reuse Product object to save info about eaten food into eatenFood structure.
        // Now, Food cals, fats, carbs and prots are actual values of given product nutritional values
        // in regard to it's serving unit and quantity (weight), which are stored in zero index of
        // according lists (no more reference values, because as food is already eaten, we don't
        // need reference anymore).
        product.setServingQuantity(Collections.singletonList(Integer.parseInt(binding.numberQuantity.getText().toString())));
        product.setServingUnit(Collections.singletonList(binding.spinnerServingUnit.getSelectedItem().toString()));
        product.setServingWeight(Collections.singletonList(Math.round(baseWeight * quantity)));
        product.setCalories(newCalories);
        product.setFats(newFats);
        product.setCarbohydrates(newCarbohydrates);
        product.setProteins(newProteins);

        int meal = binding.spinnerMeal.getSelectedItemPosition();
        dataHolder.getEatenFood().get(meal).add(product);
        dataHolder.setLastAddedMeal(meal);

        dataHolder.setCaloriesCurrent(dataHolder.getCaloriesCurrent() + newCalories);
        dataHolder.setFatsCurrent(dataHolder.getFatsCurrent() + newFats);
        dataHolder.setCarbohydratesCurrent(dataHolder.getCarbohydratesCurrent() + newCarbohydrates);
        dataHolder.setProteinsCurrent(dataHolder.getProteinsCurrent() + newProteins);

        setResult(RESULT_OK);
        finish();
    }
}
