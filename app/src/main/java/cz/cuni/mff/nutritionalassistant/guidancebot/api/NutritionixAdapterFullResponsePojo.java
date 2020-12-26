package cz.cuni.mff.nutritionalassistant.guidancebot.api;

import java.util.List;

import lombok.Getter;

@Getter
public class NutritionixAdapterFullResponsePojo {
    private List<NutritionixAdapterProductPojo> common;
    private List<NutritionixAdapterProductPojo> branded;
}
