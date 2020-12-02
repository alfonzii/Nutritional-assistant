package cz.cuni.mff.nutritionalassistant.foodtypes;

import java.util.ArrayList;
import java.util.List;

public class Product extends Food {

    private String brandName;
    private int servingQuantity;
    private List<String> servingUnit;
    private List<Integer> servingWeight;

    Product(String foodName, float calories, float fats, float carbohydrates, float proteins, FoodType foodType,
            String brandName, int servingQuantity, List<String> servingUnit,  List<Integer> servingWeight) {

        super(foodName, calories, fats, carbohydrates, proteins, foodType);
        this.brandName = brandName;
        this.servingQuantity = servingQuantity;
        this.servingUnit = servingUnit;
        this.servingWeight = servingWeight;
    }

    public Product() {
        servingUnit = new ArrayList<>();
        servingWeight = new ArrayList<>();
    }

    public int getServingQuantity() {
        return servingQuantity;
    }

    public List<String> getServingUnit() {
        return servingUnit;
    }

    public List<Integer> getServingWeight() {
        return servingWeight;
    }

    public String getBrandName() {
        return brandName;
    }


    public void setServingQuantity(int servingQuantity) {
        this.servingQuantity = servingQuantity;
    }

    public void setServingUnit(List<String> servingUnit) {
        this.servingUnit = servingUnit;
    }

    public void setServingWeight(List<Integer> servingWeight) {
        this.servingWeight = servingWeight;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
}
