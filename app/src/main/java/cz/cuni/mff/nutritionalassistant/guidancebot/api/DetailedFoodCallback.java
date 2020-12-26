package cz.cuni.mff.nutritionalassistant.guidancebot.api;

import android.support.annotation.NonNull;

import cz.cuni.mff.nutritionalassistant.foodtypes.Food;

public interface DetailedFoodCallback {
    void onSuccess(@NonNull Food response);
    void onFail(@NonNull Throwable throwable);
}
