package ar.edu.unc.famaf.redditreader.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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
//    PostModel postModel=null;
    OnPostItemSelectedListener listener;
    PostAdapter adapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity a=getActivity();
        try {
            listener  = (OnPostItemSelectedListener) a;
        } catch (ClassCastException e) {
           //throw new ClassCastException(context.toString()+ "must implement OnPostItemSelectedListener");
        }
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu (true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View context = inflater.inflate(R.layout.fragment_news, container, false);
        final List<PostModel> list= new ArrayList<>();
        final Backend backend = Backend.getInstance();
        final boolean[] mBusy = new boolean[1];
        final ListView lv = (ListView) context.findViewById(R.id.list);

        backend.getTopPosts(0,getContext(), new TopPostIterator() {
            @Override
            public void nextPosts(List<PostModel> lst, DBAdapter db) {
                adapter = new PostAdapter(getActivity(), R.layout.listview_row, list, db, mBusy[0]);
                lv.setAdapter(adapter);

                if(lst.size()!=0){
                    list.addAll(lst);
                    adapter.notifyDataSetChanged();
                }else{
                    System.out.println("LISTA VACIA");
                    Toast.makeText(getActivity(), "Error loading list. Try again",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PostModel postModel = list.get(position);
                if (postModel!=null){
                    listener.onPostItemPicked(postModel);
                }
            }
        });

        lv.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                System.out.println("page"+page);
                System.out.println(totalItemsCount);
                backend.getTopPosts(totalItemsCount, getContext(), new TopPostIterator() {
                    @Override
                    public void nextPosts(List<PostModel> lst, DBAdapter db) {
                        list.addAll(lst);
                        adapter.notifyDataSetChanged();
                    }
                });
                return true;
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                super.onScrollStateChanged(view, scrollState);
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        mBusy[0] = false;
                        adapter.notifyDataSetChanged();
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                        mBusy[0] = true;
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                        mBusy[0] = true;
                        break;

                }
            }

        });

        return  context;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        listener=null;
    }

}
