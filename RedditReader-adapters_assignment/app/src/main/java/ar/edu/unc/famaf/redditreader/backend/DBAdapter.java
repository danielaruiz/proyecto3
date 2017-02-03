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



public class DBAdapter {
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;
    private  int totalItemsCount;

    public DBAdapter(Context ctx, int totalItemsCount) {
        this.DBHelper = new DatabaseHelper(ctx);
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
            try {
                db.execSQL("CREATE TABLE " + RedditDb.RedditEntry.TABLE_NAME + " ("
                        + RedditDb.RedditEntry.ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + RedditDb.RedditEntry.AUTHOR + " TEXT,"
                        + RedditDb.RedditEntry.TITLE + " TEXT,"
                        + RedditDb.RedditEntry.SUBREDDIT + " TEXT,"
                        + RedditDb.RedditEntry.COMMENTS + " TEXT,"
                        + RedditDb.RedditEntry.SCORE + " TEXT,"
                        + RedditDb.RedditEntry.CREATED + " INTEGER,"
                        + RedditDb.RedditEntry.URL + " TEXT,"
                        + RedditDb.RedditEntry.THUMBNAIL + " TEXT,"
                        + RedditDb.RedditEntry.ICON + " BLOB,"
                        + RedditDb.RedditEntry.NAME + " TEXT,"
//                        + RedditDb.RedditEntry.CLICKUP+ " INTEGER,"
//                        + RedditDb.RedditEntry.CLICDOWN + " INTEGER,"

                        + "UNIQUE (" + RedditDb.RedditEntry.ID + "))");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + RedditDb.RedditEntry.TABLE_NAME);
            onCreate(db);
        }
    }

    public DBAdapter open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        //Close any open database object.
        DBHelper.close();
    }

    public void upgrade(){
        DBHelper.onUpgrade(db,1,2);
    }

    public DBAdapter savePostModel(List<PostModel> list) {
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
    public  void updateScore(PostModel postModel){
        if(!db.isOpen()){open();}
        ContentValues values = new ContentValues();
        values.put(RedditDb.RedditEntry.SCORE, postModel.getScore());
        db.update(RedditDb.RedditEntry.TABLE_NAME, values, RedditDb.RedditEntry.ID + "=" + postModel.getId(),null);
        close();
    }

//    public  void updateClicks(PostModel postModel){
//        if(!db.isOpen()){open();}
//        ContentValues values = new ContentValues();
//        values.put(RedditDb.RedditEntry.CLICKUP, postModel.getClickup());
//        values.put(RedditDb.RedditEntry.CLICDOWN, postModel.getClickdown());
//        db.update(RedditDb.RedditEntry.TABLE_NAME, values, RedditDb.RedditEntry.ID + "=" + postModel.getId(),null);
//        close();
//    }


    public  void updateimage(PostModel postModel) {
        if(!db.isOpen()){open();}
        ContentValues values = new ContentValues();
        values.put(RedditDb.RedditEntry.ICON, postModel.getIcon());
        db.update(RedditDb.RedditEntry.TABLE_NAME, values, RedditDb.RedditEntry.ID + "=" + postModel.getId(),null);
        close();
    }

    public List<PostModel> getAllDb(){
        List<PostModel> list = new ArrayList<PostModel>();
        int offset=totalItemsCount;
        String selectQuery2 = "SELECT  * FROM " + RedditDb.RedditEntry.TABLE_NAME + " LIMIT 5 OFFSET "+ String.valueOf(offset);
        try {
            Cursor cursor = db.rawQuery(selectQuery2, null);
            if(cursor.moveToFirst()) {
                do {
                    PostModel postModel = new PostModel();
                    postModel.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(RedditDb.RedditEntry.ID))));
                    postModel.setAuthor(cursor.getString(cursor.getColumnIndex(RedditDb.RedditEntry.AUTHOR)));
                    postModel.setTitle(cursor.getString(cursor.getColumnIndex(RedditDb.RedditEntry.TITLE)));
                    postModel.setSubreddit(cursor.getString(cursor.getColumnIndex(RedditDb.RedditEntry.SUBREDDIT)));
                    postModel.setComments(Integer.parseInt(cursor.getString(cursor.getColumnIndex(RedditDb.RedditEntry.COMMENTS))));
                    postModel.setScore(Integer.parseInt(cursor.getString(cursor.getColumnIndex(RedditDb.RedditEntry.SCORE))));
                    postModel.setCreated(cursor.getLong(cursor.getColumnIndex(RedditDb.RedditEntry.CREATED)));
                    postModel.setUrl(cursor.getString(cursor.getColumnIndex(RedditDb.RedditEntry.URL)));
                    postModel.setThumbnail(cursor.getString(cursor.getColumnIndex(RedditDb.RedditEntry.THUMBNAIL)));
                    byte[] image = cursor.getBlob(cursor.getColumnIndex(RedditDb.RedditEntry.ICON));
                    if (image != null) {
                        postModel.setIcon(image);
                    }
                    postModel.setName(cursor.getString(cursor.getColumnIndex(RedditDb.RedditEntry.NAME)));
//                    postModel.setClickup(Integer.parseInt(cursor.getString(cursor.getColumnIndex(RedditDb.RedditEntry.CLICKUP))));
//                    postModel.setClickdown(Integer.parseInt(cursor.getString(cursor.getColumnIndex(RedditDb.RedditEntry.CLICDOWN))));

                    list.add(postModel);
                } while (cursor.moveToNext());
                cursor.close();
            }
        }catch (IllegalStateException e){
            e.printStackTrace();
        }
        return list;
    }

}
