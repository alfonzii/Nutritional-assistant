package cz.cuni.mff.nutritionalassistant;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Pair;

import java.util.List;

import cz.cuni.mff.nutritionalassistant.foodtypes.Food;

public final class MainViewModel extends ViewModel {

    private MutableLiveData<Float> caloriesGoal;
    private MutableLiveData<Float> fatsGoal;
    private MutableLiveData<Float> carbohydratesGoal;
    private MutableLiveData<Float> proteinsGoal;
    private MutableLiveData<Float> caloriesCurrent;
    private MutableLiveData<Float> fatsCurrent;
    private MutableLiveData<Float> carbohydratesCurrent;
    private MutableLiveData<Float> proteinsCurrent;

    private List<Pair<Food, Boolean>> generatedFoods;
    private List<List<Food>> userAddedFoods;


    public LiveData<Float> getCaloriesGoal() {
        return caloriesGoal;
    }
    public LiveData<Float> getFatsGoal() {
        return fatsGoal;
    }
    public LiveData<Float> getCarbohydratesGoal() {
        return carbohydratesGoal;
    }
    public LiveData<Float> getProteinsGoal() {
        return proteinsGoal;
    }
    public LiveData<Float> getCaloriesCurrent() {
        return caloriesCurrent;
    }
    public LiveData<Float> getFatsCurrent() {
        return fatsCurrent;
    }
    public LiveData<Float> getCarbohydratesCurrent() {
        return carbohydratesCurrent;
    }
    public LiveData<Float> getProteinsCurrent() {
        return proteinsCurrent;
    }
}
