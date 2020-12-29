package cz.cuni.mff.nutritionalassistant.guidancebot.api;

import android.support.annotation.NonNull;

import cz.cuni.mff.nutritionalassistant.foodtypes.Food;

public interface DetailedFoodGenerateCallback {
    void onSuccess(@NonNull Food response, int position);
    void onFail(@NonNull Throwable throwable);
}
