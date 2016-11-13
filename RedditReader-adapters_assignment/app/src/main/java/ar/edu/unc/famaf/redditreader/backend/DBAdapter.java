package ar.edu.unc.famaf.redditreader.backend;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import ar.edu.unc.famaf.redditreader.model.PostModel;

/**
 * Created by dvr on 11/11/16.
 */

public class DBAdapter {
    private final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;
    private int offset;
    private int limitdb;
    private  int totalItemsCount;

    public DBAdapter(Context ctx, int totalItemsCount) {
        this.context = ctx;
        this.DBHelper = new DatabaseHelper(context);
        this.totalItemsCount= totalItemsCount;

    }

    private class DatabaseHelper extends SQLiteOpenHelper {
        private static final int DATABASE_VERSION = 1;
        private static final String DATABASE_NAME = "reddit.db";

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            System.out.println("se creo db");
            try {
                db.execSQL("CREATE TABLE " + RedditDb.RedditEntry.TABLE_NAME + " ("
                        + RedditDb.RedditEntry.ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + RedditDb.RedditEntry.AUTHOR + " TEXT,"
                        + RedditDb.RedditEntry.TITLE + " TEXT,"
                        + RedditDb.RedditEntry.SUBREDDIT + " TEXT,"
                        + RedditDb.RedditEntry.COMMENTS + " TEXT,"
                        + RedditDb.RedditEntry.CREATED + " TEXT,"
                        + RedditDb.RedditEntry.URL + " TEXT,"
                        + RedditDb.RedditEntry.ICON + " BLOB,"
                        + "UNIQUE (" + RedditDb.RedditEntry.ID + "))");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            System.out.println("upgrade table");
            db.execSQL("DROP TABLE IF EXISTS " + RedditDb.RedditEntry.TABLE_NAME);
            onCreate(db);
        }
    }

    public DBAdapter open() throws SQLException {
        System.out.println("open db");
        db = DBHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        //Close any open database object.
        DBHelper.close();
    }


    public DBAdapter savePostModel(List<PostModel> list) {
        DBHelper.onUpgrade(db,1,2);
        db = DBHelper.getWritableDatabase();
        for(int i=0; i<list.size(); i++){
            db.insert(RedditDb.RedditEntry.TABLE_NAME, null,  list.get(i).toContentValues());
            /*Guardo el id en el postmodel*/
            Cursor c = db.query(RedditDb.RedditEntry.TABLE_NAME, null, RedditDb.RedditEntry.AUTHOR + " LIKE ?",
                    new String[]{list.get(i).getAuthor()}, null, null, null);
            if (c != null){
                c.moveToFirst();
                list.get(i).setId(c.getInt(c.getColumnIndex(RedditDb.RedditEntry.ID)));
                c.close();
            }
        }
        return this;
    }

    public  void updateimage(PostModel postModel) {
        if(!db.isOpen()){open();}
        System.out.println("UpdateImage");
        ContentValues values = new ContentValues();
//        values.put(RedditDb.RedditEntry.ID, postModel.getId());
//        values.put(RedditDb.RedditEntry.AUTHOR, postModel.getAuthor());
//        values.put(RedditDb.RedditEntry.SUBREDDIT, postModel.getSubreddit());
//        values.put(RedditDb.RedditEntry.CREATED, postModel.getCreated());
//        values.put(RedditDb.RedditEntry.TITLE, postModel.getTitle());
//        values.put(RedditDb.RedditEntry.COMMENTS, postModel.getComments());
//        values.put(RedditDb.RedditEntry.URL, postModel.getUrl());
        values.put(RedditDb.RedditEntry.ICON, postModel.getIcon());
        db.update(RedditDb.RedditEntry.TABLE_NAME, values, RedditDb.RedditEntry.ID + "=" + postModel.getId(),null);
        close();
    }

    public List<PostModel> getAllDb(){
        System.out.println("Recuperando datos de la base");
        List<PostModel> list = new ArrayList<PostModel>();
        if(totalItemsCount==50){
            System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxx");
            offset=0;
            limitdb=0;
            return list;
        }
        offset=totalItemsCount;
//        String selectQuery1 = "SELECT  * FROM " + RedditDb.RedditEntry.TABLE_NAME + " WHERE id =?";
        String selectQuery= "SELECT  * FROM " + RedditDb.RedditEntry.TABLE_NAME;
        System.out.println("Recuperando datos de la base offset................................................" + this.offset);
        String selectQuery2 = "SELECT  * FROM " + RedditDb.RedditEntry.TABLE_NAME + " LIMIT 5 OFFSET "+ String.valueOf(this.offset);

        //for(int i=1; i<5; i++){
//        String[] index=new String[]{String.valueOf(10)};
        Cursor cursor = db.rawQuery(selectQuery2, null);
        if(cursor.moveToFirst()){
            do {
                PostModel postModel = new PostModel();
                System.out.println(cursor.getString(cursor.getColumnIndex(RedditDb.RedditEntry.ID)));
                postModel.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(RedditDb.RedditEntry.ID))));
                postModel.setAuthor(cursor.getString(cursor.getColumnIndex(RedditDb.RedditEntry.AUTHOR)));
                postModel.setTitle(cursor.getString(cursor.getColumnIndex(RedditDb.RedditEntry.TITLE)));
                postModel.setSubreddit(cursor.getString(cursor.getColumnIndex(RedditDb.RedditEntry.SUBREDDIT)));
                postModel.setComments(Integer.parseInt(cursor.getString(cursor.getColumnIndex(RedditDb.RedditEntry.COMMENTS))));
                postModel.setCreated(Integer.parseInt(cursor.getString(cursor.getColumnIndex(RedditDb.RedditEntry.CREATED))));
                postModel.setUrl(cursor.getString(cursor.getColumnIndex(RedditDb.RedditEntry.URL)));
                byte[] image = cursor.getBlob(cursor.getColumnIndex(RedditDb.RedditEntry.ICON));
                if (image != null) {
                    postModel.setIcon(image);
                }
                list.add(postModel);
            } while (cursor.moveToNext());
            cursor.close();
        }
        //this.offset = this.offset+ 5;
        System.out.println("despues offset es "+ this.offset);
        //this.limitdb= this.limitdb+5;
        return list;
    }

}
