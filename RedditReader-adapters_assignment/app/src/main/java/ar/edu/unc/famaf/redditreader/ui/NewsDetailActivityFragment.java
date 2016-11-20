package ar.edu.unc.famaf.redditreader.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    private static final String ARG_PARAM5 = "param5";
    private static final String ARG_PARAM6 = "param6";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String mParam3;
    private String mParam4;
    private byte[] mParam5;
    private String mParam6;
    private OnFragmentInteractionListener mListener;

    public NewsDetailActivityFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewsDetailActivityFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsDetailActivityFragment newInstance(String param1, String param2,
                                    String param3, String param4, byte[] param5, String param6) {
        NewsDetailActivityFragment fragment = new NewsDetailActivityFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        args.putString(ARG_PARAM4, param4);
        args.putByteArray(ARG_PARAM5, param5);
        args.putString(ARG_PARAM6, param6);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);//titulo
            mParam2 = getArguments().getString(ARG_PARAM2);//autor
            mParam3 = getArguments().getString(ARG_PARAM3);//creado
            mParam4 = getArguments().getString(ARG_PARAM4);//subreddit
            mParam5 = getArguments().getByteArray(ARG_PARAM5);//preview
            mParam6 = getArguments().getString(ARG_PARAM6);//url
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news_detail_activity, container, false);
        TextView text1 = (TextView) view.findViewById(R.id.textView5 );
        text1.setText(mParam3);
        TextView text2 = (TextView) view.findViewById(R.id.textView6);
        text2.setText(mParam2);
        TextView text3 = (TextView) view.findViewById(R.id.textView7);
        text3.setText(mParam4);
        TextView text4 = (TextView) view.findViewById(R.id.textView);
        text4.setText(mParam1);
        TextView text5 = (TextView) view.findViewById(R.id.textView3);
        text5.setText(mParam6);
        text5.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Uri myUri = Uri.parse(mParam6);
                mListener.onFragmentInteraction(myUri);
            }
        });


        ImageView image = (ImageView) view.findViewById(R.id.imageView4);
        image.setImageBitmap(getImage(mParam5));
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
            mListener.onFragmentInteraction(uri);
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
        void onFragmentInteraction(Uri uri);
    }
}
