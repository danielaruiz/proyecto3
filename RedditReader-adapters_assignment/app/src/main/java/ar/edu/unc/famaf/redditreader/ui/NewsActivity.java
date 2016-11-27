package ar.edu.unc.famaf.redditreader.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import ar.edu.unc.famaf.redditreader.R;
import ar.edu.unc.famaf.redditreader.model.PostModel;


public class NewsActivity extends AppCompatActivity implements OnPostItemSelectedListener{
    static final int REQUEST =0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_news, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_sign_in) {
            TextView textView = (TextView) findViewById(R.id.loginStatusTextView);
            textView.setText("");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPostItemPicked(PostModel post) {
        Context context= getApplicationContext();
        if(post!=null) {
            Intent intent = new Intent(context, NewsDetailActivity.class);
            intent.putExtra("post",post);
            startActivityForResult(intent, REQUEST);
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
    }

}
