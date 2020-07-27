package com.example.nutritionalassistant;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class ManualNHActivity extends AppCompatActivity {
    //reference to singleton object
    private DataHolder data = DataHolder.getInstance();

    private EditText fatsSetter;
    private EditText carbsSetter;
    private EditText protsSetter;
    private EditText calsSetter;

    private AlertDialog.Builder myAlertBuilder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_nh);

        calsSetter = findViewById(R.id.calories_nutSetNumber);
        fatsSetter = findViewById(R.id.fats_nutSetNumber);
        carbsSetter = findViewById(R.id.carbs_nutSetNumber);
        protsSetter = findViewById(R.id.proteins_nutSetNumber);

        myAlertBuilder = new AlertDialog.Builder(ManualNHActivity.this);
        // Add the dialog buttons.
        myAlertBuilder.setPositiveButton("OK",
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
            calsGoal = Integer.parseInt(calsSetter.getText().toString());
            fatsGoal = Integer.parseInt(fatsSetter.getText().toString());
            carbsGoal = Integer.parseInt(carbsSetter.getText().toString());
            protsGoal = Integer.parseInt(protsSetter.getText().toString());
            if (calsGoal < 0 || fatsGoal < 0 || carbsGoal < 0 || protsGoal < 0)
                throw new ArithmeticException();
        } catch (ArithmeticException e) {
            // Set the dialog title and message.
            myAlertBuilder.setTitle("Error");
            myAlertBuilder.setMessage("No negative numbers allowed.");

            // Create and show the AlertDialog.
            myAlertBuilder.show();
            return;
        } catch (NumberFormatException e) {

            // Set the dialog title and message.
            myAlertBuilder.setTitle("Error");
            myAlertBuilder.setMessage("You didn't write all values.");

            // Create and show the AlertDialog.
            myAlertBuilder.show();
            return;
        }
        //if everything is OK, then we can set data to desired values
        data.setCalsGoal(calsGoal);
        data.setFatsGoal(fatsGoal);
        data.setCarbsGoal(carbsGoal);
        data.setProtsGoal(protsGoal);
        setResult(RESULT_OK);
        finish();
    }
}

