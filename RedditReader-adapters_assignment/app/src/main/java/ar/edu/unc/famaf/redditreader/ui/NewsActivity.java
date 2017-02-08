package ar.edu.unc.famaf.redditreader.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import ar.edu.unc.famaf.redditreader.R;
import ar.edu.unc.famaf.redditreader.model.PostModel;


public class NewsActivity extends AppCompatActivity implements OnPostItemSelectedListener{
    static final int REQUEST =0;
    /*Constantes para conexion con loggin de reddit*/
    public  static  boolean LOGGIN = false;
    public  static  boolean ACTIVE_USER = false;
    private static final String TAG = "MyActivity";

    public static final String AUTH_URL =
            "https://www.reddit.com/api/v1/authorize.compact?client_id=%s" +
                    "&response_type=code&state=%s&redirect_uri=%s&" +
                    "duration=permanent&scope=identity,vote";

    public static final String CLIENT_ID = "Cqk_PzT6pdxmHg";
    public static final String REDIRECT_URI = "http://www.example.com/my_redirect";
    public static final String STATE = "MY_RANDOM_STRING_1";
    public static final String ACCESS_TOKEN_URL = "https://www.reddit.com/api/v1/access_token";
    public static final String REVOKE_TOKEN = "https://www.reddit.com/api/v1/revoke_token";
    public static  String accessToken;
    public static  String refreshToken;
    private String user;
    private String code;
    NewsActivityFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("onCreate");
        setContentView(R.layout.activity_news);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (findViewById(R.id.fragment_init) != null) {
            if (savedInstanceState != null) {
                return;
            }
            fragment = new NewsActivityFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_init, fragment).commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("USER", user);
        outState.putString("CODE", code);
        outState.putString("ACCESSTOKEN", accessToken);
        outState.putString("REFRESHTOKEN", refreshToken);
        outState.putBoolean("LOGGIN", LOGGIN);
        outState.putBoolean("ACTIVE_USER", ACTIVE_USER);
        getSupportFragmentManager().putFragment(outState, "fragment", fragment);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        user= savedInstanceState.getString("USER");
        code  = savedInstanceState.getString("CODE");
        accessToken = savedInstanceState.getString("ACCESSTOKEN");
        refreshToken =savedInstanceState.getString("REFRESHTOKEN");
        LOGGIN = savedInstanceState.getBoolean("LOGGIN");
        ACTIVE_USER = savedInstanceState.getBoolean("ACTIVE_USER");
        fragment = (NewsActivityFragment) getSupportFragmentManager().getFragment(savedInstanceState, "fragment");
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(getIntent()!=null && getIntent().getAction().equals(Intent.ACTION_VIEW) && !LOGGIN) {
            Uri uri = getIntent().getData();
            if(uri.getQueryParameter("error") != null) {
                String error = uri.getQueryParameter("error");
                Log.e(TAG, "An error has occurred : " + error);
            } else {
                String state = uri.getQueryParameter("state");
                if(state.equals(STATE)) {
                    code = uri.getQueryParameter("code");
                    //obteniendo token
                    getAccessToken(code);
                }
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("ondestroy");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_news, menu);
        menu.findItem(R.id.action_out).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sign_in && !LOGGIN) {
            // llamada a loggin de reddit OAuth2
            String url = String.format(AUTH_URL, CLIENT_ID, STATE, REDIRECT_URI);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        }else if (id == R.id.action_out){
            // llamada para revoca token en logout
            logout();
            LOGGIN = false;
            ACTIVE_USER = false;
            Toast.makeText(getApplicationContext(), "logout", Toast.LENGTH_SHORT).show();
            invalidateOptionsMenu();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // se llama luego de invalidateOptionsMenu();
        if (LOGGIN){
            menu.findItem(R.id.action_sign_in).setVisible(false);
            menu.findItem(R.id.action_out).setVisible(true);
            menu.findItem(R.id.action_out).setTitle(user + " logout");
        }else{
            menu.findItem(R.id.action_sign_in).setVisible(true);
            menu.findItem(R.id.action_out).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    // se llama cuando se pide detalle de un post
    @Override
    public void onPostItemPicked(PostModel post, int position) {
        Context context= getApplicationContext();
        if(post!=null) {
            Intent intent = new Intent(context, NewsDetailActivity.class);
            intent.putExtra("post",post);
            intent.putExtra("position", position);
            startActivityForResult(intent, REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int position=0;
        if(resultCode == RESULT_OK){
            //viene post model modificado de detail
            if(LOGGIN && data!=null) {
                PostModel post = (PostModel) data.getSerializableExtra("post");
                position = data.getIntExtra("position", position);
                fragment.changeList(post, position);
            }
        }
    }

    private void getAccessToken(String code) {
        new ConnectionTask(){
            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                if(aBoolean){
                    me_detail(accessToken, refreshToken);
                }
            }
        }.execute(code);
    }

    private void me_detail (String token, String refreshToken){
        DetailTask detailTask = new DetailTask(token, refreshToken);
        detailTask.execute("");
    }


     public void logout(){
         LogoutTask logoutTask = new LogoutTask();
         logoutTask.execute("");
    }

    public class LogoutTask extends AsyncTask<String, Integer,Boolean>{

        @Override
        protected Boolean doInBackground(String... params) {
            String authString = CLIENT_ID + ":";
            String encodedAuthString = Base64.encodeToString(authString.getBytes(), Base64.NO_WRAP);

            String urlParameters = "token="+accessToken+"&token_type_hint=access_token";
            byte[] postData = new byte[0];
            try {
                postData = urlParameters.getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            int    postDataLength = postData.length;
            String request = REVOKE_TOKEN;
            URL url = null;
            try {
                url = new URL( request );
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection conn = null;
            try {
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput( true );
                conn.setRequestProperty ("Authorization", "Basic " + encodedAuthString);
                conn.setRequestProperty ("User-Agent", "Reddit Reader");
                conn.setInstanceFollowRedirects( false );
                conn.setRequestMethod( "POST" );
                conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty( "charset", "utf-8");
                conn.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
                conn.setUseCaches( false );
                DataOutputStream wr = new DataOutputStream( conn.getOutputStream());
                wr.write( postData );
                wr.flush();

                StringBuilder sb = new StringBuilder();
                int HttpResult = conn.getResponseCode();
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();
                String json = sb.toString();
                System.out.println("....." + json);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    public class ConnectionTask extends AsyncTask<String, Integer,Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            String code= params[0];
            String authString = CLIENT_ID + ":";
            String encodedAuthString = Base64.encodeToString(authString.getBytes(), Base64.NO_WRAP);

            String urlParameters = "grant_type=authorization_code&code=" + code + "&redirect_uri=" + REDIRECT_URI;
            byte[] postData = new byte[0];
            try {
                postData = urlParameters.getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            int    postDataLength = postData.length;
            String request = ACCESS_TOKEN_URL;
            URL url = null;
            try {
                url = new URL( request );
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection conn = null;
            try {
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput( true );
                conn.setRequestProperty ("Authorization", "Basic " + encodedAuthString);
                conn.setRequestProperty ("User-Agent", "Reddit Reader");
                conn.setInstanceFollowRedirects( false );
                conn.setRequestMethod( "POST" );
                conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty( "charset", "utf-8");
                conn.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
                conn.setUseCaches( false );
                DataOutputStream wr = new DataOutputStream( conn.getOutputStream());
                wr.write( postData );
                wr.flush();

                StringBuilder sb = new StringBuilder();
                int HttpResult = conn.getResponseCode();
                if (HttpResult == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    String json = sb.toString();
                    System.out.println("....." + json);
                    JSONObject data = null;
                    try {
                        data = new JSONObject(json);
                        accessToken = data.optString("access_token");
                        refreshToken = data.optString("refresh_token");
                        LOGGIN = true;
                        Log.d(TAG, "Access Token = " + accessToken);
                        Log.d(TAG, "Refresh Token = " + refreshToken);
                        return true;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else{
                    System.out.println(conn.getResponseMessage());
                    //  que intente de nuevo
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    public class DetailTask extends AsyncTask<String, Integer,Boolean> {
        String token;
        String refreshToken;

        public DetailTask(String token, String refreshToken){
            this.token = token;
            this.refreshToken = refreshToken;

        }

        @Override
        protected Boolean doInBackground(String... params) {
            String url = "https://oauth.reddit.com/api/v1/me";
            String authorizacion = "bearer "+ token;

            URL obj = null;
            try {
                obj = new URL(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            HttpURLConnection conn = null;
            try {
                conn = (HttpURLConnection) obj.openConnection();
                conn.setRequestProperty ("Authorization",authorizacion );
                conn.setRequestProperty("User-Agent", "Reddit Reader");

                conn.setRequestMethod("GET");

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    System.out.println("\nSending 'GET' request to URL : " + url);
                    System.out.println("Response Code : " + responseCode);

                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    System.out.println(response.toString());

                    String json = response.toString();
                    JSONObject data = null;
                    try {
                        data = new JSONObject(json);
                        user = data.optString("name");
                        ACTIVE_USER = true;
                        invalidateOptionsMenu();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}


