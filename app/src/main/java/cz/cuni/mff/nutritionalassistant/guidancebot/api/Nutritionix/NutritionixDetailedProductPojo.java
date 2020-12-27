package cz.cuni.mff.nutritionalassistant.guidancebot.api.Nutritionix;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Getter;

@Getter
public class NutritionixDetailedProductPojo {
    @SerializedName("food_name")
    private String foodName;

    @SerializedName("brand_name")
    private String brandName;

    private String thumb;

    @SerializedName("nf_calories")
    private float calories;

    @SerializedName("nf_total_fat")
    private float fats;

    @SerializedName("nf_total_carbohydrate")
    private float carbohydrates;

    @SerializedName("nf_protein")
    private float proteins;

    @SerializedName("serving_qty")
    private float quantity;

    @SerializedName("serving_unit")
    private String unit;

    @SerializedName("serving_weight_grams")
    private Float weightGrams;

    @SerializedName("alt_measures")
    private List<NutritionixAltMeasuresPojo> altMeasures;
}
