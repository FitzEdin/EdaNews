package uk.ac.kent.fe44.edanews;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
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
public class ArticleListFragment extends ListFragment {

    private TextView noNetworkRetry;

    private ProgressBar mProgressBar;

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
        gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        gridLayoutManager.scrollToPosition(0);

        //set up list adapter
        listAdapter = new ArticleListAdapter(this);

        //set up visual elements
        listView = (RecyclerView)view.findViewById(R.id.article_list_view);
        listView.setLayoutManager(gridLayoutManager);
        listView.setAdapter(listAdapter);

        /*source: https://guides.codepath.com/android/Endless-Scrolling-with-AdapterViews-and-RecyclerView*/
        //add onScrollListener
        //articleListView.addOnScrollListener(new EndlessRecyclerViewScrollListener);

        return view;
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
        ArticleModel model = ArticleModel.getInstance();

        //listen for eventual response
        model.setOnListUpdateListener(this);

        //load data from network
        model.loadData(mProgressBar);
    }
}
