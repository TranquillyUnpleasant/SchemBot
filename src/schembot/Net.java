package schembot;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Net{

    public InputStream download(String url){
        try{
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
            return connection.getInputStream();
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}
