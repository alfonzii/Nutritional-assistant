package cz.cuni.mff.nutritionalassistant;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.cuni.mff.nutritionalassistant.foodtypes.Food;
import cz.cuni.mff.nutritionalassistant.foodtypes.FoodAdapterType;
import cz.cuni.mff.nutritionalassistant.guidancebot.Brain;
import cz.cuni.mff.nutritionalassistant.guidancebot.api.AdapterDataCallback;

public final class FoodAddingViewModel extends ViewModel {
    private final MutableLiveData<Integer> spinnerCategorySelection;

    // Key - EditText ID, Value - value of filter parameter
    private final MutableLiveData<HashMap<Integer, Integer>> filterTable;

    // This list will be observed and will be used for appearance of recycler view on UI
    private final MutableLiveData<List<FoodAdapterType>> foodList;

    public FoodAddingViewModel() {
        spinnerCategorySelection = new MutableLiveData<>();
        spinnerCategorySelection.setValue(Food.FoodType.PRODUCT.getId());

        filterTable = new MutableLiveData<>();
        filterTable.setValue(new HashMap<>());

        foodList = new MutableLiveData<>();
        foodList.setValue(new ArrayList<>());
    }

    LiveData<List<FoodAdapterType>> getFoodList() {
        return foodList;
    }
    MutableLiveData<Integer> getSpinnerCategorySelection() {
        return spinnerCategorySelection;
    }
    MutableLiveData<HashMap<Integer, Integer>> getFilterTable() {
        return filterTable;
    }

    public void textSubmit(String query) {

        Brain.getInstance().requestFoodAdapterTypeData(
                query, spinnerCategorySelection.getValue(), filterTable.getValue(), new AdapterDataCallback() {
                    @Override
                    public void onSuccess(@NonNull List<FoodAdapterType> response) {
                        foodList.setValue(response);
                    }

                    @Override
                    public void onFail(@NonNull Throwable throwable) {
                        foodList.getValue().clear();
                    }
                }
        );
    }

}
