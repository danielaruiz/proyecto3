package ar.edu.unc.famaf.redditreader.backend;


import android.app.AlertDialog;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Switch;


import java.util.ArrayList;
import java.util.List;


import ar.edu.unc.famaf.redditreader.model.Listing;
import ar.edu.unc.famaf.redditreader.model.PostModel;

public class Backend {
    private static Backend ourInstance = new Backend();
    private List<PostModel> mListPostModel=null;
    private boolean loadingDB =false;
    private CtrlListing ctrlListing;
    private boolean waiting=false;

    public static Backend getInstance() { return ourInstance;    }

    private Backend() {
        mListPostModel = new ArrayList<>();
    }


    public void getTopPosts(int totalItemsCount, Context context, final TopPostIterator iterator){//debe devolver los primero 5 posts
        final DBAdapter db= new DBAdapter(context, totalItemsCount).open();

        if (!isConnected(context) || loadingDB ){
            if (isConnected(context) && (totalItemsCount!=0) && (totalItemsCount % 50) == 0 && !waiting) {
                ctrlListing.control(iterator,totalItemsCount,db);
            }else if(!waiting){
                new DbLoadTask() {
                    @Override
                    protected void onPostExecute(List<PostModel> list1) {
                        iterator.nextPosts(list1, db);
                    }
                }.execute(db);
            }
        }else {
            //Esta secuencia se ejecuta la primera vez que se llama a getTopPost, luego la carga de los post
            // los posts se da en el segmento de arriba
            ctrlListing = new CtrlListing();
            ctrlListing.init_process(iterator,db);
        }

    }

    private class CtrlListing{
        private List<PostModel> list;
        private String after;
        private String before;

        public void init_process(final TopPostIterator iterator, final DBAdapter db){
            //db.upgrade();
            new GetTopPostsTask(null,0) {
                @Override
                protected void onPostExecute(Listing input) {
                    list= input.getPostModelList();// java.lang.NullPointerException:
                    after=input.getAfter();
                    before=input.getBefore();

                    if(list.size()!=0) {
                        db.upgrade();
                        new DbSaveTask(list) {
                            @Override
                            protected void onPostExecute(Void aVoid) {
                                super.onPostExecute(aVoid);
                                loadingDB = true;
                                //Cargo desde la base de datos la lista
                                new DbLoadTask() {
                                    @Override
                                    protected void onPostExecute(List<PostModel> l) {
                                        super.onPostExecute(l);
                                        iterator.nextPosts(l, db);
                                        list.clear();
                                    }
                                }.execute(db);
                            }
                        }.execute(db);
                    }
                }
            }.execute("https://www.reddit.com/top/.json?limit=50");
        }

        public void control(final TopPostIterator iterator, int offset, final DBAdapter db){
            waiting=true;
            if(after!=null){
                new GetTopPostsTask(after,offset) {//pasarle after para conectarse
                    @Override
                    protected void onPostExecute(Listing input) {
                        list=input.getPostModelList();
                        after=input.getAfter();
                        before=input.getBefore();
                        if(list!=null) {
                            new DbSaveTask(list){
                                @Override
                                protected void onPostExecute(Void aVoid) {
                                    super.onPostExecute(aVoid);
                                    new DbLoadTask() {
                                        @Override
                                        protected void onPostExecute(List<PostModel> l) {
                                            super.onPostExecute(l);
                                            iterator.nextPosts(l, db);
                                            waiting =false;
                                        }
                                    }.execute(db);
                                }
                            }.execute(db);
                        }
                    }
                }.execute("https://www.reddit.com/top/.json?limit=50");
            }
        }
    }


    private boolean isConnected(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
            dialogBuilder.setMessage("No Internet connection!");
            dialogBuilder.setCancelable(true).setTitle("Alert");
            dialogBuilder.create().show();
            return false;
        }
        return true;
    }
}
