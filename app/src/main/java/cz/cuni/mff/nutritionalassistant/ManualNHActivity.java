package cz.cuni.mff.nutritionalassistant;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.View;

import cz.cuni.mff.nutritionalassistant.activity.BaseAbstractActivity;
import cz.cuni.mff.nutritionalassistant.data.DataHolder;
import cz.cuni.mff.nutritionalassistant.databinding.ActivityManualNhBinding;

public class ManualNHActivity extends BaseAbstractActivity {
    //Reference to singleton object
    private DataHolder data = DataHolder.getInstance();

    //View binding object
    private ActivityManualNhBinding binding;

    private AlertDialog.Builder myAlertBuilder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManualNhBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        myAlertBuilder = new AlertDialog.Builder(ManualNHActivity.this);
        // Add the dialog buttons.
        myAlertBuilder.setPositiveButton(getString(R.string.ok_en),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // User clicked OK button.
                    }
                });
    }

    //on button click
    public void applyNutritionGoals(View view) {
        int calsGoal, fatsGoal, carbsGoal, protsGoal;
        try {
            calsGoal = Integer.parseInt(binding.numberCalories.getText().toString());
            fatsGoal = Integer.parseInt(binding.numberFats.getText().toString());
            carbsGoal = Integer.parseInt(binding.numberCarbs.getText().toString());
            protsGoal = Integer.parseInt(binding.numberProteins.getText().toString());
        } catch (NumberFormatException e) {
            // Set the dialog title and message.
            myAlertBuilder.setTitle(getString(R.string.error_en));
            myAlertBuilder.setMessage(getString(R.string.not_all_values_exception_en));

            // Create and show the AlertDialog.
            myAlertBuilder.show();
            return;
        }
        //if everything is OK, then we can set data to desired values
        data.setCaloriesGoal(calsGoal);
        data.setFatsGoal(fatsGoal);
        data.setCarbohydratesGoal(carbsGoal);
        data.setProteinsGoal(protsGoal);
        setResult(RESULT_OK);
        finish();
    }
}

