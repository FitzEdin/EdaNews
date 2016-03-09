package uk.ac.kent.fe44.edanews;

import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnListItemClickedListener} interface
 * to handle interaction events.
 */
public class ArticleListFragment extends ListFragment
        implements ArticleModel.OnListUpdateListener, ArticleModel.OnFavesUpdateListener  {

    private TextView noNetworkRetry;
    private ProgressBar mProgressBar;
    private boolean loading = false;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_article_list, container, false);

        mProgressBar = (ProgressBar)view.findViewById(R.id.progress_bar);
        noNetworkRetry = (TextView)view.findViewById(R.id.no_network_retry);

        //TODO: remove
       // Toast.makeText(getActivity(), "ArticleListFragment.onCreateView", Toast.LENGTH_SHORT).show();

        noNetworkRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), R.string.checking_network, Toast.LENGTH_SHORT).show();
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
        listView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = gridLayoutManager.getChildCount();
                int pastVisibleItems = gridLayoutManager.findFirstVisibleItemPosition();
                int totalItemCount = gridLayoutManager.getItemCount();

                if ((dy > 0) && (!loading) && (totalItemCount - (visibleItemCount + pastVisibleItems)) <= 3) {
                    //Toast.makeText(getActivity(), "They see me scrolling", Toast.LENGTH_SHORT).show();
                    tryForNetwork();
                    loading = true;
                }
            }
        });

        //listen for changes to the Faves List
        ArticleModel.getInstance().setOnFavesUpdateListener(this);

        return view;
    }

    /*refresh data set with new information*/
    @Override
    public void onFavesUpdate() {
        //change data set
        if(listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
    }

    /*refresh data set with new information*/
    @Override
    public void onListUpdate() {
        if(listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }

        loading = false;
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
