package cz.cuni.mff.nutritionalassistant.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;

import cz.cuni.mff.nutritionalassistant.DataHolder;
import cz.cuni.mff.nutritionalassistant.foodtypes.Food;
import cz.cuni.mff.nutritionalassistant.foodtypes.Product;
import cz.cuni.mff.nutritionalassistant.foodtypes.Recipe;
import cz.cuni.mff.nutritionalassistant.foodtypes.RestaurantFood;

public abstract class BaseAbstractActivity extends AppCompatActivity {

    public static final String SHARED_PREFERENCES_FILE = "cz.cuni.mff.nutritionalassistant";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DataHolder dataHolder = DataHolder.getInstance();

        if (!dataHolder.isInitialized()) {
            SharedPreferences mPreferences = getSharedPreferences(SHARED_PREFERENCES_FILE, MODE_PRIVATE);

            RuntimeTypeAdapterFactory<Food> foodAdapterFactory = RuntimeTypeAdapterFactory.of(Food.class, "type")
                    .registerSubtype(Product.class, "Product")
                    .registerSubtype(Recipe.class, "Recipe")
                    .registerSubtype(RestaurantFood.class, "RestaurantFood");

            Gson gson = new GsonBuilder().registerTypeAdapterFactory(foodAdapterFactory).create();

            String json = mPreferences.getString(DataHolder.class.getName(), "");
            // first run if equals ""
            if (!json.equals("")) {
                DataHolder.setInstance(gson.fromJson(json, DataHolder.class));
            }
            DataHolder.getInstance().setInitialized(true);
        }
    }
}
