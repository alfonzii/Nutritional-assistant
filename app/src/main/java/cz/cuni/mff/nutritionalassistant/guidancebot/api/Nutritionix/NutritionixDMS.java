package cz.cuni.mff.nutritionalassistant.guidancebot.api.Nutritionix;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.cuni.mff.nutritionalassistant.foodtypes.FoodAdapterType;
import cz.cuni.mff.nutritionalassistant.foodtypes.ProductAdapterType;
import cz.cuni.mff.nutritionalassistant.guidancebot.api.AdapterDataCallback;
import cz.cuni.mff.nutritionalassistant.guidancebot.api.DetailedFoodCallback;
import cz.cuni.mff.nutritionalassistant.guidancebot.api.PojoConverter;
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
        if(product.getBrandName() == null) {
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

    public void listProducts(String query, HashMap<Integer, Integer> nutritionFilterTable, AdapterDataCallback callbacks) {
        Call<NutritionixAdapterFullResponsePojo> call;

        if(nutritionFilterTable.isEmpty()) {
            call = nutritionixApi.listProducts(query);
        } /*else {
            HashMap<String, Object> parameters = new HashMap<>();



        }*/

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
                } catch (NullPointerException e) {
                    try {
                        correctResponse.addAll(PojoConverter.Nutritionix.fromNutritionixPojoList(response.body().getBranded()));
                    } catch (NullPointerException ex) {

                    }
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
            }});
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