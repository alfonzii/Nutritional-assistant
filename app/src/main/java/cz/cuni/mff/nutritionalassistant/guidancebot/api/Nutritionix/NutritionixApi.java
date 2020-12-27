package cz.cuni.mff.nutritionalassistant.guidancebot.api.Nutritionix;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface NutritionixApi {

    @Headers({"x-app-id: b48b1bce",
              "x-app-key: c95400340ee0389a6cfa23fc4fdbcebd",
              "x-remote-user-id: 0"})
    @GET("v2/search/instant")
    Call<NutritionixAdapterFullResponsePojo> listProducts(@Query("query") String query);

    @Headers({"x-app-id: b48b1bce",
            "x-app-key: c95400340ee0389a6cfa23fc4fdbcebd",
            "x-remote-user-id: 0"})
    @FormUrlEncoded
    @POST("v2/search/instant")
    Call<List<NutritionixAdapterProductPojo>> listProducts(@FieldMap HashMap<String,Object> param);


    @Headers({"x-app-id: b48b1bce",
            "x-app-key: c95400340ee0389a6cfa23fc4fdbcebd",
            "x-remote-user-id: 0"})
    @GET("v2/search/item")
    Call<NutritionixDetailedFullResponsePojo> detailsBrandedProduct(@Query("nix_item_id") String itemId);

    @Headers({"x-app-id: b48b1bce",
            "x-app-key: c95400340ee0389a6cfa23fc4fdbcebd",
            "x-remote-user-id: 0"})
    @FormUrlEncoded
    @POST("v2/natural/nutrients")
    Call<NutritionixDetailedFullResponsePojo> detailsCommonProduct(@Field("query") String query);
}
