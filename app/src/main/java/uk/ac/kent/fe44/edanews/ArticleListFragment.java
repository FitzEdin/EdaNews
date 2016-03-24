package uk.ac.kent.fe44.edanews;

import android.app.Fragment;
import android.content.Context;
import android.content.res.Configuration;
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
        implements ArticleModel.OnListUpdateListener  {

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

        noNetworkRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryForNetwork();
            }
        });

        tryForNetwork();


        int spanCount = 1;

        Configuration config = getResources().getConfiguration();

        //set span count based on screen size and orientation
        final boolean isLarge = config.isLayoutSizeAtLeast(Configuration.SCREENLAYOUT_SIZE_LARGE);
        if(isLarge) {
            //handle large screens, @least 600x1024
            switch (config.orientation) {
                case Configuration.ORIENTATION_LANDSCAPE:
                    spanCount = 3;      //
                    break;
                case Configuration.ORIENTATION_PORTRAIT:
                    spanCount = 2;      //
                    break;
            }
        }else{
            //handle everything smaller than 600x1024
            switch (config.orientation) {
                case Configuration.ORIENTATION_LANDSCAPE:
                    spanCount = 2;      //
                    break;
                case Configuration.ORIENTATION_PORTRAIT:
                    spanCount = 1;      //
                    break;
            }
        }

        //set up layout manager
        gridLayoutManager = new GridLayoutManager(getActivity(), spanCount);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        gridLayoutManager.scrollToPosition(0);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (isLarge) {
                    //for large screens in all orientations
                    if (((position % 4) == 0) || ((position % 4) == 3)) {
                        return 2;
                    } else {
                        return 1;
                    }
                } else {
                    return 1;
                }
            }
        });

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

                if ((dy > 0)
                        && (!loading)
                        && (totalItemCount - (visibleItemCount + pastVisibleItems)) <= 3
                        ) {
                    //Toast.makeText(getActivity(), "They see me scrolling", Toast.LENGTH_SHORT).show();
                    tryForNetwork();
                    loading = true;
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //listen for changes to the Faves List
        ArticleModel.getInstance().setOnFavesUpdateListener(this);
    }

    /*refresh data set with new information*/
    @Override
    public void onListUpdate() {
        if(listAdapter != null) {
            listAdapter.notifyDataSetChanged();
            mProgressBar.setVisibility(View.INVISIBLE);
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
        model.loadData();
    }
}
