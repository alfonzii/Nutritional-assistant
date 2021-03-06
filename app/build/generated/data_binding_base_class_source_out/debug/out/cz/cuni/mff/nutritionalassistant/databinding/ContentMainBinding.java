// Generated by view binder compiler. Do not edit!
package cz.cuni.mff.nutritionalassistant.databinding;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.viewbinding.ViewBinding;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import cz.cuni.mff.nutritionalassistant.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ContentMainBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final LinearLayout LinearLayoutBreakfast;

  @NonNull
  public final LinearLayout LinearLayoutDinner;

  @NonNull
  public final LinearLayout LinearLayoutLunch;

  @NonNull
  public final LinearLayout LinearLayoutSnack;

  @NonNull
  public final Button button5;

  @NonNull
  public final Button button6;

  @NonNull
  public final Button button7;

  @NonNull
  public final Button button8;

  @NonNull
  public final Button buttonRegenerate;

  @NonNull
  public final LayoutGeneratedFoodBinding generatedFoodBreakfast;

  @NonNull
  public final LayoutGeneratedFoodBinding generatedFoodDinner;

  @NonNull
  public final LayoutGeneratedFoodBinding generatedFoodLunch;

  @NonNull
  public final LayoutGeneratedFoodBinding generatedFoodSnack;

  @NonNull
  public final TextView mainBreakfast;

  @NonNull
  public final TextView mainDinner;

  @NonNull
  public final TextView mainLunch;

  @NonNull
  public final TextView mainSnack;

  @NonNull
  public final ProgressBar progressBar;

  @NonNull
  public final TextView textAttribution;

  @NonNull
  public final TextView textCalories;

  @NonNull
  public final TextView textCaloriesValue;

  @NonNull
  public final TextView textCarbs;

  @NonNull
  public final TextView textCarbsValue;

  @NonNull
  public final TextView textFats;

  @NonNull
  public final TextView textFatsValue;

  @NonNull
  public final TextView textProteins;

  @NonNull
  public final TextView textProteinsValue;

  private ContentMainBinding(@NonNull ConstraintLayout rootView,
      @NonNull LinearLayout LinearLayoutBreakfast, @NonNull LinearLayout LinearLayoutDinner,
      @NonNull LinearLayout LinearLayoutLunch, @NonNull LinearLayout LinearLayoutSnack,
      @NonNull Button button5, @NonNull Button button6, @NonNull Button button7,
      @NonNull Button button8, @NonNull Button buttonRegenerate,
      @NonNull LayoutGeneratedFoodBinding generatedFoodBreakfast,
      @NonNull LayoutGeneratedFoodBinding generatedFoodDinner,
      @NonNull LayoutGeneratedFoodBinding generatedFoodLunch,
      @NonNull LayoutGeneratedFoodBinding generatedFoodSnack, @NonNull TextView mainBreakfast,
      @NonNull TextView mainDinner, @NonNull TextView mainLunch, @NonNull TextView mainSnack,
      @NonNull ProgressBar progressBar, @NonNull TextView textAttribution,
      @NonNull TextView textCalories, @NonNull TextView textCaloriesValue,
      @NonNull TextView textCarbs, @NonNull TextView textCarbsValue, @NonNull TextView textFats,
      @NonNull TextView textFatsValue, @NonNull TextView textProteins,
      @NonNull TextView textProteinsValue) {
    this.rootView = rootView;
    this.LinearLayoutBreakfast = LinearLayoutBreakfast;
    this.LinearLayoutDinner = LinearLayoutDinner;
    this.LinearLayoutLunch = LinearLayoutLunch;
    this.LinearLayoutSnack = LinearLayoutSnack;
    this.button5 = button5;
    this.button6 = button6;
    this.button7 = button7;
    this.button8 = button8;
    this.buttonRegenerate = buttonRegenerate;
    this.generatedFoodBreakfast = generatedFoodBreakfast;
    this.generatedFoodDinner = generatedFoodDinner;
    this.generatedFoodLunch = generatedFoodLunch;
    this.generatedFoodSnack = generatedFoodSnack;
    this.mainBreakfast = mainBreakfast;
    this.mainDinner = mainDinner;
    this.mainLunch = mainLunch;
    this.mainSnack = mainSnack;
    this.progressBar = progressBar;
    this.textAttribution = textAttribution;
    this.textCalories = textCalories;
    this.textCaloriesValue = textCaloriesValue;
    this.textCarbs = textCarbs;
    this.textCarbsValue = textCarbsValue;
    this.textFats = textFats;
    this.textFatsValue = textFatsValue;
    this.textProteins = textProteins;
    this.textProteinsValue = textProteinsValue;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ContentMainBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ContentMainBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.content_main, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ContentMainBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    String missingId;
    missingId: {
      LinearLayout LinearLayoutBreakfast = rootView.findViewById(R.id.LinearLayout_breakfast);
      if (LinearLayoutBreakfast == null) {
        missingId = "LinearLayoutBreakfast";
        break missingId;
      }
      LinearLayout LinearLayoutDinner = rootView.findViewById(R.id.LinearLayout_dinner);
      if (LinearLayoutDinner == null) {
        missingId = "LinearLayoutDinner";
        break missingId;
      }
      LinearLayout LinearLayoutLunch = rootView.findViewById(R.id.LinearLayout_lunch);
      if (LinearLayoutLunch == null) {
        missingId = "LinearLayoutLunch";
        break missingId;
      }
      LinearLayout LinearLayoutSnack = rootView.findViewById(R.id.LinearLayout_snack);
      if (LinearLayoutSnack == null) {
        missingId = "LinearLayoutSnack";
        break missingId;
      }
      Button button5 = rootView.findViewById(R.id.button5);
      if (button5 == null) {
        missingId = "button5";
        break missingId;
      }
      Button button6 = rootView.findViewById(R.id.button6);
      if (button6 == null) {
        missingId = "button6";
        break missingId;
      }
      Button button7 = rootView.findViewById(R.id.button7);
      if (button7 == null) {
        missingId = "button7";
        break missingId;
      }
      Button button8 = rootView.findViewById(R.id.button8);
      if (button8 == null) {
        missingId = "button8";
        break missingId;
      }
      Button buttonRegenerate = rootView.findViewById(R.id.button_regenerate);
      if (buttonRegenerate == null) {
        missingId = "buttonRegenerate";
        break missingId;
      }
      View generatedFoodBreakfast = rootView.findViewById(R.id.generated_food_breakfast);
      if (generatedFoodBreakfast == null) {
        missingId = "generatedFoodBreakfast";
        break missingId;
      }
      LayoutGeneratedFoodBinding generatedFoodBreakfastBinding = LayoutGeneratedFoodBinding.bind(generatedFoodBreakfast);
      View generatedFoodDinner = rootView.findViewById(R.id.generated_food_dinner);
      if (generatedFoodDinner == null) {
        missingId = "generatedFoodDinner";
        break missingId;
      }
      LayoutGeneratedFoodBinding generatedFoodDinnerBinding = LayoutGeneratedFoodBinding.bind(generatedFoodDinner);
      View generatedFoodLunch = rootView.findViewById(R.id.generated_food_lunch);
      if (generatedFoodLunch == null) {
        missingId = "generatedFoodLunch";
        break missingId;
      }
      LayoutGeneratedFoodBinding generatedFoodLunchBinding = LayoutGeneratedFoodBinding.bind(generatedFoodLunch);
      View generatedFoodSnack = rootView.findViewById(R.id.generated_food_snack);
      if (generatedFoodSnack == null) {
        missingId = "generatedFoodSnack";
        break missingId;
      }
      LayoutGeneratedFoodBinding generatedFoodSnackBinding = LayoutGeneratedFoodBinding.bind(generatedFoodSnack);
      TextView mainBreakfast = rootView.findViewById(R.id.mainBreakfast);
      if (mainBreakfast == null) {
        missingId = "mainBreakfast";
        break missingId;
      }
      TextView mainDinner = rootView.findViewById(R.id.mainDinner);
      if (mainDinner == null) {
        missingId = "mainDinner";
        break missingId;
      }
      TextView mainLunch = rootView.findViewById(R.id.mainLunch);
      if (mainLunch == null) {
        missingId = "mainLunch";
        break missingId;
      }
      TextView mainSnack = rootView.findViewById(R.id.mainSnack);
      if (mainSnack == null) {
        missingId = "mainSnack";
        break missingId;
      }
      ProgressBar progressBar = rootView.findViewById(R.id.progressBar);
      if (progressBar == null) {
        missingId = "progressBar";
        break missingId;
      }
      TextView textAttribution = rootView.findViewById(R.id.text_attribution);
      if (textAttribution == null) {
        missingId = "textAttribution";
        break missingId;
      }
      TextView textCalories = rootView.findViewById(R.id.text_calories);
      if (textCalories == null) {
        missingId = "textCalories";
        break missingId;
      }
      TextView textCaloriesValue = rootView.findViewById(R.id.text_calories_value);
      if (textCaloriesValue == null) {
        missingId = "textCaloriesValue";
        break missingId;
      }
      TextView textCarbs = rootView.findViewById(R.id.text_carbs);
      if (textCarbs == null) {
        missingId = "textCarbs";
        break missingId;
      }
      TextView textCarbsValue = rootView.findViewById(R.id.text_carbs_value);
      if (textCarbsValue == null) {
        missingId = "textCarbsValue";
        break missingId;
      }
      TextView textFats = rootView.findViewById(R.id.text_fats);
      if (textFats == null) {
        missingId = "textFats";
        break missingId;
      }
      TextView textFatsValue = rootView.findViewById(R.id.text_fats_value);
      if (textFatsValue == null) {
        missingId = "textFatsValue";
        break missingId;
      }
      TextView textProteins = rootView.findViewById(R.id.text_proteins);
      if (textProteins == null) {
        missingId = "textProteins";
        break missingId;
      }
      TextView textProteinsValue = rootView.findViewById(R.id.text_proteins_value);
      if (textProteinsValue == null) {
        missingId = "textProteinsValue";
        break missingId;
      }
      return new ContentMainBinding((ConstraintLayout) rootView, LinearLayoutBreakfast,
          LinearLayoutDinner, LinearLayoutLunch, LinearLayoutSnack, button5, button6, button7,
          button8, buttonRegenerate, generatedFoodBreakfastBinding, generatedFoodDinnerBinding,
          generatedFoodLunchBinding, generatedFoodSnackBinding, mainBreakfast, mainDinner,
          mainLunch, mainSnack, progressBar, textAttribution, textCalories, textCaloriesValue,
          textCarbs, textCarbsValue, textFats, textFatsValue, textProteins, textProteinsValue);
    }
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
