package ar.edu.unc.famaf.redditreader.backend;


import android.os.AsyncTask;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import ar.edu.unc.famaf.redditreader.model.Listing;

/**
 * Created by dvr on 20/10/16.
 */

public class GetTopPostsTask extends AsyncTask<String, Integer,Listing> {

    @Override
    protected Listing doInBackground(String... params) {
        String url = params[0];
        System.out.println("URL: "+url);
        HttpURLConnection hcon;
        try {
            hcon=(HttpURLConnection)new URL(url).openConnection();
            //hcon.setReadTimeout(30000); // Timeout at 30 seconds
            //hcon.setRequestProperty("User-Agent", "Alien V1.0");
            hcon.setRequestMethod("GET");
                Parser list = new Parser();
                return list.readJsonStream(hcon.getInputStream());
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }


    @Override
    protected void onPostExecute(Listing input) {
        super.onPostExecute(input);
    }



}
