package ar.edu.unc.famaf.redditreader.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

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
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


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
        int original_score =0;
        int clicks = 0;
        final PostModelHolder holder;
        final Bitmap bitmapdefault = BitmapFactory.decodeResource(context.getResources(), R.drawable.error);

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
            holder.score = (TextView) row.findViewById(R.id.scoredetail);
            holder.up = (ImageButton) row.findViewById(R.id.up);
            holder.down = (ImageButton) row.findViewById(R.id.down);
            row.setTag(holder);
        }else{
            holder = (PostModelHolder) row.getTag();
        }

        final PostModel model = getItem(position);
        original_score= model.getScore();
        holder.mTitle.setText(model.getTitle());
        holder.mSubreddit.setText(model.getSubreddit());
        holder.mCreated.setText(setTime(model.getCreated()));
        holder.mAuthor.setText(model.getAuthor());
        holder.comments.setText(String.valueOf(model.getComments()));
        holder.score.setText(String.valueOf(model.getScore()));
        holder.down.setBackgroundColor(Color.TRANSPARENT);
        holder.up.setBackgroundColor(Color.TRANSPARENT);
        holder.position= position;
        //parte nueva
        if(NewsActivity.LOGGIN && model.getClickup() == 1){
            holder.up.setBackgroundColor(Color.DKGRAY);
            original_score=model.getScore()-1;
        }
        if(NewsActivity.LOGGIN && model.getClickdown() == 1){
            holder.down.setBackgroundColor(Color.DKGRAY);
            original_score=model.getScore()+1;
        }
        final Buttons button = new Buttons(model,holder, db, context, clicks, original_score);
        holder.up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(NewsActivity.LOGGIN && !NewsActivity.ACTIVE_USER){
                    //USUARIO NO ACTIVO, SIN AUTORIZACION
                    Toast.makeText(context, "Unauthorized. Loggin again!", Toast.LENGTH_SHORT).show();
                    System.out.println("USUARIO NO ACTIVO, SIN AUTORIZACION");
                    ((Activity) context).finish();
                }else{
                    button.Bcontrol("1");
                }

            }
        });
        holder.down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(NewsActivity.LOGGIN && !NewsActivity.ACTIVE_USER){
                    //USUARIO NO ACTIVO, SIN AUTORIZACION
                    Toast.makeText(context, "Unauthorized. Loggin again!", Toast.LENGTH_SHORT).show();
                    System.out.println("USUARIO NO ACTIVO, SIN AUTORIZACION");
                    ((Activity) context).finish();
                }else{
                    button.Bcontrol("-1");
                }

            }
        });


        //fin parte nueva

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


