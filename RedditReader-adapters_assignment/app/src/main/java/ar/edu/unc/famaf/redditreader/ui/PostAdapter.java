package ar.edu.unc.famaf.redditreader.ui;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ar.edu.unc.famaf.redditreader.R;
import ar.edu.unc.famaf.redditreader.model.PostModel;

/**
 * Created by dvr on 07/10/16.
 */

public class PostAdapter extends ArrayAdapter{
    private Context context;
    private int layoutResourceId;
    private List<PostModel> mListPostModel;

    public PostAdapter(Context context, int resource, List<PostModel> list) {
        super(context, resource, list);
        mListPostModel = list;
        this.context = context;
        this.layoutResourceId = resource;
    }

    @Override
    public int getCount() {
        return mListPostModel.size();
    }

    @Nullable
    @Override
    public PostModel getItem(int position) {
        return mListPostModel.get(position);
    }

    public int getPosition(PostModel item) {
        return mListPostModel.indexOf(item);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        PostModelHolder holder = null;
        if (row == null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new PostModelHolder();
            holder.mAuthor = (TextView) row.findViewById(R.id.author);
            holder.mCreated = (TextView) row.findViewById(R.id.created);
            holder.mSubreddit =(TextView) row.findViewById(R.id.subreddit);
            holder.mTitle = (TextView) row.findViewById(R.id.title);
            holder.icon = (ImageView) row.findViewById(R.id.imageView);
            row.setTag(holder);

        }else{
            holder = (PostModelHolder) row.getTag();
        }

        PostModel model = mListPostModel.get(position);
        holder.mTitle.setText(model.getTitle());
        holder.mSubreddit.setText(model.getSubreddit());
        holder.mCreated.setText(model.getCreated());
        holder.mAuthor.setText(model.getAuthor());
        holder.icon.setImageResource(model.getIcon());

        return row;
    }
    static  class  PostModelHolder{
        TextView mTitle;
        TextView mSubreddit;
        TextView mCreated;
        TextView mAuthor;
        ImageView icon;
    }
    @Override
    public boolean isEmpty() {
        return mListPostModel.isEmpty();
    }
}
