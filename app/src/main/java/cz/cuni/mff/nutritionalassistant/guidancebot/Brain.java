package cz.cuni.mff.nutritionalassistant.guidancebot;

import android.content.Context;

import java.util.List;

import cz.cuni.mff.nutritionalassistant.foodtypes.Food;
import cz.cuni.mff.nutritionalassistant.foodtypes.FoodAdapterType;

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
    public List<FoodAdapterType> requestFoodLightweightData(String query, Food.FoodType foodType, Context context) {
        return dataSupplier.localDBrequest(query, context);
    }

    public Food requestFoodDetailedInfo(String detailedInfoURL) {
        return null;
    }

    // TODO local
    public Food requestFoodDetailedInfo(FoodAdapterType foodAdapterType, Context context){
        return dataSupplier.localDetailedInfo(foodAdapterType, context);
    }

    //public List<Food> requestFoodData(String query, Food.FoodType foodType, filterParams)
}
