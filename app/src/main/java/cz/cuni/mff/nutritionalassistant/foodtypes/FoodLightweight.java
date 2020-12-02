package cz.cuni.mff.nutritionalassistant.foodtypes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Lightweight version of Food class to consume less memory when being binded to FoodAddingAdapter.
// There is no need to have all Food class members available while being just item in RecyclerView,when
// when they are not used at all (and will be just consuming memory).

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class FoodLightweight {
    private String foodName;
    private String thumbnailURL;
    private Food.FoodType foodType;
    private String servingUnit;
    private float calories;
    private String detailedInfoURL;
}
