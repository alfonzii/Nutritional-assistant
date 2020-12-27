package cz.cuni.mff.nutritionalassistant.guidancebot.api.Spoonacular;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Getter;

@Getter
public class SpoonacularAdapterRecipePojo {
    private int id;

    @SerializedName("title")
    private String recipeName;

    @SerializedName("image")
    private String thumbnailURL;

    private Nutrition nutrition;


    @Getter
    public static class Nutrition {
        private List<Nutrients> nutrients;

        @Getter
        public static class Nutrients {
            @SerializedName("amount")
            private float calories;
        }

    }
}
