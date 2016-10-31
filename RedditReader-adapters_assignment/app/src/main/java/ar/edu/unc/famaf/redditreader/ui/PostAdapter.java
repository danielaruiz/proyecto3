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

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    private boolean mBusy;

    public PostAdapter(Context context, int resource, List<PostModel> list, Boolean mBusy) {
        super(context, resource, list);
        mListPostModel = list;
        this.context = context;
        this.layoutResourceId = resource;
        this.mBusy=mBusy;

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
            holder.comments =(TextView) row.findViewById(R.id.comment);
            row.setTag(holder);

        }else{
            holder = (PostModelHolder) row.getTag();
        }
        System.out.println(String.valueOf(position));
        PostModel model = mListPostModel.get(position);

        holder.mTitle.setText(model.getTitle());
        holder.mSubreddit.setText(model.getSubreddit());

        holder.mCreated.setText (setTime(String.valueOf(model.getCreated())));
        holder.mAuthor.setText(model.getAuthor());
        holder.icon.setImageResource(model.getIcon());
        holder.comments.setText(String.valueOf(model.getComments()));
        if(model.getUrl() !=null) {
            //if(!mBusy){
            DownloadImageTask downloadImageTask = new DownloadImageTask(holder, row);
            String url = model.getUrl();
            URL[] urlArray = new URL[1];
            try {
                urlArray[0] = new URL(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            downloadImageTask.execute(urlArray);
            //}
        }
        return row;
    }


    public String setTime(String time){
        /*crear hora*/
        String timestamp = String.valueOf(time);
        Date createdOn = new Date(Long.parseLong(timestamp));
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
        String formattedDate = sdf.format(createdOn);

        return  String.valueOf(formattedDate);

    }
    //clase holder
    static  class  PostModelHolder{
        TextView mTitle;
        TextView mSubreddit;
        TextView mCreated;
        TextView mAuthor;
        ImageView icon;
        TextView comments;
    }

    @Override
    public boolean isEmpty() {
        return mListPostModel.isEmpty();
    }

    protected class DownloadImageTask extends AsyncTask<URL, Integer, Bitmap> {
        PostModelHolder holder = null;
        View row = null;
        ProgressBar progressBar;
        public DownloadImageTask(PostModelHolder holder, View row) {/*verrrrrrrr row*/
            this.holder = holder;
            this.row = row;
            this.progressBar = (ProgressBar) row.findViewById(R.id.progressBar);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        protected Bitmap doInBackground(URL... urls) {
            URL url = urls[0];
            System.out.println(url);
            Bitmap bitmap = null;
            InputStream is =null;
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                is = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(is, null, null);
            } catch (IOException e) {
                System.out.println("error descarga");
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            System.out.println("onPostExecute");
            if (result!=null) {
                holder.icon.setImageBitmap(result);
            }
            progressBar.setVisibility(View.GONE);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

    }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }
}
