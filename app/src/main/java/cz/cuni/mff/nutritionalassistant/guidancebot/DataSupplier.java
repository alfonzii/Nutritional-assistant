package cz.cuni.mff.nutritionalassistant.guidancebot;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import cz.cuni.mff.nutritionalassistant.foodtypes.Food;
import cz.cuni.mff.nutritionalassistant.foodtypes.FoodAdapterType;
import cz.cuni.mff.nutritionalassistant.localdatabase.NutritionDbHelper;

class DataSupplier {
    private List<FoodAdapterType> foodAdapterTypeParsedResponse;

    DataSupplier() {
        foodAdapterTypeParsedResponse = new ArrayList<>();
    }

    List<FoodAdapterType> requestFoodLightweightData(String query, Food.FoodType foodType, Context context) {
        // TODO
        return null;
    }

    Food requestFoodDetailedInfo(String detailedInfoURL){
        // TODO
        return null;
    }

    Food localDetailedInfo(FoodAdapterType foodAdapterType, Context context){
        final NutritionDbHelper dbHelper = NutritionDbHelper.getInstance(context);
        return dbHelper.getFoodDetailedInfo(
                dbHelper.getReadableDatabase(), foodAdapterType.getFoodName());
    }

    List<FoodAdapterType> localDBrequest(String query, Context context) {
        final NutritionDbHelper dbHelper = NutritionDbHelper.getInstance(context);

        return dbHelper.getFoodLightweightListByNameQuery(
                dbHelper.getReadableDatabase(), query);
    }
}
