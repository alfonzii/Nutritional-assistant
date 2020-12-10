package cz.cuni.mff.nutritionalassistant.foodtypes;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Recipe extends Food {
    List<Ingredient> ingredients;
    String instructions; //List<String>, zalezi od sposobu citania z JSON odpovede
    int numberOfServings; //neviem ci nemoze byt 0.5 serving alebo podobne, tak podla toho sa urci typ servingu
    int readyInMinutes;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Ingredient implements Serializable {
        String name;

        float usAmount;
        String usUnit;

        float metricAmount;
        String metricUnit;
    }

}
