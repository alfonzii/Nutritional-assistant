package cz.cuni.mff.nutritionalassistant.guidancebot;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeoutException;

import cz.cuni.mff.nutritionalassistant.data.DataHolder;
import cz.cuni.mff.nutritionalassistant.MainActivity;
import cz.cuni.mff.nutritionalassistant.foodtypes.Food;
import cz.cuni.mff.nutritionalassistant.foodtypes.Recipe;
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

//-------------------------------------------------RANDOMIZED-ALGORITHM----------------------------------------------------------------------------------

    // false flag means new food is needed to be generated
    void randomizedFoodGeneration(List<Boolean> generatedFoodFlags, Context context, boolean satisfyMealConstr, boolean satisfyMacroConstr, GeneratedFoodListCallback generatedListCallback) {

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
                    if (mealFoodDataList.get(mealFoodDataList.size() - 1).size() == 0) {
                        if (generatedListCallback != null) {
                            generatedListCallback.onFail(new IllegalStateException("Meal list for generation empty."));
                            return;
                        }
                    }
                } else {
                    mealFoodDataList.add(recipesList);
                }
            }
        }

        if (mealFoodDataList.isEmpty()) {
            if (generatedListCallback != null) {
                generatedListCallback.onFail(new IllegalStateException("Nothing to generate."));
                return;
            }
        }

        Random random = new Random();
        boolean isSatysfyingConstr = false;
        int counter = 0;


        while (!isSatysfyingConstr) {
            counter++;
            if (counter > 2000000) {
                if (generatedListCallback != null && satisfyMealConstr) {
                    generatedListCallback.onFail(new TimeoutException("Too many iterations of algorithm using meal constraints."));
                    return;
                } else if (generatedListCallback != null && satisfyMacroConstr) {
                    generatedListCallback.onFail(new TimeoutException("Too many iterations of algorithm WITHOUT using meal constraints."));
                    return;
                } else if (generatedListCallback != null) {
                    generatedListCallback.onFail(new TimeoutException("Too many iterations of algorithm without using meal and macro constraints."));
                    return;
                }
            }

            List<Food> foodCombination = new ArrayList<>();

            // To ensure that we satisfy meal constraints we use recipes in their default portions,
            if (satisfyMealConstr) {
                for (List<Food> mealRecipes : mealFoodDataList) {
                    int index = random.nextInt(mealRecipes.size());
                    foodCombination.add(portionHeuristic(mealRecipes.get(index), 0));
                }
            }
            // When we don't need to satisfy meal con., we can use portions size heuristic, because
            // we don't care anymore about meal constraints
            else {
                for (List<Food> mealRecipes : mealFoodDataList) {
                    int index = random.nextInt(mealRecipes.size());
                    foodCombination.add(portionHeuristic(mealRecipes.get(index), random.nextInt(3)));
                }
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
                if (satisfyMacroConstr) {
                    List<Float> macrosList = new ArrayList<>();
                    macrosList.add(totalFats);
                    macrosList.add(totalCarbs);
                    macrosList.add(totalProts);
                    if (satisfiesMacroConstraints(macrosList)) {
                        isSatysfyingConstr = true;
                    }
                } else {
                    isSatysfyingConstr = true;
                }


                if (isSatysfyingConstr) {
                    List<Food> answer = Collections.synchronizedList(new ArrayList<>());
                    for (int i = 0; i < foodCombination.size(); i++) {
                        answer.add(null);
                    }

                    DetailedFoodGenerateCallback callback = new DetailedFoodGenerateCallback() {
                        @Override
                        public void onSuccess(@NonNull Food response, int position) {
                            answer.set(position, response);
                            if (isAsyncAnswerListReady(answer)) {
                                if (generatedListCallback != null) {
                                    generatedListCallback.onSuccess(answer, generatedFoodFlags);
                                }
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
                            if (generatedListCallback != null) {
                                generatedListCallback.onFail(throwable);
                            }
                        }
                    };

                            /*Log.d("FoodLOG", "Found combination + satisfConstrEnabled:" + satisfyMealConstr);
                            Log.d("FoodLOG", "iterations:" + counter);
                            Log.d("FoodLOG", "TotalCals: " + totalCal);
                            Log.d("FoodLOG", "TotalFats: " + totalFats);
                            Log.d("FoodLOG", "TotalCarbs: " + totalCarbs);
                            Log.d("FoodLOG", "TotalProts: " + totalProts);*/
                    for (int i = 0; i < foodCombination.size(); i++) {
                        recipeDMS.getGeneratedRecipeDetails(((Recipe)
                                foodCombination.get(i)).getId(), i, foodCombination.get(i).getServingQuantity().get(0), callback);
                        Log.d("FoodLOG", foodCombination.get(i).getFoodName());
                    }
                }
            }
        }
    }

    private boolean satisfiesMacroConstraints(List<Float> macros) {
        if (satisfiesConstraints(macros.get(0), dataHolder.getFatsConstr())) {
            if (satisfiesConstraints(macros.get(1), dataHolder.getCarbConstr())) {
                if (satisfiesConstraints(macros.get(2), dataHolder.getProtConstr())) {
                    return true;
                }
            }
        }
        return false;
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

    private Food portionHeuristic(Food food, int toUse) {
        if (toUse == 0) {
            food.setServingQuantity(Collections.singletonList(1f));
            return food;
        } else if (toUse == 1) {
            Recipe recipe = new Recipe();
            recipe.setFoodName(food.getFoodName());
            recipe.setId(((Recipe) food).getId());
            recipe.setServingQuantity(Collections.singletonList(2f));
            recipe.setCalories(food.getCalories() * 2);
            recipe.setFats(food.getFats() * 2);
            recipe.setCarbohydrates(food.getCarbohydrates() * 2);
            recipe.setProteins(food.getProteins() * 2);
            return recipe;
        } else { // toUse == 2
            Recipe recipe = new Recipe();
            recipe.setFoodName(food.getFoodName());
            recipe.setId(((Recipe) food).getId());
            recipe.setServingQuantity(Collections.singletonList(0.5f));
            recipe.setCalories(food.getCalories() / 2);
            recipe.setFats(food.getFats() / 2);
            recipe.setCarbohydrates(food.getCarbohydrates() / 2);
            recipe.setProteins(food.getProteins() / 2);
            return recipe;
        }
    }

}
