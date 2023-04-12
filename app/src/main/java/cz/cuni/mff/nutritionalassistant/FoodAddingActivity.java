package cz.cuni.mff.nutritionalassistant;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import java.util.HashMap;
import java.util.List;

import cz.cuni.mff.nutritionalassistant.activity.BaseAbstractActivity;
import cz.cuni.mff.nutritionalassistant.data.DataHolder;
import cz.cuni.mff.nutritionalassistant.databinding.ActivityFoodAddingBinding;
import cz.cuni.mff.nutritionalassistant.foodtypes.Food;
import cz.cuni.mff.nutritionalassistant.foodtypes.FoodAdapterType;
import cz.cuni.mff.nutritionalassistant.guidancebot.Brain;
import cz.cuni.mff.nutritionalassistant.guidancebot.api.AdapterDataCallback;
import cz.cuni.mff.nutritionalassistant.util.FilterDialogActivity;
import cz.cuni.mff.nutritionalassistant.util.FoodAddingAdapter;

public class FoodAddingActivity extends BaseAbstractActivity {
    // Reference to singleton object
    private DataHolder data = DataHolder.getInstance();

    // View binding object
    private ActivityFoodAddingBinding binding;

    private FoodAddingAdapter foodAddingAdapter;
    private int spinnerCategorySelection = Food.FoodType.PRODUCT.getId();

    // Key - EditText ID, Value - value of filter parameter
    private HashMap<Integer, Integer> filterTable = new HashMap<>();

    private static int FILTER_REQUEST = 0;

    private SearchView.OnQueryTextListener searchQueryListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            foodAddingAdapter.clearItems();
            Brain.getInstance().requestFoodAdapterTypeData(
                    query, spinnerCategorySelection, filterTable, new AdapterDataCallback() {
                        @Override
                        public void onSuccess(@NonNull List<FoodAdapterType> list) {
                            foodAddingAdapter.addItems(list);
                        }

                        @Override
                        public void onFail(@NonNull Throwable throwable) {

                        }
                    });

            // clear focus of searchview widget to hide keyboard
            binding.toolbar.getMenu().findItem(R.id.action_search).getActionView().clearFocus();
            return true;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFoodAddingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        binding.recyclerFoodAdding.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerFoodAdding.addItemDecoration(
                new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        foodAddingAdapter = new FoodAddingAdapter(this);
        binding.recyclerFoodAdding.setAdapter(foodAddingAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_food_adding, menu);

        //NEDA SA TO NEJAK OBIST S POMOCOU VIEW BINDING???????????????????????
        // Pravdepodobne sa to da obist cez vlastny layout spinnera a nasledne staticke naplnenie
        // toho spinnera cez XML
        MenuItem foodCategorySpinner = menu.findItem(R.id.action_category);
        final AppCompatSpinner spinner = (AppCompatSpinner) foodCategorySpinner.getActionView();

        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.food_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        ((SearchView) menu.findItem(R.id.action_search).getActionView()).setOnQueryTextListener(searchQueryListener);

        MenuItem.OnActionExpandListener searchActionExpandListener = new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                for (int i = 0; i < binding.toolbar.getMenu().size(); i++) {
                    binding.toolbar.getMenu().getItem(i).setVisible(false).setEnabled(false);
                }
                return true;  // Return true to expand action view
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                invalidateOptionsMenu();
                foodAddingAdapter.clearItems();
                return true;  // Return true to collapse action view
            }
        };

        // We could use Composite design pattern, as we just reuse previous Listener with some added
        // functionality, but it's trivial enough, so we just copy code.
        MenuItem.OnActionExpandListener categoryActionExpandListener = new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                for (int i = 0; i < binding.toolbar.getMenu().size(); i++) {
                    binding.toolbar.getMenu().getItem(i).setVisible(false).setEnabled(false);
                }
                spinner.setSelection(spinnerCategorySelection);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                spinnerCategorySelection = spinner.getSelectedItemPosition();
                invalidateOptionsMenu();
                return true;
            }
        };

        binding.toolbar.getMenu().findItem(R.id.action_search).setOnActionExpandListener(searchActionExpandListener);
        binding.toolbar.getMenu().findItem(R.id.action_category).setOnActionExpandListener(categoryActionExpandListener);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Not doing anything. Just a demonstration of an activity being used as dialog.
        // Need to be further coded.
        if (item.getItemId() == R.id.action_filter) {
            Intent intent = new Intent(this, FilterDialogActivity.class);
            intent.putExtra(FilterDialogActivity.EXTRA_SERIALIZABLE_FILTER_HASHMAP, filterTable);
            startActivityForResult(intent, FILTER_REQUEST);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.FOOD_REQUEST) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK);
                finish();
            } else {
                setResult(RESULT_CANCELED);
            }
        } else if (requestCode == FILTER_REQUEST) {
            if (resultCode == RESULT_OK) {
                filterTable = (HashMap<Integer, Integer>) data.getSerializableExtra(
                        FilterDialogActivity.EXTRA_SERIALIZABLE_FILTER_HASHMAP);
            }
        }
    }
}