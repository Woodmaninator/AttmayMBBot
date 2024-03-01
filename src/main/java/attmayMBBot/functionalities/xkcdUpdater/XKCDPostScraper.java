package attmayMBBot.functionalities.xkcdUpdater;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class XKCDPostScraper {
    private static final String xkcdBaseUrl = "https://xkcd.com/";
    public XKCDInfo getXkcd(Integer number){
        //Make a http get request to the xkcdBaseUrl and check the response code
        try{
            String url = xkcdBaseUrl + number;
            Document page = Jsoup.connect(url).get();

            //Use Jsoup to parse the html and get the info
            String title = page.select("div#ctitle").text();

            Elements comic = page.select("div#comic img");

            String imageUrl = comic.attr("src");
            if(comic.hasAttr("srcset")) {
                imageUrl = comic.attr("srcset");
            }

            imageUrl = imageUrl.substring(2);
            imageUrl = "https://" + imageUrl.split(" ")[0];

            String altText = comic.attr("title");


            return new XKCDInfo(number, title, imageUrl, altText);
        } catch(Exception ex) {
            return null;
        }
    }
}
