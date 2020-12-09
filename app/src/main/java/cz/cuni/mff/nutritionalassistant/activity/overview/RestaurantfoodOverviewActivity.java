package cz.cuni.mff.nutritionalassistant.activity.overview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import cz.cuni.mff.nutritionalassistant.MainActivity;
import cz.cuni.mff.nutritionalassistant.databinding.ActivityRestaurantfoodOverviewBinding;
import cz.cuni.mff.nutritionalassistant.foodtypes.RestaurantFood;

public class RestaurantfoodOverviewActivity extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener {

    private ActivityRestaurantfoodOverviewBinding binding;

    private GeneralOverviewUtil overviewUtil;
    private RestaurantFood restaurantFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRestaurantfoodOverviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        restaurantFood = (RestaurantFood) getIntent().getSerializableExtra(MainActivity.EXTRA_SERIALIZABLE_FOOD);
        overviewUtil = new GeneralOverviewUtil(
                binding.textRestaurantFoodName, binding.textRestaurantName, binding.numberQuantity,
                binding.spinnerServingUnit, binding.textWeightUnit, binding.spinnerMeal,
                binding.textCaloriesValue, binding.textFatsValue, binding.textCarbsValue, binding.textProteinsValue,
                restaurantFood
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
        if (restaurantFood.getBrandName() != null) {
            binding.textRestaurantFoodName.setText(restaurantFood.getBrandName());
        }
        if (restaurantFood.getServingWeight() != null) {
            binding.textWeightUnit.setText(
                    "(" + overviewUtil.correctStringFormat(restaurantFood.getServingWeight().get(0)) + " g)");
        }
    }

    private void addFoodSetupSpecific() {
        ArrayAdapter<String> adapterServingUnit = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, (restaurantFood.getServingUnit()));

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
