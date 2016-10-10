package ar.edu.unc.famaf.redditreader.backend;

import java.util.ArrayList;
import java.util.List;

import ar.edu.unc.famaf.redditreader.R;
import ar.edu.unc.famaf.redditreader.model.PostModel;

public class Backend {
    private static Backend ourInstance = new Backend();
    private List<PostModel> mListPostModel;

    public static Backend getInstance() {
        return ourInstance;
    }

    private Backend() {
        mListPostModel = new ArrayList<>();

        PostModel p1 = new PostModel();
        p1.setTitle("Titulo 1");
        p1.setAuthor("Aitor 1");
        p1.setCreated("29/09/2016");
        p1.setSubreddit("/r/today");
        p1.setIcon(R.mipmap.ic_launcher);

        mListPostModel.add(p1);

        PostModel p2 = new PostModel();
        p2.setTitle("Titulo 2");
        p2.setAuthor("Aitor 2");
        p2.setCreated("29/09/2016");
        p2.setSubreddit("/r/today");
        p2.setIcon(R.mipmap.ic_launcher);

        mListPostModel.add(p2);

        PostModel p3 = new PostModel();
        p3.setTitle("Titulo 3");
        p3.setAuthor("Aitor 3");
        p3.setCreated("29/09/2016");
        p3.setSubreddit("/r/today");
        p3.setIcon(R.mipmap.ic_launcher);
        mListPostModel.add(p3);

        PostModel p4 = new PostModel();
        p4.setTitle("Titulo 4");
        p4.setAuthor("Aitor 4");
        p4.setCreated("29/09/2016");
        p4.setSubreddit("/r/today");
        p4.setIcon(R.mipmap.ic_launcher);

        mListPostModel.add(p4);

        PostModel p5 = new PostModel();
        p5.setTitle("Titulo 5");
        p5.setAuthor("Aitor 5");
        p5.setCreated("29/09/2016");
        p5.setSubreddit("/r/today");
        p5.setIcon(R.mipmap.ic_launcher);
        mListPostModel.add(p5);
    }

    public List<PostModel> getTopPosts() {
        return mListPostModel;
    }

}
