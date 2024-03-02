package attmayMBBot.functionalities.xkcdUpdater;

import com.google.gson.Gson;
import discord4j.core.object.entity.channel.MessageChannel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class XKCDUpdater {
    private Long xkcdChannelId;
    private Integer latestXkcd;
    private Long interval;

    public Long getXkcdChannelId() {
        return xkcdChannelId;
    }

    public Integer getLatestXkcd() {
        return latestXkcd;
    }

    public void startUpdateLoop(MessageChannel channel) {
        new Thread(() -> {
            XKCDPostScraper scraper = new XKCDPostScraper();
            while(true) {
                try{
                    XKCDInfo info = scraper.getXkcd(latestXkcd + 1);

                    if(info != null) {
                        //find a message channel by the id
                        new XKCDPoster().postXKCD(info, channel);

                        latestXkcd = info.getNumber();
                        saveConfigToFile();
                    }

                    Thread.sleep(interval);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }



    private void saveConfigToFile(){
        String path = "AMBBXKCDConfig.json";
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
