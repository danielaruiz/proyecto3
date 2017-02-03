package ar.edu.unc.famaf.redditreader.model;


import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

import ar.edu.unc.famaf.redditreader.backend.RedditDb;

public class PostModel implements Serializable {
    private Integer id;
    private String mTitle;/*titulo*/
    private String mSubreddit;/*csubreddit*/
    private long mCreated;/*creado fecha*/
    private String mAuthor;
    private byte[] icon= new byte[0];
    private String thumbnail;
    private String url;
    private int comments;
    private boolean download=false;
    private int score;
    private  String name;
    private int clickup=0;
    private int clickdown=0;

    public int getClickup() {
        return clickup;
    }

    public void setClickup(int click) {
        this.clickup = click;
    }

    public int getClickdown() {
        return clickdown;
    }

    public void setClickdown(int click) {
        this.clickdown = click;
    }
    //    private  String vote="none";
//
//    public String getVote() {
//        return vote;
//    }
//
//    public void setVote(String vote) {
//        this.vote = vote;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

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
        this.icon=icon2;
//        this.icon= new byte[icon2.length];
//        System.arraycopy(icon2, 0, this.icon,0,icon2.length);
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

    public long getCreated() {
        return mCreated;
    }

    public void setCreated(long created) {
        this.mCreated = created;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        this.mAuthor = author;
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
        values.put(RedditDb.RedditEntry.SCORE, score);
        values.put(RedditDb.RedditEntry.URL, url);
        values.put(RedditDb.RedditEntry.THUMBNAIL, thumbnail);
        if (this.icon.length >0 ){
            values.put(RedditDb.RedditEntry.ICON, icon);
        }
        values.put(RedditDb.RedditEntry.NAME,name);
//        values.put(RedditDb.RedditEntry.CLICKUP,clickup);
//        values.put(RedditDb.RedditEntry.CLICDOWN, clickdown);
//        values.put(RedditDb.RedditEntry.VOTE, vote);
        return  values;
    }
}
