package cz.cuni.mff.nutritionalassistant.guidancebot;

import android.support.annotation.NonNull;

import java.util.List;

import cz.cuni.mff.nutritionalassistant.foodtypes.Food;

public interface GeneratedFoodListCallback {
    void onSuccess(@NonNull List<Food> response, List<Boolean> generatedFoodsFlags);
    void onFail(@NonNull Throwable throwable);
}
