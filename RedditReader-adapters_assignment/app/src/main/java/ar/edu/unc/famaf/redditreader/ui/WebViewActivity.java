package ar.edu.unc.famaf.redditreader.ui;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import ar.edu.unc.famaf.redditreader.R;

public class WebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        Uri uri = getIntent().getData();
        WebView webView = (WebView) this.findViewById(R.id.webView);
        webView.loadUrl(uri.toString());
        webView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = webView.getSettings();
        //webSettings.setJavaScriptEnabled(true);
    }
}

