package cz.cuni.mff.nutritionalassistant.guidancebot;

import android.content.Context;

import org.json.JSONException;

import java.util.HashMap;
import java.util.List;

import cz.cuni.mff.nutritionalassistant.foodtypes.Food;
import cz.cuni.mff.nutritionalassistant.foodtypes.FoodAdapterType;
import cz.cuni.mff.nutritionalassistant.guidancebot.api.AdapterDataCallback;
import cz.cuni.mff.nutritionalassistant.guidancebot.api.DetailedFoodCallback;
import cz.cuni.mff.nutritionalassistant.guidancebot.api.Nutritionix.NutritionixDMS;
import cz.cuni.mff.nutritionalassistant.guidancebot.api.Spoonacular.SpoonacularDMS;
import cz.cuni.mff.nutritionalassistant.localdatabase.NutritionDbHelper;

class DataSupplier {
    private NutritionixDMS nutritionixDMS;
    private SpoonacularDMS spoonacularDMS;

    DataSupplier() {
        nutritionixDMS = new NutritionixDMS();
        spoonacularDMS = new SpoonacularDMS();
    }

    List<FoodAdapterType> requestFoodAdapterTypeData(
            String query, Food.FoodType foodType, HashMap<Integer, Integer> nutritionFilterTable) {
        // TODO
        return null;
    }

    void requestProductAdapterTypeData(
            String query, HashMap<Integer, Integer> nutritionFilterTable, AdapterDataCallback callback) {
        try {
            nutritionixDMS.listProducts(query, nutritionFilterTable, callback);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void requestRecipeAdapterTypeData(String query, HashMap<Integer, Integer> nutritionFilterTable, AdapterDataCallback callback) {
        spoonacularDMS.listRecipes(query, nutritionFilterTable, callback);
    }

    List<FoodAdapterType> requestRestaurantFoodAdapterTypeData(String query, HashMap<Integer, Integer> nutritionFilterTable) {
        return null;
    }

    void requestProductDetailedInfo(FoodAdapterType product, DetailedFoodCallback callback) {
        nutritionixDMS.getProductDetails(product, callback);
    }

    void requestRecipeDetailedInfo(FoodAdapterType recipe, DetailedFoodCallback callback) {
        spoonacularDMS.getRecipeDetails(recipe, callback);
    }

//-----------------------------------------------------------------------------------------------------------------------------------

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
