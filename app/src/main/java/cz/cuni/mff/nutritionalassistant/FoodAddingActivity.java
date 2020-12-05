package cz.cuni.mff.nutritionalassistant;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;

import cz.cuni.mff.nutritionalassistant.databinding.ActivityFoodAddingBinding;
import cz.cuni.mff.nutritionalassistant.foodtypes.Food;
import cz.cuni.mff.nutritionalassistant.guidancebot.Brain;
import cz.cuni.mff.nutritionalassistant.localdatabase.NutritionDbHelper;
import cz.cuni.mff.nutritionalassistant.utils.FoodAddingAdapter;

public class FoodAddingActivity extends AppCompatActivity {
    // Reference to singleton object
    private DataHolder data = DataHolder.getInstance();

    // View binding object
    private ActivityFoodAddingBinding binding;
    private FoodAddingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFoodAddingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.recyclerFoodAdding.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerFoodAdding.addItemDecoration(
                new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        adapter = new FoodAddingAdapter(this);
        binding.recyclerFoodAdding.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        NutritionDbHelper dbHelper = NutritionDbHelper.getInstance(this);
        dbHelper.close();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_food_adding, menu);

        //NEDA SA TO NEJAK OBIST S POMOCOU VIEW BINDING???????????????????????
        // Pravdepodobne sa to da obist cez vlastny layout spinnera a nasledne staticke naplnenie
        // toho spinnera cez XML
        MenuItem foodCategorySpinner = menu.findItem(R.id.action_category);
        AppCompatSpinner spinner = (AppCompatSpinner) foodCategorySpinner.getActionView();

        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.food_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        ((SearchView)menu.findItem(R.id.action_search).getActionView()).setOnQueryTextListener(searchQueryListener);

        MenuItem.OnActionExpandListener expandListener = new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                invalidateOptionsMenu();
                /*for (int i = 0; i < binding.toolbar.getMenu().size(); i++) {
                    binding.toolbar.getMenu().getItem(i).setVisible(true).setEnabled(true);
                }*/
                //binding.toolbar.getMenu().findItem(R.id.action_category).setVisible(true);
                //binding.toolbar.getMenu().findItem(R.id.action_category).setEnabled(true);
                //binding.toolbar.getMenu().findItem(R.id.action_filter).setVisible(true);
                //binding.toolbar.getMenu().findItem(R.id.action_search).setVisible(true);
                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                for (int i = 0; i < binding.toolbar.getMenu().size(); i++) {
                    binding.toolbar.getMenu().getItem(i).setVisible(false).setEnabled(false);
                }
                return true;  // Return true to expand action view
            }
        };

        binding.toolbar.getMenu().findItem(R.id.action_search).setOnActionExpandListener(expandListener);
        binding.toolbar.getMenu().findItem(R.id.action_category).setOnActionExpandListener(expandListener);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Not doing anything. Just a demonstration of an activity being used as dialog.
        // Need to be further coded.
        if (item.getItemId() == R.id.action_filter) {
            Intent intent = new Intent(this, FilterDialogActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Constants.FOOD_REQUEST) {
            if(resultCode == RESULT_OK) {
                setResult(RESULT_OK);
                finish();
            } else {
                setResult(RESULT_CANCELED);
            }
        }
    }

    private SearchView.OnQueryTextListener searchQueryListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            adapter.clearItems();
            adapter.addItems(Brain.getInstance().requestFoodLightweightData(
                    query, Food.FoodType.PRODUCT, FoodAddingActivity.this));
            // clear focus of searchview widget to hide keyboard
            binding.toolbar.getMenu().findItem(R.id.action_search).getActionView().clearFocus();
            return true;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }
    };
}