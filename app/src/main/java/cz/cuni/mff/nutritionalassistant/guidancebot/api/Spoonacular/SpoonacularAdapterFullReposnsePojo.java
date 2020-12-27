package cz.cuni.mff.nutritionalassistant.guidancebot.api.Spoonacular;

import java.util.List;

import lombok.Getter;

@Getter
public class SpoonacularAdapterFullReposnsePojo {
    private List<SpoonacularAdapterRecipePojo> results;
}
