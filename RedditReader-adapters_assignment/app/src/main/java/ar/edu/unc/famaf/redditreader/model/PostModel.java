package ar.edu.unc.famaf.redditreader.model;


import android.content.ContentValues;

import ar.edu.unc.famaf.redditreader.backend.RedditDb;

public class PostModel {

    private String mTitle;/*titulo*/
    private String mSubreddit;/*csubreddit*/
    private int mCreated;/*creado fecha*/
    private String mAuthor;
    private int icon;
    private String url;
    private int comments;

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

    public int getIcon(){return icon;}

    public void setIcon(int icon){this.icon=icon;}

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

    public ContentValues toContentValues(){
        ContentValues values= new ContentValues();
        //values.put(RedditDb.RedditEntry.ID, id);
        values.put(RedditDb.RedditEntry.AUTHOR, mAuthor);
        values.put(RedditDb.RedditEntry.SUBREDDIT, mSubreddit);
        values.put(RedditDb.RedditEntry.CREATED, mCreated);
        values.put(RedditDb.RedditEntry.TITLE, mTitle);
        values.put(RedditDb.RedditEntry.COMMENTS,comments);
        values.put(RedditDb.RedditEntry.URL, url);
        //values.put(RedditDb.RedditEntry.ICON, String.valueOf(icon));
        return  values;
    }
}
