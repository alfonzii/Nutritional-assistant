package cz.cuni.mff.nutritionalassistant;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Pair;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import cz.cuni.mff.nutritionalassistant.data.DataHolder;
import cz.cuni.mff.nutritionalassistant.foodtypes.Food;
import cz.cuni.mff.nutritionalassistant.guidancebot.Brain;

public final class MainViewModel extends ViewModel {
    private final MutableLiveData<Boolean> nutValuesTrigger;
    private final MutableLiveData<Boolean> genFoodsTrigger;
    private final MutableLiveData<Boolean> userAddTrigger;

    private DataHolder dataHolder;

    public MainViewModel() {
        nutValuesTrigger = new MutableLiveData<>();
        nutValuesTrigger.setValue(false);
        genFoodsTrigger = new MutableLiveData<>();
        genFoodsTrigger.setValue(false);
        userAddTrigger = new MutableLiveData<>();
        userAddTrigger.setValue(false);

        dataHolder = DataHolder.getInstance();
    }

    /* Instead of LiveData DataHolder, we simulate refreshes previously
     * done in MainActivity by triggering "triggers" which are observed
     * in MainActivity and upon trigger, refresh is induced but from ViewModel
     * so we are sure Activity configuration change won't make problems
     */
    LiveData<Boolean> getNutValuesTrigger() {
        return nutValuesTrigger;
    }
    LiveData<Boolean> getGenFoodsTrigger() {
        return genFoodsTrigger;
    }
    LiveData<Boolean> getUserAddTrigger() {
        return userAddTrigger;
    }

    private void trigValuesRefresh() {
        nutValuesTrigger.setValue(!nutValuesTrigger.getValue());
    }
    private void trigGenFoodsRefresh() {
        genFoodsTrigger.setValue(!genFoodsTrigger.getValue());
    }
    private void trigUserAddRefresh() {
        userAddTrigger.setValue(!userAddTrigger.getValue());
    }
    private void trigFullRefresh() {
        trigValuesRefresh();
        trigGenFoodsRefresh();
        trigUserAddRefresh();
    }


    public void dateCheckInit() {
        // Change .getDateInstance() to .getDateTimeInstance() and rerun to check Nextday
        // functionality instantly.
        if (!dataHolder.getLastRunDate().equals(DateFormat.getDateInstance().format(new Date()))) {
            reset(false);
            Brain.getInstance().requestNHConstraintsCalculation(dataHolder.getCaloriesExcess());
        }

        dataHolder.setLastRunDate(DateFormat.getDateInstance().format(new Date()));

        trigFullRefresh();
    }

    public void reset(boolean isButtonClicked) {
        // clear user added foods backend
        for (List<Food> mealList : dataHolder.getUserAddedFoods()) {
            mealList.clear();
        }

        // clear generatedFoods checkboxes backend flags
        for (int i = 0; i < MainActivity.MealController.NUMBER_OF_MEALS; i++) {
            Food genFood = dataHolder.getGeneratedFoods().get(i).first;
            dataHolder.getGeneratedFoods().set(i, new Pair<>(genFood, false));
        }

        // reset is called from MainActivity initialization
        if (!isButtonClicked) {
            if (dataHolder.getAdHocFlag() == DataHolder.AdHocFlag.NEXTDAY) {
                if (dataHolder.getCaloriesCurrent() - dataHolder.getCaloriesGoal() > 0) {
                    dataHolder.setCaloriesExcess(dataHolder.getCaloriesCurrent() - dataHolder.getCaloriesGoal());
                } else {
                    dataHolder.setCaloriesExcess(0);
                }
            } else {
                dataHolder.setCaloriesExcess(0);
            }
        }

        // set adhoc flag on UNSET
        dataHolder.setAdHocFlag(DataHolder.AdHocFlag.UNSET);

        // annulate NH values
        dataHolder.setCaloriesCurrent(0);
        dataHolder.setFatsCurrent(0);
        dataHolder.setCarbohydratesCurrent(0);
        dataHolder.setProteinsCurrent(0);

        trigFullRefresh();
    }
}



/*
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
     */