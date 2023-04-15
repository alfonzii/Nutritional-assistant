package cz.cuni.mff.nutritionalassistant;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.ContextWrapper;
import android.support.annotation.NonNull;
import android.util.Pair;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.cuni.mff.nutritionalassistant.data.DataHolder;
import cz.cuni.mff.nutritionalassistant.foodtypes.Food;
import cz.cuni.mff.nutritionalassistant.guidancebot.Brain;
import cz.cuni.mff.nutritionalassistant.guidancebot.GeneratedFoodListCallback;
import lombok.Getter;

public final class MainViewModel extends AndroidViewModel {
    private final MutableLiveData<Boolean> nutValuesTrigger;
    private final MutableLiveData<Boolean> genFoodsTrigger;
    private final MutableLiveData<Boolean> userAddTrigger;

    private final MutableLiveData<Boolean> progressBarLoading;
    private final MutableLiveData<Boolean> checkboxesEnabled;
    // 1 - succes, 2 - fail, 3 - exception
    private final MutableLiveData<Integer> backendRegenerateCallResult;

    private DataHolder dataHolder;
    @Getter
    private Throwable failThrowable;

    public MainViewModel(@NonNull Application application) {
        super(application);

        nutValuesTrigger = new MutableLiveData<>();
        nutValuesTrigger.setValue(false);
        genFoodsTrigger = new MutableLiveData<>();
        genFoodsTrigger.setValue(false);
        userAddTrigger = new MutableLiveData<>();
        userAddTrigger.setValue(false);

        progressBarLoading = new MutableLiveData<>();
        progressBarLoading.setValue(false);
        checkboxesEnabled = new MutableLiveData<>();
        checkboxesEnabled.setValue(true);
        backendRegenerateCallResult = new MutableLiveData<>();
        backendRegenerateCallResult.setValue(0);

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

    LiveData<Boolean> getProgressBarLoading() {
        return progressBarLoading;
    }
    LiveData<Boolean> getCheckboxesEnabled() {
        return checkboxesEnabled;
    }
    LiveData<Integer> getBackendRegenerateCallResult() {
        return backendRegenerateCallResult;
    }



    private void trigValuesRefresh() {
        nutValuesTrigger.setValue(!nutValuesTrigger.getValue());
    }
    private void trigGenFoodsRefresh() {
        genFoodsTrigger.setValue(!genFoodsTrigger.getValue());
    }
    private void postGenFoodsRefresh() {
        genFoodsTrigger.postValue(!genFoodsTrigger.getValue());
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

    public void onCheckboxClick(int checkboxMealID, boolean isChecked) {
        Pair<Food, Boolean> genFood = dataHolder.getGeneratedFoods().get(checkboxMealID);
        if (isChecked) {
            dataHolder.addFoodToCurrentNH(genFood.first);
        } else {
            dataHolder.subtractFoodFromCurrentNH(genFood.first);
        }
        dataHolder.getGeneratedFoods().set(checkboxMealID, new Pair<>(genFood.first, !genFood.second));

        trigValuesRefresh();
    }

    public void regenerateButtonClick() {
        List<Boolean> generatedFoodsChecked = new ArrayList<>();
        for (Pair<Food, Boolean> p : dataHolder.getGeneratedFoods()) {
            generatedFoodsChecked.add(p.second);
        }

        checkboxesEnabled.setValue(false);
        progressBarLoading.setValue(true);

        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    Brain.getInstance().requestRegenerate(generatedFoodsChecked, getApplication(), new GeneratedFoodListCallback() {
                        @Override
                        public void onSuccess(@NonNull List<Food> newGeneratedRecipes, List<Boolean> generatedFoodsFlags) {
                            for (int i = 0; i < MainActivity.MealController.NUMBER_OF_MEALS; i++) {
                                if (!generatedFoodsFlags.get(i)) {
                                    dataHolder.getGeneratedFoods().set(i, new Pair<>(newGeneratedRecipes.get(0), false));
                                    newGeneratedRecipes.remove(0);
                                }
                            }

                            postGenFoodsRefresh();
                            progressBarLoading.postValue(false);
                            backendRegenerateCallResult.postValue(1); // success
                            checkboxesEnabled.postValue(true);
                        }

                        @Override
                        public void onFail(@NonNull Throwable throwable) {
                            progressBarLoading.postValue(false);
                            failThrowable = throwable;
                            backendRegenerateCallResult.postValue(2); // fail
                            checkboxesEnabled.postValue(true);
                        }
                    });
                } catch (NullPointerException e) {
                    progressBarLoading.postValue(false);
                    backendRegenerateCallResult.postValue(3); // exception
                    checkboxesEnabled.postValue(true);
                }
            }
        };
        t.start();
    }
}