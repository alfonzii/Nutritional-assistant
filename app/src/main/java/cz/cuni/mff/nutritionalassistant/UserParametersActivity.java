package cz.cuni.mff.nutritionalassistant;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import cz.cuni.mff.nutritionalassistant.databinding.ActivityUserParametersBinding;


public class UserParametersActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {
    //Reference to a singleton object
    private DataHolder data = DataHolder.getInstance();

    //View binding object
    private ActivityUserParametersBinding binding;

    private AlertDialog.Builder myAlertBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserParametersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // No need to check if view is null. View binding solved this.
        binding.spinnerSex.setOnItemSelectedListener(this);

        /*ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sex_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner.
        // No need for null check again.
        binding.spinnerSex.setAdapter(adapter);*/

        myAlertBuilder = new AlertDialog.Builder(UserParametersActivity.this);
        // Add the dialog buttons.
        myAlertBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // User clicked OK button.
                    }
                });

        if (data.getAge() != 0 && data.getWeight() != 0 && data.getHeight() != 0) {
            binding.spinnerSex.setSelection(data.convertSex(data.getSex()));
            binding.numberAge.setText(Integer.toString(data.getAge()));
            binding.numberWeight.setText(Integer.toString(data.getWeight()));
            binding.numberHeight.setText(Integer.toString(data.getHeight()));
        } else {
            binding.numberAge.setHint("0");
            binding.numberWeight.setHint("0");
            binding.numberHeight.setHint("0");
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
            sex = binding.spinnerSex.getSelectedItemPosition();
            age = Integer.parseInt(binding.numberAge.getText().toString());
            weight = Integer.parseInt(binding.numberWeight.getText().toString());
            height = Integer.parseInt(binding.numberHeight.getText().toString());
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
