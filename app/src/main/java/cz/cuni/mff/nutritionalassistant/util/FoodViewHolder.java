package cz.cuni.mff.nutritionalassistant.util;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cz.cuni.mff.nutritionalassistant.Constants;
import cz.cuni.mff.nutritionalassistant.FoodAddingActivity;
import cz.cuni.mff.nutritionalassistant.MainActivity;
import cz.cuni.mff.nutritionalassistant.activity.overview.ProductOverviewActivity;
import cz.cuni.mff.nutritionalassistant.R;
import cz.cuni.mff.nutritionalassistant.activity.overview.RecipeOverviewActivity;
import cz.cuni.mff.nutritionalassistant.activity.overview.RestaurantfoodOverviewActivity;
import cz.cuni.mff.nutritionalassistant.foodtypes.Food;
import cz.cuni.mff.nutritionalassistant.foodtypes.FoodAdapterType;
import cz.cuni.mff.nutritionalassistant.foodtypes.ProductAdapterType;
import cz.cuni.mff.nutritionalassistant.guidancebot.Brain;
import cz.cuni.mff.nutritionalassistant.guidancebot.api.DetailedFoodCallback;

class FoodViewHolder extends RecyclerView.ViewHolder {
    private ImageView imgFoodThumbnail;
    private TextView txtProductName, txtProductServing, txtProductCalories;
    private LinearLayout layout;
    private FoodAdapterType foodAdapterType;
    private Context context;

    private static final String ACTION_ADD_FOOD =
            "cz.cuni.mff.nutritionalassistant.action.ADD_FOOD";



    FoodViewHolder(View itemView, final Context context) {
        super(itemView);
        imgFoodThumbnail = itemView.findViewById(R.id.thumbnail_food);
        txtProductName = itemView.findViewById(R.id.text_product_name);
        txtProductServing = itemView.findViewById(R.id.text_product_serving);
        txtProductCalories = itemView.findViewById(R.id.text_product_calories);
        layout = itemView.findViewById(R.id.LinearLayout_product);

        this.context = context;

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentFoodOverview;
                switch (foodAdapterType.getFoodType()) {
                    case PRODUCT:
                        intentFoodOverview = new Intent(context, ProductOverviewActivity.class);
                        break;
                    case RECIPE:
                        intentFoodOverview = new Intent(context, RecipeOverviewActivity.class);
                        break;
                    case RESTAURANTFOOD:
                        intentFoodOverview = new Intent(context, RestaurantfoodOverviewActivity.class);
                        break;
                    default:
                        throw new IllegalStateException(context.getString(R.string.unexpected_value_en) + foodAdapterType.getFoodType());
                }
                intentFoodOverview.setAction(ACTION_ADD_FOOD);

                Brain.getInstance().requestFoodDetailedInfo(foodAdapterType, new DetailedFoodCallback() {
                    @Override
                    public void onSuccess(@NonNull Food response) {
                        intentFoodOverview.putExtra(
                                MainActivity.EXTRA_SERIALIZABLE_FOOD,
                                response
                        );
                        ((FoodAddingActivity)context).startActivityForResult(intentFoodOverview, Constants.FOOD_REQUEST);
                    }

                    @Override
                    public void onFail(@NonNull Throwable throwable) {

                    }
                });

                /*intentFoodOverview.putExtra(
                        MainActivity.EXTRA_SERIALIZABLE_FOOD,
                        Brain.getInstance().requestFoodDetailedInfo(foodAdapterType,context)
                );
                ((FoodAddingActivity)context).startActivityForResult(intentFoodOverview, Constants.FOOD_REQUEST);*/
            }
        });
    }

    void setDetails(FoodAdapterType food) {
        setFoodAdapterType(food);
        setImgFoodThumbnail();
        setTxtProductName();
        if (!(food.getFoodType() == Food.FoodType.PRODUCT && (((ProductAdapterType) food).getBrandName()) == null)) {
            setTxtProductServing();
            setTxtProductCalories();
        } else {
            clearTxtProductServing();
            clearTxtProductCalories();
        }
    }

    private void setFoodAdapterType(FoodAdapterType foodAdapterType) {
        this.foodAdapterType = foodAdapterType;
    }

    private void setImgFoodThumbnail() {
        new DownloadImage(context, imgFoodThumbnail).execute(foodAdapterType.getThumbnailURL());
    }

    private void setTxtProductName() {
        txtProductName.setText(foodAdapterType.getFoodName());
    }

    private void setTxtProductServing() {
        txtProductServing.setText(foodAdapterType.getServingUnit());
    }

    private void setTxtProductCalories() {
        txtProductCalories.setText(Math.round(foodAdapterType.getCalories()) + " cal");
    }

    private void clearTxtProductServing() {
        txtProductServing.setText("");
    }

    private void clearTxtProductCalories() {
        txtProductCalories.setText("");
    }
}
