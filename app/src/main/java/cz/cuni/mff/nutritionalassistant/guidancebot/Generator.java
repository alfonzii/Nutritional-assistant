package cz.cuni.mff.nutritionalassistant.guidancebot;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cz.cuni.mff.nutritionalassistant.foodtypes.Food;
import cz.cuni.mff.nutritionalassistant.foodtypes.FoodAdapterType;

class Generator {

    private DataSupplier dataSupplier = new DataSupplier();

    List<Food> requestDummyGeneratedFoods(List<Boolean> generatedFoodsFlags, Context context) {
        Random random = new Random();
        List<Food> output = new ArrayList<>();
        List<FoodAdapterType> lightweight = dataSupplier.localDBrequest("a", Food.FoodType.PRODUCT.getId(), context);
        for (Boolean b : generatedFoodsFlags) {
            if (!b) {
                output.add(dataSupplier.localDetailedInfo(lightweight.get(random.nextInt(6)), context));
            }
        }
        return output;
    }
}
