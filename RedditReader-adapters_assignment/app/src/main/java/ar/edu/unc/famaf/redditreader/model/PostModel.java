package ar.edu.unc.famaf.redditreader.model;


import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

import ar.edu.unc.famaf.redditreader.backend.RedditDb;

public class PostModel {
    private Integer id;
    private String mTitle;/*titulo*/
    private String mSubreddit;/*csubreddit*/
    private int mCreated;/*creado fecha*/
    private String mAuthor;
    private byte[] icon= new byte[0];
    private String url;
    private int comments;
    private boolean download=false;

    public boolean isDownload() {
        return download;
    }

    public void setDownload(boolean download) {
        this.download = download;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public byte[] getIcon(){return icon;}

    public void setIcon(byte[] icon2){
        this.icon= new byte[icon2.length];
        System.arraycopy(icon2, 0, this.icon,0,icon2.length);
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getSubreddit() {
        return mSubreddit;
    }

    public void setSubreddit(String subreddit) {
        this.mSubreddit = subreddit;
    }

    public int getCreated() {
        return mCreated;
    }

    public void setCreated(int created) {
        this.mCreated = created;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        this.mAuthor = author;
    }

    public static byte[] getBytes(Bitmap bitmap)
    {
        byte[] image = new byte[0];
        try {
            ByteArrayOutputStream stream=new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG,0, stream);
            image= stream.toByteArray();
        }catch (OutOfMemoryError e){
            e.printStackTrace();
        }catch ( NullPointerException e){
            e.printStackTrace();
        }

        return image;
    }
    public static Bitmap getImage(byte[] image){
        Bitmap b=null;
        try{
            b=BitmapFactory.decodeByteArray(image, 0, image.length);
        }catch (OutOfMemoryError e){
            e.printStackTrace();
        }
        return b;
    }

    public ContentValues toContentValues(){
        ContentValues values= new ContentValues();
        //este valor se setea una vez que se inserta en la base de datos
        //values.put(RedditDb.RedditEntry.ID, id);
        values.put(RedditDb.RedditEntry.AUTHOR, mAuthor);
        values.put(RedditDb.RedditEntry.SUBREDDIT, mSubreddit);
        values.put(RedditDb.RedditEntry.CREATED, mCreated);
        values.put(RedditDb.RedditEntry.TITLE, mTitle);
        values.put(RedditDb.RedditEntry.COMMENTS,comments);
        values.put(RedditDb.RedditEntry.URL, url);
        if (this.icon.length >0 ){
            values.put(RedditDb.RedditEntry.ICON, icon);
        }
        return  values;
    }
}
