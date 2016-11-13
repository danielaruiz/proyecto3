package ar.edu.unc.famaf.redditreader.ui;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ar.edu.unc.famaf.redditreader.R;
import ar.edu.unc.famaf.redditreader.backend.Backend;
import ar.edu.unc.famaf.redditreader.backend.DBAdapter;
import ar.edu.unc.famaf.redditreader.backend.EndlessScrollListener;
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
        final List<PostModel> list= new ArrayList<>();
        final Backend backend = Backend.getInstance();
        backend.getTopPosts(0,getContext(), new TopPostIterator() {
            @Override
            public void nextPosts(List<PostModel> lst, DBAdapter db) {
                list.addAll(lst);
                final PostAdapter adapter = new PostAdapter(getActivity(), R.layout.listview_row, list, db);
                final ListView lv = (ListView) getView().findViewById(R.id.list);
                lv.setAdapter(adapter);
                lv.setOnScrollListener(new EndlessScrollListener() {
                    @Override
                    public boolean onLoadMore(int page, int totalItemsCount) {
                        System.out.println("on load more");
                        // Triggered only when new data needs to be appended to the list
                        // Add whatever code is needed to append new items to your AdapterView
                        //loadNextDataFromApi(page);
                        backend.getTopPosts(totalItemsCount, getContext(), new TopPostIterator() {
                            @Override
                            public void nextPosts(List<PostModel> lst, DBAdapter db) {
                                //list.clear();
                                list.addAll(lst);
                                adapter.notifyDataSetChanged();

                            }
                        });

                        // or loadNextDataFromApi(totalItemsCount);
                        return true; // ONLY if more data is actually being loaded; false otherwise.
                    }
                });


            }
        });

        return  context;
    }
    // Append the next page of data into the adapter
    // This method probably sends out a network request and appends new data items to your adapter.
    public void loadNextDataFromApi(int offset) {

        // Send an API request to retrieve appropriate paginated data
        //  --> Send the request including an offset value (i.e `page`) as a query parameter.
        //  --> Deserialize and construct new model objects from the API response
        //  --> Append the new data objects to the existing set of items inside the array of items
        //  --> Notify the adapter of the new items made with `notifyDataSetChanged()`
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