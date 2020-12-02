package cz.cuni.mff.nutritionalassistant.guidancebot;

import java.util.ArrayList;
import java.util.List;

import cz.cuni.mff.nutritionalassistant.foodtypes.Food;

class DataSupplier {
    private List<Food> foodDataParsedResponse;

    DataSupplier() {
        foodDataParsedResponse = new ArrayList<>();
    }

    List<Food> requestFoodData(String query, Food.FoodType foodType) {


        return foodDataParsedResponse;
    }

}
