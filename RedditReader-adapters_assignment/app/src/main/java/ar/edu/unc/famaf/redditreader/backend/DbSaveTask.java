package ar.edu.unc.famaf.redditreader.backend;

import android.os.AsyncTask;

import java.util.List;

import ar.edu.unc.famaf.redditreader.model.PostModel;

/**
 * Created by dvr on 05/11/16.
 */

//public class DbSaveTask extends AsyncTask<RedditDBHelper,Void,Void> {
//    List<PostModel> list;
//    Context context;
//    public DbSaveTask(List<PostModel> lst, Context c){
//        list=lst;
//        context=c;//eta de masss sacar
//    }
//
//    @Override
//    protected Void doInBackground(RedditDBHelper... db) {
//        System.out.println(list.size());
//        db[0].upgrade();
//        //guardar imagen por defecto
//        //Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.cruz);
//
//        for(int i=0; i<list.size(); i++){
//            //list.get(i).setIcon(list.get(i).getBytes(icon));
//            db[0].savePostModel(list.get(i));
//
//        }
//
//        return null;
//    }
//}

public class DbSaveTask extends AsyncTask<DBAdapter,Void,Void> {
    private List<PostModel> list;

    public DbSaveTask(List<PostModel> lst){
        list=lst;
    }

    @Override
    protected Void doInBackground(DBAdapter... dbAdapters) {
        System.out.println(list.size());
        dbAdapters[0].savePostModel(list);
      return null;
    }
}

