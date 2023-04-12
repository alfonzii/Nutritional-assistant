package cz.cuni.mff.nutritionalassistant;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.View;

import cz.cuni.mff.nutritionalassistant.activity.BaseAbstractActivity;
import cz.cuni.mff.nutritionalassistant.data.DataHolder;

public class NHSetActivity extends BaseAbstractActivity {
    //Reference to singleton object
    private DataHolder data = DataHolder.getInstance();

    private AlertDialog.Builder myAlertBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nh_set);

        myAlertBuilder = new AlertDialog.Builder(NHSetActivity.this);
        // Add the dialog buttons.
        myAlertBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        setResult(Constants.RESULT_AUTOMATIC_FAILURE);
                        finish();
                    }
                });
    }

    public void manualClick(View view) {
        Intent manualIntent = new Intent(this, ManualNHActivity.class);
        startActivityForResult(manualIntent, Constants.MANUAL_REQUEST);
    }

    public void automaticClick(View view) {
        if (data.getAge() + data.getWeight() + data.getHeight() == 0) {
            // Set the dialog title and message.
            myAlertBuilder.setTitle("Error");
            myAlertBuilder.setMessage(
                    "You don't have defined user parameters. Please, fill them in and then return back for automatic calculation.");
            // Create and show the AlertDialog.
            myAlertBuilder.show();
        } else {
            Intent automaticIntent = new Intent(this, CurrentLifestyleActivity.class);
            startActivityForResult(automaticIntent, Constants.AUTOMATIC_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.MANUAL_REQUEST) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK);
                finish();
            } else {
                setResult(RESULT_CANCELED);
            }
        } else if (requestCode == Constants.AUTOMATIC_REQUEST) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK);
                finish();
            } else {
                setResult(RESULT_CANCELED);
                finish();
            }
        }
    }
}