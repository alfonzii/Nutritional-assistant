package cz.cuni.mff.nutritionalassistant.guidancebot;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import cz.cuni.mff.nutritionalassistant.foodtypes.Food;
import cz.cuni.mff.nutritionalassistant.foodtypes.FoodLightweight;

public final class Brain {
    private DataSupplier dataSupplier;
    private Generator generator;
    private Mathematics mathematics;

    private static final Brain INSTANCE = new Brain();

    private Brain() {
        dataSupplier = new DataSupplier();
        generator = new Generator();
        mathematics = new Mathematics();
    }

    public static Brain getInstance() {
        return INSTANCE;
    }

    // TODO local
    public List<FoodLightweight> requestFoodLightweightData(String query, Food.FoodType foodType, Context context) {
        return dataSupplier.localDBrequest(query, context);
    }

    public Food requestFoodDetailedInfo(String detailedInfoURL) {
        return null;
    }

    // TODO local
    public Food requestFoodDetailedInfo(FoodLightweight foodLightweight, Context context){
        return dataSupplier.localDetailedInfo(foodLightweight, context);
    }

    //public List<Food> requestFoodData(String query, Food.FoodType foodType, filterParams)
}
