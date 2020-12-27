package cz.cuni.mff.nutritionalassistant.foodtypes;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RecipeAdapterType extends FoodAdapterType {
    private int id;

    public RecipeAdapterType(String foodName, String thumbUrl, Food.FoodType type, String servingUnit, float cals, int id) {
        super(foodName, thumbUrl, type, servingUnit, cals);
        this.id = id;
    }
}
