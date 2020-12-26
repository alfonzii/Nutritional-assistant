package cz.cuni.mff.nutritionalassistant.guidancebot.api;

import java.util.List;

import lombok.Getter;

@Getter
public class NutritionixDetailedFullResponsePojo {

    private List<NutritionixDetailedProductPojo> foods;
}
