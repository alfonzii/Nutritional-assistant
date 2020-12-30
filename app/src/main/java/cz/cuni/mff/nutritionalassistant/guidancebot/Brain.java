package cz.cuni.mff.nutritionalassistant.guidancebot;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

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

    // list of meals (index of list) to be regenerated (false meaning not checked && not added custom food -> to be regenerated)
    public void requestRegenerate(List<Boolean> generatedFoodsFlags, Context context, GeneratedFoodListCallback callback) {
        //return generator.requestDummyGeneratedFoods(generatedFoodsFlags, context);
        if (allGenFlagsFalse(generatedFoodsFlags)){
            mathematics.setConstraints(mathematics.getModifiedTEE());
        } else {
            mathematics.updateConstraints();
        }
        generator.randomizedFoodGeneration(generatedFoodsFlags, context, true, new GeneratedFoodListCallback() {
            @Override
            public void onSuccess(@NonNull List<Food> response) {
                if (callback != null) {
                    callback.onSuccess(response);
                }
            }

            @Override
            public void onFail(@NonNull Throwable throwable) {
                generator.randomizedFoodGeneration(generatedFoodsFlags, context, false, new GeneratedFoodListCallback() {
                    @Override
                    public void onSuccess(@NonNull List<Food> response) {
                        if (callback != null) {
                            callback.onSuccess(response);
                        }
                    }

                    @Override
                    public void onFail(@NonNull Throwable throwable) {
                        Log.e(Brain.class.getName(), throwable.getMessage());
                        if (callback != null) {
                            callback.onFail(throwable);
                        }
                    }
                });
            }
        });
    }

    public void requestNHConstraintsCalculation() {
        mathematics.setGoalNH();
        mathematics.setConstraints(mathematics.getModifiedTEE());
    }

    //requestDialog - after adding non-generated food

    private boolean allGenFlagsFalse(List<Boolean> genFoodsFlags) {
        for (Boolean bool : genFoodsFlags) {
            if (bool) { //means flag is true
                return false;
            }
        }
        return true;
    }
}