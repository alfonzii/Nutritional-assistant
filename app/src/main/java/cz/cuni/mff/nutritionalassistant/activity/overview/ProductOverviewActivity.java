package cz.cuni.mff.nutritionalassistant.activity.overview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

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
        product.setFinalServingQuantity(Integer.parseInt(binding.numberQuantity.getText().toString()));
        product.setFinalServingUnit(binding.spinnerServingUnit.getSelectedItem().toString());
        product.setFinalServingWeight(Math.round(baseWeight * quantity));
        product.setFinalCalories(newCalories);
        product.setFinalFats(newFats);
        product.setFinalCarbohydrates(newCarbohydrates);
        product.setFinalProteins(newProteins);

        int meal = binding.spinnerMeal.getSelectedItemPosition();
        dataHolder.getEatenFood().get(meal).add(product);
        dataHolder.setLastAddedMeal(meal);

        dataHolder.setCaloriesCurrent(dataHolder.getCaloriesCurrent() + Math.round(newCalories));
        dataHolder.setFatsCurrent(dataHolder.getFatsCurrent() + Math.round(newFats));
        dataHolder.setCarbohydratesCurrent(dataHolder.getCarbohydratesCurrent() + Math.round(newCarbohydrates));
        dataHolder.setProteinsCurrent(dataHolder.getProteinsCurrent() + Math.round(newProteins));

        setResult(RESULT_OK);
        finish();
    }
}
