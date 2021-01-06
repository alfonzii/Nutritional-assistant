package cz.cuni.mff.nutritionalassistant.guidancebot;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.cuni.mff.nutritionalassistant.Constants;
import cz.cuni.mff.nutritionalassistant.DataHolder;
import cz.cuni.mff.nutritionalassistant.MainActivity;
import cz.cuni.mff.nutritionalassistant.foodtypes.Food;
import cz.cuni.mff.nutritionalassistant.foodtypes.FoodAdapterType;
import cz.cuni.mff.nutritionalassistant.guidancebot.api.AdapterDataCallback;
import cz.cuni.mff.nutritionalassistant.guidancebot.api.DetailedFoodCallback;

public final class Brain {
    private DataSupplier dataSupplier;
    private Generator generator;
    private Mathematics mathematics;

    private DataHolder dataHolder;

    private static Brain INSTANCE;

    private Brain() {
        dataSupplier = new DataSupplier();
        generator = new Generator();
        mathematics = Mathematics.getInstance();
        dataHolder = DataHolder.getInstance();
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
        } else { //foodTypeFilter == Food.FoodType.RESTAURANTFOOD.getId()
            dataSupplier.requestRestaurantFoodAdapterTypeData(query, nutritionFilterTable, callbacks);
        }
    }


    public void requestFoodDetailedInfo(FoodAdapterType foodAdapterType, DetailedFoodCallback callback) {
        //return dataSupplier.localDetailedInfo(foodAdapterType, context);
        if (foodAdapterType.getFoodType() == Food.FoodType.PRODUCT) {
            dataSupplier.requestProductDetailedInfo(foodAdapterType, callback);
        } else if (foodAdapterType.getFoodType() == Food.FoodType.RECIPE) {
            dataSupplier.requestRecipeDetailedInfo(foodAdapterType, callback);
        }
    }

    // list of meals (index of list) to be regenerated (false meaning not checked && not added custom food -> to be regenerated)
    public void requestRegenerate(List<Boolean> generatedFoodsChecked, Context context, GeneratedFoodListCallback callback) {
        List<Boolean> generatedFoodsFlags = generatedFoodsChecked;

        if (dataHolder.getAdHocFlag() == DataHolder.AdHocFlag.THISDAY) {
            List<Boolean> userAddedFoodsFlags = new ArrayList<>();
            for (List<Food> mealList : dataHolder.getUserAddedFoods()) {
                userAddedFoodsFlags.add(mealList.size() != 0);
            }

            generatedFoodsFlags = new ArrayList<>();
            for (int i = 0; i < MainActivity.MealController.NUMBER_OF_MEALS; i++) {
                generatedFoodsFlags.add(generatedFoodsChecked.get(i) || userAddedFoodsFlags.get(i));
            }
        }

        if (allGenFlagsFalse(generatedFoodsFlags)) {
            mathematics.setConstraints(mathematics.getModifiedTEE(dataHolder.getCaloriesExcess()));
        } else {
            mathematics.updateConstraints();
        }


        final List<Boolean> finalGeneratedFoodsFlags = generatedFoodsFlags;
        generator.randomizedFoodGeneration(generatedFoodsFlags, context, true, new GeneratedFoodListCallback() {
            private boolean alreadyFailed = false;

            @Override
            public void onSuccess(@NonNull List<Food> response, List<Boolean> genFoodFlags) {
                if (callback != null) {
                    callback.onSuccess(response, genFoodFlags);
                }
            }

            @Override
            public void onFail(@NonNull Throwable throwable) {
                if (!alreadyFailed) {
                    alreadyFailed = true;
                    generator.randomizedFoodGeneration(finalGeneratedFoodsFlags, context, false, new GeneratedFoodListCallback() {
                        private boolean secondFailed = false;
                        @Override
                        public void onSuccess(@NonNull List<Food> response, List<Boolean> genFoodFlags) {
                            if (callback != null) {
                                callback.onSuccess(response, genFoodFlags);
                            }
                        }

                        @Override
                        public void onFail(@NonNull Throwable throwable) {
                            if (!secondFailed) {
                                secondFailed = true;
                                Log.e(Brain.class.getName(), throwable.getMessage());
                                if (callback != null) {
                                    callback.onFail(throwable);
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    public void requestNHConstraintsCalculation(float caloriesExcess) {
        mathematics.setGoalNH(caloriesExcess);
        mathematics.setConstraints(mathematics.getModifiedTEE(caloriesExcess));
    }

    private boolean allGenFlagsFalse(List<Boolean> genFoodsFlags) {
        for (Boolean bool : genFoodsFlags) {
            if (bool) { //means flag is true
                return false;
            }
        }
        return true;
    }
}