package cz.cuni.mff.nutritionalassistant.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;

import cz.cuni.mff.nutritionalassistant.foodtypes.Food;
import cz.cuni.mff.nutritionalassistant.foodtypes.Product;
import cz.cuni.mff.nutritionalassistant.foodtypes.Recipe;
import cz.cuni.mff.nutritionalassistant.foodtypes.RestaurantFood;

public final class MyGson {
    public static class CommonGson {
        private static final Gson GSON = new Gson();
        private CommonGson() {}

        public static Gson getInstance(){
            return GSON;
        }
    }

    public static class PolymorphicGson {
        private static final RuntimeTypeAdapterFactory<Food> foodAdapterFactory = RuntimeTypeAdapterFactory.of(Food.class, "type")
                .registerSubtype(Product.class, "Product")
                .registerSubtype(Recipe.class, "Recipe")
                .registerSubtype(RestaurantFood.class, "RestaurantFood");

        private static final Gson GSON = new GsonBuilder().registerTypeAdapterFactory(foodAdapterFactory).create();
        private PolymorphicGson() {

        }

        public static Gson getInstance(){
            return GSON;
        }
    }
}
