package cz.cuni.mff.nutritionalassistant.foodtypes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class Food {
    private String foodName;
    private float calories;
    private float fats;
    private float carbohydrates;
    private float proteins;
    private FoodType foodType;

    public static enum FoodType {PRODUCT, RECIPE, RESTAURANTFOOD}

    /*public Food(String foodName, float calories, float fats, float carbohydrates, float proteins, FoodType foodType) {
        this.foodName = foodName;
        this.calories = calories;
        this.fats = fats;
        this.carbohydrates = carbohydrates;
        this.proteins = proteins;
        this.foodType = foodType;
    }

    public Food() {
    }

    public float getCalories() {
        return calories;
    }

    public float getCarbohydrates() {
        return carbohydrates;
    }

    public float getFats() {
        return fats;
    }

    public float getProteins() {
        return proteins;
    }

    public String getFoodName() {
        return foodName;
    }

    public FoodType getFoodType() {
        return foodType;
    }


    public void setCalories(float calories) {
        this.calories = calories;
    }

    public void setCarbohydrates(float carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public void setFats(float fats) {
        this.fats = fats;
    }

    public void setProteins(float proteins) {
        this.proteins = proteins;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public void setFoodType(FoodType foodType) {
        this.foodType = foodType;
    }*/
}
