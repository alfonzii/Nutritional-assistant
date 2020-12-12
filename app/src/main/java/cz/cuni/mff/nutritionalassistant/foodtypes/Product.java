package cz.cuni.mff.nutritionalassistant.foodtypes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Product extends Food {
    private String brandName;

    Product(String foodName, String thumbnailURL, float calories, float fats, float carbohydrates, float proteins, FoodType foodType,
            String brandName, List<Float> servingQuantity, List<String> servingUnit, List<Float> servingWeight) {

        super(foodName, thumbnailURL, calories, fats, carbohydrates, proteins, foodType, servingQuantity, servingUnit, servingWeight);
        this.brandName = brandName;
    }

}
