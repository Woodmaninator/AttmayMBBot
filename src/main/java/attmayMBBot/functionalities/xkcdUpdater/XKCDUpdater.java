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
    private String xkcdBaseUrl;
    private Long xkcdChannelId;
    private Long latestXkcd;
    private Long interval;

    public Long getXkcdChannelId() {
        return xkcdChannelId;
    }

    public void startUpdateLoop(MessageChannel channel) {
        new Thread(() -> {
            while(true) {
                try{
                    XKCDInfo info = getXkcdInfo(latestXkcd + 1);

                    //find a message channel by the id
                    channel.createMessage(spec -> {
                        spec.addEmbed(embed -> {
                            embed.setTitle(info.getTitle());
                            embed.setImage(info.getImageUrl());
                            embed.setDescription(info.getAltText());
                        });
                    }).block();

                    if(info != null) {
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

    public XKCDInfo getXkcdInfo(Long number){
        //Make a http get request to the xkcdBaseUrl and check the response code

        try{
            String url = xkcdBaseUrl + number;
            Document page = Jsoup.connect(url).get();

            //Use Jsoup to parse the html and get the info
            String title = page.select("div#ctitle").text();

            String imageUrl = page.select("div#comic img").attr("srcset");
            imageUrl = imageUrl.substring(2);
            imageUrl = "https://" + imageUrl.split(" ")[0];

            String altText = page.select("div#comic img").attr("title");


            return new XKCDInfo(number, title, imageUrl, altText);
        } catch(Exception ex) {
            return null;
        }
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
