package ar.edu.unc.famaf.redditreader.ui;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by dvr on 21/01/17.
 */

public class PostModelHolder {
    TextView mTitle;
    TextView mSubreddit;
    TextView mCreated;
    TextView mAuthor;
    ImageView icon;
    TextView comments;
    TextView score;

    ProgressBar progressBar;
    ImageButton up;
    ImageButton down;
    int position;
}
