package ar.edu.unc.famaf.redditreader.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.view.menu.ExpandedMenuView;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ar.edu.unc.famaf.redditreader.R;
import ar.edu.unc.famaf.redditreader.backend.Backend;
import ar.edu.unc.famaf.redditreader.backend.GetTopPostsTask;
import ar.edu.unc.famaf.redditreader.model.PostModel;


/**
 * A placeholder fragment containing a simple view.
 */
public class NewsActivityFragment extends Fragment {
    private List<PostModel> posts;
    private ListView lv;
    private boolean[] mBusy = new boolean[1];

    public NewsActivityFragment() {
    }

    @Override
   public void onCreate(@Nullable Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       //setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View context = inflater.inflate(R.layout.fragment_news, container, false);
        if (!isConnected(getContext())){ return context;}

        Backend backend = Backend.getInstance();
        posts= backend.getTopPosts();
        this.lv = (ListView) context.findViewById(R.id.list);
        this.lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int currentVisibleitemCount;
            private int currentScrollState;
            private int currentFirstVisibleItem;
            private int totalItem;
            private LinearLayout lBelow;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                this.currentScrollState = scrollState;

                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        mBusy[0] = false;
                        //adapter.notifyDataSetChanged();
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                        mBusy[0] = true;
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                        mBusy[0] = true;
                        break;

                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                this.currentFirstVisibleItem = firstVisibleItem;
                this.currentVisibleitemCount = visibleItemCount;
                this.totalItem = totalItemCount;

            }
        });
        final PostAdapter adapter = new PostAdapter(context.getContext(), R.layout.listview_row, posts, mBusy[0]);
        lv.setAdapter(adapter);
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
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
       super.onActivityCreated(savedInstanceState);
        //PostAdapter adapter = new PostAdapter(getContext(), R.layout.listview_row,posts, mBusy[0]);
        //this.lv.setAdapter(adapter);
//
   }
}
