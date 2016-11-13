package ar.edu.unc.famaf.redditreader.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dvr on 04/11/16.
 */

public class Listing {
    private List<PostModel> postModelList=new ArrayList<PostModel>();
    private String before;
    private String after;


    public void add (PostModel postModel){
        if (postModel!= null) {
            this.postModelList.add(postModel);
        }
    }

    public List<PostModel> getPostModelList() {
        return this.postModelList;
    }

    public void setPostModelList(List<PostModel> postModelList) {
        this.postModelList = postModelList;
    }

    public String getBefore() {
        return before;
    }

    public void setBefore(String before) {
        this.before = before;
    }

    public String getAfter() {
        return after;
    }

    public void setAfter(String after) {
        this.after = after;
    }
}
