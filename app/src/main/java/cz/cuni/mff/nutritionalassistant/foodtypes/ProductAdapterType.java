package cz.cuni.mff.nutritionalassistant.foodtypes;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductAdapterType extends FoodAdapterType {
    private String brandName;
    private String id; //nix_item_id if branded

    public ProductAdapterType(String foodName, String thumbUrl, Food.FoodType type, String servingUnit, float cals, String brand, String id) {
        super(foodName, thumbUrl, type, servingUnit, cals);
        this.brandName = brand;
        this.id = id;
    }
}
