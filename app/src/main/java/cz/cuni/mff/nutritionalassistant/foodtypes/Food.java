package cz.cuni.mff.nutritionalassistant.foodtypes;

import java.io.Serializable;
import java.util.List;

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
    // TODO not URL but some Image class (we already downloaded image in RecyclerView)
    private String thumbnailURL;
    private float calories;
    private float fats;
    private float carbohydrates;
    private float proteins;
    private FoodType foodType;

    // Zero indexed elements are reference values (NH are bound to them)!!!
    // We always need to initialize these references before using them!!!
    // Servings will refer to immutable (just read-only) Lists (we don't need to write to them)
    private List<Float> servingQuantity;
    private List<String> servingUnit;
    private List<Float> servingWeight;

    @Getter
    public enum FoodType {
        PRODUCT(0),
        RECIPE(1),
        RESTAURANTFOOD(2);

        private int id;

        FoodType(int id){
            this.id = id;
        }
    }
}
