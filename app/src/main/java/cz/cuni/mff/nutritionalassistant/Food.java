package cz.cuni.mff.nutritionalassistant;

public class Food {
    private String name;
    private float cals;
    private float fats;
    private float carbs;
    private float prots;

    public Food(String name, float cals, float fats, float carbs, float prots) {
        this.name = name;
        this.cals = cals;
        this.fats = fats;
        this.carbs = carbs;
        this.prots = prots;
    }

    public Food() {
    }

    public float getCals() {
        return cals;
    }

    public float getCarbs() {
        return carbs;
    }

    public float getFats() {
        return fats;
    }

    public float getProts() {
        return prots;
    }

    public String getName() {
        return name;
    }


    public void setCals(float cals) {
        this.cals = cals;
    }

    public void setCarbs(float carbs) {
        this.carbs = carbs;
    }

    public void setFats(float fats) {
        this.fats = fats;
    }

    public void setProts(float prots) {
        this.prots = prots;
    }

    public void setName(String name) {
        this.name = name;
    }

    //--------------------------------------------

    /*public static Map<String, Food> databaza(){
        Map<String, Food> db = new HashMap<>();
        db.put("chicken breast", new Food("chicken breast",164, 3.6f,0,31));
        db.put("duck breast", new Food("duck breast", 123, 4, 0, 20));
        db.put("bread", new Food("bread", 264, 3.2f, 49, 9));
        db.put("milk", new Food("milk", 42, 1, 5, 3.4f));
        db.put("soy milk", new Food("soy milk", 34,2,1,3));
        db.put("oyster", new Food("oyster", 198,13,12,9));
        db.put("coca-cola", new Food("coca-cola",45,0,11,0));
        db.put("coca-cola zero", new Food("coca-cola zero",0.3f,0,0,0));
        db.put("banana", new Food("banana",94,0.2f,22,1));
        db.put("coconut ground", new Food("coconut ground",700,64,8,6));
        db.put("blueberries", new Food("bluberries", 57, 0.3f, 14, 0.7f));
        db.put("bryndza", new Food("bryndza",317,25,2,21));
        return db;
    }

    public static String[] getNamesDatabaza(){
        return databaza().keySet().toArray(new String[databaza().size()]);
    }

    public static Food getNutritionValues(String foodName){

    }*/
}
