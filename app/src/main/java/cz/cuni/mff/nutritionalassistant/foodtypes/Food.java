package cz.cuni.mff.nutritionalassistant.foodtypes;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Serializable is slow in matter of speed for Android. Much more preferable way of doing so would
// be through Parcelable, but it's more complicated to code. For sake of simplicity, we would be
// using Serializable for now.

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class Food implements Serializable {
    private String foodName;
    private String thumbnailURL;
    private float calories;
    private float fats;
    private float carbohydrates;
    private float proteins;
    private FoodType foodType;

    public static enum FoodType {PRODUCT, RECIPE, RESTAURANTFOOD}
}
