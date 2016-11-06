package ar.edu.unc.famaf.redditreader.backend;

import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import java.util.List;

import ar.edu.unc.famaf.redditreader.model.PostModel;

/**
 * Created by dvr on 05/11/16.
 */

public class DbSaveTask extends AsyncTask<RedditDBHelper,Void,Void> {
    List<PostModel> list;

    public DbSaveTask(List<PostModel> lst){
        list=lst;
    }
    @Override
    protected Void doInBackground(RedditDBHelper... db) {
        System.out.println(list.size());
        db[0].upgrade();

        for(int i=0; i<list.size(); i++){
            db[0].savePostModel(list.get(i));

        }

        return null;
    }
}
