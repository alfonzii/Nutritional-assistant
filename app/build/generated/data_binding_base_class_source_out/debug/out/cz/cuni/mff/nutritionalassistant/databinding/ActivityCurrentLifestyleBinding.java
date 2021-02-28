// Generated by view binder compiler. Do not edit!
package cz.cuni.mff.nutritionalassistant.databinding;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.viewbinding.ViewBinding;
import android.widget.Button;
import android.widget.TextView;
import cz.cuni.mff.nutritionalassistant.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityCurrentLifestyleBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final AppBarLayout ToolbarLayout;

  @NonNull
  public final Button buttonHigh;

  @NonNull
  public final Button buttonMedium;

  @NonNull
  public final Button buttonMild;

  @NonNull
  public final Button buttonSedentary;

  @NonNull
  public final Guideline guideline4;

  @NonNull
  public final Guideline guideline5;

  @NonNull
  public final TextView textTitle;

  @NonNull
  public final Toolbar toolbar;

  private ActivityCurrentLifestyleBinding(@NonNull ConstraintLayout rootView,
      @NonNull AppBarLayout ToolbarLayout, @NonNull Button buttonHigh, @NonNull Button buttonMedium,
      @NonNull Button buttonMild, @NonNull Button buttonSedentary, @NonNull Guideline guideline4,
      @NonNull Guideline guideline5, @NonNull TextView textTitle, @NonNull Toolbar toolbar) {
    this.rootView = rootView;
    this.ToolbarLayout = ToolbarLayout;
    this.buttonHigh = buttonHigh;
    this.buttonMedium = buttonMedium;
    this.buttonMild = buttonMild;
    this.buttonSedentary = buttonSedentary;
    this.guideline4 = guideline4;
    this.guideline5 = guideline5;
    this.textTitle = textTitle;
    this.toolbar = toolbar;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityCurrentLifestyleBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityCurrentLifestyleBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_current_lifestyle, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityCurrentLifestyleBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    String missingId;
    missingId: {
      AppBarLayout ToolbarLayout = rootView.findViewById(R.id.ToolbarLayout);
      if (ToolbarLayout == null) {
        missingId = "ToolbarLayout";
        break missingId;
      }
      Button buttonHigh = rootView.findViewById(R.id.button_high);
      if (buttonHigh == null) {
        missingId = "buttonHigh";
        break missingId;
      }
      Button buttonMedium = rootView.findViewById(R.id.button_medium);
      if (buttonMedium == null) {
        missingId = "buttonMedium";
        break missingId;
      }
      Button buttonMild = rootView.findViewById(R.id.button_mild);
      if (buttonMild == null) {
        missingId = "buttonMild";
        break missingId;
      }
      Button buttonSedentary = rootView.findViewById(R.id.button_sedentary);
      if (buttonSedentary == null) {
        missingId = "buttonSedentary";
        break missingId;
      }
      Guideline guideline4 = rootView.findViewById(R.id.guideline4);
      if (guideline4 == null) {
        missingId = "guideline4";
        break missingId;
      }
      Guideline guideline5 = rootView.findViewById(R.id.guideline5);
      if (guideline5 == null) {
        missingId = "guideline5";
        break missingId;
      }
      TextView textTitle = rootView.findViewById(R.id.text_title);
      if (textTitle == null) {
        missingId = "textTitle";
        break missingId;
      }
      Toolbar toolbar = rootView.findViewById(R.id.toolbar);
      if (toolbar == null) {
        missingId = "toolbar";
        break missingId;
      }
      return new ActivityCurrentLifestyleBinding((ConstraintLayout) rootView, ToolbarLayout,
          buttonHigh, buttonMedium, buttonMild, buttonSedentary, guideline4, guideline5, textTitle,
          toolbar);
    }
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
