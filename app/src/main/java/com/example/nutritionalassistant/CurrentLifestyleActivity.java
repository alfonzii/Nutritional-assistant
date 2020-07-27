package com.example.nutritionalassistant;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import static com.example.nutritionalassistant.Constants.AUTOMATIC_REQUEST;
import static com.example.nutritionalassistant.Constants.HIGH_ACTIVITY;
import static com.example.nutritionalassistant.Constants.MEDIUM_ACTIVITY;
import static com.example.nutritionalassistant.Constants.MILD_ACTIVITY;
import static com.example.nutritionalassistant.Constants.SEDENTARY;

public class CurrentLifestyleActivity extends AppCompatActivity {

    private AlertDialog.Builder myAlertBuilder;
    private DataHolder data = DataHolder.getInstance();

    private void runGoalActivity(){
        Intent intent = new Intent(this, GoalActivity.class);
        startActivityForResult(intent, AUTOMATIC_REQUEST);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_lifestyle);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        myAlertBuilder = new AlertDialog.Builder(CurrentLifestyleActivity.this);
        // Add the dialog buttons.
        myAlertBuilder.setPositiveButton("Dismiss",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lifestyle, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_info) {
            myAlertBuilder.setTitle("Info");
            myAlertBuilder.setMessage(
                    Html.fromHtml("<p>" + "<b>" + "Sedentary" + "</b>" + " - easy or no exercise at all" + "</p>" +
                            "<p>" + "<b>" + "Mild activity" + "</b>" + " - easy exercise or sport, 1-3 times a week" + "</p>" +
                            "<p>" + "<b>" + "Medium activity" + "</b>" + " - medium exercise or sport, 3-5 times a week" + "</p>" +
                            "<p>" + "<b>" + "High activity" + "</b>" + " - intense training, sport or hard manual work, 6+ times a week" + "</p>"));
            // Create and show the AlertDialog.
            myAlertBuilder.show();
            return true;
        }

        // If we got here, the user's action was not recognized.
        // Invoke the superclass to handle it.
        else
            return super.onOptionsItemSelected(item);
    }

    public void sedentaryClick(View view) {
        data.setLifestyle(SEDENTARY);
        runGoalActivity();
    }

    public void mildClick(View view) {
        data.setLifestyle(MILD_ACTIVITY);
        runGoalActivity();
    }

    public void mediumClick(View view) {
        data.setLifestyle(MEDIUM_ACTIVITY);
        runGoalActivity();
    }

    public void highClick(View view) {
        data.setLifestyle(HIGH_ACTIVITY);
        runGoalActivity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == AUTOMATIC_REQUEST){
            if(resultCode == RESULT_OK){
                setResult(RESULT_OK);
                finish();
            }
            else{
                setResult(RESULT_CANCELED);
                finish();
            }
        }

    }
}


