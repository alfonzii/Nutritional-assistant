package cz.cuni.mff.nutritionalassistant.guidancebot;

import java.util.ArrayList;
import java.util.List;

import cz.cuni.mff.nutritionalassistant.foodtypes.Food;

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

    static Brain getInstance() {
        return INSTANCE;
    }

    public List<Food> requestFoodData(String query, Food.FoodType foodType) {
        return dataSupplier.requestFoodData(query,foodType);
    }

    //public List<Food> requestFoodData(String query, Food.FoodType foodType, filterParams)
}
