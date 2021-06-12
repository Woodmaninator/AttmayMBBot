package attmayMBBot.httpHandling;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpConnectionHandler {
    public HttpConnectionHandler(){}
    public String get(String urlString){
        HttpURLConnection connection = null;
        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            connection.setRequestProperty("User-Agent","PostmanRuntime/7.28.0");

            //I have no idea what those 2 lines do
            connection.setUseCaches(false);
            connection.setDoOutput(true);

            //Get the response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder responseBuilder = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null)
                responseBuilder.append(line);
            rd.close();
            return responseBuilder.toString();
        } catch(Exception ex){
            ex.printStackTrace();
            return null;
        } finally {
            if(connection != null)
                connection.disconnect();
        }
    }
}
