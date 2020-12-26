package cz.cuni.mff.nutritionalassistant.guidancebot;

import android.content.Context;

import java.util.HashMap;
import java.util.List;

import cz.cuni.mff.nutritionalassistant.foodtypes.Food;
import cz.cuni.mff.nutritionalassistant.foodtypes.FoodAdapterType;
import cz.cuni.mff.nutritionalassistant.guidancebot.api.AdapterDataCallback;
import cz.cuni.mff.nutritionalassistant.guidancebot.api.DetailedFoodCallback;
import cz.cuni.mff.nutritionalassistant.guidancebot.api.NutritionixDMS;
import cz.cuni.mff.nutritionalassistant.localdatabase.NutritionDbHelper;

class DataSupplier {
    private NutritionixDMS nutritionixDMS;

    DataSupplier() {
        nutritionixDMS = new NutritionixDMS();
    }

    List<FoodAdapterType> requestFoodAdapterTypeData(
            String query, Food.FoodType foodType, HashMap<Integer, Integer> nutritionFilterTable) {
        // TODO
        return null;
    }

    void requestProductAdapterTypeData(
            String query, HashMap<Integer, Integer> nutritionFilterTable, AdapterDataCallback callback) {
        nutritionixDMS.listProducts(query, callback);
    }

    List<FoodAdapterType> requestRecipeAdapterTypeData(String query, HashMap<Integer, Integer> nutritionFilterTable) {
        return null;
    }

    List<FoodAdapterType> requestRestaurantFoodAdapterTypeData(String query, HashMap<Integer, Integer> nutritionFilterTable) {
        return null;
    }

    void requestBrandedFoodDetailedInfo(String id, DetailedFoodCallback callback) {
        nutritionixDMS.getBrandedFoodDetails(id, callback);
    }

    Food localDetailedInfo(FoodAdapterType foodAdapterType, Context context) {
        final NutritionDbHelper dbHelper = NutritionDbHelper.getInstance(context);
        return dbHelper.getFoodDetailedInfo(
                dbHelper.getReadableDatabase(), foodAdapterType.getFoodName(), foodAdapterType.getFoodType());
    }

    List<FoodAdapterType> localDBrequest(String query, int foodTypeFilter, Context context) {
        final NutritionDbHelper dbHelper = NutritionDbHelper.getInstance(context);

        return dbHelper.getFoodLightweightListByNameQuery(
                dbHelper.getReadableDatabase(), query, foodTypeFilter);
    }
}
