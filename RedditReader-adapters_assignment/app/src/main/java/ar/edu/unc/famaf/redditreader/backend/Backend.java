package ar.edu.unc.famaf.redditreader.backend;


import java.util.ArrayList;
import java.util.List;
import ar.edu.unc.famaf.redditreader.model.Listing;
import ar.edu.unc.famaf.redditreader.model.PostModel;

public class Backend {
    private static String Url = "https://www.reddit.com/.json?limit=50";
    private static Backend ourInstance = new Backend();
    private List<PostModel> mListPostModel=null;

    public static Backend getInstance() { return ourInstance;    }

    public Backend() {
        mListPostModel = new ArrayList<>();
    }

    public void getTopPosts(final TopPostIterator iterator) {
        new GetTopPostsTask() {
            @Override
            protected void onPostExecute(Listing input) {
                mListPostModel.clear();
                mListPostModel.addAll(input.getPostModelList());
                iterator.nextPosts(mListPostModel);
            }
        }.execute("https://www.reddit.com/top/.json?limit=50");

    }

}
