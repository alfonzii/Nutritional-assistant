package cz.cuni.mff.nutritionalassistant.utils;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cz.cuni.mff.nutritionalassistant.R;
import cz.cuni.mff.nutritionalassistant.databinding.ActivityFilterDialogBinding;

public class FilterDialogActivity extends AppCompatActivity {

    private ActivityFilterDialogBinding binding;

    private HashMap<Integer, Integer> filterTable;

    public static final String EXTRA_SERIALIZABLE_FILTER_HASHMAP =
            "cz.cuni.mff.nutritionalassistant.EXTRA_SERIALIZABLE_FILTER_HASHMAP";

    public static final int MIN_CALORIES = R.id.edit_min_calories;
    public static final int MAX_CALORIES = R.id.edit_max_calories;
    public static final int MIN_FATS = R.id.edit_min_fats;
    public static final int MAX_FATS = R.id.edit_max_fats;
    public static final int MIN_CARBOHYDRATES = R.id.edit_min_carbs;
    public static final int MAX_CARBOHYDRATES = R.id.edit_max_carbs;
    public static final int MIN_PROTEINS = R.id.edit_min_proteins;
    public static final int MAX_PROTEINS = R.id.edit_max_proteins;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFilterDialogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        filterTable = (HashMap<Integer, Integer>) getIntent().getSerializableExtra(EXTRA_SERIALIZABLE_FILTER_HASHMAP);
        for (Map.Entry<Integer, Integer> entry : filterTable.entrySet()) {
            initialization(entry.getKey(), entry.getValue().toString());
        }
    }

    private void initialization(Integer editTextID, String value) {
        EditText edit = findViewById(editTextID);
        edit.setText(value);
    }

    public void applyFilterParameters(View view) {
        for (int i = 0; i < binding.LinearLayoutParent.getChildCount(); i++) {
            ConstraintLayout childLayout = (ConstraintLayout) binding.LinearLayoutParent.getChildAt(i);
            List<Pair<Integer, Integer>> nutritionValues = new ArrayList<>();
            for (int j = 0; j < childLayout.getChildCount(); j++) {
                View child = childLayout.getChildAt(j);
                if (child instanceof EditText) {
                    try {
                        nutritionValues.add(new Pair<>(child.getId(), Integer.parseInt(((EditText) child).getText().toString())));
                    } catch (NumberFormatException e) {
                        nutritionValues.add(new Pair<>(child.getId(), -1));
                    }
                }
            }

            // When we process button constraint layout, we want to exit loop
            if (nutritionValues.size() == 0) {
                break;
            }

            Pair<Integer, Integer> p1 = nutritionValues.get(0);
            Pair<Integer, Integer> p2 = nutritionValues.get(1);
            if (p1.second <= p2.second || p2.second.equals(-1)) {
                toFilterTable(p1);
                toFilterTable(p2);
            } else { // Incorrect format (min is bigger than max)
                AlertDialog.Builder myAlertBuilder =
                        new AlertDialog.Builder(FilterDialogActivity.this);

                myAlertBuilder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // User clicked OK button.
                            }
                        });
                myAlertBuilder.setTitle("Error");
                myAlertBuilder.setMessage("Wrong format. Minimum number shouldn't be bigger than max.");
                myAlertBuilder.show();
                return;
            }
        }
        Intent data = new Intent();
        data.putExtra(EXTRA_SERIALIZABLE_FILTER_HASHMAP, filterTable);
        setResult(RESULT_OK, data);
        finish();
    }

    private void toFilterTable(Pair<Integer, Integer> pair) {
        if (pair.second > 0) {
            filterTable.put(pair.first, pair.second);
        } else {
            filterTable.remove(pair.first);
        }
    }
}