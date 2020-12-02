package cz.cuni.mff.nutritionalassistant.utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cz.cuni.mff.nutritionalassistant.R;
import cz.cuni.mff.nutritionalassistant.foodtypes.Food;
import cz.cuni.mff.nutritionalassistant.foodtypes.FoodLightweight;
import cz.cuni.mff.nutritionalassistant.foodtypes.ProductLightweight;
import lombok.AccessLevel;

class FoodViewHolder extends RecyclerView.ViewHolder {
    private ImageView imgFoodThumbnail;
    private TextView txtProductName, txtProductServing, txtProductCalories;
    private LinearLayout layout;
    private FoodLightweight foodLightweight;

    FoodViewHolder(View itemView) {
        super(itemView);
        imgFoodThumbnail = itemView.findViewById(R.id.thumbnail_food);
        txtProductName = itemView.findViewById(R.id.text_product_name);
        txtProductServing = itemView.findViewById(R.id.text_product_serving);
        txtProductCalories = itemView.findViewById(R.id.text_product_calories);
        layout = itemView.findViewById(R.id.LinearLayout_product);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // intent to show overview activity
            }
        });
    }

    void setDetails(FoodLightweight food) {
        setFoodLightweight(food);
        setImgFoodThumbnail();
        setTxtProductName();
        if (! (food.getFoodType() == Food.FoodType.PRODUCT && !(((ProductLightweight) food).getBrandName()).equals(null))){
            setTxtProductServing();
            setTxtProductCalories();
        }
    }

    private void setFoodLightweight(FoodLightweight foodLightweight) {
        this.foodLightweight = foodLightweight;
    }

    private void setImgFoodThumbnail() {
        // TODO
    }

    private void setTxtProductName() {
        txtProductName.setText(foodLightweight.getFoodName());
    }

    private void setTxtProductServing() {
        txtProductServing.setText(foodLightweight.getServingUnit());
    }

    private void setTxtProductCalories() {
        txtProductCalories.setText(Math.round(foodLightweight.getCalories()) + " cal");
    }
}
