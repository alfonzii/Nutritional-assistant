package cz.cuni.mff.nutritionalassistant.activity.overview;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Collections;

import cz.cuni.mff.nutritionalassistant.DataHolder;
import cz.cuni.mff.nutritionalassistant.MainActivity;
import cz.cuni.mff.nutritionalassistant.foodtypes.Product;
import cz.cuni.mff.nutritionalassistant.databinding.ActivityProductOverviewBinding;
import cz.cuni.mff.nutritionalassistant.utils.FormatUtil;

public class ProductOverviewActivity extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener {

    private ActivityProductOverviewBinding binding;

    private GeneralOverviewUtil overviewUtil;
    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductOverviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        product = (Product) getIntent().getSerializableExtra(MainActivity.EXTRA_SERIALIZABLE_FOOD);
        overviewUtil = new GeneralOverviewUtil(
                binding.textProductName, binding.textProductBrand, binding.numberQuantity,
                binding.spinnerServingUnit, binding.textWeightUnit, binding.spinnerMeal,
                binding.textCaloriesValue, binding.textFatsValue, binding.textCarbsValue, binding.textProteinsValue,
                product
        );

        overviewUtil.initialSetupGeneral();
        this.initialSetupSpecific();

        if (getIntent().getAction().equals(MainActivity.ACTION_EXAMINE_DETAILS)) {
            overviewUtil.examineDetailsSetupGeneral(this, binding.ConstraintLayoutHeader,
                    binding.textQuantity, binding.textMeal, binding.buttonAdd);
        } else { // equals(FoodViewHolder.ACTION_ADD_FOOD)
            overviewUtil.addFoodSetupGeneral();
            this.addFoodSetupSpecific();
        }
    }

    private void initialSetupSpecific() {
        if (product.getBrandName() != null) {
            binding.textProductBrand.setText(product.getBrandName());
        }
        if (product.getServingWeight() != null) {
            binding.textWeightUnit.setText(
                    "(" + FormatUtil.correctStringFormat(product.getServingWeight().get(0)) + " g)");
        }
    }

    private void addFoodSetupSpecific() {
        ArrayAdapter<String> adapterServingUnit = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, (product.getServingUnit()));

        adapterServingUnit.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerServingUnit.setAdapter(adapterServingUnit);
        binding.spinnerServingUnit.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        overviewUtil.onServingUnitSelected(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public void onAddButtonClick(View view) {
        overviewUtil.onAddButtonClickGeneral();
        setResult(RESULT_OK);
        finish();
    }
}
