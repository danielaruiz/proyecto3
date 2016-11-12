package ar.edu.unc.famaf.redditreader.backend;

import java.util.List;

import ar.edu.unc.famaf.redditreader.model.PostModel;

/**
 * Created by dvr on 04/11/16.
 */

//public interface TopPostIterator {
//    void nextPosts(List<PostModel> lst, RedditDBHelper db);
//
//}
public interface TopPostIterator {
    void nextPosts(List<PostModel> lst, DBAdapter db);

}
