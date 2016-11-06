package ar.edu.unc.famaf.redditreader.backend;

        import android.content.Context;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;

        import java.util.ArrayList;
        import java.util.List;

        import ar.edu.unc.famaf.redditreader.model.PostModel;

/**
 * Created by dvr on 05/11/16.
 */

public class RedditDBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "reddit.db";

    //    public RedditDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
//        super(context, name, factory, version);
//    }
    public RedditDBHelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
}

    @Override
    public void onCreate(SQLiteDatabase db) {
        /*/data/data/<paquete>/databases/<nombre-de-la-bd>.db*/
        /*crear la tabla*/
        System.out.println("se creo db...........................");
        db.execSQL("CREATE TABLE " + RedditDb.RedditEntry.TABLE_NAME + " ("
                + RedditDb.RedditEntry.ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + RedditDb.RedditEntry.AUTHOR + " TEXT,"
                + RedditDb.RedditEntry.TITLE + " TEXT,"
                + RedditDb.RedditEntry.SUBREDDIT + " TEXT,"
                + RedditDb.RedditEntry.COMMENTS + " TEXT,"
                + RedditDb.RedditEntry.CREATED + " TEXT,"
                + RedditDb.RedditEntry.URL + " TEXT"
                + RedditDb.RedditEntry.ICON + " TEXT,"
                + "UNIQUE (" + RedditDb.RedditEntry.ID + "))");

    }


    public long savePostModel(PostModel postModel) {
        System.out.println("save........................");
        SQLiteDatabase db = getWritableDatabase();
        return db.insert(
                RedditDb.RedditEntry.TABLE_NAME, null,  postModel.toContentValues());

    }

    public List<PostModel> getAllDb(){
        System.out.println("getALL....................");
        List<PostModel> list = new ArrayList<PostModel>();
        String selectQuery= "SELECT  * FROM " + RedditDb.RedditEntry.TABLE_NAME;
        SQLiteDatabase db =getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()){
            //cursor.moveToNext();
            do{
                PostModel postModel=new PostModel();
                postModel.setAuthor(cursor.getString(cursor.getColumnIndex(RedditDb.RedditEntry.AUTHOR)));
                postModel.setTitle(cursor.getString(cursor.getColumnIndex(RedditDb.RedditEntry.TITLE)));
                postModel.setSubreddit(cursor.getString(cursor.getColumnIndex(RedditDb.RedditEntry.SUBREDDIT)));
                postModel.setComments(Integer.parseInt(cursor.getString(cursor.getColumnIndex(RedditDb.RedditEntry.COMMENTS))));
                postModel.setCreated(Integer.parseInt(cursor.getString(cursor.getColumnIndex(RedditDb.RedditEntry.CREATED))));
                postModel.setUrl(cursor.getString(cursor.getColumnIndex(RedditDb.RedditEntry.URL)));
                //postModel.setIcon();
                list.add(postModel);
            }while(cursor.moveToNext());
            System.out.println("getALL....................");
            System.out.println(list.size());
        cursor.close();
        }
        return list;
    }

    public void upgrade(){
        SQLiteDatabase db =getWritableDatabase();
        onUpgrade(db,DATABASE_VERSION, DATABASE_VERSION+1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + RedditDb.RedditEntry.TABLE_NAME);
        onCreate(db);

    }
}
