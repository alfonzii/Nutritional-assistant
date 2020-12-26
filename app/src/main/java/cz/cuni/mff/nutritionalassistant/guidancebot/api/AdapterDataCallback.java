package cz.cuni.mff.nutritionalassistant.guidancebot.api;

import android.support.annotation.NonNull;

import java.util.List;

import cz.cuni.mff.nutritionalassistant.foodtypes.FoodAdapterType;

public interface AdapterDataCallback {
    void onSuccess(@NonNull List<FoodAdapterType> response);
    void onFail(@NonNull Throwable throwable);
}
