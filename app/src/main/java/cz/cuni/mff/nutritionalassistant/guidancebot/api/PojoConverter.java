package cz.cuni.mff.nutritionalassistant.guidancebot.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cz.cuni.mff.nutritionalassistant.foodtypes.Food;
import cz.cuni.mff.nutritionalassistant.foodtypes.FoodAdapterType;
import cz.cuni.mff.nutritionalassistant.foodtypes.Product;
import cz.cuni.mff.nutritionalassistant.foodtypes.ProductAdapterType;
import cz.cuni.mff.nutritionalassistant.foodtypes.RecipeAdapterType;
import cz.cuni.mff.nutritionalassistant.guidancebot.api.Nutritionix.NutritionixAdapterProductPojo;
import cz.cuni.mff.nutritionalassistant.guidancebot.api.Nutritionix.NutritionixAltMeasuresPojo;
import cz.cuni.mff.nutritionalassistant.guidancebot.api.Nutritionix.NutritionixDetailedProductPojo;
import cz.cuni.mff.nutritionalassistant.guidancebot.api.Spoonacular.SpoonacularAdapterRecipePojo;

public class PojoConverter {

    private PojoConverter() {
    }

    public static class Nutritionix {
        public static FoodAdapterType fromNutritionixAdapterProductPojo(NutritionixAdapterProductPojo pojo) {
            return new ProductAdapterType(pojo.getFoodName(), pojo.getThumb(), Food.FoodType.PRODUCT,
                    pojo.getServingUnit(), pojo.getCalories(), pojo.getBrandName(), pojo.getId());
        }

        public static List<FoodAdapterType> fromNutritionixPojoList(List<NutritionixAdapterProductPojo> pojoList) {
            List<FoodAdapterType> list = new ArrayList<>();

            for (NutritionixAdapterProductPojo pojo : pojoList) {
                list.add(fromNutritionixAdapterProductPojo(pojo));
            }

            return list;
        }

        public static Food fromNutritionixDetailedProductPojo(NutritionixDetailedProductPojo pojo) {
            List<Float> servingQuantity = new ArrayList<>();
            List<String> servingUnit = new ArrayList<>();
            List<Float> servingWeight = null;

            servingQuantity.add(pojo.getQuantity());
            servingUnit.add(pojo.getUnit());

            if (pojo.getWeightGrams() != null) {
                servingWeight = new ArrayList<>();
                servingWeight.add(pojo.getWeightGrams());
            }

            if (pojo.getAltMeasures() != null) {
                for (NutritionixAltMeasuresPojo alt : pojo.getAltMeasures()) {
                    servingQuantity.add(alt.getServingQuantity());
                    servingUnit.add(alt.getServingUnit());
                    servingWeight.add(alt.getServingWeight());
                }
            }

            List<Float> servingQuantityFinal = Collections.unmodifiableList(servingQuantity);
            List<String> servingUnitFinal = Collections.unmodifiableList(servingUnit);
            List<Float> servingWeightFinal = servingWeight == null ? null : Collections.unmodifiableList(servingWeight);

            return new Product(pojo.getFoodName(), pojo.getThumb(), pojo.getCalories(),
                    pojo.getFats(), pojo.getCarbohydrates(), pojo.getProteins(), Food.FoodType.PRODUCT, pojo.getBrandName(),
                    servingQuantityFinal, servingUnitFinal, servingWeightFinal);
        }
    }

    public static class Spoonacular {
        public static List<FoodAdapterType> fromSpoonacularPojoList(List<SpoonacularAdapterRecipePojo> pojoList) {
            List<FoodAdapterType> list = new ArrayList<>();

            for (SpoonacularAdapterRecipePojo pojo : pojoList) {
                list.add(fromSpoonacularAdapterRecipePojo(pojo));
            }

            return list;
        }

        public static FoodAdapterType fromSpoonacularAdapterRecipePojo(SpoonacularAdapterRecipePojo pojo) {
            return new RecipeAdapterType(
                    pojo.getRecipeName(), pojo.getThumbnailURL(),
                    Food.FoodType.RECIPE, "serving",
                    pojo.getNutrition().getNutrients().get(0).getCalories(), pojo.getId());
        }
    }

}
