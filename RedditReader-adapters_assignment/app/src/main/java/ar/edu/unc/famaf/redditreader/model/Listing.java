package ar.edu.unc.famaf.redditreader.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dvr on 04/11/16.
 */

public class Listing {
    private List<PostModel> postModelList=new ArrayList<PostModel>();
    private int before;
    private int after;


    public void add (PostModel postModel){
        if (postModel!= null) {
            this.postModelList.add(postModel);
        }
    }

    public List<PostModel> getPostModelList() {
        return postModelList;
    }

    public void setPostModelList(List<PostModel> postModelList) {
        this.postModelList = postModelList;
    }

    public int getBefore() {
        return before;
    }

    public void setBefore(int before) {
        this.before = before;
    }

    public int getAfter() {
        return after;
    }

    public void setAfter(int after) {
        this.after = after;
    }
}
