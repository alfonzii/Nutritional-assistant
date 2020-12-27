package cz.cuni.mff.nutritionalassistant.guidancebot;

import android.content.Context;

import java.util.HashMap;
import java.util.List;

import cz.cuni.mff.nutritionalassistant.Constants;
import cz.cuni.mff.nutritionalassistant.foodtypes.Food;
import cz.cuni.mff.nutritionalassistant.foodtypes.FoodAdapterType;
import cz.cuni.mff.nutritionalassistant.foodtypes.Product;
import cz.cuni.mff.nutritionalassistant.foodtypes.ProductAdapterType;
import cz.cuni.mff.nutritionalassistant.guidancebot.api.AdapterDataCallback;
import cz.cuni.mff.nutritionalassistant.guidancebot.api.DetailedFoodCallback;

public final class Brain {
    private DataSupplier dataSupplier;
    private Generator generator;
    private Mathematics mathematics;

    private static final Brain INSTANCE = new Brain();

    private Brain() {
        dataSupplier = new DataSupplier();
        generator = new Generator();
        mathematics = new Mathematics();
    }

    public static Brain getInstance() {
        return INSTANCE;
    }

    public void requestFoodAdapterTypeData(
            String query, int foodTypeFilter, HashMap<Integer, Integer> nutritionFilterTable, AdapterDataCallback callbacks) {

        if (foodTypeFilter == Food.FoodType.PRODUCT.getId()) {
            dataSupplier.requestProductAdapterTypeData(query, nutritionFilterTable, callbacks);
        } else if (foodTypeFilter == Food.FoodType.RECIPE.getId()) {
            dataSupplier.requestRecipeAdapterTypeData(query, nutritionFilterTable, callbacks);
        }
        //return dataSupplier.localDBrequest(query, foodTypeFilter, context);
    }


    public void requestFoodDetailedInfo(FoodAdapterType foodAdapterType, DetailedFoodCallback callback) {
        //return dataSupplier.localDetailedInfo(foodAdapterType, context);
        if (foodAdapterType.getFoodType() == Food.FoodType.PRODUCT) {
            dataSupplier.requestProductDetailedInfo(foodAdapterType, callback);
        }
    }


    // To be used API
    public List<FoodAdapterType> requestFoodAdapterTypeData(
            String query, int foodTypeFilter, HashMap<Integer, Integer> nutritionFilterTable) {
        return null;
    }

    public Food requestFoodDetailedInfo(String detailedInfoURL) {
        return null;
    }

    public List<FoodAdapterType> requestSwapFoodAdapterTypeData(
            Food foodToSwap, int foodTypeFilter, int restaurantRadius) {
        return null;
    }

    // TODO delete context, only local
    // list of meals (index of list) to be regenerated (false meaning not checked -> to be regenerated)
    public List<Food> requestRegenerate(List<Boolean> generatedFoodsFlags, Context context) {
        return generator.requestDummyGeneratedFoods(generatedFoodsFlags, context);
    }

    public List<Float> requestNHConstraintsCalculation(
            Constants.Sex sex, int height, int weight, int age, Constants.Lifestyle lifestyle, Constants.Goal goal) {
        return null;
    }

    //requestDialog - after adding non-generated food
}