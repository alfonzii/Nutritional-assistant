package cz.cuni.mff.nutritionalassistant;

import android.os.Bundle;
import android.view.View;

import cz.cuni.mff.nutritionalassistant.activity.BaseAbstractActivity;
import cz.cuni.mff.nutritionalassistant.guidancebot.Brain;
import cz.cuni.mff.nutritionalassistant.guidancebot.Mathematics;


public class GoalActivity extends BaseAbstractActivity {
    // Reference to singleton object
    private DataHolder data = DataHolder.getInstance();

    private void endAcivity() {
        //calculateMacronutrients();
        Brain.getInstance().requestNHConstraintsCalculation(data.getCaloriesExcess());
        setResult(RESULT_OK);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);
    }

    public void gainClick(View view) {
        //data.setCaloriesGoal(Math.round(calculateBasal()) + 400);
        data.setGoal(Mathematics.Goal.GAIN);
        endAcivity();
    }

    public void loseClick(View view) {
        //data.setCaloriesGoal(Math.round(calculateBasal()) - 400);
        data.setGoal(Mathematics.Goal.LOSE);
        endAcivity();
    }

    public void maintainClick(View view) {
        //data.setCaloriesGoal(Math.round(calculateBasal()));
        data.setGoal(Mathematics.Goal.MAINTAIN);
        endAcivity();
    }
}
