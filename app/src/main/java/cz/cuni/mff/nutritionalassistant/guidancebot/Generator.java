package cz.cuni.mff.nutritionalassistant.guidancebot;

import android.content.Context;
import android.util.Pair;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cz.cuni.mff.nutritionalassistant.foodtypes.Food;
import cz.cuni.mff.nutritionalassistant.foodtypes.FoodAdapterType;
import cz.cuni.mff.nutritionalassistant.guidancebot.api.Spoonacular.SpoonacularAdapterFullReposnsePojo;

class Generator {

    private DataSupplier dataSupplier;
    private Mathematics mathematics;

    Generator() {
        dataSupplier = new DataSupplier();
        mathematics = Mathematics.getInstance();
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
    List<Food> randomizedFoodGeneration(List<Boolean> generatedFoodFlags, Context context) {
        List<Food> breakfastList = filterUnsatisfyingFoods(dataSupplier.getBreakfastRecipesList(context), mathematics.getBreakfastConstr());
        List<Food> lunchList = filterUnsatisfyingFoods(dataSupplier.getMainCourseRecipesList(context), mathematics.getLunchConstr());
        List<Food> dinnerList = filterUnsatisfyingFoods(dataSupplier.getMainCourseRecipesList(context), mathematics.getDinnerConstr());
        List<Food> snackList = filterUnsatisfyingFoods(dataSupplier.getSnackRecipesList(context), mathematics.getSnackConstr());

        Random random = new Random();
        boolean isSatysfyingConstr = false;

        int breakfastIndex, lunchIndex, dinnerIndex, snackIndex;

        List<Food> answer = new ArrayList<>();

        while (!isSatysfyingConstr) {
            breakfastIndex = random.nextInt(breakfastList.size());
            lunchIndex = random.nextInt(lunchList.size());
            dinnerIndex = random.nextInt(dinnerList.size());
            snackIndex = random.nextInt(snackList.size());

            Food breakfastFood, lunchFood, dinnerFood, snackFood;

            breakfastFood = breakfastList.get(breakfastIndex);
            lunchFood = lunchList.get(lunchIndex);
            dinnerFood = dinnerList.get(dinnerIndex);
            snackFood = snackList.get(snackIndex);


            float totalCal = breakfastFood.getCalories() + lunchFood.getCalories() + dinnerFood.getCalories() + snackFood.getCalories();
            if (satisfiesConstraints(totalCal, mathematics.getCalsConstr())) {

                float totalFats = breakfastFood.getFats() + lunchFood.getFats() + dinnerFood.getFats() + snackFood.getFats();
                if (satisfiesConstraints(totalFats, mathematics.getFatsConstr())) {

                    float totalCarbs = breakfastFood.getCarbohydrates() + lunchFood.getCarbohydrates() + dinnerFood.getCarbohydrates() + snackFood.getCarbohydrates();
                    if (satisfiesConstraints(totalCarbs, mathematics.getCarbConstr())) {

                        float totalProts = breakfastFood.getProteins() + lunchFood.getProteins() + dinnerFood.getProteins() + snackFood.getProteins();
                        if (satisfiesConstraints(totalProts, mathematics.getProtConstr())) {

                            // satisfying combination found
                            isSatysfyingConstr = true;
                            answer.add(breakfastFood);
                            answer.add(lunchFood);
                            answer.add(dinnerFood);
                            answer.add(snackFood);
                        }
                    }
                }
            }
        }
        return answer;
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
