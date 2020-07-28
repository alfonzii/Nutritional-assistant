package com.example.nutritionalassistant;

import static com.example.nutritionalassistant.Constants.Lifestyle;
import static com.example.nutritionalassistant.Constants.Goal;
import static com.example.nutritionalassistant.Constants.Sex;

//SINGLETON CLASS
final class DataHolder {
    private static final DataHolder INSTANCE = new DataHolder();

    private DataHolder() {
    }

    static DataHolder getInstance() {
        return INSTANCE;
    }

    private Sex sex;
    private int height = 0;
    private int weight = 0;
    private int age = 0;

    private Lifestyle lifestyle;
    private Goal goal;

    private int calsGoal = 0;
    private int fatsGoal = 0;
    private int carbsGoal = 0;
    private int protsGoal = 0;
    private int calsCurrent = 0;
    private int fatsCurrent = 0;
    private int carbsCurrent = 0;
    private int protsCurrent = 0;

    int convertSex(Sex sex) {
        if (sex == Sex.MALE)
            return 0;
        else
            return 1;
    }

    Sex convertSex(int i) {
        if (i == 0)
            return Sex.MALE;
        else if (i == 1)
            return Sex.FEMALE;
        else
            throw new IllegalArgumentException();
    }

    int getAge() {
        return age;
    }

    Sex getSex() {
        return sex;
    }

    int getWeight() {
        return weight;
    }

    int getHeight() {
        return height;
    }

    Lifestyle getLifestyle() {
        return lifestyle;
    }

    Goal getGoal() {
        return goal;
    }

    int getCalsCurrent() {
        return calsCurrent;
    }

    int getCalsGoal() {
        return calsGoal;
    }

    int getCarbsCurrent() {
        return carbsCurrent;
    }

    int getCarbsGoal() {
        return carbsGoal;
    }

    int getFatsCurrent() {
        return fatsCurrent;
    }

    int getFatsGoal() {
        return fatsGoal;
    }

    int getProtsCurrent() {
        return protsCurrent;
    }

    int getProtsGoal() {
        return protsGoal;
    }


    void setAge(int age) {
        this.age = age;
    }

    void setSex(Sex sex) {
        this.sex = sex;
    }

    void setWeight(int weight) {
        this.weight = weight;
    }

    void setHeight(int height) {
        this.height = height;
    }

    void setLifestyle(Lifestyle lifestyle) {
        this.lifestyle = lifestyle;
    }

    void setGoal(Goal goal) {
        this.goal = goal;
    }

    void setCalsCurrent(int calsCurrent) {
        this.calsCurrent = calsCurrent;
    }

    void setCalsGoal(int calsGoal) {
        this.calsGoal = calsGoal;
    }

    void setCarbsCurrent(int carbsCurrent) {
        this.carbsCurrent = carbsCurrent;
    }

    void setCarbsGoal(int carbsGoal) {
        this.carbsGoal = carbsGoal;
    }

    void setFatsCurrent(int fatsCurrent) {
        this.fatsCurrent = fatsCurrent;
    }

    void setFatsGoal(int fatsGoal) {
        this.fatsGoal = fatsGoal;
    }

    void setProtsCurrent(int protsCurrent) {
        this.protsCurrent = protsCurrent;
    }

    void setProtsGoal(int protsGoal) {
        this.protsGoal = protsGoal;
    }
}
