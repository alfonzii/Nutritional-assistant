package cz.cuni.mff.nutritionalassistant.guidancebot;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import cz.cuni.mff.nutritionalassistant.DataHolder;
import cz.cuni.mff.nutritionalassistant.MainActivity;
import cz.cuni.mff.nutritionalassistant.foodtypes.Food;
import cz.cuni.mff.nutritionalassistant.foodtypes.FoodAdapterType;
import cz.cuni.mff.nutritionalassistant.foodtypes.Product;
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
                    if (mealFoodDataList.get(mealFoodDataList.size() - 1).size() == 0) {
                        if (generatedListCallback != null) {
                            generatedListCallback.onFail(new Throwable("Meal list for generation empty."));
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
                generatedListCallback.onFail(new Throwable("Nothing to generate."));
                return;
            }
        }

        Random random = new Random();
        boolean isSatysfyingConstr = false;
        int counter = 0;


        while (!isSatysfyingConstr) {
            counter++;
            if (counter > 1000000) {
                if (generatedListCallback != null && satisfyMealConstr) {
                    generatedListCallback.onFail(new Throwable("Too many iterations of algorithm using meal constraints."));
                    return;
                } else if (generatedListCallback != null) {
                    generatedListCallback.onFail(new Throwable("Too many iterations of algorithm WITHOUT using meal constraints."));
                    return;
                }
            }

            List<Food> foodCombination = new ArrayList<>();

            for (List<Food> mealRecipes : mealFoodDataList) {
                int index = random.nextInt(mealRecipes.size());
                foodCombination.add(doublePortion(mealRecipes.get(index), random.nextInt(2)));
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
                //Log.d("FoodLOG", "-----------------------------------------------------------------------");
                //Log.d("FoodLOG", "Satisfies calories constraints");
                if (satisfiesConstraints(totalFats, dataHolder.getFatsConstr())) {
                    //Log.d("FoodLOG", "Satisfies fats constraints");
                    if (satisfiesConstraints(totalCarbs, dataHolder.getCarbConstr())) {
                        //Log.d("FoodLOG", "Satisfies carbs constraints");
                        if (satisfiesConstraints(totalProts, dataHolder.getProtConstr())) {
                            //Log.d("FoodLOG", "Satisfies proteins constrants");

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

                            Log.d("FoodLOG", "Found combination + satisfConstrEnabled:" + satisfyMealConstr);
                            Log.d("FoodLOG", "iterations:" + counter);
                            Log.d("FoodLOG", "TotalCals: " + totalCal);
                            Log.d("FoodLOG", "TotalFats: " + totalFats);
                            Log.d("FoodLOG", "TotalCarbs: " + totalCarbs);
                            Log.d("FoodLOG", "TotalProts: " + totalProts);
                            for (int i = 0; i < foodCombination.size(); i++) {
                                recipeDMS.getGeneratedRecipeDetails(((Recipe)
                                        foodCombination.get(i)).getId(), i, foodCombination.get(i).getServingQuantity().get(0), callback);
                                Log.d("FoodLOG", foodCombination.get(i).getFoodName());
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

    private Food doublePortion(Food food, int toUse) {
        if (toUse == 0) {
            food.setServingQuantity(Collections.singletonList(1f));
            return food;
        } else {
            Recipe recipe = new Recipe();
            recipe.setFoodName(food.getFoodName());
            recipe.setId(((Recipe) food).getId());
            recipe.setServingQuantity(Collections.singletonList(2f));
            recipe.setCalories(food.getCalories()*2);
            recipe.setFats(food.getFats()*2);
            recipe.setCarbohydrates(food.getCarbohydrates()*2);
            recipe.setProteins(food.getProteins()*2);
            return recipe;
        }
    }

}
