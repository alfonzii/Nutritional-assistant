package cz.cuni.mff.nutritionalassistant.activity.overview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cz.cuni.mff.nutritionalassistant.R;

// TODO use GeneralOverviewUtil to work + implement specific init of ingredients + instructions

public class RecipeOverviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_overview);
    }
}
