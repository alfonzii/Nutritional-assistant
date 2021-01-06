package cz.cuni.mff.nutritionalassistant.guidancebot.api.Nutritionix;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Getter;

@Getter
class NutritionixRestaurantsFullResponsePojo {
    @SerializedName("locations")
    private List<NutritionixRestaurantPojo> restaurants;
}