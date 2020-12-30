package cz.cuni.mff.nutritionalassistant.foodtypes;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Recipe extends Food {
    int id;
    List<Ingredient> ingredients;
    String instructions; //List<String>, zalezi od sposobu citania z JSON odpovede
    int numberOfServings; //neviem ci nemoze byt 0.5 serving alebo podobne, tak podla toho sa urci typ servingu
    int readyInMinutes;

    public Recipe(String foodName, String thumbnailURL, float calories, float fats, float carbohydrates, float proteins, FoodType foodType,
                   List<Float> servingQuantity, List<String> servingUnit, List<Float> servingWeight,
                  List<Ingredient> ingredients, String instructions, int numberOfServings, int readyInMinutes) {

        super(foodName, thumbnailURL, calories, fats, carbohydrates, proteins, foodType, servingQuantity, servingUnit, servingWeight);
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.numberOfServings = numberOfServings;
        this.readyInMinutes = readyInMinutes;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Ingredient implements Serializable {
        String name;

        float usAmount;
        String usUnit;

        float metricAmount;
        String metricUnit;
    }

}
