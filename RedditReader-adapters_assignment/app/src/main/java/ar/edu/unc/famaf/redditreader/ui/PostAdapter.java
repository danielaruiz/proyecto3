package ar.edu.unc.famaf.redditreader.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ar.edu.unc.famaf.redditreader.R;
import ar.edu.unc.famaf.redditreader.backend.DBAdapter;
import ar.edu.unc.famaf.redditreader.backend.DbSaveTask;
import ar.edu.unc.famaf.redditreader.backend.RedditDBHelper;
import ar.edu.unc.famaf.redditreader.model.PostModel;

/**
 * Created by dvr on 07/10/16.
 */

public class PostAdapter extends ArrayAdapter {
    private Context context;
    private int layoutResourceId;
    private List<PostModel> mListPostModel;
    private DBAdapter db;

    public PostAdapter(Context context, int resource, List<PostModel> list, DBAdapter db) {
        super(context, resource, list);
        mListPostModel = list;
        this.context = context;
        this.layoutResourceId = resource;
        this.db = db;

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
        final PostModelHolder holder;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new PostModelHolder();
            holder.mAuthor = (TextView) row.findViewById(R.id.author);
            holder.mCreated = (TextView) row.findViewById(R.id.created);
            holder.mSubreddit = (TextView) row.findViewById(R.id.subreddit);
            holder.mTitle = (TextView) row.findViewById(R.id.title);
            holder.icon = (ImageView) row.findViewById(R.id.imageView);
            holder.comments = (TextView) row.findViewById(R.id.comment);
            holder.progressBar = (ProgressBar) row.findViewById(R.id.progressBar);
            row.setTag(holder);

        } else {
            holder = (PostModelHolder) row.getTag();
        }
        final PostModel model = mListPostModel.get(position);

        holder.mTitle.setText(model.getTitle());
        holder.mSubreddit.setText(model.getSubreddit());
        holder.mCreated.setText(setTime(String.valueOf(model.getCreated())));
        holder.mAuthor.setText(model.getAuthor());
        holder.comments.setText(String.valueOf(model.getComments()));
        if (model.getIcon().length > 0) {
            System.out.println("icon postmodel no vacio" + position);
            holder.icon.setImageBitmap(model.getImage(model.getIcon()));
            holder.progressBar.setVisibility(View.GONE);
            return row;

        }
        if (model.getUrl() != null) {
            DownloadImageTask downloadImageTask = new DownloadImageTask(holder, model);
            String url = model.getUrl();
            URL[] urlArray = new URL[1];
            try {
                urlArray[0] = new URL(url);
                downloadImageTask.execute(urlArray);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return row;
    }


    private String setTime(String time) {
        /*crear hora*/
        String timestamp = String.valueOf(time);
        Date createdOn = new Date(Long.parseLong(timestamp));
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
        String formattedDate = sdf.format(createdOn);

        return String.valueOf(formattedDate);

    }

    private class PostModelHolder {
        TextView mTitle;
        TextView mSubreddit;
        TextView mCreated;
        TextView mAuthor;
        ImageView icon;
        TextView comments;
        ProgressBar progressBar;
    }

    @Override
    public boolean isEmpty() {
        return mListPostModel.isEmpty();
    }


    private class DownloadImageTask extends AsyncTask<URL, Integer, Bitmap> {
        PostModelHolder holder = null;
        PostModel model;


        public DownloadImageTask(PostModelHolder holder, PostModel model) {
            this.holder = holder;
            this.model = model;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            holder.progressBar.setVisibility(View.VISIBLE);
        }

        protected Bitmap doInBackground(URL... urls) {
            URL url = urls[0];
            System.out.println(url);
            Bitmap bitmap = null;
            InputStream is;
            HttpURLConnection connection;
            try {
                connection = (HttpURLConnection) url.openConnection();
                is = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(is, null, null);
                if(bitmap==null){
                    bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.error);
                }
            } catch (IOException e) {
                e.printStackTrace();
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.error);
            }
            byte[] image = model.getBytes(bitmap);
            model.setIcon(image);
            db.updateimage(model);
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            System.out.println("onPostExecute");
            if (result != null) {
                holder.icon.setImageBitmap(result);
            }
            holder.progressBar.setVisibility(View.GONE);
        }
    }
}
