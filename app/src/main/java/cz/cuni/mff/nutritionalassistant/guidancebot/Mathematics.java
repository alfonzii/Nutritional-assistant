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
    float getModifiedTEE(int modif) {
        return getTEE() + dataHolder.getGoal().getTeeModification() - modif;
    }

    float getModifiedTEE() {
        return getTEE() + dataHolder.getGoal().getTeeModification();
    }

//------------------------------------------------------------------------------------------------------------------------------------------------

    // For UI purposes
    void setGoalNH() {
        dataHolder.setCaloriesGoal(getModifiedTEE());
        dataHolder.setFatsGoal(0.28f * (getModifiedTEE() / 9));
        dataHolder.setCarbohydratesGoal(0.58f * (getModifiedTEE() / 4));
        dataHolder.setProteinsGoal(0.14f * (getModifiedTEE() / 4));
    }

    // For mealplan generation purposes
    void setConstraints(float tee) {
        Random random = new Random();

        fatsReq = ((random.nextInt(6) + 25) / 100f) * (tee / 9);
        carbsReq = ((random.nextInt(6) + 55) / 100f) * (tee / 4);
        protsReq = ((random.nextInt(6) + 10) / 100f) * (tee / 4);

        calsConstr = new Pair<>(0.9f * tee, 1.1f * tee);
        fatsConstr = new Pair<>(0.9f * fatsReq, 1.1f * fatsReq);
        carbConstr = new Pair<>(0.9f * carbsReq, 1.1f * carbsReq);
        protConstr = new Pair<>(0.9f * protsReq, 1.1f * protsReq);

        breakfastConstr = new Pair<>(0.9f * (0.25f * tee), 1.1f * (0.25f * tee));
        lunchConstr = new Pair<>(0.9f * (0.35f * tee), 1.1f * (0.35f * tee));
        dinnerConstr = new Pair<>(0.9f * (0.2f * tee), 1.1f * (0.2f * tee));
        snackConstr = new Pair<>(0.9f * (0.2f * tee), 1.1f * (0.2f * tee));

        saveConstraints();
    }

    void updateConstraints() {
        calsConstr = new Pair<>(0.9f * dataHolder.getCaloriesGoal() - dataHolder.getCaloriesCurrent(), 1.1f * dataHolder.getCaloriesGoal() - dataHolder.getCaloriesCurrent());
        fatsConstr = new Pair<>(0.9f * dataHolder.getFatsReq() - dataHolder.getFatsCurrent(), 1.1f * dataHolder.getFatsReq() - dataHolder.getFatsCurrent());
        carbConstr = new Pair<>(0.9f * dataHolder.getCarbsReq() - dataHolder.getCarbohydratesCurrent(), 1.1f * dataHolder.getCarbsReq() - dataHolder.getCarbohydratesCurrent());
        protConstr = new Pair<>(0.9f * dataHolder.getProtsReq() - dataHolder.getProteinsCurrent(), 1.1f * dataHolder.getProtsReq() - dataHolder.getProteinsCurrent());

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
