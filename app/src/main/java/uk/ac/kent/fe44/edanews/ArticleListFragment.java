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

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnListItemClickedListener} interface
 * to handle interaction events.
 */
public class ArticleListFragment extends Fragment implements ArticleModel.OnListUpdateListener {

    private TextView noNetworkRetry;

    private ProgressBar mProgressBar;

    private RecyclerView articleListView;
    private LinearLayoutManager layoutManager;
    private ArticleListAdapter listAdapter;

    private OnListItemClickedListener mListenerActivity;

    public ArticleListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_article_list, container, false);

        mProgressBar = (ProgressBar)view.findViewById(R.id.progress_bar);
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

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListenerActivity = (OnListItemClickedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnListItemClickedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListenerActivity = null;
    }

    /*refresh data set with new information*/
    @Override
    public void onListUpdate(ArrayList<Article> articleList) {
        if(listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
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
        void onItemClicked(int position);
    }

    public void onItemClicked(int position) {
        //perform secondary network request
        mListenerActivity.onItemClicked(position);
    }

    //Source: previous project kn.muscovado.thadailygeek
    //check that the network is available
    private boolean networkIsAvailable() {
        ConnectivityManager mngr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = mngr.getActiveNetworkInfo();

        boolean isAvailable = false;

        //checks that the network is present && it's connected
        if( ( networkInfo != null ) && ( networkInfo.isConnected() ) ){
            isAvailable = true;		//network is up & running
        }

        return isAvailable;
    }//end networkIsAvailable() method

    public void tryForNetwork(){
        //hide everything
        mProgressBar.setVisibility(View.INVISIBLE);
        noNetworkRetry.setVisibility(View.INVISIBLE);

        if(networkIsAvailable()){   //network is there? grab things
            //hide no network message if present, show fab+progress bar
            mProgressBar.setVisibility(View.VISIBLE);
            getData();
        }else {        //no network? hide progress bar and tell the user
            noNetworkRetry.setVisibility(View.VISIBLE);
        }
    }

    private void getData() {
        //get data
        ArticleModel model = ArticleModel.getInstance();
        model.setOnListUpdateListener(this);
        model.loadData(mProgressBar);
    }
}
