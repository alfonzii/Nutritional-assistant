package cz.cuni.mff.nutritionalassistant.guidancebot.api.Nutritionix;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;

@Getter
class NutritionixRestaurantPojo {
    private String name;

    @SerializedName("brand_id")
    private String brandId;

    @SerializedName("distance_km")
    private float distance;
}
