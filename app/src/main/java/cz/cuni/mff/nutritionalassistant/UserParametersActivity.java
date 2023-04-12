package cz.cuni.mff.nutritionalassistant;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.View;

import cz.cuni.mff.nutritionalassistant.activity.BaseAbstractActivity;
import cz.cuni.mff.nutritionalassistant.data.DataHolder;
import cz.cuni.mff.nutritionalassistant.databinding.ActivityUserParametersBinding;


public class UserParametersActivity extends BaseAbstractActivity {
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

        myAlertBuilder = new AlertDialog.Builder(UserParametersActivity.this);
        // Add the dialog buttons.
        myAlertBuilder.setPositiveButton(getString(R.string.ok_en),
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

    public void applyUserParameters(View view) {
        int sex, age, weight, height;
        try {
            sex = binding.spinnerSex.getSelectedItemPosition();
            age = Integer.parseInt(binding.numberAge.getText().toString());
            weight = Integer.parseInt(binding.numberWeight.getText().toString());
            height = Integer.parseInt(binding.numberHeight.getText().toString());
        } catch (NumberFormatException e) {
            // Set the dialog title and message.
            myAlertBuilder.setTitle(getString(R.string.error_en));
            myAlertBuilder.setMessage(getString(R.string.not_all_values_exception_en));

            // Create and show the AlertDialog.
            myAlertBuilder.show();
            return;
        }
        //if everything is OK, then we can set data to desired values
        data.setSex(data.convertSex(sex));
        data.setAge(age);
        data.setWeight(weight);
        data.setHeight(height);

        Intent intent = new Intent(this, NHSetActivity.class);
        startActivity(intent);

        setResult(RESULT_OK);
        finish();
    }


}
