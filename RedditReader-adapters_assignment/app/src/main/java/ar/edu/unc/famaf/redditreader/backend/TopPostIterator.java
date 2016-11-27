package ar.edu.unc.famaf.redditreader.backend;

import java.util.List;

import ar.edu.unc.famaf.redditreader.model.PostModel;

public interface TopPostIterator {
    void nextPosts(List<PostModel> lst, DBAdapter db);

}
