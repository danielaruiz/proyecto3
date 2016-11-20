package ar.edu.unc.famaf.redditreader.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ar.edu.unc.famaf.redditreader.R;

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
            String s1=getIntent().getExtras().getString("titulo");
            String s2=getIntent().getExtras().getString("autor");
            String s3=getIntent().getExtras().getString("creado");
            String s4=getIntent().getExtras().getString("subreddit");
            byte[] s5=getIntent().getByteArrayExtra("preview");
            String s6=getIntent().getExtras().getString("url");

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
