package ar.edu.unc.famaf.redditreader.backend;

import android.util.JsonReader;
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

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("title")) {
                post.setTitle(reader.nextString());
            } else if (name.equals("subreddit")) {
                post.setSubreddit(reader.nextString());
            } else if (name.equals("created") ) {
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


