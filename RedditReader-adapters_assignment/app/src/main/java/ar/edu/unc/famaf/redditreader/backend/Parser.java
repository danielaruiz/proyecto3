package ar.edu.unc.famaf.redditreader.backend;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.JsonReader;
import android.util.JsonToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import ar.edu.unc.famaf.redditreader.R;
import ar.edu.unc.famaf.redditreader.model.Listing;
import ar.edu.unc.famaf.redditreader.model.PostModel;

public class Parser {

    public Listing readJsonStream(InputStream in) throws IOException {
        // Nueva instancia JsonReader
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            // Leer Array
            return ArrayData(reader);
        } finally {
            reader.close();
        }

    }

    public Listing ArrayData(JsonReader reader) throws IOException {
        Listing list=null;
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

    Listing readData(JsonReader reader) throws IOException {
        Listing list=null;
        String before=null;
        String after=null;
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("children")) {
                list = readChildren(reader);
                //break;
            }else if (name.equals("after")){
                after=reader.nextString();
            }else if(name.equals("before") && reader.peek()!= JsonToken.NULL){
                before=reader.nextString();
            } else {
                reader.skipValue();
            }
        }
        list.setBefore(before);
        list.setAfter(after);
        //reader.endObject();
        return list;
    }

    Listing readChildren(JsonReader reader) throws IOException {
        Listing listing = new Listing();

        reader.beginArray();
        while (reader.hasNext()) {
            listing.add(readMessage(reader));
        }
        reader.endArray();
        return listing;

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
        post.setIcon(new byte[0]);
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case "title":
                    post.setTitle(reader.nextString());
                    break;
                case "subreddit":
                    post.setSubreddit(reader.nextString());
                    break;
                case "created":
                    post.setCreated(reader.nextLong());
                    break;
                case "author":
                    post.setAuthor(reader.nextString());
                    break;
                case "url":
                    post.setUrl(reader.nextString());
                    break;
                case "num_comments":
                    post.setComments(reader.nextInt());
                    break;
                case "thumbnail":
                    post.setThumbnail(reader.nextString());
                    break;
                case "score":
                    post.setScore(reader.nextInt());
                    break;
                case "name":
                    post.setName(reader.nextString());
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();

        return post;
    }

}