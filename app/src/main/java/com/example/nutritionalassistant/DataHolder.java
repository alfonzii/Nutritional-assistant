package com.example.nutritionalassistant;

import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

//SINGLETON CLASS
final class DataHolder {
    private static final DataHolder INSTANCE = new DataHolder();

    private DataHolder() {
    }

    static DataHolder getInstance() {
        return INSTANCE;
    }

    private int sex = 0;
    private int height = 0;
    private int weight = 0;
    private int age = 0;

    private int lifestyle = -1;
    private int goal = -1;

    private int calsGoal = 0;
    private int fatsGoal = 0;
    private int carbsGoal = 0;
    private int protsGoal = 0;
    private int calsCurrent = 0;
    private int fatsCurrent = 0;
    private int carbsCurrent = 0;
    private int protsCurrent = 0;

    int getAge() {
        return age;
    }

    int getSex() {
        return sex;
    }

    int getWeight() {
        return weight;
    }

    int getHeight() {
        return height;
    }

    int getLifestyle() {
        return lifestyle;
    }

    int getGoal() {
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

    void setSex(int sex) {
        this.sex = sex;
    }

    void setWeight(int weight) {
        this.weight = weight;
    }

    void setHeight(int height) {
        this.height = height;
    }

    void setLifestyle(int lifestyle) {
        this.lifestyle = lifestyle;
    }

    void setGoal(int goal) {
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
