package attmayMBBot.APIHandling.spoonacularAPIHandling;

import attmayMBBot.config.AttmayMBBotConfig;
import attmayMBBot.httpHandling.HttpConnectionHandler;
import attmayMBBot.util.Recipe;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class SpoonacularAPIHandler {
    private AttmayMBBotConfig config;

    public SpoonacularAPIHandler(AttmayMBBotConfig config) {
        this.config = config;
    }
    public Recipe getRandomRecipe(){
        String url = "https://api.spoonacular.com/recipes/random?apiKey=" + this.config.getSpoonacularApiKey() + "&limitLicense=true&number=1";
        JsonObject jsonObject = new Gson().fromJson(new HttpConnectionHandler().get(url),JsonObject.class);
        return new Recipe(
                jsonObject.get("recipes").getAsJsonArray().get(0).getAsJsonObject().get("title").getAsString(),
                jsonObject.get("recipes").getAsJsonArray().get(0).getAsJsonObject().get("image").getAsString(),
                jsonObject.get("recipes").getAsJsonArray().get(0).getAsJsonObject().get("summary").getAsString(),
                jsonObject.get("recipes").getAsJsonArray().get(0).getAsJsonObject().get("sourceUrl").getAsString()
        );
    }
}
