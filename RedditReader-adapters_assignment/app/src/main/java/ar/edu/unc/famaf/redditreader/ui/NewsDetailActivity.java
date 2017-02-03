package ar.edu.unc.famaf.redditreader.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.Serializable;

import ar.edu.unc.famaf.redditreader.R;
import ar.edu.unc.famaf.redditreader.model.PostModel;

public class NewsDetailActivity extends AppCompatActivity implements NewsDetailActivityFragment.OnFragmentInteractionListener {
    static final int REQUEST =0;
    int position=0;
    NewsDetailActivityFragment fragment;
    PostModel post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            Intent intent = getIntent();
            post = (PostModel) intent.getSerializableExtra("post");
            position = intent.getIntExtra("position", position);
            fragment = NewsDetailActivityFragment.newInstance(post);
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position", position);
        outState.putSerializable("post", post);
        getSupportFragmentManager().putFragment(outState, "fragmento", fragment);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        position= savedInstanceState.getInt("position");
        post =(PostModel) savedInstanceState.getSerializable("post");
        fragment = (NewsDetailActivityFragment) getSupportFragmentManager().getFragment(savedInstanceState, "fragmento");
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onFragmentInteraction(Uri uri, PostModel post) {//vuelve model a activity
        if(uri !=null) {
            Context context= getApplicationContext();
            Intent intent = new Intent(Intent.ACTION_VIEW, uri, context, WebViewActivity.class);
            startActivityForResult(intent, REQUEST);
        }else if(post!=null && NewsActivity.LOGGIN){
            Intent intent = new Intent();
            intent.putExtra("post",post);
            intent.putExtra("position", position);
            setResult(RESULT_OK, intent);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

}
