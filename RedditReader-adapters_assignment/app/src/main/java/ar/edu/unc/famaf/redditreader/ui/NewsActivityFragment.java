package ar.edu.unc.famaf.redditreader.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import java.util.List;

import ar.edu.unc.famaf.redditreader.R;
import ar.edu.unc.famaf.redditreader.backend.Backend;
import ar.edu.unc.famaf.redditreader.backend.DbLoadTask;
import ar.edu.unc.famaf.redditreader.backend.DbSaveTask;
import ar.edu.unc.famaf.redditreader.backend.RedditDBHelper;
import ar.edu.unc.famaf.redditreader.backend.TopPostIterator;
import ar.edu.unc.famaf.redditreader.model.PostModel;


/**
 * A placeholder fragment containing a simple view.
 */
public class NewsActivityFragment extends Fragment {
    private boolean[] mBusy = new boolean[1];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View context = inflater.inflate(R.layout.fragment_news, container, false);
        final RedditDBHelper mRedditDb = new RedditDBHelper(getContext());


        if (!isConnected(getContext())){
            //levantar datos de la base de datos
            //new DbLoadTask().execute(mRedditDb);
            new DbLoadTask(){
                @Override
                protected void onPostExecute(List<PostModel> list1) {
                    final PostAdapter adapter = new PostAdapter(getActivity(), R.layout.listview_row, list1, mBusy[0]);
                    final ListView lv = (ListView) getView().findViewById(R.id.list);
                    lv.setAdapter(adapter);
                }
            }.execute(mRedditDb);
            return context;
        }else{
            Backend backend = Backend.getInstance();
            backend.getTopPosts(new TopPostIterator() {
                @Override
                public void nextPosts(List<PostModel> list) {
                    new DbSaveTask(list){
                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            new DbLoadTask(){
                                @Override
                                protected void onPostExecute(List<PostModel> list1) {

                                    final PostAdapter adapter = new PostAdapter(getActivity(), R.layout.listview_row, list1, mBusy[0]);
                                    final ListView lv = (ListView) getView().findViewById(R.id.list);
                                    lv.setAdapter(adapter);
                                }
                            }.execute(mRedditDb);
                        }
                    }.execute(mRedditDb);

//                    final PostAdapter adapter = new PostAdapter(getActivity(), R.layout.listview_row, list1, mBusy[0]);
//                    final ListView lv = (ListView) getView().findViewById(R.id.list);
//                    lv.setAdapter(adapter);

                }
            });

        }

        return context;
    }

    public boolean isConnected(Context context) {
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


//                lv.setOnScrollListener(new AbsListView.OnScrollListener() {
//                    private int currentVisibleitemCount;
//                    private int currentScrollState;
//                    private int currentFirstVisibleItem;
//                    private int totalItem;
//                    private LinearLayout lBelow;
//
//                    @Override
//                    public void onScrollStateChanged(AbsListView view, int scrollState) {
//                        this.currentScrollState = scrollState;
//
//                        switch (scrollState) {
//                            case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
//                                mBusy[0] = false;
//                                adapter.notifyDataSetChanged();
//                                break;
//                            case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
//                                mBusy[0] = true;
//                                break;
//                            case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
//                                mBusy[0] = true;
//                                break;
//
//                        }
//                    }
//
//                    @Override
//                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                        this.currentFirstVisibleItem = firstVisibleItem;
//                        this.currentVisibleitemCount = visibleItemCount;
//                        this.totalItem = totalItemCount;
//
//                    }
//                });