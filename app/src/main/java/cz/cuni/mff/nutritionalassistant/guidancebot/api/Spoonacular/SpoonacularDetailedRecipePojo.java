package cz.cuni.mff.nutritionalassistant.guidancebot.api.Spoonacular;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Getter;

@Getter
public class SpoonacularDetailedRecipePojo {
    @SerializedName("title")
    private String recipeName;

    @SerializedName("image")
    private String thumbnailURL;

    private Nutrition nutrition;

    @SerializedName("extendedIngredients")
    private List<Ingredient> ingredients;

    private int readyInMinutes;
    private int servings;
    private String instructions;


    @Getter
    public static class Ingredient {
        private String name;

        private Measures measures;

        @Getter
        public static class Measures {
            private MeasureValues us;
            private MeasureValues metric;

            @Getter
            public static class MeasureValues {
                private float amount;
                private String unitShort;
            }
        }
    }

    @Getter
    public static class Nutrition {
        private List<Nutrients> nutrients;

        @Getter
        public static class Nutrients {
            @SerializedName("title")
            private String nutrientName;

            @SerializedName("amount")
            private float nutrientAmount;
        }
    }
}
