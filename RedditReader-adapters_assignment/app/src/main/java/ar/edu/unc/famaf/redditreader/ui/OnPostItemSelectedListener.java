package ar.edu.unc.famaf.redditreader.ui;

import java.util.List;

import ar.edu.unc.famaf.redditreader.model.PostModel;

/**
 * Created by dvr on 15/11/16.
 */

public interface OnPostItemSelectedListener {

    void onPostItemPicked(PostModel post, int position);
}
