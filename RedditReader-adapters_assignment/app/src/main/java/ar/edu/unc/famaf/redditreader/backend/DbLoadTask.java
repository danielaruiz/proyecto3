package ar.edu.unc.famaf.redditreader.backend;

import android.database.Cursor;
import android.os.AsyncTask;
import ar.edu.unc.famaf.redditreader.backend.RedditDBHelper;
import java.util.List;

import ar.edu.unc.famaf.redditreader.model.PostModel;

/**
 * Created by dvr on 05/11/16.
 */

public class DbLoadTask extends AsyncTask<RedditDBHelper,Void,List<PostModel>>{
    @Override
    protected List<PostModel> doInBackground(RedditDBHelper... db) {
        return db[0].getAllDb();
    }

    @Override
    protected void onPostExecute(List<PostModel> list1){
        super.onPostExecute(list1);
    }
}
