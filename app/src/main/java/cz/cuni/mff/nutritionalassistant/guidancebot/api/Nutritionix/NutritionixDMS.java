package cz.cuni.mff.nutritionalassistant.guidancebot.api.Nutritionix;

import android.accounts.NetworkErrorException;
import android.util.Log;
import android.util.Pair;

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

    /*CONSTANTS FOR RESTAURANTS
     * In case of tweaking, change the final variables to desired values.
     * Default coordinate values set your location to park in front of White House in Washington D.C.
     * To use restaurant features, only US locations coordinates can be used
     */
    private final Pair<String, String> coordinates = new Pair<>("38.8950", "-77.0366");
    private final int radiusMeters = 500;

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
                    if (response.body() != null) {
                        callback.onSuccess(PojoConverter.Nutritionix.fromNutritionixDetailedProductPojo(response.body().getFoods().get(0)));
                    } else {
                        callback.onFail(new NetworkErrorException("Error while getting product details."));
                    }
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

    public void listProducts(
            String query,
            HashMap<Integer, Integer> nutritionFilterTable,
            AdapterDataCallback callback
    ) throws JSONException {
        listProducts(query, nutritionFilterTable, new HashMap<>(), callback);
    }

    private void listProducts(
            String query,
            HashMap<Integer, Integer> nutritionFilterTable,
            HashMap<String, Object> additionalArgs, //argument used for listing RestaurantFoods
            AdapterDataCallback callback
    ) throws JSONException {

        Call<NutritionixAdapterFullResponsePojo> call;

        if (nutritionFilterTable.isEmpty()) {
            if (additionalArgs.isEmpty()) {
                call = nutritionixApi.listProducts(query);
            } else {
                HashMap<String, Object> args = new HashMap<>(additionalArgs);
                args.put("query", query);
                call = nutritionixApi.listProducts(args);
            }
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

            if (additionalArgs.isEmpty()) {
                call = nutritionixApi.listProducts(parameters);
            } else {
                parameters.putAll(additionalArgs);
                /*for (Map.Entry<String, Object> entry : additionalArgs.entrySet()) {
                    parameters.put(entry.getKey(), entry.getValue());
                }*/
                call = nutritionixApi.listProducts(parameters);
            }
        }

        call.enqueue(new Callback<NutritionixAdapterFullResponsePojo>() {
            @Override
            public void onResponse(Call<NutritionixAdapterFullResponsePojo> call, Response<NutritionixAdapterFullResponsePojo> response) {
                if (!response.isSuccessful()) {
                    Log.d(NutritionixDMS.class.getName(), "Code: " + response.code());
                    return;
                }
                final List<FoodAdapterType> correctResponse = new ArrayList<>();


                if (response.body() != null) {
                    if (response.body().getCommon() != null){
                        correctResponse.addAll(PojoConverter.Nutritionix.fromNutritionixPojoList(response.body().getCommon()));
                    }
                    if (response.body().getBranded() != null){
                        correctResponse.addAll(PojoConverter.Nutritionix.fromNutritionixPojoList(response.body().getBranded()));
                    }
                }

                if (callback != null) {
                    callback.onSuccess(correctResponse);
                }
            }

            @Override
            public void onFailure(Call<NutritionixAdapterFullResponsePojo> call, Throwable t) {
                Log.d(NutritionixDMS.class.getName(), t.getMessage());
                if (callback != null) {
                    callback.onFail(t);
                }
            }
        });
    }

    public void listRestaurantFoods(
            String query,
            HashMap<Integer, Integer> nutritionFilterTable,
            AdapterDataCallback callback
    ) {
        Call<NutritionixRestaurantsFullResponsePojo> call;
        call = nutritionixApi.listRestaurants(coordinates.first + "," + coordinates.second, radiusMeters);

        call.enqueue(new Callback<NutritionixRestaurantsFullResponsePojo>() {

            private String brandedIdsListFormat(List<String> idsList) {
                String str = idsList.toString();
                str = str.replaceAll(",", "\",");
                str = str.replaceAll(" ", " \"");
                str = str.replace("[", "[\"");
                str = str.replace("]", "\"]");
                return str;
            }

            @Override
            public void onResponse(Call<NutritionixRestaurantsFullResponsePojo> call, Response<NutritionixRestaurantsFullResponsePojo> response) {
                if (!response.isSuccessful()) {
                    Log.d(NutritionixDMS.class.getName(), "Code: " + response.code());
                    return;
                }

                if (response.body() != null) {
                    // TODO otestovat co sa deje v pripade ze nemam ziadne restauracie v okoli (ci getRestaurants vracia prazdny array alebo null)
                    final List<NutritionixRestaurantPojo> restaurantPojoList = new ArrayList<>(response.body().getRestaurants());
                    final List<String> brandIdList = new ArrayList<>();

                    for (NutritionixRestaurantPojo pojo : restaurantPojoList) {
                        brandIdList.add(pojo.getBrandId());
                    }
                    HashMap<String, Object> additionalArgs = new HashMap<>();
                    additionalArgs.put("branded", "true");
                    additionalArgs.put("self", "false");
                    additionalArgs.put("common", "false");
                    additionalArgs.put("brand_ids", brandedIdsListFormat(brandIdList));
                    try {
                        listProducts(query, nutritionFilterTable, additionalArgs, callback);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<NutritionixRestaurantsFullResponsePojo> call, Throwable t) {
                Log.d(NutritionixDMS.class.getName(), t.getMessage());
                if (callback != null) {
                    callback.onFail(t);
                }
            }
        });
    }


}