// Generated by view binder compiler. Do not edit!
package cz.cuni.mff.nutritionalassistant.databinding;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.viewbinding.ViewBinding;
import cz.cuni.mff.nutritionalassistant.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivitySwapBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final AppCompatSpinner spinnerSwapCategories;

  @NonNull
  public final Toolbar toolbar;

  private ActivitySwapBinding(@NonNull ConstraintLayout rootView,
      @NonNull AppCompatSpinner spinnerSwapCategories, @NonNull Toolbar toolbar) {
    this.rootView = rootView;
    this.spinnerSwapCategories = spinnerSwapCategories;
    this.toolbar = toolbar;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivitySwapBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivitySwapBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_swap, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivitySwapBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    String missingId;
    missingId: {
      AppCompatSpinner spinnerSwapCategories = rootView.findViewById(R.id.spinner_swap_categories);
      if (spinnerSwapCategories == null) {
        missingId = "spinnerSwapCategories";
        break missingId;
      }
      Toolbar toolbar = rootView.findViewById(R.id.toolbar);
      if (toolbar == null) {
        missingId = "toolbar";
        break missingId;
      }
      return new ActivitySwapBinding((ConstraintLayout) rootView, spinnerSwapCategories, toolbar);
    }
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
