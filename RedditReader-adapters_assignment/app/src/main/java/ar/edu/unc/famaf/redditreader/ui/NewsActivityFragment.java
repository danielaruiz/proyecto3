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
public class NewsActivityFragment extends Fragment{
    OnPostItemSelectedListener listener;
    PostAdapter adapter;
    public static DBAdapter db;
    List<PostModel> list;

    public NewsActivityFragment () {
        // Required empty public constructor
    }

    public static NewsActivityFragment newInstance() {
        
        Bundle args = new Bundle();
        
        NewsActivityFragment fragment = new NewsActivityFragment();
        fragment.setArguments(args);
        return fragment;
    }

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
        setRetainInstance(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View context = inflater.inflate(R.layout.fragment_news, container, false);
        list= new ArrayList<>();
        final Backend backend = Backend.getInstance();
        final boolean[] mBusy = new boolean[1];
        final ListView lv = (ListView) context.findViewById(R.id.list);
        backend.getTopPosts(0,getContext(), new TopPostIterator() {
            @Override
            public void nextPosts(List<PostModel> lst, DBAdapter dbase) {
                db=dbase;
                adapter = new PostAdapter(getActivity(), R.layout.listview_row, list, dbase, mBusy[0]);
                lv.setAdapter(adapter);

                if(lst.size()!=0){
                    list.addAll(lst);
                    adapter.notifyDataSetChanged();
                }else{
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
                    listener.onPostItemPicked(postModel, position);
                }
            }
        });

        lv.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
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
                System.out.println(scrollState);

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

    public  void  changeList(PostModel postModel, int position){
        list.set(position, postModel);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onPause() {
        super.onPause();
        //guardar info
        //Bundle args = new Bundle();
        //setArguments(args);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        listener=null;
    }
}
