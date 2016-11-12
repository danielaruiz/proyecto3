package ar.edu.unc.famaf.redditreader.backend;


import android.os.AsyncTask;

import java.util.List;

import ar.edu.unc.famaf.redditreader.model.PostModel;

/**
 * Created by dvr on 05/11/16.
 */

//public class DbLoadTask extends AsyncTask<RedditDBHelper,Void,List<PostModel>>{
//    @Override
//    protected List<PostModel> doInBackground(RedditDBHelper... db) {
//        return db[0].getAllDb();
//    }
//
//    @Override
//    protected void onPostExecute(List<PostModel> list1){
//        super.onPostExecute(list1);
//    }
//}


public class DbLoadTask extends AsyncTask<DBAdapter,Void,List<PostModel>>{
    @Override
    protected List<PostModel> doInBackground(DBAdapter... dbAdapters) {
        List<PostModel> list=dbAdapters[0].getAllDb();
        dbAdapters[0].close();
        return list;
    }

    @Override
    protected void onPostExecute(List<PostModel> list1){
        super.onPostExecute(list1);
    }
}