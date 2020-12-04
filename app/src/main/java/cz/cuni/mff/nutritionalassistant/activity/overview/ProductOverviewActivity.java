package cz.cuni.mff.nutritionalassistant.activity.overview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;

import cz.cuni.mff.nutritionalassistant.FoodAddingActivity;
import cz.cuni.mff.nutritionalassistant.R;
import cz.cuni.mff.nutritionalassistant.foodtypes.Product;
import cz.cuni.mff.nutritionalassistant.databinding.ActivityProductOverviewBinding;

public class ProductOverviewActivity extends AppCompatActivity {

    private ActivityProductOverviewBinding binding;
    private Product product;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_overview);
        binding = ActivityProductOverviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initialSetup();

        binding.numberQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                float quantity = s.toString().equals("") ? 0 : Float.parseFloat(s.toString());
                float multiplicator = quantity / (float) product.getServingWeight().get(0);
                float newCalories = product.getCalories() * multiplicator;
                float newFats = product.getFats() * multiplicator;
                float newCarbohydrates = product.getCarbohydrates() * multiplicator;
                float newProteins = product.getProteins() * multiplicator;
                refreshValues(newCalories, newFats, newCarbohydrates, newProteins);
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

    }

    private void refreshValues(float newCalories, float newFats, float newCarbohydrates, float newProteins) {
        binding.textCaloriesValue.setText(String.valueOf(Math.round(newCalories)));
        binding.textFatsValue.setText(String.valueOf(Math.round(newFats)));
        binding.textCarbsValue.setText(String.valueOf(Math.round(newCarbohydrates)));
        binding.textProteinsValue.setText(String.valueOf(Math.round(newProteins)));
    }
}
