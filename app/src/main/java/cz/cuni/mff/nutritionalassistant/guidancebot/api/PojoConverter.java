package cz.cuni.mff.nutritionalassistant.guidancebot.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import cz.cuni.mff.nutritionalassistant.foodtypes.Food;
import cz.cuni.mff.nutritionalassistant.foodtypes.FoodAdapterType;
import cz.cuni.mff.nutritionalassistant.foodtypes.Product;
import cz.cuni.mff.nutritionalassistant.foodtypes.ProductAdapterType;
import cz.cuni.mff.nutritionalassistant.foodtypes.Recipe;
import cz.cuni.mff.nutritionalassistant.foodtypes.RecipeAdapterType;
import cz.cuni.mff.nutritionalassistant.guidancebot.api.Nutritionix.NutritionixAdapterProductPojo;
import cz.cuni.mff.nutritionalassistant.guidancebot.api.Nutritionix.NutritionixAltMeasuresPojo;
import cz.cuni.mff.nutritionalassistant.guidancebot.api.Nutritionix.NutritionixDetailedProductPojo;
import cz.cuni.mff.nutritionalassistant.guidancebot.api.Spoonacular.SpoonacularAdapterRecipePojo;
import cz.cuni.mff.nutritionalassistant.guidancebot.api.Spoonacular.SpoonacularDetailedRecipePojo;

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
                    pojo.getNutrition().getNutrients().get(0).getNutrientAmount(), pojo.getId());
                                                        // nutrientAmount should be calories
        }

        public static List<Food> fromSpoonacularGenerativeRecipesPojoList(List<SpoonacularAdapterRecipePojo> pojoList) {
            List<Food> list = new ArrayList<>();

            for(SpoonacularAdapterRecipePojo pojo : pojoList) {
                list.add(fromSpoonacularGenerativeRecipePojo(pojo));
            }

            return list;
        }

        private static Food fromSpoonacularGenerativeRecipePojo(SpoonacularAdapterRecipePojo pojo) {
            int caloriesIndex = 0;
            int proteinsIndex = 1;
            int fatsIndex = 2;
            int carbsIndex = 3;

            Recipe recipe = new Recipe(
                    pojo.getRecipeName(),
                    pojo.getThumbnailURL(),
                    pojo.getNutrition().getNutrients().get(caloriesIndex).getNutrientAmount(),
                    pojo.getNutrition().getNutrients().get(fatsIndex).getNutrientAmount(),
                    pojo.getNutrition().getNutrients().get(carbsIndex).getNutrientAmount(),
                    pojo.getNutrition().getNutrients().get(proteinsIndex).getNutrientAmount(),
                    Food.FoodType.RECIPE,
                    null,
                    null,
                    null,
                    null,
                    null,
                    0,
                    0
            );
            recipe.setId(pojo.getId());
            return recipe;
        }


        public static Food fromSpoonacularDetailedRecipePojo(SpoonacularDetailedRecipePojo pojo) {
            List<Float> servingQuantity = Collections.singletonList(1f);
            List<String> servingUnit = Collections.singletonList("serving");
            //List<Float> servingWeight = null;

            float calories = 0;
            float fats = 0;
            float carbohydrates = 0;
            float proteins = 0;

            for (SpoonacularDetailedRecipePojo.Nutrition.Nutrients nutrient : pojo.getNutrition().getNutrients()) {
                switch (nutrient.getNutrientName()) {
                    case "Calories":
                        calories = nutrient.getNutrientAmount();
                        break;
                    case "Fat":
                        fats = nutrient.getNutrientAmount();
                        break;
                    case "Carbohydrates":
                        carbohydrates = nutrient.getNutrientAmount();
                        break;
                    case "Protein":
                        proteins = nutrient.getNutrientAmount();
                        break;
                }
            }

            List<Recipe.Ingredient> ingredientList = new ArrayList<>();

            for (SpoonacularDetailedRecipePojo.Ingredient ingredient : pojo.getIngredients()) {
                Recipe.Ingredient newIngredient = new Recipe.Ingredient();

                newIngredient.setName(ingredient.getName());
                newIngredient.setMetricUnit(ingredient.getMeasures().getMetric().getUnitShort());
                newIngredient.setMetricAmount(ingredient.getMeasures().getMetric().getAmount());
                newIngredient.setUsUnit(ingredient.getMeasures().getUs().getUnitShort());
                newIngredient.setUsAmount(ingredient.getMeasures().getUs().getAmount());

                ingredientList.add(newIngredient);
            }

            return new Recipe(pojo.getRecipeName(), pojo.getThumbnailURL(), calories, fats, carbohydrates, proteins,
                    Food.FoodType.RECIPE, servingQuantity, servingUnit, null, ingredientList, pojo.getInstructions(),
                    pojo.getServings(), pojo.getReadyInMinutes());

        }
    }

}
