package cz.cuni.mff.nutritionalassistant;

import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

import cz.cuni.mff.nutritionalassistant.foodtypes.Food;
import cz.cuni.mff.nutritionalassistant.foodtypes.Recipe;
import cz.cuni.mff.nutritionalassistant.guidancebot.Mathematics;
import lombok.Getter;
import lombok.Setter;

import static cz.cuni.mff.nutritionalassistant.Constants.Sex;

//SINGLETON CLASS (but not proper, because we need to assign saved object from SharedPreferences)
@Getter
@Setter
public final class DataHolder {
    // Should be final to be proper singleton
    private static DataHolder INSTANCE = new DataHolder();

    private boolean isInitialized = false;

    private DataHolder() {
        generatedFoods = new ArrayList<>();
        for (int i = 0; i < MainActivity.MealController.NUMBER_OF_MEALS; i++) {
            generatedFoods.add(new Pair<Food, Boolean>(new Recipe(), false));
        }

        userAddedFoods = new ArrayList<>();
        for (int i = 0; i < MainActivity.MealController.NUMBER_OF_MEALS; i++){
            userAddedFoods.add(new ArrayList<Food>());
        }
    }

    // Should not exist to be proper singleton
    public static void setInstance(DataHolder d) { INSTANCE = d; }
    public static DataHolder getInstance() {
        return INSTANCE;
    }

    private Sex sex;
    private int height = 0;
    private int weight = 0;
    private int age = 0;

    private Mathematics.Lifestyle lifestyle;
    private Mathematics.Goal goal;

    // TODO mali by to byt floaty, a zobrazovacie views by sa mali starat o ich konverziu
    private float caloriesGoal = 0;
    private float fatsGoal = 0;
    private float carbohydratesGoal = 0;
    private float proteinsGoal = 0;
    private float caloriesCurrent = 0;
    private float fatsCurrent = 0;
    private float carbohydratesCurrent = 0;
    private float proteinsCurrent = 0;

    private float fatsReq, carbsReq, protsReq;
    private Pair<Float, Float> calsConstr, fatsConstr, carbConstr, protConstr;
    private Pair<Float, Float> breakfastConstr, lunchConstr, dinnerConstr, snackConstr;

    // Generated food + flag, if it is checked (eaten)
    private List<Pair<Food, Boolean>> generatedFoods;
    private List<List<Food>> userAddedFoods;
    private int lastAddedMeal;

    int convertSex(Sex sex) {
        if (sex == Sex.MALE)
            return 0;
        else
            return 1;
    }

    Sex convertSex(int i) {
        if (i == 0)
            return Sex.MALE;
        else if (i == 1)
            return Sex.FEMALE;
        else
            throw new IllegalArgumentException();
    }

    Food getLastEatenFood() {
        int lastFood = userAddedFoods.get(lastAddedMeal).size() - 1;
        return userAddedFoods.get(lastAddedMeal).get(lastFood);
    }

    public void addFoodToCurrentNH(Food food) {
        float quantity = food.getServingQuantity().get(0);
        caloriesCurrent += food.getCalories() * quantity;
        fatsCurrent += food.getFats() * quantity;
        carbohydratesCurrent += food.getCarbohydrates() * quantity;
        proteinsCurrent += food.getProteins() * quantity;
    }

    public void subtractFoodFromCurrentNH(Food food) {
        float quantity = food.getServingQuantity().get(0);
        caloriesCurrent -= food.getCalories() * quantity;
        fatsCurrent -= food.getFats() * quantity;
        carbohydratesCurrent -= food.getCarbohydrates() * quantity;
        proteinsCurrent -= food.getProteins() * quantity;
    }
}
