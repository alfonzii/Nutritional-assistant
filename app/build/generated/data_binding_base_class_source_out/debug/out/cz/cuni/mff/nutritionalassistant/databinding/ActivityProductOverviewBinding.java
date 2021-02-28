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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import cz.cuni.mff.nutritionalassistant.R;
import cz.cuni.mff.nutritionalassistant.activity.overview.DynamicWidthSpinner;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityProductOverviewBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final ConstraintLayout ConstraintLayoutHeader;

  @NonNull
  public final Button buttonAdd;

  @NonNull
  public final EditText numberQuantity;

  @NonNull
  public final Spinner spinnerMeal;

  @NonNull
  public final DynamicWidthSpinner spinnerServingUnit;

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
  public final TextView textMeal;

  @NonNull
  public final TextView textProductBrand;

  @NonNull
  public final TextView textProductName;

  @NonNull
  public final TextView textProteins;

  @NonNull
  public final TextView textProteinsValue;

  @NonNull
  public final TextView textQuantity;

  @NonNull
  public final TextView textWeightUnit;

  @NonNull
  public final ImageView thumbnail;

  private ActivityProductOverviewBinding(@NonNull LinearLayout rootView,
      @NonNull ConstraintLayout ConstraintLayoutHeader, @NonNull Button buttonAdd,
      @NonNull EditText numberQuantity, @NonNull Spinner spinnerMeal,
      @NonNull DynamicWidthSpinner spinnerServingUnit, @NonNull TextView textCalories,
      @NonNull TextView textCaloriesValue, @NonNull TextView textCarbs,
      @NonNull TextView textCarbsValue, @NonNull TextView textFats, @NonNull TextView textFatsValue,
      @NonNull TextView textMeal, @NonNull TextView textProductBrand,
      @NonNull TextView textProductName, @NonNull TextView textProteins,
      @NonNull TextView textProteinsValue, @NonNull TextView textQuantity,
      @NonNull TextView textWeightUnit, @NonNull ImageView thumbnail) {
    this.rootView = rootView;
    this.ConstraintLayoutHeader = ConstraintLayoutHeader;
    this.buttonAdd = buttonAdd;
    this.numberQuantity = numberQuantity;
    this.spinnerMeal = spinnerMeal;
    this.spinnerServingUnit = spinnerServingUnit;
    this.textCalories = textCalories;
    this.textCaloriesValue = textCaloriesValue;
    this.textCarbs = textCarbs;
    this.textCarbsValue = textCarbsValue;
    this.textFats = textFats;
    this.textFatsValue = textFatsValue;
    this.textMeal = textMeal;
    this.textProductBrand = textProductBrand;
    this.textProductName = textProductName;
    this.textProteins = textProteins;
    this.textProteinsValue = textProteinsValue;
    this.textQuantity = textQuantity;
    this.textWeightUnit = textWeightUnit;
    this.thumbnail = thumbnail;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityProductOverviewBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityProductOverviewBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_product_overview, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityProductOverviewBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    String missingId;
    missingId: {
      ConstraintLayout ConstraintLayoutHeader = rootView.findViewById(R.id.ConstraintLayout_header);
      if (ConstraintLayoutHeader == null) {
        missingId = "ConstraintLayoutHeader";
        break missingId;
      }
      Button buttonAdd = rootView.findViewById(R.id.button_add);
      if (buttonAdd == null) {
        missingId = "buttonAdd";
        break missingId;
      }
      EditText numberQuantity = rootView.findViewById(R.id.number_quantity);
      if (numberQuantity == null) {
        missingId = "numberQuantity";
        break missingId;
      }
      Spinner spinnerMeal = rootView.findViewById(R.id.spinner_meal);
      if (spinnerMeal == null) {
        missingId = "spinnerMeal";
        break missingId;
      }
      DynamicWidthSpinner spinnerServingUnit = rootView.findViewById(R.id.spinner_serving_unit);
      if (spinnerServingUnit == null) {
        missingId = "spinnerServingUnit";
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
      TextView textMeal = rootView.findViewById(R.id.text_meal);
      if (textMeal == null) {
        missingId = "textMeal";
        break missingId;
      }
      TextView textProductBrand = rootView.findViewById(R.id.text_product_brand);
      if (textProductBrand == null) {
        missingId = "textProductBrand";
        break missingId;
      }
      TextView textProductName = rootView.findViewById(R.id.text_product_name);
      if (textProductName == null) {
        missingId = "textProductName";
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
      TextView textQuantity = rootView.findViewById(R.id.text_quantity);
      if (textQuantity == null) {
        missingId = "textQuantity";
        break missingId;
      }
      TextView textWeightUnit = rootView.findViewById(R.id.text_weight_unit);
      if (textWeightUnit == null) {
        missingId = "textWeightUnit";
        break missingId;
      }
      ImageView thumbnail = rootView.findViewById(R.id.thumbnail);
      if (thumbnail == null) {
        missingId = "thumbnail";
        break missingId;
      }
      return new ActivityProductOverviewBinding((LinearLayout) rootView, ConstraintLayoutHeader,
          buttonAdd, numberQuantity, spinnerMeal, spinnerServingUnit, textCalories,
          textCaloriesValue, textCarbs, textCarbsValue, textFats, textFatsValue, textMeal,
          textProductBrand, textProductName, textProteins, textProteinsValue, textQuantity,
          textWeightUnit, thumbnail);
    }
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}