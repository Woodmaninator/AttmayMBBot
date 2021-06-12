package attmayMBBot.APIHandling.jokesAPIHandling;

import attmayMBBot.httpHandling.HttpConnectionHandler;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class JokesAPIHandler {
    public String getRandomJokeAsString(){
        String url = "https://v2.jokeapi.dev/joke/Programming,Miscellaneous,Dark,Pun,Spooky?blacklistFlags=racist";
        JsonObject jsonObject = new Gson().fromJson(new HttpConnectionHandler().get(url),JsonObject.class);
        String result = "";

        if(jsonObject.get("type").getAsString().equals("single")){
            //My crush: Nice weather!
            //Me: Thanks
            //SINGLE!
            return jsonObject.get("joke").getAsString();
        }
        if(jsonObject.get("type").getAsString().equals("twopart"))
            return jsonObject.get("setup").getAsString() + "\n" + jsonObject.get("delivery").getAsString();
        return "Knock Knock.\nCome in!";
    }
}
