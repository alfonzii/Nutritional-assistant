package cz.cuni.mff.nutritionalassistant.guidancebot;

import android.util.Pair;

import java.util.Random;

import cz.cuni.mff.nutritionalassistant.Constants;
import cz.cuni.mff.nutritionalassistant.DataHolder;
import lombok.Getter;

@Getter
public class Mathematics {
    private static Mathematics INSTANCE;

    private DataHolder dataHolder;

    private float fatsReq, carbsReq, protsReq;
    private Pair<Float, Float> calsConstr, fatsConstr, carbConstr, protConstr;
    private Pair<Float, Float> breakfastConstr, lunchConstr, dinnerConstr, snackConstr;

    // constants which can be changed to create different constraints
    private float marginError = 0.1f;
    private float lowerMargin = 1f - marginError, upperMargin = 1f + marginError;
    private float pBreakfast = 0.25f;
    private float pLunch = 0.35f;
    private float pDinner = 0.2f;
    private float pSnack = 0.2f;


    private Mathematics() {
        dataHolder = DataHolder.getInstance();
    }

    static Mathematics getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Mathematics();
        }
        return  INSTANCE;
    }

    // Basal Metabolic Rate
    private float getBMR() {
        float bmr;
        bmr = 10 * dataHolder.getWeight() + 6.25f * dataHolder.getHeight() - 5 * dataHolder.getAge();
        bmr += dataHolder.getSex() == Constants.Sex.MALE ? 5 : -161;
        return bmr;
    }

    // Thermic Effect of Food
    private float getTEF() {
        return getBMR() * 0.1f;
    }

    // Thermic Effect of Activity
    private float getTEA() {
        return getBMR() * dataHolder.getLifestyle().getPercentOfBMR();
    }

    // Total Energy Expenditure
    private float getTEE() {
        return getBMR() + getTEF() + getTEA();
    }

    // modif parameters is for further modification of TEE, for example when having surplus
    // of calories from yesterday
    float getModifiedTEE(float caloriesExcess) {
        return getTEE() + dataHolder.getGoal().getTeeModification() - caloriesExcess;
    }

    float getModifiedTEE() {
        return getTEE() + dataHolder.getGoal().getTeeModification();
    }

//------------------------------------------------------------------------------------------------------------------------------------------------

    // For UI purposes
    void setGoalNH(float caloriesExcess) {
        dataHolder.setCaloriesGoal(getModifiedTEE(caloriesExcess));
        dataHolder.setFatsGoal(0.28f * (getModifiedTEE(caloriesExcess) / 9));
        dataHolder.setCarbohydratesGoal(0.58f * (getModifiedTEE(caloriesExcess) / 4));
        dataHolder.setProteinsGoal(0.14f * (getModifiedTEE(caloriesExcess) / 4));
    }

    // For mealplan generation purposes
    void setConstraints(float tee) {
        Random random = new Random();

        fatsReq = ((random.nextInt(6) + 25) / 100f) * (tee / 9);
        carbsReq = ((random.nextInt(6) + 55) / 100f) * (tee / 4);
        protsReq = ((random.nextInt(6) + 10) / 100f) * (tee / 4);

        calsConstr = new Pair<>(lowerMargin * tee, upperMargin * tee);
        fatsConstr = new Pair<>(lowerMargin * fatsReq, upperMargin * fatsReq);
        carbConstr = new Pair<>(lowerMargin * carbsReq, upperMargin * carbsReq);
        protConstr = new Pair<>(lowerMargin * protsReq, upperMargin * protsReq);

        breakfastConstr = new Pair<>(lowerMargin * (pBreakfast * tee), upperMargin * (pBreakfast * tee));
        lunchConstr = new Pair<>(lowerMargin * (pLunch * tee), upperMargin * (pLunch * tee));
        dinnerConstr = new Pair<>(lowerMargin * (pDinner * tee), upperMargin * (pDinner * tee));
        snackConstr = new Pair<>(lowerMargin * (pSnack * tee), upperMargin * (pSnack * tee));

        saveConstraints();
    }

    void updateConstraints() {
        calsConstr = new Pair<>(lowerMargin * dataHolder.getCaloriesGoal() - dataHolder.getCaloriesCurrent(), upperMargin * dataHolder.getCaloriesGoal() - dataHolder.getCaloriesCurrent());
        fatsConstr = new Pair<>(lowerMargin * dataHolder.getFatsReq() - dataHolder.getFatsCurrent(), upperMargin * dataHolder.getFatsReq() - dataHolder.getFatsCurrent());
        carbConstr = new Pair<>(lowerMargin * dataHolder.getCarbsReq() - dataHolder.getCarbohydratesCurrent(), upperMargin * dataHolder.getCarbsReq() - dataHolder.getCarbohydratesCurrent());
        protConstr = new Pair<>(lowerMargin * dataHolder.getProtsReq() - dataHolder.getProteinsCurrent(), upperMargin * dataHolder.getProtsReq() - dataHolder.getProteinsCurrent());

        dataHolder.setCalsConstr(calsConstr);
        dataHolder.setFatsConstr(fatsConstr);
        dataHolder.setCarbConstr(carbConstr);
        dataHolder.setProtConstr(protConstr);
    }

    private void saveConstraints() {
        dataHolder.setFatsReq(fatsReq);
        dataHolder.setCarbsReq(carbsReq);
        dataHolder.setProtsReq(protsReq);

        dataHolder.setCalsConstr(calsConstr);
        dataHolder.setFatsConstr(fatsConstr);
        dataHolder.setCarbConstr(carbConstr);
        dataHolder.setProtConstr(protConstr);

        dataHolder.setBreakfastConstr(breakfastConstr);
        dataHolder.setLunchConstr(lunchConstr);
        dataHolder.setDinnerConstr(dinnerConstr);
        dataHolder.setSnackConstr(snackConstr);
    }

    @Getter
    public static enum Lifestyle {
        SEDENTARY(0.2f),
        MILD_ACTIVITY(0.375f),
        MEDIUM_ACTIVITY(0.55f),
        HIGH_ACTIVITY(0.725f);

        private float percentOfBMR;

        Lifestyle(float percentOfBMR) {
            this.percentOfBMR = percentOfBMR;
        }
    }

    @Getter
    public static enum Goal {
        GAIN(500),
        LOSE(-500),
        MAINTAIN(0);

        private int teeModification;

        Goal(int teeModification) {
            this.teeModification = teeModification;
        }
    }

}
