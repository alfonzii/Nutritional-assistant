package cz.cuni.mff.nutritionalassistant.guidancebot.api.Nutritionix;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;

@Getter
public class NutritionixAdapterProductPojo {
    @SerializedName("food_name")
    private String foodName;

    @SerializedName("serving_unit")
    private String servingUnit;

    @SerializedName("nf_calories")
    private float calories;

    private Photo photo;

    @SerializedName("nix_item_id")
    private String id;

    @SerializedName("brand_name")
    private String brandName;

    @Getter
    public static class Photo {
        private String thumb;
    }
}
