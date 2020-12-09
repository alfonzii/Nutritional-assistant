package cz.cuni.mff.nutritionalassistant.foodtypes;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RestaurantFoodAdapterType extends FoodAdapterType {
    private String brandName;
    double distance;
}
