package cz.cuni.mff.nutritionalassistant.guidancebot.api.Spoonacular;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface SpoonacularApi {

    @GET("recipes/complexSearch")
    Call<SpoonacularAdapterFullReposnsePojo> listRecipes(
            @Query("query") String query,
            @Query("instructionsRequired") boolean instrucReq,
            @Query("minCalories") int minCals,
            @Query("number") int number,
            @Query("apiKey") String apiKey
    );

    @GET("recipes/complexSearch")
    Call<SpoonacularAdapterFullReposnsePojo> listRecipes(
            @QueryMap HashMap<String, String> param
    );

    @GET("recipes/{id}/information")
    Call<SpoonacularDetailedRecipePojo> detailsRecipe(
            @Path("id") int id,
            @Query("includeNutrition") boolean nutritIncl,
            @Query("apiKey") String apiKey
    );
}
