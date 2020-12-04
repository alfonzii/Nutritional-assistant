package cz.cuni.mff.nutritionalassistant.foodtypes;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Product extends Food {

    private String brandName;
    // TODO look up final keyword, because I don't have to change internals of these Lists
    // zero indexed elements are reference values (NH are bound to them)
    private List<Integer> servingQuantity;
    private List<String> servingUnit;
    private List<Integer> servingWeight;
    private Integer finalServingQuantity;
    private String finalServingUnit;
    private Integer finalServingWeight;

    Product(String foodName, String thumbnailURL, float calories, float fats, float carbohydrates, float proteins, FoodType foodType,
            String brandName, List<Integer> servingQuantity, List<String> servingUnit, List<Integer> servingWeight,
            Integer finalServingQuantity, String finalServingUnit, Integer finalServingWeight) {

        super(foodName, thumbnailURL, calories, fats, carbohydrates, proteins, foodType);
        this.brandName = brandName;
        this.servingQuantity = servingQuantity;
        this.servingUnit = servingUnit;
        this.servingWeight = servingWeight;
        this.finalServingQuantity = finalServingQuantity;
        this.finalServingUnit = finalServingUnit;
        this.finalServingWeight = finalServingWeight;
    }

    public Product() {
        servingQuantity = new ArrayList<>();
        servingUnit = new ArrayList<>();
        servingWeight = new ArrayList<>();
    }
}
