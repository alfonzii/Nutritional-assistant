package cz.cuni.mff.nutritionalassistant.guidancebot.api;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cz.cuni.mff.nutritionalassistant.foodtypes.Food;
import cz.cuni.mff.nutritionalassistant.foodtypes.FoodAdapterType;
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

    public void getBrandedFoodDetails(String id, DetailedFoodCallback callback) {
        Call<NutritionixDetailedFullResponsePojo> call = nutritionixApi.detailsBrandedProduct(id);

        call.enqueue(new Callback<NutritionixDetailedFullResponsePojo>() {
            @Override
            public void onResponse(Call<NutritionixDetailedFullResponsePojo> call, Response<NutritionixDetailedFullResponsePojo> response) {
                if (!response.isSuccessful()) {
                    Log.d(NutritionixDMS.class.getName(), "Code: " + response.code());
                    return;
                }

                if (callback != null) {
                    callback.onSuccess(PojoConverter.fromNutritionixDetailedProductPojo(response.body().getFoods().get(0)));
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

        /*call.enqueue(new Callback<NutritionixDetailedProductPojo>() {
            @Override
            public void onResponse(Call<NutritionixDetailedProductPojo> call, Response<NutritionixDetailedProductPojo> response) {
                if (!response.isSuccessful()) {
                    Log.d(NutritionixDMS.class.getName(), "Code: " + response.code());
                    return;
                }

                if (callback != null) {
                    callback.onSuccess(PojoConverter.fromNutritionixDetailedProductPojo(response.body()));
                }
            }

            @Override
            public void onFailure(Call<NutritionixDetailedProductPojo> call, Throwable t) {
                Log.d(NutritionixDMS.class.getName(), t.getMessage());
                if (callback != null) {
                    callback.onFail(t);
                }
            }
        });*/
    }

    public void listProducts(String query, AdapterDataCallback callbacks) {
        Call<NutritionixAdapterFullResponsePojo> call = nutritionixApi.listProducts(query);

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

        call.enqueue(new Callback<NutritionixAdapterFullResponsePojo>() {
            @Override
            public void onResponse(Call<NutritionixAdapterFullResponsePojo> call, Response<NutritionixAdapterFullResponsePojo> response) {
                if (!response.isSuccessful()) {
                    Log.d(NutritionixDMS.class.getName(), "Code: " + response.code());
                    return;
                }
                final List<FoodAdapterType> correctResponse = new ArrayList<>();

                try {
                    correctResponse.addAll(PojoConverter.fromNutritionixPojoList(response.body().getCommon()));
                    correctResponse.addAll(PojoConverter.fromNutritionixPojoList(response.body().getBranded()));
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
            }});
        }


    }
