package ar.edu.unc.famaf.redditreader.backend;


import android.app.AlertDialog;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


import java.util.ArrayList;
import java.util.List;


import ar.edu.unc.famaf.redditreader.model.Listing;
import ar.edu.unc.famaf.redditreader.model.PostModel;


public class Backend {
    private static Backend ourInstance = new Backend();
    private List<PostModel> mListPostModel=null;
    private boolean loadingDB =false;

    public static Backend getInstance() { return ourInstance;    }

    private Backend() {
        mListPostModel = new ArrayList<>();
    }


    public void getTopPosts(int totalItemsCount, Context context, final TopPostIterator iterator){//debe devolver los primero 5 posts
        final DBAdapter db= new DBAdapter(context, totalItemsCount).open();

        if (!isConnected(context) || loadingDB ){
            System.out.println("loadingDb");
            new DbLoadTask(){
                @Override
                protected void onPostExecute(List<PostModel> list1) {
                    iterator.nextPosts(list1, db);
                }
            }.execute(db);

        }else{
            new GetTopPostsTask() {
                @Override
                protected void onPostExecute(Listing input) {
                    mListPostModel.clear();
                    List<PostModel> list =input.getPostModelList();
                    if(list!=null){
                        mListPostModel.addAll(input.getPostModelList());
                        //Guardo datos en la base de datos
                        new DbSaveTask(mListPostModel){
                            @Override
                            protected void onPostExecute(Void aVoid) {
                                super.onPostExecute(aVoid);
                                loadingDB=true;
                                //Cargo desde la base de datos la lista
                                new DbLoadTask(){
                                    @Override
                                    protected void onPostExecute(List<PostModel> list1) {
                                        super.onPostExecute(list1);
                                        iterator.nextPosts(list1, db);
                                    }
                                }.execute(db);
                            }
                        }.execute(db);
                    }
                }
            }.execute("https://www.reddit.com/top/.json?limit=50");
        }

    }

    private boolean isConnected(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){
            System.out.println("No hay internet");
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
            dialogBuilder.setMessage("No Internet connection!");
            dialogBuilder.setCancelable(true).setTitle("Alert");
            dialogBuilder.create().show();
            return false;
        }
        return true;
    }
}
