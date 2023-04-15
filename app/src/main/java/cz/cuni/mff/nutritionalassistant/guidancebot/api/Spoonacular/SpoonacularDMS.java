package cz.cuni.mff.nutritionalassistant.guidancebot.api.Spoonacular;

import android.accounts.NetworkErrorException;
import android.util.Log;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.cuni.mff.nutritionalassistant.foodtypes.Food;
import cz.cuni.mff.nutritionalassistant.foodtypes.FoodAdapterType;
import cz.cuni.mff.nutritionalassistant.foodtypes.Recipe;
import cz.cuni.mff.nutritionalassistant.foodtypes.RecipeAdapterType;
import cz.cuni.mff.nutritionalassistant.guidancebot.api.AdapterDataCallback;
import cz.cuni.mff.nutritionalassistant.guidancebot.api.DetailedFoodCallback;
import cz.cuni.mff.nutritionalassistant.guidancebot.api.DetailedFoodGenerateCallback;
import cz.cuni.mff.nutritionalassistant.guidancebot.api.PojoConverter;
import cz.cuni.mff.nutritionalassistant.util.FilterDialogActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SpoonacularDMS {

    private Retrofit retrofit;
    private SpoonacularApi spoonacularApi;
    private static final String apiKey = "53eb0eca288d46358db6dafd0961a0b9"; //"54483141f36447f38d9451d5ea8236cd";
    private static final String baseurl = "https://api.spoonacular.com/";

    public SpoonacularDMS() {
        retrofit = new Retrofit.Builder()
                .baseUrl(baseurl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        spoonacularApi = retrofit.create(SpoonacularApi.class);
    }

    public void getRecipeDetails(int recipeId, DetailedFoodCallback callback) {
        Call<SpoonacularDetailedRecipePojo> call = spoonacularApi.detailsRecipe(recipeId, true, apiKey);

        call.enqueue(new Callback<SpoonacularDetailedRecipePojo>() {
            @Override
            public void onResponse(Call<SpoonacularDetailedRecipePojo> call, Response<SpoonacularDetailedRecipePojo> response) {
                if (!response.isSuccessful()) {
                    Log.d(SpoonacularDMS.class.getName(), "Code: " + response.code());
                    return;
                }

                if (callback != null) {
                    callback.onSuccess(PojoConverter.Spoonacular.fromSpoonacularDetailedRecipePojo(response.body()));
                }
            }

            @Override
            public void onFailure(Call<SpoonacularDetailedRecipePojo> call, Throwable t) {
                Log.d(SpoonacularDMS.class.getName(), t.getMessage());
                if (callback != null) {
                    callback.onFail(t);
                }
            }
        });
    }

    public void getGeneratedRecipeDetails(int recipeId, int mealPosition, float quantity, DetailedFoodGenerateCallback callback) {
        Call<SpoonacularDetailedRecipePojo> call = spoonacularApi.detailsRecipe(recipeId, true, apiKey);

        call.enqueue(new Callback<SpoonacularDetailedRecipePojo>() {
            @Override
            public void onResponse(Call<SpoonacularDetailedRecipePojo> call, Response<SpoonacularDetailedRecipePojo> response) {
                if (!response.isSuccessful()) {
                    Log.d(SpoonacularDMS.class.getName(), "Code: " + response.code());
                    callback.onFail(new ConnectException(response.message() + " " + response.code()));
                    return;
                }

                if (callback != null) {
                    Food food = PojoConverter.Spoonacular.fromSpoonacularDetailedRecipePojo(response.body());
                    food.setServingQuantity(Collections.singletonList(quantity));
                    if (quantity != 1) {
                        food.setCalories(food.getCalories()*quantity);
                        food.setFats(food.getFats()*quantity);
                        food.setCarbohydrates(food.getCarbohydrates()*quantity);
                        food.setProteins(food.getProteins()*quantity);
                    }
                    callback.onSuccess(food, mealPosition);
                }
            }

            @Override
            public void onFailure(Call<SpoonacularDetailedRecipePojo> call, Throwable t) {
                Log.d(SpoonacularDMS.class.getName(), t.getMessage());
                if (callback != null) {
                    callback.onFail(t);
                }
            }
        });
    }

    public void listRecipes(String query, HashMap<Integer, Integer> nutritionFilterTable, AdapterDataCallback callback) {
        Call<SpoonacularAdapterFullReposnsePojo> call;

        if (nutritionFilterTable.isEmpty()) {
            call = spoonacularApi.listRecipes(query, true, 0, 50, apiKey);
        } else {
            HashMap<String, String> params = new HashMap<>();
            params.put("query", query);
            params.put("instructionsRequired", "true");
            params.put("number", "50");

            for (Map.Entry<Integer, Integer> entry : nutritionFilterTable.entrySet()) {
                switch (entry.getKey()) {
                    case FilterDialogActivity.MIN_CALORIES:
                        params.put("minCalories", entry.getValue().toString());
                        break;
                    case FilterDialogActivity.MAX_CALORIES:
                        params.put("maxCalories", entry.getValue().toString());
                        break;
                    case FilterDialogActivity.MIN_FATS:
                        params.put("minFat", entry.getValue().toString());
                        break;
                    case FilterDialogActivity.MAX_FATS:
                        params.put("maxFat", entry.getValue().toString());
                        break;
                    case FilterDialogActivity.MIN_CARBOHYDRATES:
                        params.put("minCarbs", entry.getValue().toString());
                        break;
                    case FilterDialogActivity.MAX_CARBOHYDRATES:
                        params.put("maxCarbs", entry.getValue().toString());
                        break;
                    case FilterDialogActivity.MIN_PROTEINS:
                        params.put("minProtein", entry.getValue().toString());
                        break;
                    case FilterDialogActivity.MAX_PROTEINS:
                        params.put("maxProtein", entry.getValue().toString());
                        break;
                }
            }

            params.put("apiKey", apiKey);


            call = spoonacularApi.listRecipes(params);
        }

        call.enqueue(new Callback<SpoonacularAdapterFullReposnsePojo>() {
            @Override
            public void onResponse(Call<SpoonacularAdapterFullReposnsePojo> call, Response<SpoonacularAdapterFullReposnsePojo> response) {
                if (!response.isSuccessful()) {
                    Log.d(SpoonacularDMS.class.getName(), "Code: " + response.code());
                    return;
                }
                final List<FoodAdapterType> correctResponse = new ArrayList<>();


                if (response.body() != null) {
                    correctResponse.addAll(PojoConverter.Spoonacular.fromSpoonacularPojoList(response.body().getResults()));
                }


                if (callback != null) {
                    callback.onSuccess(correctResponse);
                }
            }

            @Override
            public void onFailure(Call<SpoonacularAdapterFullReposnsePojo> call, Throwable t) {
                Log.d(SpoonacularDMS.class.getName(), t.getMessage());
                if (callback != null) {
                    callback.onFail(t);
                }
            }
        });
    }
}
