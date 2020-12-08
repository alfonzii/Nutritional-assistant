package cz.cuni.mff.nutritionalassistant.foodtypes;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Recipe extends Food {
    private float servingQuantity = 1;
    private final String servingUnit = "portion";
    private float servingWeight;
}
