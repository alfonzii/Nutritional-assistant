package cz.cuni.mff.nutritionalassistant;

import java.util.ArrayList;
import java.util.List;

import cz.cuni.mff.nutritionalassistant.foodtypes.Food;
import lombok.Getter;
import lombok.Setter;

import static cz.cuni.mff.nutritionalassistant.Constants.Lifestyle;
import static cz.cuni.mff.nutritionalassistant.Constants.Goal;
import static cz.cuni.mff.nutritionalassistant.Constants.Sex;

//SINGLETON CLASS
@Getter
@Setter
public final class DataHolder {
    private static final DataHolder INSTANCE = new DataHolder();

    private DataHolder() {
        eatenFood = new ArrayList<>();
        eatenFood.add(new ArrayList<Food>());
        eatenFood.add(new ArrayList<Food>());
        eatenFood.add(new ArrayList<Food>());
        eatenFood.add(new ArrayList<Food>());
    }

    public static DataHolder getInstance() {
        return INSTANCE;
    }

    private Sex sex;
    private int height = 0;
    private int weight = 0;
    private int age = 0;

    private Lifestyle lifestyle;
    private Goal goal;

    // TODO mali by to byt floaty, a zobrazovacie views by sa mali starat o ich konverziu
    private int caloriesGoal = 0;
    private int fatsGoal = 0;
    private int carbohydratesGoal = 0;
    private int proteinsGoal = 0;
    private int caloriesCurrent = 0;
    private int fatsCurrent = 0;
    private int carbohydratesCurrent = 0;
    private int proteinsCurrent = 0;

    private List<List<Food>> eatenFood;
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
        int lastFood = eatenFood.get(lastAddedMeal).size()-1;
        return eatenFood.get(lastAddedMeal).get(lastFood);
    }
}
