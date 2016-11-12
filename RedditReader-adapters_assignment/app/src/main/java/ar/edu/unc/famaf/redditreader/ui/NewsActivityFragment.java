package ar.edu.unc.famaf.redditreader.ui;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ListView;
import java.util.List;

import ar.edu.unc.famaf.redditreader.R;
import ar.edu.unc.famaf.redditreader.backend.Backend;
import ar.edu.unc.famaf.redditreader.backend.DBAdapter;
import ar.edu.unc.famaf.redditreader.backend.TopPostIterator;
import ar.edu.unc.famaf.redditreader.model.PostModel;


/**
 * A placeholder fragment containing a simple view.
 */
public class NewsActivityFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View context = inflater.inflate(R.layout.fragment_news, container, false);

        Backend backend = Backend.getInstance();
        backend.getTopPosts(getContext(), new TopPostIterator() {
            @Override
            public void nextPosts(List<PostModel> lst, DBAdapter db) {
                final PostAdapter adapter = new PostAdapter(getActivity(), R.layout.listview_row, lst, db);
                final ListView lv = (ListView) getView().findViewById(R.id.list);
                lv.setAdapter(adapter);

            }
        });
        return  context;
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