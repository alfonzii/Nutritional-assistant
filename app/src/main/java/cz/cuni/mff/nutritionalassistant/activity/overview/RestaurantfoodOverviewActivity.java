package cz.cuni.mff.nutritionalassistant.activity.overview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cz.cuni.mff.nutritionalassistant.databinding.ActivityRestaurantfoodOverviewBinding;

public class RestaurantfoodOverviewActivity extends AppCompatActivity {

    private ActivityRestaurantfoodOverviewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRestaurantfoodOverviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}
