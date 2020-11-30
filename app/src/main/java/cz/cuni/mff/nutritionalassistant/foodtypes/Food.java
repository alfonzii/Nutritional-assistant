package cz.cuni.mff.nutritionalassistant.foodtypes;

public abstract class Food {
    private String name;
    private float calories;
    private float fats;
    private float carbohydrates;
    private float proteins;
    private int amount;

    public Food(String name, float calories, float fats, float carbohydrates, float proteins, int amount) {
        this.name = name;
        this.calories = calories;
        this.fats = fats;
        this.carbohydrates = carbohydrates;
        this.proteins = proteins;
        this.amount = amount;
    }

    public Food() {}

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

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
