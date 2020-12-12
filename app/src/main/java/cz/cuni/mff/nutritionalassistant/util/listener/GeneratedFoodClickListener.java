package cz.cuni.mff.nutritionalassistant.util.listener;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import cz.cuni.mff.nutritionalassistant.MainActivity;
import cz.cuni.mff.nutritionalassistant.activity.overview.RecipeOverviewActivity;
import cz.cuni.mff.nutritionalassistant.activity.overview.RestaurantfoodOverviewActivity;
import cz.cuni.mff.nutritionalassistant.foodtypes.Food;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GeneratedFoodClickListener implements View.OnClickListener {

    private Activity activity;
    private Food genFood;

    @Override
    public void onClick(View v) {
        Intent intent;
        if (genFood.getFoodType() == Food.FoodType.RECIPE) {
            intent = new Intent(activity, RecipeOverviewActivity.class);
        } else if (genFood.getFoodType() == Food.FoodType.RESTAURANTFOOD) {
            intent = new Intent(activity, RestaurantfoodOverviewActivity.class);
        } else {
            throw new IllegalStateException("Unexpected value: " + genFood.getFoodType());
        }
        intent.setAction(MainActivity.ACTION_EXAMINE_DETAILS);
        intent.putExtra(MainActivity.EXTRA_SERIALIZABLE_FOOD, genFood);
        activity.startActivity(intent);
    }
}
