package cz.cuni.mff.nutritionalassistant.foodtypes;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantFood extends Food {
    private String brandName;

    RestaurantFood(String foodName, String thumbnailURL, float calories, float fats, float carbohydrates, float proteins, FoodType foodType,
                   String brandName, List<Float> servingQuantity, List<String> servingUnit, List<Float> servingWeight) {

        super(foodName, thumbnailURL, calories, fats, carbohydrates, proteins, foodType, servingQuantity, servingUnit, servingWeight);
        this.brandName = brandName;
    }
}
