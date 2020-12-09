package cz.cuni.mff.nutritionalassistant;

import android.app.Activity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import cz.cuni.mff.nutritionalassistant.foodtypes.Food;
import cz.cuni.mff.nutritionalassistant.foodtypes.Product;
import lombok.Getter;

public class LinearLayoutTouchListener implements View.OnTouchListener {
    static final String logTag = "ActivitySwipeDetector";
    private Activity activity;
    private View item;
    private Food food;
    static final int MIN_DISTANCE = 100;// TODO change this runtime based on screen resolution. for 1920x1080 is to small the 100 distance
    private float downX, downY, upX, upY;

    private DataHolder dataHolder = DataHolder.getInstance();

    // private MainActivity mMainActivity;

    public LinearLayoutTouchListener(MainActivity mainActivity, View item, Food food) {
        activity = mainActivity;
        this.item = item;
        this.food = food;
    }

    public void onRightToLeftSwipe() {
        Log.i(logTag, "RightToLeftSwipe!");
        Toast.makeText(activity, "RightToLeftSwipe", Toast.LENGTH_SHORT).show();
        // activity.doSomething();
    }

    public void onLeftToRightSwipe() {
        Log.i(logTag, "LeftToRightSwipe!");
        Toast.makeText(activity, "LeftToRightSwipe", Toast.LENGTH_SHORT).show();
        // activity.doSomething();
    }

    public void onTopToBottomSwipe() {
        Log.i(logTag, "onTopToBottomSwipe!");
        Toast.makeText(activity, "onTopToBottomSwipe", Toast.LENGTH_SHORT).show();
        // activity.doSomething();
    }

    public void onBottomToTopSwipe() {
        Log.i(logTag, "onBottomToTopSwipe!");
        Toast.makeText(activity, "onBottomToTopSwipe", Toast.LENGTH_SHORT).show();
        // activity.doSomething();
    }

    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                downX = event.getX();
                downY = event.getY();
                return true;
            }
            case MotionEvent.ACTION_UP: {
                upX = event.getX();
                upY = event.getY();

                float deltaX = downX - upX;
                float deltaY = downY - upY;

                // swipe horizontal?
                if (Math.abs(deltaX) > MIN_DISTANCE) {
                    // left or right
                    if (deltaX < 0) {
                        this.onLeftToRightSwipe();
                        return true;
                    }
                    if (deltaX > 0) {
                        this.onRightToLeftSwipe();
                        ViewGroup parent = ((ViewGroup) item.getParent());
                        nutritionValuesSubtraction();
                        // (indexOfChild - 2) because first child is constLayout and second is generated food.
                        // First added food (if any) starts with index 3.
                        removeFoodFromDataholder(getMealLayoutIndex(parent), parent.indexOfChild(item) - 2);
                        parent.removeView(item);
                        ((MainActivity) activity).refreshValues();
                        return true;
                    }
                } else {
                    Log.i(logTag, "Swipe was only " + Math.abs(deltaX) + " long horizontally, need at least " + MIN_DISTANCE);
                    // If we don't swipe food, then we click it and show detailed info
                    ((MainActivity) activity).setClickedFood(food);
                    v.performClick();
                    // return false; // We don't consume the event
                }

                // swipe vertical?
                if (Math.abs(deltaY) > MIN_DISTANCE) {
                    // top or down
                    if (deltaY < 0) {
                        this.onTopToBottomSwipe();
                        return true;
                    }
                    if (deltaY > 0) {
                        this.onBottomToTopSwipe();
                        return true;
                    }
                } else {
                    Log.i(logTag, "Swipe was only " + Math.abs(deltaX) + " long vertically, need at least " + MIN_DISTANCE);
                    // return false; // We don't consume the event
                }

                return false; // no swipe horizontally and no swipe vertically
            }// case MotionEvent.ACTION_UP:
        }
        return false;
    }

    private void nutritionValuesSubtraction() {
        dataHolder.setCaloriesCurrent(dataHolder.getCaloriesCurrent() - food.getCalories());
        dataHolder.setFatsCurrent(dataHolder.getFatsCurrent() - food.getFats());
        dataHolder.setCarbohydratesCurrent(dataHolder.getCarbohydratesCurrent() - food.getCarbohydrates());
        dataHolder.setProteinsCurrent(dataHolder.getProteinsCurrent() - food.getProteins());
    }

    private void removeFoodFromDataholder(int mealIndex, int foodIndex) {
        dataHolder.getEatenFood().get(mealIndex).remove(foodIndex);
    }

    private int getMealLayoutIndex(ViewGroup parent) {
        switch (parent.getId()) {
            case R.id.LinearLayout_breakfast:
                return MainActivity.BREAKFAST;
            case R.id.LinearLayout_lunch:
                return MainActivity.LUNCH;
            case R.id.LinearLayout_dinner:
                return MainActivity.DINNER;
            case R.id.LinearLayout_snack:
                return MainActivity.SNACK;
            default:
                throw new IllegalStateException("Unexpected value: " + parent.getId());
        }
    }
}
