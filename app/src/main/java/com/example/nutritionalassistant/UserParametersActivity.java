package com.example.nutritionalassistant;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;


public class UserParametersActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {
    //reference to a singleton object
    private DataHolder data = DataHolder.getInstance();

    private EditText weightEditText;
    private EditText heightEditText;
    private EditText ageEditText;
    private Spinner sexSpinner;

    private AlertDialog.Builder myAlertBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_parameters);

        sexSpinner = findViewById(R.id.userSex_Spinner);
        if (sexSpinner != null) {
            sexSpinner.setOnItemSelectedListener(this);
        }

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sex_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner.
        if (sexSpinner != null) {
            sexSpinner.setAdapter(adapter);
        }

        weightEditText = findViewById(R.id.userWeight_EditTextNumber);
        heightEditText = findViewById(R.id.userHeight_EditTextNumber);
        ageEditText = findViewById(R.id.userAge_EditTextNumber);

        myAlertBuilder = new AlertDialog.Builder(UserParametersActivity.this);
        // Add the dialog buttons.
        myAlertBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // User clicked OK button.
                    }
                });

        if (data.getAge() != 0 && data.getWeight() != 0 && data.getHeight() != 0) {
            sexSpinner.setSelection(data.convertSex(data.getSex()));
            ageEditText.setText(Integer.toString(data.getAge()));
            weightEditText.setText(Integer.toString(data.getWeight()));
            heightEditText.setText(Integer.toString(data.getHeight()));
        } else {
            ageEditText.setHint("0");
            weightEditText.setHint("0");
            heightEditText.setHint("0");
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void applyUserParameters(View view) {
        int sex, age, weight, height;
        try {
            sex = sexSpinner.getSelectedItemPosition();
            age = Integer.parseInt(ageEditText.getText().toString());
            weight = Integer.parseInt(weightEditText.getText().toString());
            height = Integer.parseInt(heightEditText.getText().toString());
            if (age < 0 || weight < 0 || height < 0)
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
        data.setSex(data.convertSex(sex));
        data.setAge(age);
        data.setWeight(weight);
        data.setHeight(height);
        setResult(RESULT_OK);
        finish();
    }


}
