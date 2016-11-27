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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            PostModel post = (PostModel) getIntent().getSerializableExtra("post");

            String s1= post.getTitle();
            String s2=post.getAuthor();
            String s3=String.valueOf(post.getCreated());
            String s4=post.getSubreddit();
            byte[] s5=post.getIcon();
            String s6=post.getUrl();

            NewsDetailActivityFragment fragment =
                    NewsDetailActivityFragment.newInstance(s1, s2, s3, s4, s5, s6);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragment).commit();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Context context= getApplicationContext();
        Intent intent = new Intent(Intent.ACTION_VIEW, uri, context, WebViewActivity.class);
        startActivityForResult(intent, REQUEST);

    }

}
