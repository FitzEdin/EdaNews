package uk.ac.kent.fe44.edanews;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnListItemClickedListener} interface
 * to handle interaction events.
 */
public class ArticleListFragment extends Fragment {

    private TextView noNetworkRetry;
    protected ProgressBar mProgressBar;

    private RecyclerView articleListView;
    private LinearLayoutManager layoutManager;
    private ArticleListAdapter listAdapter;

    private OnListItemClickedListener mListener;

    public ArticleListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_article_list, container, false);

        mProgressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        noNetworkRetry = (TextView)view.findViewById(R.id.no_network_retry);

        //TODO: remove
        Toast.makeText(getActivity(), "ArticleListFragment.onCreateView", Toast.LENGTH_SHORT).show();

        noNetworkRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), R.string.checking_network, Toast.LENGTH_SHORT).show();
                tryForNetwork();
            }
        });

        tryForNetwork();

        //set up layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);

        //set up list adapter
        listAdapter = new ArticleListAdapter(this);

        //set up visual elements
        articleListView = (RecyclerView)view.findViewById(R.id.article_list_view);
        articleListView.setLayoutManager(layoutManager);
        articleListView.setAdapter(listAdapter);

        //kill progressBar
        mProgressBar.setVisibility(View.INVISIBLE);
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnListItemClickedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnListItemClickedListener");
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListItemClickedListener {
        // TODO: Update argument type and name
        void onItemClicked(int position);
    }

    public void onItemClicked(int position) {
        mListener.onItemClicked(position);
    }

    //Source: previous project kn.muscovado.thadailygeek
    //check that the network is available
    private boolean isNetworkAvailable() {
        ConnectivityManager mngr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = mngr.getActiveNetworkInfo();

        boolean isAvailable = false;

        //checks that the network is present && it's connected
        if( ( networkInfo != null ) && ( networkInfo.isConnected() ) ){
            isAvailable = true;		//network is up & running
        }

        return isAvailable;
    }//end isNetworkAvailable() method

    public void tryForNetwork(){
        //check that there is a connection before starting async thread

        //hide everything
        mProgressBar.setVisibility(View.INVISIBLE);
        noNetworkRetry.setVisibility(View.INVISIBLE);

        if(isNetworkAvailable()){   //network is there? grab things
            //hide no network message if present, show fab+progress bar
            mProgressBar.setVisibility(View.VISIBLE);
            /*create object to get blog posts
            GetBlogPostsTask getBlogPosts = new GetBlogPostsTask();
            //run that object
            getBlogPosts.execute(); */
        }else {        //no network? hide progress bar and tell the user
            noNetworkRetry.setVisibility(View.VISIBLE);
        }
    }
}
