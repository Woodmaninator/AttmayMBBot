package attmayMBBot.APIHandling.datamuseAPIHandling;

import attmayMBBot.httpHandling.HttpConnectionHandler;
import attmayMBBot.util.EWordQueryMode;
import attmayMBBot.util.EWordType;
import attmayMBBot.util.WordObject;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class DatamuseAPIHandler {
    public List<WordObject> getRandomWord(String pattern, EWordQueryMode wordQueryMode){
        return this.getRandomWord(pattern, "", wordQueryMode);
    }
    public List<WordObject> getRandomWord(String pattern, String searchTerm, EWordQueryMode wordQueryMode){
        return this.getWordObjectListFromJsonString(getResponseFromDatamuseAPI(pattern, searchTerm, wordQueryMode));
    }
    private String getResponseFromDatamuseAPI(String pattern, String searchTerm, EWordQueryMode wordQueryMode) {
        String urlString = "https://api.datamuse.com/words?sp=" + pattern + "&md=p";
        if (wordQueryMode == EWordQueryMode.ADJECTIVE) {
            if (!searchTerm.equals(""))
                urlString = urlString + "&rel_jjb=" + searchTerm;
        }
        if (wordQueryMode == EWordQueryMode.NOUN_BASED_ADJECTIVE) {
            if (!searchTerm.equals(""))
                urlString = urlString + "&topics=" + searchTerm;
        }
        return new HttpConnectionHandler().get(urlString);
    }
    private List<WordObject> getWordObjectListFromJsonString(String jsonString){
        List<WordObject> wordObjectList = new ArrayList<>();
        JsonArray jsonArray = new Gson().fromJson(jsonString, JsonArray.class);
        for(JsonElement object: jsonArray) {
            JsonObject jsonObject = object.getAsJsonObject();
            if (jsonObject.has("tags")) {
                JsonArray tagsArray = jsonObject.get("tags").getAsJsonArray();
                for (JsonElement tagObject : tagsArray) {
                    String tagString = (String) tagObject.getAsString();
                    switch (tagString) {
                        case "n":
                            wordObjectList.add(new WordObject(jsonObject.get("word").getAsString(), EWordType.NOUN));
                            break;
                        case "adj":
                            wordObjectList.add(new WordObject(jsonObject.get("word").getAsString(), EWordType.ADJECTIVE));
                            break;
                    }
                }
            }
        }
        return wordObjectList;
    }
}
