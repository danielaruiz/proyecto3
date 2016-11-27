package ar.edu.unc.famaf.redditreader.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;

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


import ar.edu.unc.famaf.redditreader.model.PostModel;


public class PostAdapter extends ArrayAdapter {
    private Context context;
    private int layoutResourceId;
    private List<PostModel> mListPostModel;
    private DBAdapter db;
    private boolean mbusy;

    public PostAdapter(Context context, int resource, List<PostModel> list, DBAdapter db, boolean mBusy) {
        super(context, resource, list);
        mListPostModel = list;
        this.context = context;
        this.layoutResourceId = resource;
        this.db = db;
        this.mbusy=mBusy;

    }

    @Nullable
    @Override
    public PostModel getItem(int position) {
        return mListPostModel.get(position);
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        final Bitmap bitmapdefault = BitmapFactory.decodeResource(context.getResources(), R.drawable.error);
        final PostModelHolder holder;

        if (row == null || !(row.getTag() instanceof PostModelHolder)) {
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
        }else{
            holder = (PostModelHolder) row.getTag();
        }

        PostModel model = getItem(position);
        holder.mTitle.setText(model.getTitle());
        holder.mSubreddit.setText(model.getSubreddit());
        holder.mCreated.setText(setTime(model.getCreated()));
        holder.mAuthor.setText(model.getAuthor());
        holder.comments.setText(String.valueOf(model.getComments()));
        holder.position= position;

        if (model.getIcon().length > 0) {
            holder.icon.setImageBitmap(getImage(model.getIcon()));
            holder.progressBar.setVisibility(View.GONE);
            return row;
        }
        //caso contrario descargamos imagen si existe url
        if (model.getThumbnail() != null && !mbusy && !model.isDownload()) {
            try {
                URL urlArray = new URL(model.getThumbnail());
                //Descargando imagen
                new DownloadImageTask(model){
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        holder.progressBar.setVisibility(View.VISIBLE);
                        model.setDownload(true);
                    }
                    @Override
                    protected void onPostExecute(Bitmap result) {
                        super.onPostExecute(result);
                        if(holder.position==position ){
                            if(result== null){
                                result= bitmapdefault;
                            }
                            holder.icon.setImageBitmap(result);
                            holder.progressBar.setVisibility(View.GONE);
                        }
                    }
                }.execute(urlArray);

            } catch (MalformedURLException e) {
                e.printStackTrace();
                holder.icon.setImageBitmap(bitmapdefault);
                holder.progressBar.setVisibility(View.GONE);
            }

        } else if (model.getThumbnail()==null){
            holder.icon.setImageBitmap(bitmapdefault);
            holder.progressBar.setVisibility(View.GONE);
        }
        return row;
    }

    private class PostModelHolder {
        TextView mTitle;
        TextView mSubreddit;
        TextView mCreated;
        TextView mAuthor;
        ImageView icon;
        TextView comments;
        ProgressBar progressBar;
        int position;
    }

    private class DownloadImageTask extends AsyncTask<URL, Integer, Bitmap> {
        PostModel model;

        private DownloadImageTask(PostModel model) {
            this.model = model;
        }
        protected Bitmap doInBackground(URL... urls) {
            URL url = urls[0];
            Bitmap bitmap = null;
            try {
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream is = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(is, null, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(bitmap!=null){
                model.setIcon(getBytes(bitmap));
                db.updateimage(model);
            }
            return bitmap;
        }
    }
    //Funciones auxiliares
    private static byte[] getBytes(Bitmap bitmap) {
        byte[] image = new byte[0];
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 30, stream);
            image = stream.toByteArray();
        }catch (OutOfMemoryError e){
            e.printStackTrace();
        }
        return image;
    }

    private static Bitmap getImage(byte[] image){
        Bitmap b=null;
        try{
            b=BitmapFactory.decodeByteArray(image, 0, image.length);
        }catch (OutOfMemoryError e){
            e.printStackTrace();
        }
        return b;
    }
    private String setTime(long time) {
        int one_hr=1000*60*60;
        Date now =new Date();
        Date before = new Date(time);
        long diff = now.getTime()- before.getTime();
        long hs= diff/one_hr;
        SimpleDateFormat sdf = new SimpleDateFormat("K");
        String formatt = sdf.format(new Date(hs));
        return formatt + " h";
    }

}