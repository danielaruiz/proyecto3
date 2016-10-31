package ar.edu.unc.famaf.redditreader.backend;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ar.edu.unc.famaf.redditreader.R;
import ar.edu.unc.famaf.redditreader.model.PostModel;

public class Backend {
    private static String Url = "https://www.reddit.com/.json?limit=50";
    private static Backend ourInstance = new Backend();
    private List<PostModel> mListPostModel=null;

    public static Backend getInstance() {
        return ourInstance;
    }

    private Backend() {
        mListPostModel = new ArrayList<>();
        List<PostModel> list=null;
        String[] urlArray = new String[1];
        urlArray[0] = this.Url;
        GetTopPostsTask getTopPostsTask =  new GetTopPostsTask(mListPostModel);
        mListPostModel= getTopPostsTask.GetTopPosts();

        getTopPostsTask.execute(urlArray);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (mListPostModel==null){System.out.println("vacio?");}

    }

    public List<PostModel> getTopPosts() {
        return mListPostModel;
    }

}
