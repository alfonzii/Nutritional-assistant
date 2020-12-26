package cz.cuni.mff.nutritionalassistant.guidancebot.api;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;

@Getter
public class NutritionixAltMeasuresPojo {
    @SerializedName("serving_weight")
    private float servingWeight;

    @SerializedName("measure")
    private String servingUnit;

    @SerializedName("qty")
    private float servingQuantity;
}
