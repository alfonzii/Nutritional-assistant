package cz.cuni.mff.nutritionalassistant.guidancebot;

import android.content.Context;

import java.util.HashMap;
import java.util.List;

import cz.cuni.mff.nutritionalassistant.Constants;
import cz.cuni.mff.nutritionalassistant.foodtypes.Food;
import cz.cuni.mff.nutritionalassistant.foodtypes.FoodAdapterType;
import cz.cuni.mff.nutritionalassistant.guidancebot.api.AdapterDataCallback;
import cz.cuni.mff.nutritionalassistant.guidancebot.api.DetailedFoodCallback;

public final class Brain {
    private DataSupplier dataSupplier;
    private Generator generator;
    private Mathematics mathematics;

    private static Brain INSTANCE;

    private Brain() {
        dataSupplier = new DataSupplier();
        generator = new Generator();
        mathematics = Mathematics.getInstance();
    }

    public static Brain getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Brain();
        }
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
        } else if (foodAdapterType.getFoodType() == Food.FoodType.RECIPE) {
            dataSupplier.requestRecipeDetailedInfo(foodAdapterType, callback);
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
        //return generator.requestDummyGeneratedFoods(generatedFoodsFlags, context);
        mathematics.setConstraints(mathematics.getModifiedTEE());
        return generator.randomizedFoodGeneration(generatedFoodsFlags, context);
    }

    public void requestNHConstraintsCalculation() {
        mathematics.setGoalNH();
        mathematics.setConstraints(mathematics.getModifiedTEE());
    }

    //requestDialog - after adding non-generated food
}