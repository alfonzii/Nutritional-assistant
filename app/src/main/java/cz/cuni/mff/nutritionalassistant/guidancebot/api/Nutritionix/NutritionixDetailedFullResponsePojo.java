package cz.cuni.mff.nutritionalassistant.guidancebot.api.Nutritionix;

import java.util.List;

import lombok.Getter;

@Getter
class NutritionixDetailedFullResponsePojo {
    private List<NutritionixDetailedProductPojo> foods;
}
