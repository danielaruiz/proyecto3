package ar.edu.unc.famaf.redditreader.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import ar.edu.unc.famaf.redditreader.R;
import ar.edu.unc.famaf.redditreader.model.PostModel;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewsDetailActivityFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewsDetailActivityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsDetailActivityFragment extends Fragment {
    private PostModel postModel;
    private int clicks=0;
    private OnFragmentInteractionListener mListener;

    public NewsDetailActivityFragment() {
        // Required empty public constructor
    }


    public static NewsDetailActivityFragment newInstance(PostModel post) {

        NewsDetailActivityFragment fragment = new NewsDetailActivityFragment();
        Bundle args = new Bundle();
        args.putSerializable("post", post);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            postModel = (PostModel) getArguments().getSerializable("post");
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int score= postModel.getScore();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news_detail_activity, container, false);

        TextView text1 = (TextView) view.findViewById(R.id.textView5 );
        text1.setText(String.valueOf(postModel.getCreated()));

        TextView text2 = (TextView) view.findViewById(R.id.textView6);
        text2.setText(postModel.getAuthor());

        TextView text3 = (TextView) view.findViewById(R.id.textView7);
        text3.setText(postModel.getSubreddit());

        TextView text4 = (TextView) view.findViewById(R.id.textView);
        text4.setText(postModel.getTitle());

        TextView text5 = (TextView) view.findViewById(R.id.textView3);
        text5.setText(postModel.getUrl());
        text5.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Uri myUri = Uri.parse(postModel.getUrl());
                mListener.onFragmentInteraction(myUri, null);
            }
        });

        ImageView image = (ImageView) view.findViewById(R.id.imageView4);
        image.setImageBitmap(getImage(postModel.getIcon()));

        //Parte nueva
        PostModelHolder holder = new PostModelHolder();
        holder.score=(TextView) view.findViewById(R.id.scoredetail);
        holder.score.setText(String.valueOf(postModel.getScore()));

        holder.up = (ImageButton) view.findViewById(R.id. upDetail);
        holder.down = (ImageButton) view.findViewById(R.id.downDetail);

        if(NewsActivity.LOGGIN && postModel.getClickup() == 1){
            holder.up.setBackgroundColor(Color.DKGRAY);
            score=postModel.getScore()-1;
        }
        if(NewsActivity.LOGGIN && postModel.getClickdown() == 1){
            holder.down.setBackgroundColor(Color.DKGRAY);
            score=postModel.getScore()+1;
        }

        final Buttons button = new Buttons(postModel,holder, NewsActivityFragment.db   , view.getContext(), clicks, score);

        holder.up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                button.Bcontrol("1");
                if(NewsActivity.LOGGIN) {
                    mListener.onFragmentInteraction(null, postModel);
                }
            }
        });

        holder.down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                button.Bcontrol("-1");
                if(NewsActivity.LOGGIN){
                    mListener.onFragmentInteraction(null, postModel);
                }
            }
        });

        return view;
    }
    public static Bitmap getImage(byte[] image){
        Bitmap b=null;
        try{
            b= BitmapFactory.decodeByteArray(image, 0, image.length);
        }catch (OutOfMemoryError e){
            e.printStackTrace();
        }
        return b;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri, null); ///agrgar model
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri, PostModel post);
    }

}
