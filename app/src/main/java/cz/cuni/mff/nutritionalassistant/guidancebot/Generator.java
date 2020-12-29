package cz.cuni.mff.nutritionalassistant.guidancebot;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Pair;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import cz.cuni.mff.nutritionalassistant.DataHolder;
import cz.cuni.mff.nutritionalassistant.MainActivity;
import cz.cuni.mff.nutritionalassistant.foodtypes.Food;
import cz.cuni.mff.nutritionalassistant.foodtypes.FoodAdapterType;
import cz.cuni.mff.nutritionalassistant.foodtypes.Recipe;
import cz.cuni.mff.nutritionalassistant.guidancebot.api.DetailedFoodCallback;
import cz.cuni.mff.nutritionalassistant.guidancebot.api.DetailedFoodGenerateCallback;
import cz.cuni.mff.nutritionalassistant.guidancebot.api.Spoonacular.SpoonacularDMS;

class Generator {

    private DataSupplier dataSupplier;
    private DataHolder dataHolder;
    private SpoonacularDMS recipeDMS;

    Generator() {
        dataSupplier = new DataSupplier();
        dataHolder = DataHolder.getInstance();
        recipeDMS = new SpoonacularDMS();
    }

    List<Food> requestDummyGeneratedFoods(List<Boolean> generatedFoodsFlags, Context context) {
        Random random = new Random();
        List<Food> output = new ArrayList<>();
        List<FoodAdapterType> lightweight = dataSupplier.localDBrequest("a", Food.FoodType.RECIPE.getId(), context);
        for (Boolean b : generatedFoodsFlags) {
            if (!b) {
                output.add(dataSupplier.localDetailedInfo(lightweight.get(random.nextInt(6)), context));
            }
        }
        return output;
    }

//-------------------------------------------------RANDOMIZED-ALGORITHM----------------------------------------------------------------------------------

    // false flag means new food is needed to be generated
    void randomizedFoodGeneration(List<Boolean> generatedFoodFlags, Context context, boolean satisfyMealConstr, GeneratedFoodListCallback generatedListCallback) {

        List<List<Food>> mealFoodDataList = new ArrayList<>();

        for (int i = 0; i < MainActivity.MealController.NUMBER_OF_MEALS; i++) {
            if (!generatedFoodFlags.get(i)) {
                List<Food> recipesList;
                Pair<Float, Float> constr;

                if (i == MainActivity.MealController.BREAKFAST) {
                    recipesList = dataSupplier.getBreakfastRecipesList(context);
                    constr = dataHolder.getBreakfastConstr();
                } else if (i == MainActivity.MealController.LUNCH) {
                    recipesList = dataSupplier.getMainCourseRecipesList(context);
                    constr = dataHolder.getLunchConstr();
                } else if (i == MainActivity.MealController.DINNER) {
                    recipesList = dataSupplier.getMainCourseRecipesList(context);
                    constr = dataHolder.getDinnerConstr();
                } else { // i == SNACK
                    recipesList = dataSupplier.getSnackRecipesList(context);
                    constr = dataHolder.getSnackConstr();
                }

                if (satisfyMealConstr) {
                    mealFoodDataList.add(filterUnsatisfyingFoods(recipesList, constr));
                } else {
                    mealFoodDataList.add(recipesList);
                }
            }
        }

        Random random = new Random();
        boolean isSatysfyingConstr = false;

        while (!isSatysfyingConstr) {

            List<Food> foodCombination = new ArrayList<>();

            for (List<Food> mealRecipes : mealFoodDataList) {
                int index = random.nextInt(mealRecipes.size());
                foodCombination.add(mealRecipes.get(index));
            }

            float totalCal = 0;
            float totalFats = 0;
            float totalCarbs = 0;
            float totalProts = 0;
            for (Food food : foodCombination) {
                totalCal += food.getCalories();
                totalFats += food.getFats();
                totalCarbs += food.getCarbohydrates();
                totalProts += food.getProteins();
            }
            if (satisfiesConstraints(totalCal, dataHolder.getCalsConstr())) {
                if (satisfiesConstraints(totalFats, dataHolder.getFatsConstr())) {
                    if (satisfiesConstraints(totalCarbs, dataHolder.getCarbConstr())) {
                        if (satisfiesConstraints(totalProts, dataHolder.getProtConstr())) {

                            // satisfying combination found
                            isSatysfyingConstr = true;

                            List<Food> answer = Collections.synchronizedList(new ArrayList<>());
                            for (int i = 0; i < foodCombination.size(); i++) {
                                answer.add(null);
                            }

                            DetailedFoodGenerateCallback callback = new DetailedFoodGenerateCallback() {
                                @Override
                                public void onSuccess(@NonNull Food response, int position) {
                                    answer.set(position, response);
                                    if (isAsyncAnswerListReady(answer)) {
                                        generatedListCallback.onSuccess(answer);
                                    }
                                }

                                private boolean isAsyncAnswerListReady(List<Food> answer) {
                                    for (int i = 0; i < answer.size(); i++) {
                                        if (answer.get(i) == null) {
                                            return false;
                                        }
                                    }
                                    return true;
                                }

                                @Override
                                public void onFail(@NonNull Throwable throwable) {
                                    Log.e(Generator.class.getName(), throwable.getMessage());
                                    generatedListCallback.onFail(throwable);
                                }
                            };

                            for (int i = 0; i < foodCombination.size(); i++) {
                                recipeDMS.getGeneratedRecipeDetails(((Recipe) foodCombination.get(i)).getId(), i, callback);
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean satisfiesConstraints(float nutriValue, Pair<Float, Float> constr) {
        return nutriValue >= constr.first && nutriValue <= constr.second;
    }

    private List<Food> filterUnsatisfyingFoods(List<Food> foodList, Pair<Float, Float> mealConstr) {
        List<Food> filteredList = new ArrayList<>();
        for (Food food : foodList) {
            if (food.getCalories() >= mealConstr.first && food.getCalories() <= mealConstr.second) {
                filteredList.add(food);
            }
        }
        return filteredList;
    }

}
