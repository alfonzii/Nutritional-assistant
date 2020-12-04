package cz.cuni.mff.nutritionalassistant.activity.overview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import cz.cuni.mff.nutritionalassistant.DataHolder;
import cz.cuni.mff.nutritionalassistant.FoodAddingActivity;
import cz.cuni.mff.nutritionalassistant.R;
import cz.cuni.mff.nutritionalassistant.foodtypes.Product;
import cz.cuni.mff.nutritionalassistant.databinding.ActivityProductOverviewBinding;

public class ProductOverviewActivity extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener{

    private ActivityProductOverviewBinding binding;
    private Product product;
    private DataHolder dataHolder = DataHolder.getInstance();

    private float baseWeight;
    private float quantity = 100;
    private float newCalories;
    private float newFats;
    private float newCarbohydrates;
    private float newProteins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_overview);
        binding = ActivityProductOverviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initialSetup();
        binding.spinnerServingUnit.setOnItemSelectedListener(this);

        binding.numberQuantity.addTextChangedListener(new TextWatcher() {
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
        });
    }


    private void initialSetup() {
        product = (Product) getIntent().getSerializableExtra(FoodAddingActivity.EXTRA_SERIALIZABLE_FOOD);

        ArrayAdapter<String> adapterServingUnit = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, product.getServingUnit());

        adapterServingUnit.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerServingUnit.setAdapter(adapterServingUnit);

        binding.textProductName.setText(product.getFoodName());
        //binding.thumbnail SET IMAGE BUT ALWAYS DOWNLOADING IS NASTY. PROBABLY SAVE SOMEWHERE FROM
        // FOOD LIGHTWEIGHT WHERE IT IS ALREADY DOWNLOADED.
        binding.numberQuantity.setText(product.getServingQuantity().get(0).toString());
        binding.textWeightUnit.setText(product.getServingWeight().get(0) + " g");
        binding.textCaloriesValue.setText(String.valueOf(Math.round(product.getCalories())));
        binding.textFatsValue.setText(String.valueOf(Math.round(product.getFats())));
        binding.textCarbsValue.setText(String.valueOf(Math.round(product.getCarbohydrates())));
        binding.textProteinsValue.setText(String.valueOf(Math.round(product.getProteins())));
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
        binding.textWeightUnit.setText(Math.round(baseWeight * quantity) + " g");
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
        product.setServingQuantity(new ArrayList<Integer>() {{add(Integer.parseInt(binding.numberQuantity.getText().toString()));}});
        product.setServingUnit(new ArrayList<String>() {{add(binding.spinnerServingUnit.getSelectedItem().toString());}});
        product.setServingWeight(new ArrayList<Integer>() {{add(Math.round(baseWeight * quantity));}});
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
