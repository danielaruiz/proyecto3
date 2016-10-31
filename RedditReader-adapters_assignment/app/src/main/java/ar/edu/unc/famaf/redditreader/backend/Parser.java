package ar.edu.unc.famaf.redditreader.backend;

import android.util.JsonReader;
import android.util.JsonToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import ar.edu.unc.famaf.redditreader.R;
import ar.edu.unc.famaf.redditreader.model.PostModel;

public class Parser {

//    static class Listing{
//        int after;
//        int before=null;
//        List<PostModel> children;
//    }

    public List<PostModel> readJsonStream(InputStream in) throws IOException {
        // Nueva instancia JsonReader


        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            // Leer Array
            return ArrayDatos(reader);
        } finally {
            reader.close();
        }

    }

    public List<PostModel> ArrayDatos(JsonReader reader) throws IOException {
        // Lista temporal
        List<PostModel> list = new ArrayList<PostModel>();

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("data")) {
                list = readData(reader);
                break;
            } else {
                reader.skipValue();
            }
        }
        //reader.endObject();
        return list;

    }

    List<PostModel> readData(JsonReader reader) throws IOException {
        List<PostModel> list=null;
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("children")) {
                list = readChildren(reader);
                break;
            } else {
                reader.skipValue();
            }
        }
        //reader.endObject();
        return list;
    }

    List<PostModel> readChildren(JsonReader reader) throws IOException {
        List<PostModel> list = new ArrayList<PostModel>();

        reader.beginArray();
        while (reader.hasNext()) {
            list.add(readMessage(reader));
        }
        reader.endArray();
        return list;

    }

    public PostModel readMessage(JsonReader reader) throws IOException {
        PostModel obj = null;
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("data")) {
                obj = readObj(reader);
                //break;
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return obj;
    }

    public PostModel readObj(JsonReader reader) throws IOException {
        PostModel post = new PostModel();

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("title")) {
                post.setTitle(reader.nextString());
            } else if (name.equals("subreddit")) {
                post.setSubreddit(reader.nextString());
            } else if (name.equals("created") && reader.peek() != JsonToken.NULL) {
                post.setCreated(reader.nextInt());
            } else if (name.equals("author")) {
                post.setAuthor(reader.nextString());

            } else if (name.equals("url")) {
                post.setUrl(reader.nextString());

            }else if(name.equals("num_comments")){
                post.setComments(reader.nextInt());
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        post.setIcon(R.mipmap.ic_launcher);
        return post;
    }

}


