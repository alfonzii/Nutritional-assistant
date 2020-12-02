package cz.cuni.mff.nutritionalassistant.guidancebot;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import cz.cuni.mff.nutritionalassistant.FoodAddingActivity;
import cz.cuni.mff.nutritionalassistant.foodtypes.Food;
import cz.cuni.mff.nutritionalassistant.foodtypes.FoodLightweight;
import cz.cuni.mff.nutritionalassistant.localdatabase.NutritionDbHelper;

class DataSupplier {
    private List<FoodLightweight> foodLightweightParsedResponse;

    DataSupplier() {
        foodLightweightParsedResponse = new ArrayList<>();
    }

    List<FoodLightweight> requestFoodData(String query, Food.FoodType foodType, Context context) {
        return localDBrequest(query, context);
    }

    List<FoodLightweight> localDBrequest(String query, Context context) {
        final NutritionDbHelper dbHelper = NutritionDbHelper.getInstance(context);

        return dbHelper.getFoodLightweightListByNameQuery(
                dbHelper.getReadableDatabase(), query);
    }
}
