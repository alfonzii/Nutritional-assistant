package cz.cuni.mff.nutritionalassistant.guidancebot;

import android.content.Context;

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

    Generator() {
        dataSupplier = new DataSupplier();
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

    // false flag means new food is needed to be generated
    /*List<Food> requestGeneratedFoods(List<Boolean> generatedFoodFlags, Context context) {
        List<Food> breakfastList = dataSupplier.getBreakfastRecipesList(context);
        List<Food> mainCourseList = dataSupplier.getMainCourseRecipesList(context);
        List<Food> snackList = dataSupplier.getSnackRecipesList(context);

        Random random = new Random();
        boolean isSatysfyingConstr = false;

        int breakfastIndex, lunchIndex, dinnerIndex, snackIndex;

        while(!isSatysfyingConstr){
            breakfastIndex = random.nextInt(breakfastList.size());
            lunchIndex = random.nextInt(mainCourseList.size());
            dinnerIndex = random.nextInt(mainCourseList.size());
            snackIndex = random.nextInt(snackList.size());

            Food breakfastFood, lunchFood, dinnerFood, snackFood;

            breakfastFood = breakfastList.get(breakfastIndex);
            lunchFood = mainCourseList.get(lunchIndex);
            dinnerFood = mainCourseList.get(dinnerIndex);
            snackFood = snackList.get(snackIndex);

            if()
        }



        return new ArrayList<>();
    }*/

}
