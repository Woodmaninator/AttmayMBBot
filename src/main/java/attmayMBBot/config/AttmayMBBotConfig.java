package attmayMBBot.config;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class AttmayMBBotConfig {
    private String token;
    private String spoonacularApiKey;
    private long authorizedRoleID;
    private long guildID;
    private long quoteRankingChannelID;
    private AMBBGoodnightConfig goodnightConfig;
    private ArcadeConfig arcadeConfig;

    public String getToken(){
        return this.token;
    }
    public String getSpoonacularApiKey(){
        return this.spoonacularApiKey;
    }
    public long getAuthorizedRoleID(){
        return this.authorizedRoleID;
    }
    public long getGuildID() {
        return guildID;
    }
    public long getQuoteRankingChannelID() {
        return quoteRankingChannelID;
    }
    public AMBBGoodnightConfig getGoodnightConfig(){
        return this.goodnightConfig;
    }
    public ArcadeConfig getArcadeConfig(){
        return this.arcadeConfig;
    }

    public void saveConfigToFile(){
        String path = "AMBBConfig.json";
        try {
            String jsonString = new Gson().toJson(this);
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            writer.write(jsonString);
            writer.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
