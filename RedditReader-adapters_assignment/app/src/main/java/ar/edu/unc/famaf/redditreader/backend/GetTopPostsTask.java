package ar.edu.unc.famaf.redditreader.backend;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

import ar.edu.unc.famaf.redditreader.model.PostModel;

/**
 * Created by dvr on 20/10/16.
 */

public class GetTopPostsTask extends AsyncTask<String, Integer,String> {
    private List<PostModel> list;

    public GetTopPostsTask(List<PostModel> lists){
        list= lists;
    }
    public List<PostModel> GetTopPosts() {return list;}

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String  doInBackground(String... params) {
        String url = params[0];
        System.out.println("URL: "+url);
        HttpURLConnection hcon = null;
        try {
            hcon=(HttpURLConnection)new URL(url).openConnection();
            //hcon.setReadTimeout(30000); // Timeout at 30 seconds
            //hcon.setRequestProperty("User-Agent", "Alien V1.0");
            hcon.setRequestMethod("GET");
            try{
                StringBuffer sbuffer=new StringBuffer(8192);
                String tmp="";
                BufferedReader br=new BufferedReader(new InputStreamReader(hcon.getInputStream()));
                while((tmp=br.readLine())!=null)
                    sbuffer.append(tmp).append("\n");
                br.close();
                return sbuffer.toString();
            }catch(IOException e){
                e.printStackTrace();
                return null;
            }
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
    protected void onProgressUpdate(Integer... values) {

        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String  input) {
        super.onPostExecute(input);
        System.out.println("onPostExecute GetTopPost");
        InputStream is = new ByteArrayInputStream(input.getBytes());
        Parser datos = new Parser();
           try {
               list.addAll(datos.readJsonStream(is));
           } catch (IOException e) {
               e.printStackTrace();
           }
        return;
    }


    @Override
    protected void onCancelled(String inputStream) {

        super.onCancelled(inputStream);
    }


}
