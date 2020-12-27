package cz.cuni.mff.nutritionalassistant.guidancebot.api.Nutritionix;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.cuni.mff.nutritionalassistant.foodtypes.FoodAdapterType;
import cz.cuni.mff.nutritionalassistant.foodtypes.ProductAdapterType;
import cz.cuni.mff.nutritionalassistant.guidancebot.api.AdapterDataCallback;
import cz.cuni.mff.nutritionalassistant.guidancebot.api.DetailedFoodCallback;
import cz.cuni.mff.nutritionalassistant.guidancebot.api.PojoConverter;
import cz.cuni.mff.nutritionalassistant.util.FilterDialogActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// Nutritionix Data Manager System
public class NutritionixDMS {

    private Retrofit retrofit;
    private NutritionixApi nutritionixApi;

    public NutritionixDMS() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://trackapi.nutritionix.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        nutritionixApi = retrofit.create(NutritionixApi.class);
    }

    public void getProductDetails(FoodAdapterType productAdapterType, DetailedFoodCallback callback) {

        ProductAdapterType product = (ProductAdapterType) productAdapterType;
        Call<NutritionixDetailedFullResponsePojo> call;

        // is common
        if (product.getBrandName() == null) {
            call = nutritionixApi.detailsCommonProduct(product.getFoodName());
        } else { //is branded
            call = nutritionixApi.detailsBrandedProduct(product.getId());
        }

        call.enqueue(new Callback<NutritionixDetailedFullResponsePojo>() {
            @Override
            public void onResponse(Call<NutritionixDetailedFullResponsePojo> call, Response<NutritionixDetailedFullResponsePojo> response) {
                if (!response.isSuccessful()) {
                    Log.d(NutritionixDMS.class.getName(), "Code: " + response.code());
                    return;
                }

                if (callback != null) {
                    callback.onSuccess(PojoConverter.Nutritionix.fromNutritionixDetailedProductPojo(response.body().getFoods().get(0)));
                }
            }

            @Override
            public void onFailure(Call<NutritionixDetailedFullResponsePojo> call, Throwable t) {
                Log.d(NutritionixDMS.class.getName(), t.getMessage());
                if (callback != null) {
                    callback.onFail(t);
                }
            }
        });
    }

    public void listProducts(String query, HashMap<Integer, Integer> nutritionFilterTable, AdapterDataCallback callbacks) throws JSONException {
        Call<NutritionixAdapterFullResponsePojo> call;

        if (nutritionFilterTable.isEmpty()) {
            call = nutritionixApi.listProducts(query);
        } else {
            HashMap<String, Object> parameters = new HashMap<>();
            JSONObject caloriesBounds = new JSONObject();
            JSONObject fatsBounds = new JSONObject();
            JSONObject carbsBounds = new JSONObject();
            JSONObject proteinBounds = new JSONObject();

            for (Map.Entry<Integer, Integer> entry : nutritionFilterTable.entrySet()) {
                switch (entry.getKey()) {
                    case FilterDialogActivity.MIN_CALORIES:
                        caloriesBounds.put("gte", entry.getValue());
                        break;
                    case FilterDialogActivity.MAX_CALORIES:
                        caloriesBounds.put("lte", entry.getValue());
                        break;
                    case FilterDialogActivity.MIN_FATS:
                        fatsBounds.put("gte", entry.getValue());
                        break;
                    case FilterDialogActivity.MAX_FATS:
                        fatsBounds.put("lte", entry.getValue());
                        break;
                    case FilterDialogActivity.MIN_CARBOHYDRATES:
                        carbsBounds.put("gte", entry.getValue());
                        break;
                    case FilterDialogActivity.MAX_CARBOHYDRATES:
                        carbsBounds.put("lte", entry.getValue());
                        break;
                    case FilterDialogActivity.MIN_PROTEINS:
                        proteinBounds.put("gte", entry.getValue());
                        break;
                    case FilterDialogActivity.MAX_PROTEINS:
                        proteinBounds.put("lte", entry.getValue());
                        break;
                }
            }

            JSONObject filterArgs = new JSONObject();

            if (caloriesBounds.length() != 0) {
                filterArgs.put("208", caloriesBounds);
            }
            if (fatsBounds.length() != 0) {
                filterArgs.put("204", fatsBounds);
            }
            if (carbsBounds.length() != 0) {
                filterArgs.put("205", carbsBounds);
            }
            if (proteinBounds.length() != 0) {
                filterArgs.put("203", proteinBounds);
            }

            parameters.put("query", query);
            parameters.put("full_nutrients", filterArgs);

            call = nutritionixApi.listProducts(parameters);
        }

        call.enqueue(new Callback<NutritionixAdapterFullResponsePojo>() {
            @Override
            public void onResponse(Call<NutritionixAdapterFullResponsePojo> call, Response<NutritionixAdapterFullResponsePojo> response) {
                if (!response.isSuccessful()) {
                    Log.d(NutritionixDMS.class.getName(), "Code: " + response.code());
                    return;
                }
                final List<FoodAdapterType> correctResponse = new ArrayList<>();

                try {
                    correctResponse.addAll(PojoConverter.Nutritionix.fromNutritionixPojoList(response.body().getCommon()));
                    correctResponse.addAll(PojoConverter.Nutritionix.fromNutritionixPojoList(response.body().getBranded()));
                } catch (NullPointerException e) {

                }
                if (callbacks != null) {
                    callbacks.onSuccess(correctResponse);
                }
            }

            @Override
            public void onFailure(Call<NutritionixAdapterFullResponsePojo> call, Throwable t) {
                Log.d(NutritionixDMS.class.getName(), t.getMessage());
                if (callbacks != null) {
                    callbacks.onFail(t);
                }
            }
        });
    }
}

    /*try {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Response<NutritionixAdapterFullResponsePojo> response = call.execute();
                        correctResponse.addAll(PojoConverter.fromNutritionixPojoList(response.body().getCommon()));
                        correctResponse.addAll(PojoConverter.fromNutritionixPojoList(response.body().getBranded()));
                    } catch (IOException e){

                    }

                }
            });
            t.start();
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/