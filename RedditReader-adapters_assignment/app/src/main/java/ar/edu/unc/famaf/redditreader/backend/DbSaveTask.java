package ar.edu.unc.famaf.redditreader.backend;

import android.os.AsyncTask;

import java.util.List;

import ar.edu.unc.famaf.redditreader.model.PostModel;


public class DbSaveTask extends AsyncTask<DBAdapter,Void,Void> {
    private List<PostModel> list;

    public DbSaveTask(List<PostModel> lst){
        list=lst;
    }

    @Override
    protected Void doInBackground(DBAdapter... dbAdapters) {
        dbAdapters[0].savePostModel(list);
      return null;
    }
}

